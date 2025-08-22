package com.worthmytime.data.repo

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

data class StateTaxInfo(
    val state: String,
    val name: String,
    val base: Double,
    val avgCombined: Double
)

@Singleton
class TaxRepository @Inject constructor(
    private val context: Context
) {
    
    private var stateTaxMap: Map<String, Double>? = null
    private val gson = Gson()
    
    suspend fun getStateTaxMap(): Map<String, Double> {
        return stateTaxMap ?: loadStateTaxData()
    }
    
    private suspend fun loadStateTaxData(): Map<String, Double> = withContext(Dispatchers.IO) {
        try {
            val jsonString = context.assets.open("sales_tax_states.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<StateTaxInfo>>() {}.type
            val stateTaxList: List<StateTaxInfo> = gson.fromJson(jsonString, type)
            
            stateTaxMap = stateTaxList.associate { it.state to it.avgCombined }
            stateTaxMap!!
        } catch (e: Exception) {
            // Return empty map if loading fails
            emptyMap()
        }
    }
    
    suspend fun getTaxRateForState(stateCode: String): Double {
        val taxMap = getStateTaxMap()
        return taxMap[stateCode] ?: 0.0
    }
}
