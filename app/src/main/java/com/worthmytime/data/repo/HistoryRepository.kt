package com.worthmytime.data.repo

import com.worthmytime.data.db.dao.HistoryDao
import com.worthmytime.data.db.entities.HistoryEntity
import com.worthmytime.domain.model.Decision
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepository @Inject constructor(
    private val historyDao: HistoryDao
) {
    
    fun observeRecent(limit: Int): Flow<List<HistoryEntity>> = historyDao.observeRecent(limit)
    
    fun observeAll(): Flow<List<HistoryEntity>> = historyDao.observeAll()
    
    suspend fun insertHistoryItem(
        priceBeforeTax: Double,
        salesTaxPctAtCheck: Double,
        priceWithTax: Double,
        netHourlyAtCheck: Double,
        hours: Double,
        workdays: Double,
        workweeks: Double
    ) {
        val historyItem = HistoryEntity(
            id = UUID.randomUUID().toString(),
            priceBeforeTax = priceBeforeTax,
            salesTaxPctAtCheck = salesTaxPctAtCheck,
            priceWithTax = priceWithTax,
            netHourlyAtCheck = netHourlyAtCheck,
            hours = hours,
            workdays = workdays,
            workweeks = workweeks,
            createdAt = System.currentTimeMillis()
        )
        historyDao.insert(historyItem)
    }
    
    suspend fun updateDecision(historyId: String, decision: Decision?) {
        historyDao.updateDecision(historyId, decision?.name)
    }
    
    suspend fun deleteHistoryItem(historyId: String) {
        historyDao.deleteById(historyId)
    }
    
    suspend fun clearAllHistory() {
        historyDao.clearAll()
    }
}
