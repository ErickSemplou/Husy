package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.audio.SoundEffectsManager
import com.example.data.model.EpochType
import com.example.game.EvolutionUiState
import com.example.game.EvolutionViewModel
import com.example.game.GameScreen
import com.example.ui.theme.AmberFirePrimary
import com.example.ui.theme.AshCardSurface
import com.example.ui.theme.BoneIvory
import com.example.ui.theme.CaveStoneSurface
import com.example.ui.theme.FireGlow
import com.example.ui.theme.OchreDark
import com.example.ui.theme.OchreTerracotta
import com.example.ui.theme.SavannahGreen
import com.example.ui.theme.SuccessGreen

@Composable
fun WelcomeScreen(
    state: EvolutionUiState,
    viewModel: EvolutionViewModel,
    modifier: Modifier = Modifier
) {
    val currentOrder = state.progress.currentEpochOrder
    val maxUnlockedOrder = state.progress.maxUnlockedEpochOrder
    val completedQuestsCount = state.completedQuestIds.size
    val totalQuestsForDryo = 4
    val totalEpochs = EpochType.entries.size
    val currentEpoch = state.currentEpoch

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("welcome_screen_scroll"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))

            // Game Logo & Hero Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("welcome_hero_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CaveStoneSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(190.dp)
                    ) {
                        Image(
                            painter = painterResource(id = currentEpoch.avatarRes),
                            contentDescription = "Логотип гри Еволюція",
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
                                            CaveStoneSurface.copy(alpha = 0.8f),
                                            CaveStoneSurface
                                        )
                                    )
                                )
                        )

                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 12.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color.Black.copy(alpha = 0.75f),
                            border = CardDefaults.outlinedCardBorder()
                        ) {
                            Text(
                                text = "🌿 ${currentEpoch.title} • ${currentEpoch.timePeriod}",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = AmberFirePrimary
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Еволюція: Витоки Людства",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = BoneIvory,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Пройди шлях від перших дереволазних людиноподібних (Дріопітек, 4 млн р.т.) до сучасної Людини розумної! Проходь поступові міні-квести, розвивай прямоходіння та складай підсумкові іспити для переходу між епохами.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.LightGray,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Primary Start / Continue Button
                        Button(
                            onClick = {
                                SoundEffectsManager.playTribalDrum()
                                viewModel.setScreen(GameScreen.CAMP)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("start_game_primary_btn"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AmberFirePrimary
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (maxUnlockedOrder > 1 || completedQuestsCount > 0) "ПРОДОВЖИТИ ГРУ" else "ПОЧАТИ ГРУ (ДРІОПІТЕК)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
        }

        // GLOBAL EVOLUTION INDICATOR CARD (Requested feature)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("global_evolution_indicator_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CaveStoneSurface),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, AmberFirePrimary.copy(alpha = 0.8f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🧬 ", fontSize = 22.sp)
                            Text(
                                text = "Загальний Прогрес Еволюції",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = BoneIvory
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AmberFirePrimary
                        ) {
                            Text(
                                text = "Етап $maxUnlockedOrder з $totalEpochs",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Progress Bar
                    LinearProgressIndicator(
                        progress = {
                            val epochPart = (maxUnlockedOrder - 1).toFloat() / totalEpochs.toFloat()
                            val questBonus = if (maxUnlockedOrder == 1) {
                                (completedQuestsCount.toFloat() / totalQuestsForDryo.toFloat()) * (1f / totalEpochs.toFloat())
                            } else 0f
                            (epochPart + questBonus).coerceIn(0f, 1f)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = AmberFirePrimary,
                        trackColor = AshCardSurface
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3 Stat Badges Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Current Species Badge with Avatar
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            color = AshCardSurface
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = currentEpoch.avatarRes),
                                    contentDescription = currentEpoch.title,
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = currentEpoch.title,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = BoneIvory
                                    )
                                    Text(
                                        text = "Поточний вид",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.Gray,
                                        fontSize = 8.sp
                                    )
                                }
                            }
                        }

                        // Evolution Points Badge
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            color = AshCardSurface
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🧠",
                                    fontSize = 18.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${state.progress.evolutionPoints} EP",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberFirePrimary,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Очки еволюції",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray,
                                    fontSize = 9.sp
                                )
                            }
                        }

                        // Completed Quests Badge
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            color = AshCardSurface
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🎯",
                                    fontSize = 18.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "$completedQuestsCount / $totalQuestsForDryo",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (completedQuestsCount >= totalQuestsForDryo) SuccessGreen else SavannahGreen,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Квестів епохи",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray,
                                    fontSize = 9.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // 5 Sequential Evolution Stages
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "🏛️ Етапи еволюції (послідовне відкриття):",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BoneIvory
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Усі наступні епохи відкриваються лише після завершення квестів та іспиту попереднього етапу!",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
            }
        }

        items(EpochType.entries) { epoch ->
            val isUnlocked = epoch.order <= maxUnlockedOrder
            val isCurrent = epoch.order == currentOrder

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (isCurrent) 2.dp else 1.dp,
                        color = if (isCurrent) AmberFirePrimary else if (isUnlocked) OchreDark else Color.Transparent,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .testTag("stage_card_${epoch.order}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isUnlocked) CaveStoneSurface else AshCardSurface.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Hominid Avatar image thumbnail
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .border(
                                    width = if (isCurrent) 2.dp else 1.dp,
                                    color = if (isCurrent) AmberFirePrimary else if (isUnlocked) OchreDark else Color.Gray,
                                    shape = CircleShape
                                )
                                .background(if (isUnlocked) OchreDark else AshCardSurface),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isUnlocked) {
                                Image(
                                    painter = painterResource(id = epoch.avatarRes),
                                    contentDescription = epoch.title,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Text(text = "🔒", fontSize = 18.sp)
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${epoch.order}. ${epoch.title}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isUnlocked) BoneIvory else Color.Gray
                                )
                            }
                            Text(
                                text = "⏳ ${epoch.timePeriod}",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isUnlocked) AmberFirePrimary else Color.DarkGray,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = epoch.keyFact,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isUnlocked) Color.LightGray else Color.DarkGray,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    if (isCurrent) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AmberFirePrimary
                        ) {
                            Text(
                                text = "ВІДКРИТО",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    } else if (isUnlocked) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Пройдено",
                            tint = SuccessGreen
                        )
                    } else {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "Закрито",
                            tint = Color.Gray
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
