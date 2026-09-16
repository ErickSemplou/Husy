package com.example.ui.screens

import com.example.data.model.EpochType
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EpochQuestCatalog
import com.example.data.model.QuestType
import com.example.game.EvolutionUiState
import com.example.game.EvolutionViewModel
import com.example.game.GameScreen
import com.example.ui.components.DilemmaDialog
import com.example.ui.components.DilemmaOutcomeDialog
import com.example.ui.components.ResourceHUD
import com.example.ui.minigames.BrachiationMiniGame
import com.example.ui.minigames.NutCrackerMiniGame
import com.example.ui.minigames.SavannahStepMiniGame
import com.example.ui.theme.AmberFirePrimary
import com.example.ui.theme.AshCardSurface
import com.example.ui.theme.BoneIvory
import com.example.ui.theme.CaveStoneSurface
import com.example.ui.theme.DangerRed
import com.example.ui.theme.OchreDark
import com.example.ui.theme.OchreTerracotta
import com.example.ui.theme.SuccessGreen

@Composable
fun CampScreen(
    state: EvolutionUiState,
    viewModel: EvolutionViewModel,
    modifier: Modifier = Modifier
) {
    val progress = state.progress
    val currentEpoch = state.currentEpoch
    val canAscend = viewModel.canAscendToNextEpoch()
    var activeMiniGame by remember { mutableStateOf<QuestType?>(null) }
    val completedQuests = state.completedQuestIds

    // Launch Mini-Game Modals
    when (activeMiniGame) {
        QuestType.BRACHIATION -> {
            BrachiationMiniGame(
                epoch = currentEpoch,
                onComplete = { points, food ->
                    val epochQuests = EpochQuestCatalog.getQuestsForEpoch(currentEpoch)
                    val targetQuest = epochQuests.firstOrNull { it.type == QuestType.BRACHIATION }
                    viewModel.completeQuest(
                        questId = targetQuest?.id ?: "quest_brachiation_${currentEpoch.name}",
                        deltaEvolution = points,
                        deltaFood = food,
                        outcomeMessage = if (currentEpoch == EpochType.AUSTRALOPITHECUS) "Знайдено поживні підземні бульби та корінці у савані!" else "Отримано цінні ресурси під час вилазки!"
                    )
                    activeMiniGame = null
                },
                onDismiss = { activeMiniGame = null }
            )
        }
        QuestType.SAVANNAH_STEP -> {
            SavannahStepMiniGame(
                epoch = currentEpoch,
                onComplete = { points ->
                    val epochQuests = EpochQuestCatalog.getQuestsForEpoch(currentEpoch)
                    val targetQuest = epochQuests.firstOrNull { it.type == QuestType.SAVANNAH_STEP }
                    viewModel.completeQuest(
                        questId = targetQuest?.id ?: "quest_savannah_${currentEpoch.name}",
                        deltaEvolution = points,
                        deltaFood = 15,
                        outcomeMessage = if (currentEpoch == EpochType.AUSTRALOPITHECUS) "Успішно здійснено перехід саваною на двох ногах!" else "Успішно досліджено нові території!"
                    )
                    activeMiniGame = null
                },
                onDismiss = { activeMiniGame = null }
            )
        }
        QuestType.NUT_CRACKER -> {
            NutCrackerMiniGame(
                epoch = currentEpoch,
                onComplete = { points, materials ->
                    val epochQuests = EpochQuestCatalog.getQuestsForEpoch(currentEpoch)
                    val targetQuest = epochQuests.firstOrNull { it.type == QuestType.NUT_CRACKER }
                    viewModel.completeQuest(
                        questId = targetQuest?.id ?: "quest_nut_cracker_${currentEpoch.name}",
                        deltaEvolution = points,
                        deltaMaterials = materials,
                        outcomeMessage = if (currentEpoch == EpochType.AUSTRALOPITHECUS) "Виготовлено примітивні олдувайські чоппери з каменю!" else "Використано кмітливість для обробки матеріалів!"
                    )
                    activeMiniGame = null
                },
                onDismiss = { activeMiniGame = null }
            )
        }
        QuestType.FINAL_EXAM -> {
            viewModel.startQuizForEpoch(currentEpoch)
            activeMiniGame = null
        }
        null -> {}
    }

    // Active Dilemma popup
    state.activeDilemma?.let { dilemma ->
        DilemmaDialog(
            dilemma = dilemma,
            onChoiceSelected = { choice ->
                viewModel.makeDilemmaChoice(choice)
            }
        )
    }

    if (state.showDilemmaOutcomeDialog && state.latestDilemmaOutcome != null) {
        DilemmaOutcomeDialog(
            outcomeText = state.latestDilemmaOutcome,
            onDismiss = { viewModel.dismissDilemmaOutcomeDialog() }
        )
    }

    val epochQuests = EpochQuestCatalog.getQuestsForEpoch(currentEpoch)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("epoch_hub_scroll"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            ResourceHUD(
                progress = progress,
                currentEpoch = currentEpoch
            )
        }

        // Hero Card with Avatar & Historical Facts
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("epoch_avatar_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CaveStoneSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        Image(
                            painter = painterResource(id = currentEpoch.avatarRes),
                            contentDescription = "Аватар Епохи",
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Color.Transparent,
                                            CaveStoneSurface.copy(alpha = 0.85f),
                                            CaveStoneSurface
                                        )
                                    )
                                )
                        )

                        // Timeline Date Badge
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(12.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = AmberFirePrimary
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("⏳ ", fontSize = 14.sp)
                                Text(
                                    text = currentEpoch.timePeriod,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }

                        // Brain Volume Badge
                        Surface(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = Color.Black.copy(alpha = 0.7f)
                        ) {
                            Text(
                                text = "🧠 Мозок: ${currentEpoch.brainVolume}",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = BoneIvory
                            )
                        }
                    }

                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = currentEpoch.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = BoneIvory
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentEpoch.keyFact,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Sequential Evolution Progression
        item {
            val totalQuests = epochQuests.size
            val completedCount = epochQuests.count { it.id in completedQuests }
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (canAscend) OchreDark.copy(alpha = 0.4f) else AshCardSurface
                ),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Еволюційний прогрес епохи:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = BoneIvory
                        )
                        Text(
                            text = "$completedCount / $totalQuests квестів",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = if (canAscend) SuccessGreen else AmberFirePrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { (completedCount.toFloat() / totalQuests.toFloat()).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (canAscend) SuccessGreen else AmberFirePrimary,
                        trackColor = CaveStoneSurface
                    )

                    if (canAscend) {
                        Spacer(modifier = Modifier.height(12.dp))
                        androidx.compose.material3.Button(
                            onClick = { viewModel.ascendToNextEpoch() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("ascend_epoch_button"),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "🧬 ЕВОЛЮЦІОНУВАТИ ДО НАСТУПНОЇ ЕПОХИ!",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // Quick Epoch Actions (Tech Tree)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.setScreen(GameScreen.TECH_TREE) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("open_tech_tree_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = AshCardSurface)
                ) {
                    Icon(Icons.Default.Timeline, contentDescription = null, tint = OchreTerracotta)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Дерево наук та винаходів", color = BoneIvory, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Quests List with Sequential Unlocking
        itemsIndexed(epochQuests) { index, quest ->
            val isCompleted = completedQuests.contains(quest.id)
            
            // Sequential unlocking logic:
            // First quest is always unlocked.
            // Subsequent quests are unlocked only if the previous quest in the list is completed.
            val isPreviousCompleted = if (index == 0) {
                true
            } else {
                completedQuests.contains(epochQuests[index - 1].id)
            }
            
            // Also check if evolution points requirement is met
            val isEvolutionMet = progress.evolutionPoints >= quest.requiredEvolution
            val isLocked = !isPreviousCompleted || (!isEvolutionMet && !isCompleted)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("quest_card_${quest.id}")
                    .clickable(enabled = !isLocked) {
                        activeMiniGame = quest.type
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCompleted) AshCardSurface.copy(alpha = 0.5f) else CaveStoneSurface
                ),
                border = if (isCompleted) CardDefaults.outlinedCardBorder() else androidx.compose.foundation.BorderStroke(1.5.dp, AmberFirePrimary.copy(alpha = if (isLocked) 0.2f else 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(if (isLocked) AshCardSurface else AmberFirePrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = "Виконано", tint = SuccessGreen)
                        } else if (isLocked) {
                            Text("🔒", fontSize = 24.sp)
                        } else {
                            Text(quest.icon, fontSize = 28.sp)
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = quest.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isLocked) Color.Gray else BoneIvory
                        )
                        Text(
                            text = quest.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isLocked) Color.DarkGray else OchreTerracotta
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = quest.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isLocked) Color.DarkGray else Color.LightGray,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        if (isCompleted) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SuccessGreen.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "💡 ${quest.historicalLesson}",
                                    modifier = Modifier.padding(8.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SuccessGreen,
                                    lineHeight = 14.sp
                                )
                            }
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (quest.rewardEvolution > 0) {
                                    Text(
                                        text = "+${quest.rewardEvolution} 🧠",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isLocked) Color.Gray else AmberFirePrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                if (quest.rewardFood > 0) {
                                    Text(
                                        text = "+${quest.rewardFood} 🍖",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isLocked) Color.Gray else SuccessGreen
                                    )
                                }
                                if (quest.rewardMaterials > 0) {
                                    Text(
                                        text = "+${quest.rewardMaterials} 🪵",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isLocked) Color.Gray else OchreTerracotta
                                    )
                                }
                                if (isLocked) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    val lockReason = if (!isPreviousCompleted) {
                                        "(Пройдіть попередній квест)"
                                    } else {
                                        "(Потрібно ${quest.requiredEvolution} 🧠)"
                                    }
                                    Text(
                                        text = lockReason,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = DangerRed
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
