package com.worthmytime.data.repo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.worthmytime.data.datastore.ProfilePreferences
import com.worthmytime.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "profile")

@Singleton
class ProfileRepository @Inject constructor(
    private val context: Context
) {
    
    val profile: Flow<Profile> = context.dataStore.data.map { preferences ->
        Profile(
            state = preferences[ProfilePreferences.STATE] ?: "NM",
            incomeType = IncomeType.valueOf(preferences[ProfilePreferences.INCOME_TYPE] ?: IncomeType.HOURLY.name),
            hourlyRate = preferences[ProfilePreferences.HOURLY_RATE] ?: 25.0,
            annualSalary = preferences[ProfilePreferences.ANNUAL_SALARY] ?: 50000.0,
            effectiveTaxPct = preferences[ProfilePreferences.EFFECTIVE_TAX_PCT] ?: 25.0,
            hoursPerWeek = preferences[ProfilePreferences.HOURS_PER_WEEK] ?: 40.0,
            lifestyleBenchmarks = LifestyleBenchmarks(
                coffee = preferences[ProfilePreferences.COFFEE_COST] ?: 5.0,
                groceriesDay = preferences[ProfilePreferences.GROCERIES_PER_DAY] ?: 15.0,
                streamingMonth = preferences[ProfilePreferences.STREAMING_PER_MONTH] ?: 12.0
            ),
            jobRole = preferences[ProfilePreferences.JOB_ROLE],
            salesTaxMode = SalesTaxMode.valueOf(preferences[ProfilePreferences.SALES_TAX_MODE] ?: SalesTaxMode.MANUAL.name),
            salesTaxPct = preferences[ProfilePreferences.SALES_TAX_PCT] ?: 0.0,
            salesTaxState = preferences[ProfilePreferences.SALES_TAX_STATE] ?: "NM",
            includeSalesTaxInCalc = preferences[ProfilePreferences.INCLUDE_SALES_TAX_IN_CALC] ?: true,
            defaultDisplayUnit = DisplayUnit.valueOf(preferences[ProfilePreferences.DEFAULT_DISPLAY_UNIT] ?: DisplayUnit.HOURS.name),
            theme = Theme.valueOf(preferences[ProfilePreferences.THEME] ?: Theme.SYSTEM.name),
            autoLogChecks = preferences[ProfilePreferences.AUTO_LOG_CHECKS] ?: false,
            historyLimit = preferences[ProfilePreferences.HISTORY_LIMIT] ?: 200,
            privacyHideDollarsInHistory = preferences[ProfilePreferences.PRIVACY_HIDE_DOLLARS_IN_HISTORY] ?: false
        )
    }
    
    suspend fun updateState(state: String) {
        context.dataStore.edit { preferences ->
            preferences[ProfilePreferences.STATE] = state
        }
    }
    
    suspend fun updateIncomeType(incomeType: IncomeType) {
        context.dataStore.edit { preferences ->
            preferences[ProfilePreferences.INCOME_TYPE] = incomeType.name
        }
    }
    
    suspend fun updateHourlyRate(hourlyRate: Double) {
        context.dataStore.edit { preferences ->
            preferences[ProfilePreferences.HOURLY_RATE] = hourlyRate
        }
    }
    
    suspend fun updateAnnualSalary(annualSalary: Double) {
        context.dataStore.edit { preferences ->
            preferences[ProfilePreferences.ANNUAL_SALARY] = annualSalary
        }
    }
    
    suspend fun updateEffectiveTaxPct(effectiveTaxPct: Double) {
        context.dataStore.edit { preferences ->
            preferences[ProfilePreferences.EFFECTIVE_TAX_PCT] = effectiveTaxPct
        }
    }
    
    suspend fun updateHoursPerWeek(hoursPerWeek: Double) {
        context.dataStore.edit { preferences ->
            preferences[ProfilePreferences.HOURS_PER_WEEK] = hoursPerWeek
        }
    }
    
    suspend fun updateJobRole(jobRole: String?) {
        context.dataStore.edit { preferences ->
            if (jobRole != null) {
                preferences[ProfilePreferences.JOB_ROLE] = jobRole
            } else {
                preferences.remove(ProfilePreferences.JOB_ROLE)
            }
        }
    }
    
    suspend fun updateLifestyleBenchmarks(benchmarks: LifestyleBenchmarks) {
        context.dataStore.edit { preferences ->
            preferences[ProfilePreferences.COFFEE_COST] = benchmarks.coffee
            preferences[ProfilePreferences.GROCERIES_PER_DAY] = benchmarks.groceriesDay
            preferences[ProfilePreferences.STREAMING_PER_MONTH] = benchmarks.streamingMonth
        }
    }
    
    suspend fun updateSalesTaxMode(mode: SalesTaxMode) {
        context.dataStore.edit { preferences ->
            preferences[ProfilePreferences.SALES_TAX_MODE] = mode.name
        }
    }
    
    suspend fun updateSalesTaxPct(salesTaxPct: Double) {
        context.dataStore.edit { preferences ->
            preferences[ProfilePreferences.SALES_TAX_PCT] = salesTaxPct
        }
    }
    
    suspend fun updateSalesTaxState(state: String) {
        context.dataStore.edit { preferences ->
            preferences[ProfilePreferences.SALES_TAX_STATE] = state
        }
    }
    
    suspend fun updateIncludeSalesTaxInCalc(include: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ProfilePreferences.INCLUDE_SALES_TAX_IN_CALC] = include
        }
    }
    
    suspend fun updateDefaultDisplayUnit(unit: DisplayUnit) {
        context.dataStore.edit { preferences ->
            preferences[ProfilePreferences.DEFAULT_DISPLAY_UNIT] = unit.name
        }
    }
    
    suspend fun updateTheme(theme: Theme) {
        context.dataStore.edit { preferences ->
            preferences[ProfilePreferences.THEME] = theme.name
        }
    }
    
    suspend fun updateAutoLogChecks(autoLog: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ProfilePreferences.AUTO_LOG_CHECKS] = autoLog
        }
    }
    
    suspend fun updateHistoryLimit(limit: Int) {
        context.dataStore.edit { preferences ->
            preferences[ProfilePreferences.HISTORY_LIMIT] = limit
        }
    }
    
    suspend fun updatePrivacyHideDollarsInHistory(hide: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ProfilePreferences.PRIVACY_HIDE_DOLLARS_IN_HISTORY] = hide
        }
    }
}
