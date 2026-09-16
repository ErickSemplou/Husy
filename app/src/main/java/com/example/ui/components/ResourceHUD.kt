package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.GameProgressEntity
import com.example.data.model.EpochType
import com.example.ui.theme.AmberFirePrimary
import com.example.ui.theme.AshCardSurface
import com.example.ui.theme.BoneIvory
import com.example.ui.theme.CaveStoneSurface
import com.example.ui.theme.DangerRed
import com.example.ui.theme.FireGlow
import com.example.ui.theme.OchreDark
import com.example.ui.theme.OchreTerracotta
import com.example.ui.theme.SavannahGreen
import com.example.ui.theme.SuccessGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ResourceHUD(
    progress: GameProgressEntity,
    currentEpoch: EpochType,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("resource_hud_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CaveStoneSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Header Row: Tribe Name, Day Turn & Epoch Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(OchreDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = currentEpoch.iconEmoji, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = progress.tribeName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BoneIvory
                        )
                        Text(
                            text = "${currentEpoch.title} (${currentEpoch.timePeriod})",
                            style = MaterialTheme.typography.bodySmall,
                            color = AmberFirePrimary,
                            fontSize = 11.sp
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AshCardSurface,
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "День ${progress.dayTurn}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = BoneIvory
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Resource Pills Grid
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ResourcePill(
                    icon = "🍖",
                    label = "Їжа",
                    value = "${progress.food}",
                    subText = "-${progress.population * 2}/д",
                    badgeColor = if (progress.food > 15) SuccessGreen else DangerRed
                )
                ResourcePill(
                    icon = "🪵",
                    label = "Матеріали",
                    value = "${progress.materials}",
                    subText = "+${progress.crafters * 3}",
                    badgeColor = OchreTerracotta
                )
                ResourcePill(
                    icon = "🧠",
                    label = "Еволюція",
                    value = "${progress.evolutionPoints}",
                    subText = "Очки",
                    badgeColor = AmberFirePrimary
                )
                ResourcePill(
                    icon = "👥",
                    label = "Плем'я",
                    value = "${progress.population}",
                    subText = "осіб",
                    badgeColor = SavannahGreen
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Vital Status Gauges: Warmth/Safety & Morale
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Warmth / Fire Bar
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🔥 Вогнище & Безпека",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.sp,
                            color = BoneIvory
                        )
                        Text(
                            text = "${progress.warmthSafety}%",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = if (progress.warmthSafety > 40) FireGlow else DangerRed
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { progress.warmthSafety / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (progress.warmthSafety > 50) FireGlow else DangerRed,
                        trackColor = AshCardSurface
                    )
                }

                // Morale Bar
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "⚡ Мораль племені",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.sp,
                            color = BoneIvory
                        )
                        Text(
                            text = "${progress.morale}%",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = if (progress.morale > 40) SavannahGreen else DangerRed
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { progress.morale / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (progress.morale > 50) SavannahGreen else DangerRed,
                        trackColor = AshCardSurface
                    )
                }
            }
        }
    }
}

@Composable
fun ResourcePill(
    icon: String,
    label: String,
    value: String,
    subText: String,
    badgeColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = AshCardSurface,
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = Color.LightGray
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = BoneIvory
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = subText,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        color = badgeColor
                    )
                }
            }
        }
    }
}
