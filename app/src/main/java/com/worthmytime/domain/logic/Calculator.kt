package com.worthmytime.domain.logic

import com.worthmytime.domain.model.IncomeType
import com.worthmytime.domain.model.LifestyleBenchmarks
import kotlin.math.max
import kotlin.math.roundToInt

object Calculator {
    
    /**
     * Calculate gross hourly rate from income type and values
     */
    fun calculateGrossHourly(
        incomeType: IncomeType,
        hourlyRate: Double,
        annualSalary: Double,
        hoursPerWeek: Double
    ): Double {
        return when (incomeType) {
            IncomeType.HOURLY -> hourlyRate
            IncomeType.ANNUAL -> annualSalary / (hoursPerWeek * 52)
        }
    }
    
    /**
     * Calculate net hourly rate after taxes
     */
    fun calculateNetHourly(
        grossHourly: Double,
        effectiveTaxPct: Double
    ): Double {
        return max(0.01, grossHourly * (1 - effectiveTaxPct / 100))
    }
    
    /**
     * Calculate effective price including sales tax if applicable
     */
    fun calculateEffectivePrice(
        price: Double,
        salesTaxPct: Double,
        includeSalesTax: Boolean
    ): Double {
        return if (includeSalesTax) {
            price * (1 + salesTaxPct / 100)
        } else {
            price
        }
    }
    
    /**
     * Convert price to hours of work
     */
    fun priceToHours(
        effectivePrice: Double,
        netHourly: Double
    ): Double {
        return effectivePrice / netHourly
    }
    
    /**
     * Convert hours to workdays (8 hours per day)
     */
    fun hoursToWorkdays(hours: Double): Double {
        return hours / 8.0
    }
    
    /**
     * Convert hours to workweeks
     */
    fun hoursToWorkweeks(hours: Double, hoursPerWeek: Double): Double {
        return hours / hoursPerWeek
    }
    
    /**
     * Calculate lifestyle equivalents
     */
    fun calculateLifestyleEquivalents(
        effectivePrice: Double,
        benchmarks: LifestyleBenchmarks
    ): Triple<Int, Int, Int> {
        val coffees = (effectivePrice / benchmarks.coffee).roundToInt()
        val daysGroceries = (effectivePrice / benchmarks.groceriesDay).roundToInt()
        val monthsStreaming = (effectivePrice / benchmarks.streamingMonth).roundToInt()
        
        return Triple(coffees, daysGroceries, monthsStreaming)
    }
    
    /**
     * Round hours for display (1 decimal, 2 decimals if < 1.0)
     */
    fun roundHours(hours: Double): Double {
        return if (hours < 1.0) {
            (hours * 100).roundToInt() / 100.0
        } else {
            (hours * 10).roundToInt() / 10.0
        }
    }
    
    /**
     * Round tax percentage to 2 decimals
     */
    fun roundTaxPercent(taxPercent: Double): Double {
        return (taxPercent * 100).roundToInt() / 100.0
    }
    
    /**
     * Round price to 2 decimals
     */
    fun roundPrice(price: Double): Double {
        return (price * 100).roundToInt() / 100.0
    }
    
    /**
     * Round workdays/workweeks to 2 decimals
     */
    fun roundWorkUnits(units: Double): Double {
        return (units * 100).roundToInt() / 100.0
    }
}
