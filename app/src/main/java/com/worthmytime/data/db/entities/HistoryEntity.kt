package com.worthmytime.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.worthmytime.domain.model.Decision

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey
    val id: String,
    val priceBeforeTax: Double,
    val salesTaxPctAtCheck: Double,
    val priceWithTax: Double,
    val netHourlyAtCheck: Double,
    val hours: Double,
    val workdays: Double,
    val workweeks: Double,
    val decision: Decision? = null,
    val createdAt: Long
)
