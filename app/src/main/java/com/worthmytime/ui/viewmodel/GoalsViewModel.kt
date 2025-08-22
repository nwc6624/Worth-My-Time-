package com.worthmytime.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.worthmytime.data.datastore.ProfilePreferences
import com.worthmytime.data.db.entities.GoalEntity
import com.worthmytime.data.repo.GoalsRepository
import com.worthmytime.data.repo.ProfileRepository
import com.worthmytime.data.repo.TaxRepository
import com.worthmytime.domain.logic.Calculator
import com.worthmytime.domain.model.GoalCategory
import com.worthmytime.domain.model.Profile
import com.worthmytime.domain.model.SalesTaxMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GoalWithProgress(
    val goal: GoalEntity,
    val effectivePrice: Double,
    val hoursNeeded: Double,
    val hoursBanked: Double,
    val percentComplete: Double
)

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val goalsRepository: GoalsRepository,
    private val profileRepository: ProfileRepository,
    private val taxRepository: TaxRepository
) : ViewModel() {
    
    val goals: StateFlow<List<GoalEntity>> = goalsRepository.observeGoals().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
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
    
    val goalsWithProgress: StateFlow<List<GoalWithProgress>> = combine(
        goals,
        profile,
        resolvedSalesTaxPct,
        netHourly
    ) { goalsList, profile, salesTaxPct, netHourly ->
        goalsList.map { goal ->
            val effectivePrice = if (goal.useTaxSnapshot && goal.salesTaxPctAtCreation != null) {
                Calculator.calculateEffectivePrice(
                    goal.price,
                    goal.salesTaxPctAtCreation,
                    true
                )
            } else {
                Calculator.calculateEffectivePrice(
                    goal.price,
                    salesTaxPct,
                    profile.includeSalesTaxInCalc
                )
            }
            
            val hoursNeeded = Calculator.priceToHours(effectivePrice, netHourly)
            val hoursBanked = goal.savedDollars / netHourly
            val percentComplete = (hoursBanked / hoursNeeded).coerceIn(0.0, 1.0)
            
            GoalWithProgress(
                goal = goal,
                effectivePrice = effectivePrice,
                hoursNeeded = hoursNeeded,
                hoursBanked = hoursBanked,
                percentComplete = percentComplete
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    fun addGoal(
        label: String,
        price: Double,
        category: GoalCategory,
        useTaxSnapshot: Boolean = false,
        salesTaxPctAtCreation: Double? = null
    ) {
        viewModelScope.launch {
            goalsRepository.addGoal(label, price, category, useTaxSnapshot, salesTaxPctAtCreation)
        }
    }
    
    fun updateGoal(goal: GoalEntity) {
        viewModelScope.launch {
            goalsRepository.updateGoal(goal)
        }
    }
    
    fun updateSavedDollars(goalId: String, savedDollars: Double) {
        viewModelScope.launch {
            goalsRepository.updateSavedDollars(goalId, savedDollars)
        }
    }
    
    fun addSavedDollars(goalId: String, delta: Double) {
        viewModelScope.launch {
            val goal = goals.value.find { it.id == goalId } ?: return@launch
            val newSavedDollars = (goal.savedDollars + delta).coerceAtLeast(0.0)
            goalsRepository.updateSavedDollars(goalId, newSavedDollars)
        }
    }
    
    fun updateTaxSnapshot(
        goalId: String,
        useTaxSnapshot: Boolean,
        salesTaxPctAtCreation: Double?
    ) {
        viewModelScope.launch {
            goalsRepository.updateTaxSnapshot(goalId, useTaxSnapshot, salesTaxPctAtCreation)
        }
    }
    
    fun deleteGoal(goalId: String) {
        viewModelScope.launch {
            goalsRepository.deleteGoal(goalId)
        }
    }
}
