package com.worthmytime.data.db.dao

import androidx.room.*
import com.worthmytime.data.db.entities.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    
    @Query("SELECT * FROM history ORDER BY createdAt DESC LIMIT :limit")
    fun observeRecent(limit: Int): Flow<List<HistoryEntity>>
    
    @Query("SELECT * FROM history ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<HistoryEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historyItem: HistoryEntity)
    
    @Query("UPDATE history SET decision = :decision WHERE id = :historyId")
    suspend fun updateDecision(historyId: String, decision: String?)
    
    @Delete
    suspend fun delete(historyItem: HistoryEntity)
    
    @Query("DELETE FROM history WHERE id = :historyId")
    suspend fun deleteById(historyId: String)
    
    @Query("DELETE FROM history")
    suspend fun clearAll()
}
