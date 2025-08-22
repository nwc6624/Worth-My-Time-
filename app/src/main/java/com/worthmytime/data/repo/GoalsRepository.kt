package com.worthmytime.data.repo

import com.worthmytime.data.db.dao.GoalDao
import com.worthmytime.data.db.entities.GoalEntity
import com.worthmytime.domain.model.GoalCategory
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoalsRepository @Inject constructor(
    private val goalDao: GoalDao
) {
    
    fun observeGoals(): Flow<List<GoalEntity>> = goalDao.observeGoals()
    
    suspend fun addGoal(
        label: String,
        price: Double,
        category: GoalCategory,
        useTaxSnapshot: Boolean = false,
        salesTaxPctAtCreation: Double? = null
    ) {
        val goal = GoalEntity(
            id = UUID.randomUUID().toString(),
            label = label,
            price = price,
            category = category,
            createdAt = System.currentTimeMillis(),
            useTaxSnapshot = useTaxSnapshot,
            salesTaxPctAtCreation = salesTaxPctAtCreation
        )
        goalDao.insert(goal)
    }
    
    suspend fun updateGoal(goal: GoalEntity) {
        goalDao.update(goal)
    }
    
    suspend fun updateSavedDollars(goalId: String, savedDollars: Double) {
        goalDao.updateSavedDollars(goalId, savedDollars)
    }
    
    suspend fun updateTaxSnapshot(
        goalId: String,
        useTaxSnapshot: Boolean,
        salesTaxPctAtCreation: Double?
    ) {
        goalDao.updateTaxSnapshot(goalId, useTaxSnapshot, salesTaxPctAtCreation)
    }
    
    suspend fun deleteGoal(goalId: String) {
        goalDao.deleteById(goalId)
    }
}
