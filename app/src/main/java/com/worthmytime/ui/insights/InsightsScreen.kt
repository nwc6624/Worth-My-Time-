package com.worthmytime.ui.insights

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.worthmytime.R
import com.worthmytime.domain.model.Decision
import com.worthmytime.ui.viewmodel.InsightsViewModel
import com.worthmytime.ui.viewmodel.InsightsPeriod
import com.worthmytime.ui.viewmodel.InsightsData
import com.worthmytime.ui.viewmodel.CategorySpending
import com.worthmytime.ui.viewmodel.RecentActivity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    viewModel: InsightsViewModel = hiltViewModel()
) {
    val insightsData by viewModel.insightsData.collectAsState()
    val selectedPeriod by viewModel.selectedPeriod.collectAsState()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Header
            Text(
                text = stringResource(R.string.insights_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            // Period selector
            PeriodSelector(
                selectedPeriod = selectedPeriod,
                onPeriodSelected = viewModel::updateSelectedPeriod
            )
        }
        
        // Spending overview card
        item {
            SpendingOverviewCard(insightsData)
        }
        
        // Worth vs Not Worth breakdown
        item {
            WorthBreakdownCard(insightsData)
        }
        
        // Top spending categories
        if (insightsData.topCategories.isNotEmpty()) {
            item {
                TopCategoriesCard(insightsData.topCategories)
            }
        }
        
        // Savings tips
        item {
            SavingsTipsCard(insightsData)
        }
        
        // Recent activity
        if (insightsData.recentActivity.isNotEmpty()) {
            item {
                Text(
                    text = "Recent Activity",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            
            items(insightsData.recentActivity.take(5)) { activity ->
                RecentActivityItem(activity)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodSelector(
    selectedPeriod: InsightsPeriod,
    onPeriodSelected: (InsightsPeriod) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Time Period",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InsightsPeriod.values().forEach { period ->
                    FilterChip(
                        onClick = { onPeriodSelected(period) },
                        label = { Text(period.displayName) },
                        selected = selectedPeriod == period
                    )
                }
            }
        }
    }
}

@Composable
fun SpendingOverviewCard(data: InsightsData) {
    val numberFormat = remember { NumberFormat.getCurrencyInstance() }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Spending Overview",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Total Spent",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = numberFormat.format(data.totalSpent),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Time Value",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${data.totalHours.toInt()} hours",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Average per item",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = numberFormat.format(data.averagePerItem),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Items checked",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${data.totalItems}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun WorthBreakdownCard(data: InsightsData) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Worth Analysis",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                WorthItem(
                    label = "Worth It",
                    count = data.worthCount,
                    total = data.totalItems,
                    color = MaterialTheme.colorScheme.primary
                )
                
                WorthItem(
                    label = "Not Worth",
                    count = data.notWorthCount,
                    total = data.totalItems,
                    color = MaterialTheme.colorScheme.error
                )
                
                WorthItem(
                    label = "Undecided",
                    count = data.undecidedCount,
                    total = data.totalItems,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun WorthItem(
    label: String,
    count: Int,
    total: Int,
    color: androidx.compose.ui.graphics.Color
) {
    val percentage = if (total > 0) (count.toFloat() / total * 100) else 0f
    
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "$count",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "${percentage.toInt()}%",
            style = MaterialTheme.typography.bodySmall,
            color = color
        )
    }
}

@Composable
fun TopCategoriesCard(categories: List<CategorySpending>) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Top Spending Areas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            
            categories.forEach { category ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "${category.count} items",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun SavingsTipsCard(data: InsightsData) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Savings Tips",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            
            if (data.notWorthCount > data.worthCount) {
                TipItem(
                    icon = Icons.Default.Lightbulb,
                    title = "Consider your spending",
                    description = "You've marked more items as 'not worth it' than 'worth it'. Consider reviewing your purchasing decisions."
                )
            }
            
            if (data.averagePerItem > 50) {
                TipItem(
                    icon = Icons.Default.AttachMoney,
                    title = "High average spending",
                    description = "Your average item cost is ${NumberFormat.getCurrencyInstance().format(data.averagePerItem)}. Consider if you need everything you're buying."
                )
            }
            
            if (data.totalHours > 40) {
                TipItem(
                    icon = Icons.Default.Schedule,
                    title = "Significant time investment",
                    description = "You've spent ${data.totalHours.toInt()} hours worth of time on purchases. That's a full work week!"
                )
            }
            
            TipItem(
                icon = Icons.Default.TrendingUp,
                title = "Track your progress",
                description = "Keep using the app to build awareness of your spending habits and time value."
            )
        }
    }
}

@Composable
fun TipItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun RecentActivityItem(activity: RecentActivity) {
    val dateFormat = remember { SimpleDateFormat("MM/dd", Locale.getDefault()) }
    val numberFormat = remember { NumberFormat.getCurrencyInstance() }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = numberFormat.format(activity.price),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${activity.hours.toInt()} hours • ${dateFormat.format(Date(activity.timestamp))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            when (activity.decision) {
                Decision.WORTH -> {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "Worth it",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Decision.NOT_WORTH -> {
                    Icon(
                        imageVector = Icons.Default.ThumbDown,
                        contentDescription = "Not worth it",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
                null -> {
                    Icon(
                        imageVector = Icons.Default.Help,
                        contentDescription = "Undecided",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
