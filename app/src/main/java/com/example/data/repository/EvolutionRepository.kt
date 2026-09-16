package com.example.data.repository

import com.example.data.local.CompletedQuestEntity
import com.example.data.local.EvolutionDao
import com.example.data.local.GameProgressEntity
import com.example.data.local.QuizResultEntity
import com.example.data.local.TechUnlockedEntity
import kotlinx.coroutines.flow.Flow

class EvolutionRepository(private val dao: EvolutionDao) {

    val gameProgressFlow: Flow<GameProgressEntity?> = dao.getGameProgressFlow()
    val unlockedTechsFlow: Flow<List<TechUnlockedEntity>> = dao.getAllUnlockedTechsFlow()
    val quizResultsFlow: Flow<List<QuizResultEntity>> = dao.getAllQuizResultsFlow()
    val completedQuestsFlow: Flow<List<CompletedQuestEntity>> = dao.getAllCompletedQuestsFlow()

    suspend fun getGameProgress(): GameProgressEntity {
        return dao.getGameProgress() ?: GameProgressEntity().also {
            dao.saveGameProgress(it)
        }
    }

    suspend fun saveProgress(progress: GameProgressEntity) {
        dao.saveGameProgress(progress)
    }

    suspend fun unlockTech(techId: String, currentTurn: Int) {
        dao.insertUnlockedTech(TechUnlockedEntity(techId, currentTurn))
    }

    suspend fun completeQuest(questId: String) {
        dao.insertCompletedQuest(CompletedQuestEntity(questId))
    }

    suspend fun saveQuizResult(epochOrder: Int, score: Int, total: Int, passed: Boolean) {
        val existing = dao.getQuizResultForEpoch(epochOrder)
        val bestScore = if (existing != null) maxOf(existing.bestScore, score) else score
        val isPassed = (existing?.isPassed == true) || passed
        dao.saveQuizResult(
            QuizResultEntity(
                epochOrder = epochOrder,
                bestScore = bestScore,
                totalQuestions = total,
                isPassed = isPassed
            )
        )
    }

    suspend fun resetGame() {
        dao.resetGameData()
    }
}
