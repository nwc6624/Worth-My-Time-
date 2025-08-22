package com.worthmytime.ui.home

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
import androidx.compose.material3.ExperimentalMaterial3Api
import com.worthmytime.R
import com.worthmytime.domain.logic.Calculator
import com.worthmytime.domain.model.Decision
import com.worthmytime.domain.model.DisplayUnit
import com.worthmytime.ui.viewmodel.CalculatorViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: CalculatorViewModel = hiltViewModel()
) {
    val calculationResult by viewModel.calculationResult.collectAsState()
    val recentHistory by viewModel.recentHistory.collectAsState()
    val price by viewModel.price.collectAsState()
    val selectedDisplayUnit by remember { derivedStateOf { viewModel.selectedDisplayUnit } }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Header
            Column {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = stringResource(R.string.home_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        item {
            // Input Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = price,
                        onValueChange = { viewModel.updatePrice(it) },
                        label = { Text(stringResource(R.string.price_hint)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Unit selection chips
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DisplayUnit.values().forEach { unit ->
                            FilterChip(
                                onClick = { viewModel.updateDisplayUnit(unit) },
                                label = { Text(stringResource(getUnitStringRes(unit))) },
                                selected = selectedDisplayUnit == unit
                            )
                        }
                    }
                    
                    // Action buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.onCheck() },
                            modifier = Modifier.weight(1f),
                            enabled = price.isNotEmpty() && price.toDoubleOrNull() != null
                        ) {
                            Text(stringResource(R.string.check_button))
                        }
                        
                        OutlinedButton(
                            onClick = { viewModel.onAddAsGoal() },
                            modifier = Modifier.weight(1f),
                            enabled = price.isNotEmpty() && price.toDoubleOrNull() != null
                        ) {
                            Text(stringResource(R.string.add_as_goal_button))
                        }
                    }
                }
            }
        }
        
        // Results
        calculationResult?.let { result ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Main result
                        Text(
                            text = "${viewModel.getDisplayValue(result)} ${viewModel.getDisplayUnitString()}",
                            style = MaterialTheme.typography.headlineLarge
                        )
                        
                        // Secondary units
                        Text(
                            text = "≈ ${Calculator.roundWorkUnits(result.workdays)} workdays • ${Calculator.roundWorkUnits(result.workweeks)} workweeks",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        // Net hourly
                        Text(
                            text = stringResource(R.string.net_hourly_label, result.netHourly),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        // Sales tax info
                        if (result.resolvedSalesTaxPct > 0) {
                            Text(
                                text = stringResource(R.string.tax_included, result.resolvedSalesTaxPct, result.effectivePrice),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.tax_not_included),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        // Lifestyle equivalents
                        Text(
                            text = stringResource(
                                R.string.lifestyle_equivalents,
                                result.equivalents.first,
                                result.equivalents.second,
                                result.equivalents.third
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        // Recent history
        if (recentHistory.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.recent_checks_title),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            items(recentHistory) { historyItem ->
                HistoryItem(
                    historyItem = historyItem,
                    onTap = { viewModel.onHistoryTap(historyItem) },
                    onDecision = { decision -> viewModel.onHistoryDecision(historyItem.id, decision) },
                    onDelete = { viewModel.onHistoryDelete(historyItem.id) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryItem(
    historyItem: com.worthmytime.data.db.entities.HistoryEntity,
    onTap: () -> Unit,
    onDecision: (Decision?) -> Unit,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()) }
    val numberFormat = remember { NumberFormat.getCurrencyInstance() }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onTap
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${numberFormat.format(historyItem.priceBeforeTax)} (+${historyItem.salesTaxPctAtCheck}% → ${numberFormat.format(historyItem.priceWithTax)}) → ${Calculator.roundHours(historyItem.hours)}h",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "≈ ${Calculator.roundWorkUnits(historyItem.workdays)} days • ${Calculator.roundWorkUnits(historyItem.workweeks)} weeks • ${dateFormat.format(Date(historyItem.createdAt))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                FilterChip(
                    onClick = { onDecision(Decision.WORTH) },
                    label = { Text(stringResource(R.string.worth_choice)) },
                    selected = historyItem.decision == Decision.WORTH
                )
                FilterChip(
                    onClick = { onDecision(Decision.NOT_WORTH) },
                    label = { Text(stringResource(R.string.not_worth_choice)) },
                    selected = historyItem.decision == Decision.NOT_WORTH
                )
            }
        }
    }
}

private fun getUnitStringRes(unit: DisplayUnit): Int {
    return when (unit) {
        DisplayUnit.HOURS -> R.string.hours_unit
        DisplayUnit.DAYS -> R.string.days_unit
        DisplayUnit.WEEKS -> R.string.weeks_unit
    }
}
