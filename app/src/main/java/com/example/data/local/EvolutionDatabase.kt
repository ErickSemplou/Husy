package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        GameProgressEntity::class,
        TechUnlockedEntity::class,
        QuizResultEntity::class,
        CompletedQuestEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class EvolutionDatabase : RoomDatabase() {
    abstract fun evolutionDao(): EvolutionDao

    companion object {
        @Volatile
        private var INSTANCE: EvolutionDatabase? = null

        fun getInstance(context: Context): EvolutionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EvolutionDatabase::class.java,
                    "human_evolution.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
