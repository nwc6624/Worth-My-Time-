package com.worthmytime.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.worthmytime.data.repo.HistoryRepository
import com.worthmytime.domain.model.Decision
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

enum class InsightsPeriod(val displayName: String, val days: Int) {
    THIS_MONTH("This Month", 30),
    LAST_MONTH("Last Month", 60),
    THIS_WEEK("This Week", 7),
    CUSTOM("Custom", 0)
}

data class InsightsData(
    val totalSpent: Double = 0.0,
    val totalHours: Double = 0.0,
    val totalItems: Int = 0,
    val averagePerItem: Double = 0.0,
    val worthCount: Int = 0,
    val notWorthCount: Int = 0,
    val undecidedCount: Int = 0,
    val topCategories: List<CategorySpending> = emptyList(),
    val recentActivity: List<RecentActivity> = emptyList()
)

data class CategorySpending(
    val name: String,
    val count: Int,
    val totalSpent: Double
)

data class RecentActivity(
    val price: Double,
    val hours: Double,
    val decision: Decision?,
    val timestamp: Long
)

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {
    
    private val _selectedPeriod = MutableStateFlow(InsightsPeriod.THIS_MONTH)
    val selectedPeriod: StateFlow<InsightsPeriod> = _selectedPeriod.asStateFlow()
    
    val insightsData: StateFlow<InsightsData> = combine(
        selectedPeriod,
        historyRepository.observeAll()
    ) { period, history ->
        val cutoffDate = when (period) {
            InsightsPeriod.THIS_MONTH -> {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                calendar.timeInMillis
            }
            InsightsPeriod.LAST_MONTH -> {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.MONTH, -1)
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                calendar.timeInMillis
            }
            InsightsPeriod.THIS_WEEK -> {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                calendar.timeInMillis
            }
            InsightsPeriod.CUSTOM -> 0L // For now, show all data
        }
        
        val filteredHistory = if (cutoffDate > 0) {
            history.filter { it.createdAt >= cutoffDate }
        } else {
            history
        }
        
        if (filteredHistory.isEmpty()) {
            InsightsData()
        } else {
            val totalSpent = filteredHistory.sumOf { it.priceWithTax }
            val totalHours = filteredHistory.sumOf { it.hours }
            val totalItems = filteredHistory.size
            val averagePerItem = totalSpent / totalItems
            
            val worthCount = filteredHistory.count { it.decision == Decision.WORTH }
            val notWorthCount = filteredHistory.count { it.decision == Decision.NOT_WORTH }
            val undecidedCount = filteredHistory.count { it.decision == null }
            
            // Group by price ranges for "categories"
            val categories = buildList {
                val under25 = filteredHistory.filter { it.priceWithTax < 25 }
                if (under25.isNotEmpty()) {
                    add(CategorySpending("Under $25", under25.size, under25.sumOf { it.priceWithTax }))
                }
                
                val under50 = filteredHistory.filter { it.priceWithTax >= 25 && it.priceWithTax < 50 }
                if (under50.isNotEmpty()) {
                    add(CategorySpending("$25-$50", under50.size, under50.sumOf { it.priceWithTax }))
                }
                
                val under100 = filteredHistory.filter { it.priceWithTax >= 50 && it.priceWithTax < 100 }
                if (under100.isNotEmpty()) {
                    add(CategorySpending("$50-$100", under100.size, under100.sumOf { it.priceWithTax }))
                }
                
                val over100 = filteredHistory.filter { it.priceWithTax >= 100 }
                if (over100.isNotEmpty()) {
                    add(CategorySpending("$100+", over100.size, over100.sumOf { it.priceWithTax }))
                }
            }.sortedByDescending { it.count }
            
            val recentActivity = filteredHistory
                .sortedByDescending { it.createdAt }
                .take(10)
                .map { historyItem ->
                    RecentActivity(
                        price = historyItem.priceWithTax,
                        hours = historyItem.hours,
                        decision = historyItem.decision,
                        timestamp = historyItem.createdAt
                    )
                }
            
            InsightsData(
                totalSpent = totalSpent,
                totalHours = totalHours,
                totalItems = totalItems,
                averagePerItem = averagePerItem,
                worthCount = worthCount,
                notWorthCount = notWorthCount,
                undecidedCount = undecidedCount,
                topCategories = categories,
                recentActivity = recentActivity
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = InsightsData()
    )
    
    fun updateSelectedPeriod(period: InsightsPeriod) {
        _selectedPeriod.value = period
    }
}
