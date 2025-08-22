package com.worthmytime.data.datastore

import androidx.datastore.preferences.core.*
import com.worthmytime.domain.model.*

object ProfilePreferences {
    
    // Profile keys
    val STATE = stringPreferencesKey("state")
    val INCOME_TYPE = stringPreferencesKey("income_type")
    val HOURLY_RATE = doublePreferencesKey("hourly_rate")
    val ANNUAL_SALARY = doublePreferencesKey("annual_salary")
    val EFFECTIVE_TAX_PCT = doublePreferencesKey("effective_tax_pct")
    val HOURS_PER_WEEK = doublePreferencesKey("hours_per_week")
    val JOB_ROLE = stringPreferencesKey("job_role")
    
    // Lifestyle benchmarks
    val COFFEE_COST = doublePreferencesKey("coffee_cost")
    val GROCERIES_PER_DAY = doublePreferencesKey("groceries_per_day")
    val STREAMING_PER_MONTH = doublePreferencesKey("streaming_per_month")
    
    // Sales tax settings
    val SALES_TAX_MODE = stringPreferencesKey("sales_tax_mode")
    val SALES_TAX_PCT = doublePreferencesKey("sales_tax_pct")
    val SALES_TAX_STATE = stringPreferencesKey("sales_tax_state")
    val INCLUDE_SALES_TAX_IN_CALC = booleanPreferencesKey("include_sales_tax_in_calc")
    
    // Display settings
    val DEFAULT_DISPLAY_UNIT = stringPreferencesKey("default_display_unit")
    val THEME = stringPreferencesKey("theme")
    
    // Behavior settings
    val AUTO_LOG_CHECKS = booleanPreferencesKey("auto_log_checks")
    val HISTORY_LIMIT = intPreferencesKey("history_limit")
    val PRIVACY_HIDE_DOLLARS_IN_HISTORY = booleanPreferencesKey("privacy_hide_dollars_in_history")
    
    // Default values
    fun getDefaultProfile(): Profile {
        return Profile(
            state = "NM",
            incomeType = IncomeType.HOURLY,
            hourlyRate = 25.0,
            annualSalary = 50000.0,
            effectiveTaxPct = 25.0,
            hoursPerWeek = 40.0,
            lifestyleBenchmarks = LifestyleBenchmarks(),
            jobRole = null,
            salesTaxMode = SalesTaxMode.MANUAL,
            salesTaxPct = 0.0,
            salesTaxState = "NM",
            includeSalesTaxInCalc = true,
            defaultDisplayUnit = DisplayUnit.HOURS,
            theme = Theme.SYSTEM,
            autoLogChecks = false,
            historyLimit = 200,
            privacyHideDollarsInHistory = false
        )
    }
}
