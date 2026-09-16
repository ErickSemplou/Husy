package com.example.game

import com.example.data.local.GameProgressEntity
import com.example.data.local.QuizResultEntity
import com.example.data.local.TechUnlockedEntity
import com.example.data.model.EncyclopediaEntry
import com.example.data.model.EpochType
import com.example.data.model.EventDilemma
import com.example.data.model.Technology

enum class GameScreen {
    WELCOME,
    CAMP,
    TECH_TREE,
    ENCYCLOPEDIA,
    EPOCH_MAP,
    QUIZ
}

data class DailyLog(
    val dayTurn: Int,
    val text: String,
    val type: LogType = LogType.NORMAL
)

enum class LogType {
    NORMAL,
    SUCCESS,
    WARNING,
    DISCOVERY,
    DANGER
}

data class EvolutionUiState(
    val progress: GameProgressEntity = GameProgressEntity(),
    val unlockedTechs: Set<String> = emptySet(),
    val completedQuestIds: Set<String> = emptySet(),
    val quizResults: Map<Int, QuizResultEntity> = emptyMap(),
    val currentScreen: GameScreen = GameScreen.WELCOME,
    val currentEpoch: EpochType = EpochType.DRYOPITHECUS,
    val activeDilemma: EventDilemma? = null,
    val latestDilemmaOutcome: String? = null,
    val showDilemmaOutcomeDialog: Boolean = false,
    val selectedEncyclopediaEntry: EncyclopediaEntry? = null,
    val encyclopediaFilter: String = "",
    val encyclopediaEpochFilter: EpochType? = null,
    val showEpochAscensionDialog: Boolean = false,
    val ascendedEpoch: EpochType? = null,
    val simulationLogs: List<DailyLog> = emptyList(),
    val currentQuizEpoch: EpochType = EpochType.DRYOPITHECUS,
    val currentQuizQuestionIndex: Int = 0,
    val currentQuizScore: Int = 0,
    val selectedAnswerIndex: Int? = null,
    val isAnswerSubmitted: Boolean = false,
    val isQuizFinished: Boolean = false,
    val quizEvolutionRewardEarned: Int = 0
)
