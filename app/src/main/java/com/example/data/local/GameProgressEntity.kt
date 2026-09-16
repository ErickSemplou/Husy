package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_progress")
data class GameProgressEntity(
    @PrimaryKey val id: Int = 1,
    val tribeName: String = "Плем'я Омо",
    val currentEpochOrder: Int = 1,
    val maxUnlockedEpochOrder: Int = 1,
    val dayTurn: Int = 1,
    val food: Int = 40,
    val materials: Int = 25,
    val evolutionPoints: Int = 0,
    val population: Int = 8,
    val foragers: Int = 4,
    val hunters: Int = 2,
    val crafters: Int = 1,
    val guardians: Int = 1,
    val warmthSafety: Int = 75,
    val morale: Int = 80,
    val totalDecisionsMade: Int = 0,
    val isGameCompleted: Boolean = false
)

@Entity(tableName = "unlocked_techs")
data class TechUnlockedEntity(
    @PrimaryKey val techId: String,
    val unlockedAtTurn: Int,
    val unlockedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey val epochOrder: Int,
    val bestScore: Int,
    val totalQuestions: Int,
    val isPassed: Boolean,
    val completedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "completed_quests")
data class CompletedQuestEntity(
    @PrimaryKey val questId: String,
    val completedTimestamp: Long = System.currentTimeMillis()
)
