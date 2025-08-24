package com.worthmytime.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.worthmytime.data.repo.ProfileRepository
import com.worthmytime.data.repo.TaxRepository
import com.worthmytime.domain.logic.Calculator
import com.worthmytime.data.datastore.ProfilePreferences
import com.worthmytime.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val taxRepository: TaxRepository,
    private val historyRepository: com.worthmytime.data.repo.HistoryRepository
) : ViewModel() {
    
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
    
    // Profile update functions
    fun updateState(state: String) {
        viewModelScope.launch {
            profileRepository.updateState(state)
        }
    }
    
    fun updateIncomeType(incomeType: IncomeType) {
        viewModelScope.launch {
            profileRepository.updateIncomeType(incomeType)
        }
    }
    
    fun updateHourlyRate(hourlyRate: Double) {
        viewModelScope.launch {
            profileRepository.updateHourlyRate(hourlyRate)
        }
    }
    
    fun updateAnnualSalary(annualSalary: Double) {
        viewModelScope.launch {
            profileRepository.updateAnnualSalary(annualSalary)
        }
    }
    
    fun updateEffectiveTaxPct(effectiveTaxPct: Double) {
        viewModelScope.launch {
            profileRepository.updateEffectiveTaxPct(effectiveTaxPct)
        }
    }
    
    fun updateHoursPerWeek(hoursPerWeek: Double) {
        viewModelScope.launch {
            profileRepository.updateHoursPerWeek(hoursPerWeek)
        }
    }
    
    fun updateJobRole(jobRole: String?) {
        viewModelScope.launch {
            profileRepository.updateJobRole(jobRole)
        }
    }
    
    fun updateLifestyleBenchmarks(benchmarks: LifestyleBenchmarks) {
        viewModelScope.launch {
            profileRepository.updateLifestyleBenchmarks(benchmarks)
        }
    }
    
    fun updateSalesTaxMode(mode: SalesTaxMode) {
        viewModelScope.launch {
            profileRepository.updateSalesTaxMode(mode)
        }
    }
    
    fun updateSalesTaxPct(salesTaxPct: Double) {
        viewModelScope.launch {
            profileRepository.updateSalesTaxPct(salesTaxPct)
        }
    }
    
    fun updateSalesTaxState(state: String) {
        viewModelScope.launch {
            profileRepository.updateSalesTaxState(state)
        }
    }
    
    fun updateIncludeSalesTaxInCalc(include: Boolean) {
        viewModelScope.launch {
            profileRepository.updateIncludeSalesTaxInCalc(include)
        }
    }
    
    fun updateDefaultDisplayUnit(unit: DisplayUnit) {
        viewModelScope.launch {
            profileRepository.updateDefaultDisplayUnit(unit)
        }
    }
    
    fun updateTheme(theme: Theme) {
        viewModelScope.launch {
            profileRepository.updateTheme(theme)
        }
    }
    
    fun updateAutoLogChecks(autoLog: Boolean) {
        viewModelScope.launch {
            profileRepository.updateAutoLogChecks(autoLog)
        }
    }
    
    fun updateHistoryLimit(limit: Int) {
        viewModelScope.launch {
            profileRepository.updateHistoryLimit(limit)
        }
    }
    
    fun updatePrivacyHideDollarsInHistory(hide: Boolean) {
        viewModelScope.launch {
            profileRepository.updatePrivacyHideDollarsInHistory(hide)
        }
    }
    
    fun clearHistory() {
        viewModelScope.launch {
            historyRepository.clearAllHistory()
        }
    }
    
    fun exportData(): String {
        // This would typically export to a file, but for now we'll return a JSON string
        // In a real implementation, you'd use Android's file system APIs
        return """
        {
            "exportDate": "${java.util.Date()}",
            "profile": {
                "hourlyRate": ${profile.value.hourlyRate},
                "annualSalary": ${profile.value.annualSalary},
                "hoursPerWeek": ${profile.value.hoursPerWeek},
                "effectiveTaxPct": ${profile.value.effectiveTaxPct},
                "incomeType": "${profile.value.incomeType}",
                "salesTaxMode": "${profile.value.salesTaxMode}",
                "salesTaxPct": ${profile.value.salesTaxPct},
                "salesTaxState": "${profile.value.salesTaxState}",
                "includeSalesTaxInCalc": ${profile.value.includeSalesTaxInCalc},
                "theme": "${profile.value.theme}",
                "defaultDisplayUnit": "${profile.value.defaultDisplayUnit}",
                "autoLogChecks": ${profile.value.autoLogChecks},
                "historyLimit": ${profile.value.historyLimit},
                "privacyHideDollarsInHistory": ${profile.value.privacyHideDollarsInHistory}
            }
        }
        """.trimIndent()
    }
}
