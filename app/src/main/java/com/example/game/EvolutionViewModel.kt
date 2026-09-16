package com.example.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.audio.SoundEffectsManager
import com.example.data.local.GameProgressEntity
import com.example.data.model.DilemmaCatalog
import com.example.data.model.DilemmaChoice
import com.example.data.model.EncyclopediaCatalog
import com.example.data.model.EncyclopediaEntry
import com.example.data.model.EpochType
import com.example.data.model.EpochQuestCatalog
import com.example.data.model.EventDilemma
import com.example.data.model.QuizCatalog
import com.example.data.model.Technology
import com.example.data.model.TechnologyCatalog
import com.example.data.repository.EvolutionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class EvolutionViewModel(
    private val repository: EvolutionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EvolutionUiState())
    val uiState: StateFlow<EvolutionUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.gameProgressFlow,
                repository.unlockedTechsFlow,
                repository.quizResultsFlow,
                repository.completedQuestsFlow
            ) { progress, techs, quizzes, quests ->
                val actualProgress = progress ?: GameProgressEntity()
                val minPop = getMinimumPopulationForEpoch(actualProgress.currentEpochOrder)
                val effectiveProgress = if (actualProgress.population < minPop) {
                    actualProgress.copy(
                        population = minPop,
                        food = maxOf(actualProgress.food, minPop * 8)
                    )
                } else {
                    actualProgress
                }
                val techIds = techs.map { it.techId }.toSet()
                val quizMap = quizzes.associateBy { it.epochOrder }
                val questIds = quests.map { it.questId }.toSet()
                val currentEpoch = EpochType.fromOrder(effectiveProgress.currentEpochOrder)

                _uiState.value.copy(
                    progress = effectiveProgress,
                    unlockedTechs = techIds,
                    quizResults = quizMap,
                    completedQuestIds = questIds,
                    currentEpoch = currentEpoch
                )
            }.collect { newState ->
                _uiState.update { current ->
                    newState.copy(
                        currentScreen = current.currentScreen,
                        activeDilemma = current.activeDilemma,
                        latestDilemmaOutcome = current.latestDilemmaOutcome,
                        showDilemmaOutcomeDialog = current.showDilemmaOutcomeDialog,
                        selectedEncyclopediaEntry = current.selectedEncyclopediaEntry,
                        encyclopediaFilter = current.encyclopediaFilter,
                        encyclopediaEpochFilter = current.encyclopediaEpochFilter,
                        showEpochAscensionDialog = current.showEpochAscensionDialog,
                        ascendedEpoch = current.ascendedEpoch,
                        simulationLogs = current.simulationLogs.ifEmpty {
                            listOf(
                                DailyLog(
                                    dayTurn = 1,
                                    text = "Плем'я розпочало свій шлях у міоценових пралісах. Стережіться хижаків та збирайте їжу!",
                                    type = LogType.NORMAL
                                )
                            )
                        },
                        currentQuizEpoch = current.currentQuizEpoch,
                        currentQuizQuestionIndex = current.currentQuizQuestionIndex,
                        currentQuizScore = current.currentQuizScore,
                        selectedAnswerIndex = current.selectedAnswerIndex,
                        isAnswerSubmitted = current.isAnswerSubmitted,
                        isQuizFinished = current.isQuizFinished,
                        quizEvolutionRewardEarned = current.quizEvolutionRewardEarned
                    )
                }
            }
        }
    }

    fun setScreen(screen: GameScreen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun setTribeName(name: String) {
        val current = _uiState.value.progress
        val updated = current.copy(tribeName = name.trim().ifEmpty { "Плем'я Омо" })
        viewModelScope.launch {
            repository.saveProgress(updated)
        }
    }

    fun updateWorkerAllocation(foragers: Int, hunters: Int, crafters: Int, guardians: Int) {
        val current = _uiState.value.progress
        val totalAllocated = foragers + hunters + crafters + guardians
        if (totalAllocated <= current.population && foragers >= 0 && hunters >= 0 && crafters >= 0 && guardians >= 0) {
            val updated = current.copy(
                foragers = foragers,
                hunters = hunters,
                crafters = crafters,
                guardians = guardians
            )
            viewModelScope.launch {
                repository.saveProgress(updated)
            }
        }
    }

    fun advanceTurn() {
        advanceDayTurn()
    }

    fun advanceDayTurn() {
        val state = _uiState.value
        val progress = state.progress
        val epoch = state.currentEpoch
        val unlockedTechs = state.unlockedTechs

        // Calculate Production
        val allocatedWorkers = progress.foragers + progress.hunters + progress.crafters + progress.guardians
        val unallocatedWorkers = (progress.population - allocatedWorkers).coerceAtLeast(0)
        var foodProduced = (progress.foragers * 4) + (progress.hunters * 8) + (unallocatedWorkers * 2) + (epoch.order * 12)
        var materialsProduced = (progress.crafters * 4) + (progress.guardians * 1)
        var evolutionGained = 2 + (progress.dayTurn / 5)

        // Technology Modifiers
        if ("tech_flint_knapping" in unlockedTechs) foodProduced += 4
        if ("tech_foraging_baskets" in unlockedTechs) foodProduced += 6
        if ("tech_wooden_clubs" in unlockedTechs) materialsProduced += 3
        if ("tech_fire_control" in unlockedTechs) {
            foodProduced += 5
            evolutionGained += 4
        }
        if ("tech_hearth_cooking" in unlockedTechs) foodProduced += 8
        if ("tech_cave_art" in unlockedTechs) evolutionGained += 8
        if ("tech_bow_arrow" in unlockedTechs) foodProduced += 12

        // Consumption
        val foodConsumed = progress.population * 2
        val netFoodChange = foodProduced - foodConsumed
        val newFood = (progress.food + netFoodChange).coerceAtLeast(0)

        // Starvation and Morale Check
        var newPopulation = progress.population
        var newMorale = progress.morale
        var newWarmth = progress.warmthSafety

        val logs = mutableListOf<DailyLog>()

        if (newFood == 0 && netFoodChange < 0) {
            if (newPopulation > 3 && Random.nextFloat() < 0.35f) {
                newPopulation -= 1
                logs.add(
                    DailyLog(
                        dayTurn = progress.dayTurn + 1,
                        text = "💀 Голод забрав одного з членів роду!",
                        type = LogType.DANGER
                    )
                )
            }
            newMorale = (newMorale - 15).coerceIn(0, 100)
            logs.add(
                DailyLog(
                    dayTurn = progress.dayTurn + 1,
                    text = "⚠️ Запаси їжі вичерпано. Плем'я страждає від голоду.",
                    type = LogType.WARNING
                )
            )
        } else if (newFood > progress.population * 6 && newMorale > 60 && Random.nextFloat() < 0.25f) {
            newPopulation += 1
            logs.add(
                DailyLog(
                    dayTurn = progress.dayTurn + 1,
                    text = "👶 У племені народився новий предок! Популяція зросла.",
                    type = LogType.SUCCESS
                )
            )
        }

        // Random Historical Event
        val possibleDilemmas = DilemmaCatalog.getDilemmasForEpoch(epoch)
        val triggeredDilemma = if (possibleDilemmas.isNotEmpty() && Random.nextFloat() < 0.40f) {
            possibleDilemmas.random()
        } else null

        val updatedProgress = progress.copy(
            dayTurn = progress.dayTurn + 1,
            food = newFood,
            materials = (progress.materials + materialsProduced).coerceAtLeast(0),
            evolutionPoints = progress.evolutionPoints + evolutionGained,
            population = newPopulation,
            morale = newMorale,
            warmthSafety = newWarmth
        )

        logs.add(
            0,
            DailyLog(
                dayTurn = updatedProgress.dayTurn,
                text = "Світанок ${updatedProgress.dayTurn}-го дня. Зібрано: +$foodProduced їжі, +$materialsProduced матеріалів, +$evolutionGained 🧠",
                type = LogType.NORMAL
            )
        )

        _uiState.update {
            it.copy(
                activeDilemma = triggeredDilemma,
                simulationLogs = (logs + it.simulationLogs).take(40)
            )
        }

        viewModelScope.launch {
            repository.saveProgress(updatedProgress)
        }
    }

    fun makeDilemmaChoice(choice: DilemmaChoice) {
        val state = _uiState.value
        val progress = state.progress

        val newFood = (progress.food + choice.deltaFood).coerceAtLeast(0)
        val newMaterials = (progress.materials + choice.deltaMaterials).coerceAtLeast(0)
        val newEvolution = (progress.evolutionPoints + choice.deltaEvolution).coerceAtLeast(0)
        val newMorale = (progress.morale + choice.deltaMorale).coerceIn(0, 100)
        val newWarmth = (progress.warmthSafety + choice.deltaWarmth).coerceIn(0, 100)
        val newPopulation = (progress.population + choice.deltaPopulation).coerceAtLeast(2)

        val updated = progress.copy(
            food = newFood,
            materials = newMaterials,
            evolutionPoints = newEvolution,
            morale = newMorale,
            warmthSafety = newWarmth,
            population = newPopulation,
            totalDecisionsMade = progress.totalDecisionsMade + 1
        )

        val log = DailyLog(
            dayTurn = progress.dayTurn,
            text = "📜 Рішення: ${choice.title}. ${choice.historicalOutcome}",
            type = if (choice.deltaEvolution > 0) LogType.SUCCESS else LogType.NORMAL
        )

        _uiState.update {
            it.copy(
                activeDilemma = null,
                latestDilemmaOutcome = choice.historicalOutcome,
                showDilemmaOutcomeDialog = true,
                simulationLogs = listOf(log) + it.simulationLogs
            )
        }

        viewModelScope.launch {
            repository.saveProgress(updated)
        }
    }

    fun dismissDilemmaOutcomeDialog() {
        _uiState.update { it.copy(showDilemmaOutcomeDialog = false, latestDilemmaOutcome = null) }
    }

    fun completeQuest(
        questId: String,
        deltaEvolution: Int,
        deltaFood: Int = 0,
        deltaMaterials: Int = 0,
        outcomeMessage: String
    ) {
        SoundEffectsManager.playQuestSuccess()
        val state = _uiState.value
        var progress = state.progress
        var currentUnlockedTechs = state.unlockedTechs

        // Update basic rewards
        progress = progress.copy(
            evolutionPoints = progress.evolutionPoints + deltaEvolution,
            food = progress.food + deltaFood,
            materials = progress.materials + deltaMaterials
        )

        val logs = mutableListOf<DailyLog>()
        logs.add(
            DailyLog(
                dayTurn = progress.dayTurn,
                text = "🎯 Завершено квест: $outcomeMessage (+ $deltaEvolution 🧠)",
                type = LogType.SUCCESS
            )
        )

        // Find associated quest and epoch
        val quest = EpochQuestCatalog.quests.find { it.id == questId }
        val questEpoch = quest?.epoch ?: state.currentEpoch

        // Population growth proportional to quest epoch stage
        val popIncrease = questEpoch.order * 5
        val foodBonus = popIncrease * 10
        progress = progress.copy(
            population = progress.population + popIncrease,
            food = progress.food + foodBonus
        )
        logs.add(
            DailyLog(
                dayTurn = progress.dayTurn,
                text = "👥 Завдяки квесту епохи ${questEpoch.title} чисельність племені зросла на +$popIncrease осіб! (Разом у племені: ${progress.population} осіб)",
                type = LogType.SUCCESS
            )
        )

        // Auto unlock the next locked technology for this epoch
        val epochTechs = TechnologyCatalog.getTechnologiesForEpoch(questEpoch)
        val nextLockedTech = epochTechs.firstOrNull { it.id !in currentUnlockedTechs }

        if (nextLockedTech != null) {
            currentUnlockedTechs = currentUnlockedTechs + nextLockedTech.id
            progress = progress.copy(
                food = progress.food + nextLockedTech.foodBonus,
                morale = (progress.morale + nextLockedTech.moraleBonus).coerceIn(0, 100),
                warmthSafety = (progress.warmthSafety + nextLockedTech.warmthBonus).coerceIn(0, 100)
            )
            logs.add(
                DailyLog(
                    dayTurn = progress.dayTurn,
                    text = "✨ Автоматично досліджено науку: ${nextLockedTech.name}! (${nextLockedTech.shortEffect})",
                    type = LogType.DISCOVERY
                )
            )
            viewModelScope.launch {
                repository.unlockTech(nextLockedTech.id, progress.dayTurn)
            }
        }

        val updatedProgress = progress
        val updatedUnlockedTechs = currentUnlockedTechs
        val updatedLogs = logs

        _uiState.update {
            it.copy(
                completedQuestIds = it.completedQuestIds + questId,
                unlockedTechs = updatedUnlockedTechs,
                progress = updatedProgress,
                simulationLogs = updatedLogs + it.simulationLogs
            )
        }

        viewModelScope.launch {
            repository.completeQuest(questId)
            repository.saveProgress(updatedProgress)
        }
    }

    fun unlockTechnology(tech: Technology) {
        val state = _uiState.value
        val progress = state.progress

        if (tech.id in state.unlockedTechs) return

        SoundEffectsManager.playQuizCorrect()
        val updated = progress.copy(
            food = progress.food + tech.foodBonus,
            morale = (progress.morale + tech.moraleBonus).coerceIn(0, 100),
            warmthSafety = (progress.warmthSafety + tech.warmthBonus).coerceIn(0, 100)
        )

        val log = DailyLog(
            dayTurn = progress.dayTurn,
            text = "✨ Відкрито науку: ${tech.name}! ${tech.shortEffect}",
            type = LogType.DISCOVERY
        )

        viewModelScope.launch {
            repository.unlockTech(tech.id, progress.dayTurn)
            repository.saveProgress(updated)
        }

        _uiState.update {
            it.copy(
                unlockedTechs = it.unlockedTechs + tech.id,
                progress = updated,
                simulationLogs = listOf(log) + it.simulationLogs
            )
        }
    }

    fun canAscendToNextEpoch(): Boolean {
        val state = _uiState.value
        val currentEpoch = state.currentEpoch
        if (currentEpoch == EpochType.HOMO_SAPIENS) return false

        val epochQuests = EpochQuestCatalog.getQuestsForEpoch(currentEpoch)
        val allQuestsDone = epochQuests.all { it.id in state.completedQuestIds }
        val isQuizPassed = state.quizResults[currentEpoch.order]?.isPassed == true

        if (currentEpoch == EpochType.DRYOPITHECUS) {
            return allQuestsDone && isQuizPassed
        }

        val requiredTechs = TechnologyCatalog.getTechnologiesForEpoch(currentEpoch)
        val unlockedCount = requiredTechs.count { it.id in state.unlockedTechs }
        val isTechRequirementMet = unlockedCount >= maxOf(1, requiredTechs.size - 1)

        return isTechRequirementMet && isQuizPassed && allQuestsDone
    }

    fun ascendToNextEpoch() {
        val state = _uiState.value
        val nextOrder = state.currentEpoch.order + 1
        if (nextOrder > EpochType.entries.size) return

        SoundEffectsManager.playEvolutionFanfare()
        val nextEpoch = EpochType.fromOrder(nextOrder)
        val minNextPop = getMinimumPopulationForEpoch(nextOrder)
        val newPopulation = maxOf(state.progress.population + 20, minNextPop)
        val foodSupply = newPopulation * 10

        val updated = state.progress.copy(
            currentEpochOrder = nextOrder,
            maxUnlockedEpochOrder = maxOf(state.progress.maxUnlockedEpochOrder, nextOrder),
            evolutionPoints = state.progress.evolutionPoints + 100,
            population = newPopulation,
            food = maxOf(state.progress.food, foodSupply),
            morale = 100,
            warmthSafety = 90
        )

        val log = DailyLog(
            dayTurn = state.progress.dayTurn,
            text = "🌟 ЕВОЛЮЦІЙНИЙ СТРИБОК! Людство перейшло в епоху: ${nextEpoch.title} (${nextEpoch.scientificName})! Чисельність племені зросла до $newPopulation осіб!",
            type = LogType.SUCCESS
        )

        _uiState.update {
            it.copy(
                currentEpoch = nextEpoch,
                showEpochAscensionDialog = true,
                ascendedEpoch = nextEpoch,
                simulationLogs = listOf(log) + it.simulationLogs
            )
        }

        viewModelScope.launch {
            repository.saveProgress(updated)
        }
    }

    fun dismissAscensionDialog() {
        _uiState.update { it.copy(showEpochAscensionDialog = false, ascendedEpoch = null) }
    }

    fun returnToEpoch(epoch: EpochType) {
        val state = _uiState.value
        if (epoch.order <= state.progress.maxUnlockedEpochOrder) {
            SoundEffectsManager.playTribalDrum()
            val updated = state.progress.copy(currentEpochOrder = epoch.order)
            _uiState.update { it.copy(currentEpoch = epoch, currentScreen = GameScreen.CAMP) }
            viewModelScope.launch {
                repository.saveProgress(updated)
            }
        }
    }

    fun startQuizForEpoch(epoch: EpochType) {
        SoundEffectsManager.playTribalDrum()
        _uiState.update {
            it.copy(
                currentScreen = GameScreen.QUIZ,
                currentQuizEpoch = epoch,
                currentQuizQuestionIndex = 0,
                currentQuizScore = 0,
                selectedAnswerIndex = null,
                isAnswerSubmitted = false,
                isQuizFinished = false,
                quizEvolutionRewardEarned = 0
            )
        }
    }

    fun selectQuizAnswer(index: Int) {
        if (!_uiState.value.isAnswerSubmitted && !_uiState.value.isQuizFinished) {
            _uiState.update { it.copy(selectedAnswerIndex = index) }
        }
    }

    fun submitQuizAnswer() {
        val state = _uiState.value
        val selected = state.selectedAnswerIndex ?: return
        val quiz = QuizCatalog.getQuizForEpoch(state.currentQuizEpoch)
        val currentQuestion = quiz.questions.getOrNull(state.currentQuizQuestionIndex) ?: return

        val isCorrect = selected == currentQuestion.correctIndex
        if (isCorrect) {
            SoundEffectsManager.playQuizCorrect()
        } else {
            SoundEffectsManager.playQuizWrong()
        }
        val newScore = if (isCorrect) state.currentQuizScore + 1 else state.currentQuizScore

        _uiState.update {
            it.copy(
                isAnswerSubmitted = true,
                currentQuizScore = newScore
            )
        }
    }

    fun nextQuizQuestion() {
        val state = _uiState.value
        val quiz = QuizCatalog.getQuizForEpoch(state.currentQuizEpoch)
        val nextIndex = state.currentQuizQuestionIndex + 1

        if (nextIndex < quiz.questions.size) {
            _uiState.update {
                it.copy(
                    currentQuizQuestionIndex = nextIndex,
                    selectedAnswerIndex = null,
                    isAnswerSubmitted = false
                )
            }
        } else {
            // Quiz completed
            val passed = state.currentQuizScore >= (quiz.questions.size / 2 + 1)
            val reward = if (passed) quiz.rewardEvolutionPoints else (quiz.rewardEvolutionPoints / 3)

            val updatedProgress = state.progress.copy(
                evolutionPoints = state.progress.evolutionPoints + reward
            )

            val examQuestId = "quest_final_exam_${state.currentQuizEpoch.name}"

            viewModelScope.launch {
                repository.saveQuizResult(
                    epochOrder = state.currentQuizEpoch.order,
                    score = state.currentQuizScore,
                    total = quiz.questions.size,
                    passed = passed
                )
                if (passed) {
                    repository.completeQuest(examQuestId)
                }
                repository.saveProgress(updatedProgress)
            }

            _uiState.update {
                val newQuests = if (passed) {
                    it.completedQuestIds + examQuestId
                } else it.completedQuestIds

                it.copy(
                    isQuizFinished = true,
                    completedQuestIds = newQuests,
                    quizEvolutionRewardEarned = reward
                )
            }
        }
    }

    fun selectEncyclopediaEntry(entry: EncyclopediaEntry?) {
        _uiState.update { it.copy(selectedEncyclopediaEntry = entry) }
    }

    fun setEncyclopediaSearch(query: String) {
        _uiState.update { it.copy(encyclopediaFilter = query) }
    }

    fun setEncyclopediaEpochFilter(epoch: EpochType?) {
        _uiState.update { it.copy(encyclopediaEpochFilter = epoch) }
    }

    fun resetEntireGame() {
        viewModelScope.launch {
            repository.resetGame()
        }
        _uiState.update {
            EvolutionUiState()
        }
    }

    private fun getMinimumPopulationForEpoch(epochOrder: Int): Int {
        return when (epochOrder) {
            1 -> 8         // Дріопітек: малий первісний рід (~8 осіб)
            2 -> 25        // Австралопітек: родино-племінна група савани (~25 осіб)
            3 -> 65        // Людина прямоходяча: великоплемінна община (~65 осіб)
            4 -> 140       // Неандерталець: стоянки та мисливські клани (~140 осіб)
            5 -> 300       // Людина розумна: племінні союзи та поселення (~300 осіб)
            else -> 8
        }
    }

    companion object {
        fun provideFactory(repository: EvolutionRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return EvolutionViewModel(repository) as T
                }
            }
    }
}
