package com.worthmytime.data.db.dao

import androidx.room.*
import com.worthmytime.data.db.entities.GoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    
    @Query("SELECT * FROM goals ORDER BY createdAt DESC")
    fun observeGoals(): Flow<List<GoalEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: GoalEntity)
    
    @Update
    suspend fun update(goal: GoalEntity)
    
    @Query("UPDATE goals SET savedDollars = :savedDollars WHERE id = :goalId")
    suspend fun updateSavedDollars(goalId: String, savedDollars: Double)
    
    @Query("UPDATE goals SET useTaxSnapshot = :useTaxSnapshot, salesTaxPctAtCreation = :salesTaxPct WHERE id = :goalId")
    suspend fun updateTaxSnapshot(goalId: String, useTaxSnapshot: Boolean, salesTaxPct: Double?)
    
    @Delete
    suspend fun delete(goal: GoalEntity)
    
    @Query("DELETE FROM goals WHERE id = :goalId")
    suspend fun deleteById(goalId: String)
}
