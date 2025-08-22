package com.worthmytime.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.worthmytime.domain.model.GoalCategory

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey
    val id: String,
    val label: String,
    val price: Double,
    val category: GoalCategory,
    val savedDollars: Double = 0.0,
    val createdAt: Long,
    val useTaxSnapshot: Boolean = false,
    val salesTaxPctAtCreation: Double? = null
)
