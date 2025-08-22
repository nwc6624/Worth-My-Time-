package com.worthmytime.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.worthmytime.data.db.dao.GoalDao
import com.worthmytime.data.db.dao.HistoryDao
import com.worthmytime.data.db.entities.GoalEntity
import com.worthmytime.data.db.entities.HistoryEntity

@Database(
    entities = [GoalEntity::class, HistoryEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun goalDao(): GoalDao
    abstract fun historyDao(): HistoryDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "worth_my_time.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
