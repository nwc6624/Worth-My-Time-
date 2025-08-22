package com.worthmytime.data.db

import androidx.room.TypeConverter
import com.worthmytime.domain.model.Decision
import com.worthmytime.domain.model.GoalCategory

class Converters {
    
    @TypeConverter
    fun fromGoalCategory(category: GoalCategory): String {
        return category.name
    }
    
    @TypeConverter
    fun toGoalCategory(value: String): GoalCategory {
        return GoalCategory.valueOf(value)
    }
    
    @TypeConverter
    fun fromDecision(decision: Decision?): String? {
        return decision?.name
    }
    
    @TypeConverter
    fun toDecision(value: String?): Decision? {
        return value?.let { Decision.valueOf(it) }
    }
}
