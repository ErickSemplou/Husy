package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.SoundEffectsManager
import com.example.data.model.EpochType
import com.example.game.EvolutionUiState
import com.example.game.EvolutionViewModel
import com.example.game.GameScreen
import com.example.ui.theme.AmberFirePrimary
import com.example.ui.theme.AshCardSurface
import com.example.ui.theme.BoneIvory
import com.example.ui.theme.CaveStoneSurface
import com.example.ui.theme.OchreDark
import com.example.ui.theme.OchreTerracotta
import com.example.ui.theme.SavannahGreen
import com.example.ui.theme.SuccessGreen

@Composable
fun EvolutionEpochMapScreen(
    state: EvolutionUiState,
    viewModel: EvolutionViewModel,
    modifier: Modifier = Modifier
) {
    val currentEpoch = state.currentEpoch
    val maxUnlockedOrder = state.progress.maxUnlockedEpochOrder
    val quizResults = state.quizResults

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("epoch_map_scroll"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CaveStoneSurface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "🗺️ Хронологічна Карта Еволюції",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = BoneIvory
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Послідовні етапи розвитку предків людини: від Дріопітека (4 млн р.т.) до Людини розумної. Кожен гомінід має унікальні анатомічні риси, спосіб життя та знаряддя праці.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray
                    )
                }
            }
        }

        // List of 5 Milestone Nodes with Dedicated Avatars
        items(EpochType.entries) { epoch ->
            val isUnlocked = epoch.order <= maxUnlockedOrder
            val isCurrent = epoch == currentEpoch
            val isQuizPassed = quizResults[epoch.order]?.isPassed ?: false

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (isCurrent) 2.dp else 1.dp,
                        color = if (isCurrent) AmberFirePrimary else if (isUnlocked) OchreDark else Color.Transparent,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .testTag("epoch_map_item_${epoch.order}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isUnlocked) CaveStoneSurface else AshCardSurface.copy(alpha = 0.5f)
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Hominid Avatar with glow border
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
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
                                        contentDescription = "Аватар ${epoch.title}",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Text(text = "🔒", fontSize = 20.sp)
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "${epoch.order}. ${epoch.title}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isUnlocked) BoneIvory else Color.Gray
                                )
                                Text(
                                    text = epoch.timePeriod,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isUnlocked) AmberFirePrimary else Color.DarkGray
                                )
                                Text(
                                    text = epoch.scientificName,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        if (isCurrent) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = AmberFirePrimary
                            ) {
                                Text(
                                    text = "АКТИВНО",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        } else if (isUnlocked) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Відкрито",
                                tint = SuccessGreen
                            )
                        } else {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = "Заблоковано",
                                tint = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = epoch.keyFact,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isUnlocked) Color.LightGray else Color.DarkGray,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = AshCardSurface
                        ) {
                            Text(
                                text = "🧠 Об'єм мозку: ${epoch.brainVolume}",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isUnlocked) BoneIvory else Color.DarkGray
                            )
                        }

                        if (isUnlocked) {
                            Button(
                                onClick = {
                                    SoundEffectsManager.playTribalDrum()
                                    if (isCurrent) {
                                        viewModel.setScreen(GameScreen.CAMP)
                                    } else {
                                        viewModel.returnToEpoch(epoch)
                                    }
                                },
                                modifier = Modifier.height(36.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isCurrent) AmberFirePrimary else AshCardSurface
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (isCurrent) "Грати" else "Повернутися",
                                    color = if (isCurrent) Color.Black else BoneIvory,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
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
