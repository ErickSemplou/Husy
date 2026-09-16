package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface EvolutionDao {

    @Query("SELECT * FROM game_progress WHERE id = 1 LIMIT 1")
    fun getGameProgressFlow(): Flow<GameProgressEntity?>

    @Query("SELECT * FROM game_progress WHERE id = 1 LIMIT 1")
    suspend fun getGameProgress(): GameProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGameProgress(progress: GameProgressEntity)

    @Query("SELECT * FROM unlocked_techs")
    fun getAllUnlockedTechsFlow(): Flow<List<TechUnlockedEntity>>

    @Query("SELECT techId FROM unlocked_techs")
    suspend fun getAllUnlockedTechIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnlockedTech(tech: TechUnlockedEntity)

    @Query("SELECT * FROM quiz_results")
    fun getAllQuizResultsFlow(): Flow<List<QuizResultEntity>>

    @Query("SELECT * FROM quiz_results WHERE epochOrder = :epochOrder LIMIT 1")
    suspend fun getQuizResultForEpoch(epochOrder: Int): QuizResultEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveQuizResult(result: QuizResultEntity)

    @Query("SELECT * FROM completed_quests")
    fun getAllCompletedQuestsFlow(): Flow<List<CompletedQuestEntity>>

    @Query("SELECT questId FROM completed_quests")
    suspend fun getAllCompletedQuestIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletedQuest(quest: CompletedQuestEntity)

    @Query("DELETE FROM unlocked_techs")
    suspend fun clearAllTechs()

    @Query("DELETE FROM quiz_results")
    suspend fun clearAllQuizzes()

    @Query("DELETE FROM completed_quests")
    suspend fun clearAllQuests()

    @Query("DELETE FROM game_progress")
    suspend fun clearGameProgress()

    @Transaction
    suspend fun resetGameData() {
        clearAllTechs()
        clearAllQuizzes()
        clearAllQuests()
        clearGameProgress()
        saveGameProgress(GameProgressEntity())
    }
}
