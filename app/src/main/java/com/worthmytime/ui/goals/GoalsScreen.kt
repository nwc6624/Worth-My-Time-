package com.worthmytime.ui.goals

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
import com.worthmytime.domain.logic.Calculator
import com.worthmytime.domain.model.GoalCategory
import com.worthmytime.ui.viewmodel.GoalsViewModel
import com.worthmytime.ui.viewmodel.GoalWithProgress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    viewModel: GoalsViewModel = hiltViewModel()
) {
    val goalsWithProgress by viewModel.goalsWithProgress.collectAsState()
    var showAddGoalDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header with add button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.goals_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Track your savings goals and progress",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            FloatingActionButton(
                onClick = { showAddGoalDialog = true },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add goal"
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        if (goalsWithProgress.isEmpty()) {
            // Empty state
            EmptyGoalsState()
        } else {
            // Goals list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(goalsWithProgress) { goalWithProgress ->
                    GoalCard(
                        goalWithProgress = goalWithProgress,
                        onDelete = { viewModel.deleteGoal(goalWithProgress.goal.id) },
                        onEdit = { viewModel.updateGoal(it) },
                        onAddSavedDollars = { amount ->
                            viewModel.addSavedDollars(goalWithProgress.goal.id, amount)
                        }
                    )
                }
            }
        }
    }
    
    // Add goal dialog
    if (showAddGoalDialog) {
        AddGoalDialog(
            onGoalAdded = { label, price, category ->
                viewModel.addGoal(label, price, category)
                showAddGoalDialog = false
            },
            onDismiss = { showAddGoalDialog = false }
        )
    }
}

@Composable
fun EmptyGoalsState() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Flag,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "No goals yet",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Add items as goals from the Home screen or create custom goals to start tracking your savings progress",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalCard(
    goalWithProgress: GoalWithProgress,
    onDelete: () -> Unit,
    onEdit: (com.worthmytime.data.db.entities.GoalEntity) -> Unit,
    onAddSavedDollars: (Double) -> Unit
) {
    var showAddSavedDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var savedAmountText by remember { mutableStateOf("") }
    
    // Check if goal is completed
    val isCompleted = goalWithProgress.goal.savedDollars >= goalWithProgress.goal.price
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header row with title and actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = goalWithProgress.goal.label,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = goalWithProgress.goal.category.name.lowercase().capitalize(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Completed",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit goal",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete goal",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Price and progress info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Goal: $${goalWithProgress.goal.price}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Text(
                        text = "Saved: $${goalWithProgress.goal.savedDollars}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                if (!isCompleted) {
                    Button(
                        onClick = { showAddSavedDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Saved")
                    }
                } else {
                    Text(
                        text = "Goal Completed! 🎉",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Progress bar
            val progress = if (goalWithProgress.goal.price > 0) {
                (goalWithProgress.goal.savedDollars / goalWithProgress.goal.price).coerceIn(0.0, 1.0)
            } else 0.0
            
            LinearProgressIndicator(
                progress = progress.toFloat(),
                modifier = Modifier.fillMaxWidth(),
                color = if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Progress percentage
            Text(
                text = "${(progress * 100).toInt()}% complete",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            // Time equivalents
            if (goalWithProgress.hoursNeeded > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                val remainingHours = (goalWithProgress.hoursNeeded - goalWithProgress.hoursBanked).coerceAtLeast(0.0)
                val remainingWorkdays = Calculator.hoursToWorkdays(remainingHours)
                Text(
                    text = if (isCompleted) {
                        "Goal achieved! 🎉"
                    } else {
                        "Time remaining: ${remainingHours.toInt()} hours (${remainingWorkdays.toInt()} workdays)"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
    
    // Add saved amount dialog
    if (showAddSavedDialog) {
        AlertDialog(
            onDismissRequest = { showAddSavedDialog = false },
            title = { Text("Add Saved Amount") },
            text = {
                OutlinedTextField(
                    value = savedAmountText,
                    onValueChange = { savedAmountText = it },
                    label = { Text("Amount saved") },
                    prefix = { Text("$") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        savedAmountText.toDoubleOrNull()?.let { amount ->
                            onAddSavedDollars(amount)
                        }
                        showAddSavedDialog = false
                        savedAmountText = ""
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAddSavedDialog = false
                        savedAmountText = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    
    // Edit goal dialog
    if (showEditDialog) {
        EditGoalDialog(
            goal = goalWithProgress.goal,
            onGoalUpdated = { updatedGoal ->
                onEdit(updatedGoal)
                showEditDialog = false
            },
            onDismiss = { showEditDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalDialog(
    onGoalAdded: (String, Double, GoalCategory) -> Unit,
    onDismiss: () -> Unit
) {
    var labelText by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(GoalCategory.LUXURY) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Goal") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = labelText,
                    onValueChange = { labelText = it },
                    label = { Text("Goal name") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("Target amount") },
                    prefix = { Text("$") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Text(
                    text = "Category",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GoalCategory.values().forEach { category ->
                        FilterChip(
                            onClick = { selectedCategory = category },
                            label = { Text(category.name.lowercase().capitalize()) },
                            selected = selectedCategory == category
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val price = priceText.toDoubleOrNull()
                    if (labelText.isNotEmpty() && price != null && price > 0) {
                        onGoalAdded(labelText, price, selectedCategory)
                    }
                },
                enabled = labelText.isNotEmpty() && priceText.toDoubleOrNull() != null && priceText.toDoubleOrNull()!! > 0
            ) {
                Text("Add Goal")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGoalDialog(
    goal: com.worthmytime.data.db.entities.GoalEntity,
    onGoalUpdated: (com.worthmytime.data.db.entities.GoalEntity) -> Unit,
    onDismiss: () -> Unit
) {
    var labelText by remember { mutableStateOf(goal.label) }
    var priceText by remember { mutableStateOf(goal.price.toString()) }
    var selectedCategory by remember { mutableStateOf(goal.category) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Goal") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = labelText,
                    onValueChange = { labelText = it },
                    label = { Text("Goal name") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("Target amount") },
                    prefix = { Text("$") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Text(
                    text = "Category",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GoalCategory.values().forEach { category ->
                        FilterChip(
                            onClick = { selectedCategory = category },
                            label = { Text(category.name.lowercase().capitalize()) },
                            selected = selectedCategory == category
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val price = priceText.toDoubleOrNull()
                    if (labelText.isNotEmpty() && price != null && price > 0) {
                        val updatedGoal = goal.copy(
                            label = labelText,
                            price = price,
                            category = selectedCategory
                        )
                        onGoalUpdated(updatedGoal)
                    }
                },
                enabled = labelText.isNotEmpty() && priceText.toDoubleOrNull() != null && priceText.toDoubleOrNull()!! > 0
            ) {
                Text("Save Changes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun String.capitalize(): String {
    return if (isNotEmpty()) {
        this[0].uppercase() + substring(1)
    } else {
        this
    }
}
