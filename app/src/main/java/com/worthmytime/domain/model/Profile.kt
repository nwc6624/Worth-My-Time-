package com.worthmytime.domain.model

data class Profile(
    val state: String = "NM",
    val incomeType: IncomeType = IncomeType.HOURLY,
    val hourlyRate: Double = 25.0,
    val annualSalary: Double = 50000.0,
    val effectiveTaxPct: Double = 25.0,
    val hoursPerWeek: Double = 40.0,
    val lifestyleBenchmarks: LifestyleBenchmarks = LifestyleBenchmarks(),
    val jobRole: String? = null,
    val salesTaxMode: SalesTaxMode = SalesTaxMode.MANUAL,
    val salesTaxPct: Double = 0.0,
    val salesTaxState: String = "NM",
    val includeSalesTaxInCalc: Boolean = true,
    val defaultDisplayUnit: DisplayUnit = DisplayUnit.HOURS,
    val theme: Theme = Theme.SYSTEM,
    val autoLogChecks: Boolean = false,
    val historyLimit: Int = 200,
    val privacyHideDollarsInHistory: Boolean = false
)
