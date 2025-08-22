package com.worthmytime.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.worthmytime.R
import com.worthmytime.domain.model.IncomeType
import com.worthmytime.domain.model.SalesTaxMode
import com.worthmytime.ui.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profile by viewModel.profile.collectAsState()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.headlineMedium
            )
        }
        
        item {
            IncomeSection(
                profile = profile,
                onIncomeTypeChange = viewModel::updateIncomeType,
                onHourlyRateChange = viewModel::updateHourlyRate,
                onAnnualSalaryChange = viewModel::updateAnnualSalary,
                onHoursPerWeekChange = viewModel::updateHoursPerWeek,
                onEffectiveTaxPctChange = viewModel::updateEffectiveTaxPct
            )
        }
        
        item {
            SalesTaxSection(
                profile = profile,
                onSalesTaxModeChange = viewModel::updateSalesTaxMode,
                onSalesTaxPctChange = viewModel::updateSalesTaxPct,
                onSalesTaxStateChange = viewModel::updateSalesTaxState,
                onIncludeSalesTaxChange = viewModel::updateIncludeSalesTaxInCalc
            )
        }
        
        item {
            LifestyleBenchmarksSection(
                profile = profile,
                onBenchmarksChange = viewModel::updateLifestyleBenchmarks
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeSection(
    profile: com.worthmytime.domain.model.Profile,
    onIncomeTypeChange: (IncomeType) -> Unit,
    onHourlyRateChange: (Double) -> Unit,
    onAnnualSalaryChange: (Double) -> Unit,
    onHoursPerWeekChange: (Double) -> Unit,
    onEffectiveTaxPctChange: (Double) -> Unit
) {
    var hourlyRateText by remember(profile.hourlyRate) { mutableStateOf(profile.hourlyRate.toString()) }
    var annualSalaryText by remember(profile.annualSalary) { mutableStateOf(profile.annualSalary.toString()) }
    var hoursPerWeekText by remember(profile.hoursPerWeek) { mutableStateOf(profile.hoursPerWeek.toString()) }
    var taxPctText by remember(profile.effectiveTaxPct) { mutableStateOf(profile.effectiveTaxPct.toString()) }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.income_section),
                style = MaterialTheme.typography.titleMedium
            )
            
            // Income Type Toggle
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    onClick = { onIncomeTypeChange(IncomeType.HOURLY) },
                    label = { Text("Hourly") },
                    selected = profile.incomeType == IncomeType.HOURLY
                )
                FilterChip(
                    onClick = { onIncomeTypeChange(IncomeType.ANNUAL) },
                    label = { Text("Annual") },
                    selected = profile.incomeType == IncomeType.ANNUAL
                )
            }
            
            // Income Input
            if (profile.incomeType == IncomeType.HOURLY) {
                OutlinedTextField(
                    value = hourlyRateText,
                    onValueChange = { 
                        hourlyRateText = it
                        it.toDoubleOrNull()?.let { rate -> onHourlyRateChange(rate) }
                    },
                    label = { Text(stringResource(R.string.hourly_rate)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    prefix = { Text("$") }
                )
            } else {
                OutlinedTextField(
                    value = annualSalaryText,
                    onValueChange = { 
                        annualSalaryText = it
                        it.toDoubleOrNull()?.let { salary -> onAnnualSalaryChange(salary) }
                    },
                    label = { Text(stringResource(R.string.annual_salary)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    prefix = { Text("$") }
                )
            }
            
            OutlinedTextField(
                value = hoursPerWeekText,
                onValueChange = { 
                    hoursPerWeekText = it
                    it.toDoubleOrNull()?.let { hours -> onHoursPerWeekChange(hours) }
                },
                label = { Text(stringResource(R.string.hours_per_week)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = taxPctText,
                onValueChange = { 
                    taxPctText = it
                    it.toDoubleOrNull()?.let { tax -> onEffectiveTaxPctChange(tax) }
                },
                label = { Text(stringResource(R.string.effective_tax_percent)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                suffix = { Text("%") }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesTaxSection(
    profile: com.worthmytime.domain.model.Profile,
    onSalesTaxModeChange: (SalesTaxMode) -> Unit,
    onSalesTaxPctChange: (Double) -> Unit,
    onSalesTaxStateChange: (String) -> Unit,
    onIncludeSalesTaxChange: (Boolean) -> Unit
) {
    var salesTaxPctText by remember(profile.salesTaxPct) { mutableStateOf(profile.salesTaxPct.toString()) }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.sales_tax_section),
                style = MaterialTheme.typography.titleMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.include_sales_tax))
                Switch(
                    checked = profile.includeSalesTaxInCalc,
                    onCheckedChange = onIncludeSalesTaxChange
                )
            }
            
            if (profile.includeSalesTaxInCalc) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        onClick = { onSalesTaxModeChange(SalesTaxMode.MANUAL) },
                        label = { Text(stringResource(R.string.manual_tax)) },
                        selected = profile.salesTaxMode == SalesTaxMode.MANUAL
                    )
                    FilterChip(
                        onClick = { onSalesTaxModeChange(SalesTaxMode.STATE) },
                        label = { Text(stringResource(R.string.by_state_tax)) },
                        selected = profile.salesTaxMode == SalesTaxMode.STATE
                    )
                }
                
                if (profile.salesTaxMode == SalesTaxMode.MANUAL) {
                    OutlinedTextField(
                        value = salesTaxPctText,
                        onValueChange = { 
                            salesTaxPctText = it
                            it.toDoubleOrNull()?.let { tax -> onSalesTaxPctChange(tax) }
                        },
                        label = { Text(stringResource(R.string.sales_tax_percent)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        suffix = { Text("%") }
                    )
                } else {
                    Text(
                        text = "Using ${profile.salesTaxState} tax rate",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = stringResource(R.string.state_tax_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun LifestyleBenchmarksSection(
    profile: com.worthmytime.domain.model.Profile,
    onBenchmarksChange: (com.worthmytime.domain.model.LifestyleBenchmarks) -> Unit
) {
    var coffeeText by remember(profile.lifestyleBenchmarks.coffee) { 
        mutableStateOf(profile.lifestyleBenchmarks.coffee.toString()) 
    }
    var groceriesText by remember(profile.lifestyleBenchmarks.groceriesDay) { 
        mutableStateOf(profile.lifestyleBenchmarks.groceriesDay.toString()) 
    }
    var streamingText by remember(profile.lifestyleBenchmarks.streamingMonth) { 
        mutableStateOf(profile.lifestyleBenchmarks.streamingMonth.toString()) 
    }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.lifestyle_benchmarks_section),
                style = MaterialTheme.typography.titleMedium
            )
            
            OutlinedTextField(
                value = coffeeText,
                onValueChange = { 
                    coffeeText = it
                    it.toDoubleOrNull()?.let { coffee -> 
                        onBenchmarksChange(
                            profile.lifestyleBenchmarks.copy(coffee = coffee)
                        )
                    }
                },
                label = { Text(stringResource(R.string.coffee_cost)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                prefix = { Text("$") }
            )
            
            OutlinedTextField(
                value = groceriesText,
                onValueChange = { 
                    groceriesText = it
                    it.toDoubleOrNull()?.let { groceries -> 
                        onBenchmarksChange(
                            profile.lifestyleBenchmarks.copy(groceriesDay = groceries)
                        )
                    }
                },
                label = { Text(stringResource(R.string.groceries_per_day)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                prefix = { Text("$") }
            )
            
            OutlinedTextField(
                value = streamingText,
                onValueChange = { 
                    streamingText = it
                    it.toDoubleOrNull()?.let { streaming -> 
                        onBenchmarksChange(
                            profile.lifestyleBenchmarks.copy(streamingMonth = streaming)
                        )
                    }
                },
                label = { Text(stringResource(R.string.streaming_per_month)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                prefix = { Text("$") }
            )
        }
    }
}
