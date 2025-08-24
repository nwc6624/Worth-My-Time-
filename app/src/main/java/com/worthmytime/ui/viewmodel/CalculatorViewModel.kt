package com.worthmytime.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.worthmytime.data.datastore.ProfilePreferences
import com.worthmytime.data.repo.GoalsRepository
import com.worthmytime.data.repo.HistoryRepository
import com.worthmytime.data.repo.ProfileRepository
import com.worthmytime.data.repo.TaxRepository
import com.worthmytime.domain.logic.Calculator
import com.worthmytime.domain.model.Decision
import com.worthmytime.domain.model.DisplayUnit
import com.worthmytime.domain.model.GoalCategory
import com.worthmytime.domain.model.Profile
import com.worthmytime.domain.model.SalesTaxMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CalculationResult(
    val netHourly: Double,
    val resolvedSalesTaxPct: Double,
    val effectivePrice: Double,
    val hours: Double,
    val workdays: Double,
    val workweeks: Double,
    val equivalents: Triple<Int, Int, Int>
)

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val goalsRepository: GoalsRepository,
    private val profileRepository: ProfileRepository,
    private val taxRepository: TaxRepository
) : ViewModel() {
    
    private val _price = MutableStateFlow("")
    val price: StateFlow<String> = _price.asStateFlow()
    
    var selectedDisplayUnit by mutableStateOf(DisplayUnit.HOURS)
        private set
    
    val profile: StateFlow<Profile> = profileRepository.profile.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfilePreferences.getDefaultProfile()
    )
    
    val resolvedSalesTaxPct: StateFlow<Double> = combine(
        profile,
        flow { emit(taxRepository.getStateTaxMap()) }
    ) { profile, stateTaxMap ->
        when (profile.salesTaxMode) {
            SalesTaxMode.MANUAL -> profile.salesTaxPct
            SalesTaxMode.STATE -> stateTaxMap[profile.salesTaxState] ?: 0.0
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )
    
    val netHourly: StateFlow<Double> = profile.map { profile ->
        val grossHourly = Calculator.calculateGrossHourly(
            profile.incomeType,
            profile.hourlyRate,
            profile.annualSalary,
            profile.hoursPerWeek
        )
        Calculator.calculateNetHourly(grossHourly, profile.effectiveTaxPct)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 18.75
    )
    
    val calculationResult: StateFlow<CalculationResult?> = combine(
        profile,
        resolvedSalesTaxPct,
        netHourly,
        price
    ) { profile, salesTaxPct, netHourly, priceString ->
        val priceValue = priceString.toDoubleOrNull() ?: return@combine null
        
        val effectivePrice = Calculator.calculateEffectivePrice(
            priceValue,
            salesTaxPct,
            profile.includeSalesTaxInCalc
        )
        
        val hours = Calculator.priceToHours(effectivePrice, netHourly)
        val workdays = Calculator.hoursToWorkdays(hours)
        val workweeks = Calculator.hoursToWorkweeks(hours, profile.hoursPerWeek)
        
        val equivalents = Calculator.calculateLifestyleEquivalents(
            effectivePrice,
            profile.lifestyleBenchmarks
        )
        
        CalculationResult(
            netHourly = netHourly,
            resolvedSalesTaxPct = salesTaxPct,
            effectivePrice = effectivePrice,
            hours = hours,
            workdays = workdays,
            workweeks = workweeks,
            equivalents = equivalents
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )
    
    val recentHistory: StateFlow<List<com.worthmytime.data.db.entities.HistoryEntity>> = 
        historyRepository.observeRecent(10).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    fun updatePrice(newPrice: String) {
        _price.value = newPrice
    }
    
    fun updateDisplayUnit(unit: DisplayUnit) {
        selectedDisplayUnit = unit
    }
    
    fun onCheck() {
        val priceValue = price.value.toDoubleOrNull() ?: return
        val result = calculationResult.value ?: return
        
        viewModelScope.launch {
            historyRepository.insertHistoryItem(
                priceBeforeTax = priceValue,
                salesTaxPctAtCheck = result.resolvedSalesTaxPct,
                priceWithTax = result.effectivePrice,
                netHourlyAtCheck = result.netHourly,
                hours = result.hours,
                workdays = result.workdays,
                workweeks = result.workweeks
            )
        }
    }
    
    fun onHistoryTap(historyItem: com.worthmytime.data.db.entities.HistoryEntity) {
        updatePrice(historyItem.priceBeforeTax.toString())
    }
    
    fun onHistoryDecision(historyId: String, decision: Decision?) {
        viewModelScope.launch {
            historyRepository.updateDecision(historyId, decision)
        }
    }
    
    fun onHistoryDelete(historyId: String) {
        viewModelScope.launch {
            historyRepository.deleteHistoryItem(historyId)
        }
    }
    
    fun onAddAsGoal() {
        val priceValue = price.value.toDoubleOrNull() ?: return
        if (priceValue <= 0) return
        
        viewModelScope.launch {
            goalsRepository.addGoal(
                label = "New Goal - $${priceValue}",
                price = priceValue,
                category = GoalCategory.LUXURY, // Default category
                useTaxSnapshot = false,
                salesTaxPctAtCreation = null
            )
        }
    }
    
    fun onAddAsGoalWithDetails(label: String, category: GoalCategory) {
        val priceValue = price.value.toDoubleOrNull() ?: return
        if (priceValue <= 0) return
        
        viewModelScope.launch {
            goalsRepository.addGoal(
                label = label,
                price = priceValue,
                category = category,
                useTaxSnapshot = false,
                salesTaxPctAtCreation = null
            )
        }
    }
    
    fun getDisplayValue(result: CalculationResult): Double {
        return when (selectedDisplayUnit) {
            DisplayUnit.HOURS -> Calculator.roundHours(result.hours)
            DisplayUnit.DAYS -> Calculator.roundWorkUnits(result.workdays)
            DisplayUnit.WEEKS -> Calculator.roundWorkUnits(result.workweeks)
        }
    }
    
    fun getDisplayUnitString(): String {
        return when (selectedDisplayUnit) {
            DisplayUnit.HOURS -> "hours"
            DisplayUnit.DAYS -> "workdays"
            DisplayUnit.WEEKS -> "workweeks"
        }
    }
}
