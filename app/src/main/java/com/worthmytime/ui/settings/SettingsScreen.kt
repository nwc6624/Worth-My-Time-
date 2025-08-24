package com.worthmytime.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.text.font.FontWeight

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
        
        item {
            DisplaySection(
                profile = profile,
                onDefaultDisplayUnitChange = viewModel::updateDefaultDisplayUnit,
                onThemeChange = viewModel::updateTheme
            )
        }
        
        item {
            BehaviorSection(
                profile = profile,
                onAutoLogChecksChange = viewModel::updateAutoLogChecks,
                onHistoryLimitChange = viewModel::updateHistoryLimit,
                onClearHistory = { viewModel.clearHistory() }
            )
        }
        
        item {
            PrivacySection(
                profile = profile,
                onHideDollarsChange = viewModel::updatePrivacyHideDollarsInHistory,
                onExportData = { viewModel.exportData() }
            )
        }
        
        item {
            AboutSection()
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
    var showStateDialog by remember { mutableStateOf(false) }
    
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
                    OutlinedTextField(
                        value = profile.salesTaxState.ifEmpty { "Select State" },
                        onValueChange = { },
                        label = { Text(stringResource(R.string.select_state)) },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showStateDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Select state"
                                )
                            }
                        }
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
    
    // State selection dialog
    if (showStateDialog) {
        StateSelectionDialog(
            currentState = profile.salesTaxState,
            onStateSelected = { state ->
                onSalesTaxStateChange(state)
                showStateDialog = false
            },
            onDismiss = { showStateDialog = false }
        )
    }
}

@Composable
fun StateSelectionDialog(
    currentState: String,
    onStateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val states = listOf(
        "Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut",
        "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa",
        "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan",
        "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire",
        "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio",
        "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota",
        "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia",
        "Wisconsin", "Wyoming"
    )
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select State") },
        text = {
            LazyColumn(
                modifier = Modifier.heightIn(max = 400.dp)
            ) {
                items(states) { state ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onStateSelected(state) }
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = state,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (currentState == state) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplaySection(
    profile: com.worthmytime.domain.model.Profile,
    onDefaultDisplayUnitChange: (com.worthmytime.domain.model.DisplayUnit) -> Unit,
    onThemeChange: (com.worthmytime.domain.model.Theme) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.display_section),
                style = MaterialTheme.typography.titleMedium
            )
            
            // Default display unit
            Column {
                Text(
                    text = stringResource(R.string.default_unit),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    com.worthmytime.domain.model.DisplayUnit.values().forEach { unit ->
                        FilterChip(
                            onClick = { onDefaultDisplayUnitChange(unit) },
                            label = { Text(unit.name.lowercase().capitalize()) },
                            selected = profile.defaultDisplayUnit == unit
                        )
                    }
                }
            }
            
            // Theme selection
            Column {
                Text(
                    text = stringResource(R.string.theme),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    com.worthmytime.domain.model.Theme.values().forEach { theme ->
                        FilterChip(
                            onClick = { onThemeChange(theme) },
                            label = { Text(getThemeDisplayName(theme)) },
                            selected = profile.theme == theme
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BehaviorSection(
    profile: com.worthmytime.domain.model.Profile,
    onAutoLogChecksChange: (Boolean) -> Unit,
    onHistoryLimitChange: (Int) -> Unit,
    onClearHistory: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.behavior_section),
                style = MaterialTheme.typography.titleMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.auto_log_checks))
                Switch(
                    checked = profile.autoLogChecks,
                    onCheckedChange = onAutoLogChecksChange
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.history_limit))
                Text("${profile.historyLimit} items")
            }
            
            OutlinedButton(
                onClick = onClearHistory,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.clear_history))
            }
        }
    }
}

@Composable
fun PrivacySection(
    profile: com.worthmytime.domain.model.Profile,
    onHideDollarsChange: (Boolean) -> Unit,
    onExportData: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Privacy & Data",
                style = MaterialTheme.typography.titleMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.hide_dollars_in_history))
                Switch(
                    checked = profile.privacyHideDollarsInHistory,
                    onCheckedChange = onHideDollarsChange
                )
            }
            
            OutlinedButton(
                onClick = onExportData,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.export_data))
            }
            
            Text(
                text = stringResource(R.string.storage_privacy_note),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AboutSection() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.about_section),
                style = MaterialTheme.typography.titleMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.version))
                Text("1.0.0")
            }
            
            Text(
                text = "Worth My Time helps you understand the true cost of purchases in terms of your time and effort.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun getThemeDisplayName(theme: com.worthmytime.domain.model.Theme): String {
    return when (theme) {
        com.worthmytime.domain.model.Theme.SYSTEM -> stringResource(R.string.system_theme)
        com.worthmytime.domain.model.Theme.LIGHT -> stringResource(R.string.light_theme)
        com.worthmytime.domain.model.Theme.DARK -> stringResource(R.string.dark_theme)
    }
}
