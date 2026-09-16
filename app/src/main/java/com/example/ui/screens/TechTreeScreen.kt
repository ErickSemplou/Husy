package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EpochType
import com.example.data.model.TechCategory
import com.example.data.model.Technology
import com.example.data.model.TechnologyCatalog
import com.example.game.EvolutionUiState
import com.example.game.EvolutionViewModel
import com.example.ui.theme.AmberFireDark
import com.example.ui.theme.AmberFirePrimary
import com.example.ui.theme.AshCardSurface
import com.example.ui.theme.BoneIvory
import com.example.ui.theme.CaveStoneSurface
import com.example.ui.theme.DangerRed
import com.example.ui.theme.OchreDark
import com.example.ui.theme.OchreTerracotta
import com.example.ui.theme.SavannahGreen
import com.example.ui.theme.SuccessGreen

@Composable
fun TechTreeScreen(
    state: EvolutionUiState,
    viewModel: EvolutionViewModel,
    modifier: Modifier = Modifier
) {
    val currentEpoch = state.currentEpoch
    val unlockedTechs = state.unlockedTechs
    val progress = state.progress

    var selectedEpochFilter by remember { mutableStateOf<EpochType?>(currentEpoch) }
    var selectedCategoryFilter by remember { mutableStateOf<TechCategory?>(null) }

    val filteredTechs = TechnologyCatalog.allTechnologies.filter { tech ->
        (selectedEpochFilter == null || tech.epoch == selectedEpochFilter) &&
                (selectedCategoryFilter == null || tech.category == selectedCategoryFilter)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("tech_tree_scroll"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Header stats
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CaveStoneSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Дерево Технологій",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = BoneIvory
                        )
                        Text(
                            text = "Відкрито: ${unlockedTechs.size} з ${TechnologyCatalog.allTechnologies.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AmberFirePrimary
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = AshCardSurface
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🧠 ", fontSize = 12.sp)
                                Text(
                                    text = "${progress.evolutionPoints}",
                                    fontWeight = FontWeight.Bold,
                                    color = BoneIvory,
                                    fontSize = 13.sp
                                )
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = AshCardSurface
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🪵 ", fontSize = 12.sp)
                                Text(
                                    text = "${progress.materials}",
                                    fontWeight = FontWeight.Bold,
                                    color = BoneIvory,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Epoch Filter Carousel
        item {
            Text(
                text = "Епохи розвитку:",
                style = MaterialTheme.typography.labelMedium,
                color = Color.LightGray
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                item {
                    FilterChip(
                        selected = selectedEpochFilter == null,
                        onClick = { selectedEpochFilter = null },
                        label = { Text("Усі епохи") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AmberFirePrimary,
                            selectedLabelColor = Color.Black
                        )
                    )
                }
                items(EpochType.entries) { epoch ->
                    val isCurrent = epoch == currentEpoch
                    val isPast = epoch.order < currentEpoch.order
                    FilterChip(
                        selected = selectedEpochFilter == epoch,
                        onClick = { selectedEpochFilter = epoch },
                        label = {
                            Text("${epoch.iconEmoji} ${epoch.title.take(16)}...")
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = if (isCurrent) AmberFirePrimary else OchreTerracotta,
                            selectedLabelColor = Color.Black,
                            containerColor = if (isPast) AshCardSurface else CaveStoneSurface
                        )
                    )
                }
            }
        }

        // Technology Cards List
        items(filteredTechs) { tech ->
            val isUnlocked = tech.id in unlockedTechs
            val isPrereqMet = tech.prerequisiteId == null || tech.prerequisiteId in unlockedTechs
            val canAfford = progress.evolutionPoints >= tech.costEvolution && progress.materials >= tech.costMaterials
            val isEpochReached = tech.epoch.order <= currentEpoch.order

            TechnologyCard(
                tech = tech,
                isUnlocked = isUnlocked,
                isPrereqMet = isPrereqMet,
                canAfford = canAfford,
                isEpochReached = isEpochReached,
                onResearch = { viewModel.unlockTechnology(tech) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TechnologyCard(
    tech: Technology,
    isUnlocked: Boolean,
    isPrereqMet: Boolean,
    canAfford: Boolean,
    isEpochReached: Boolean,
    onResearch: () -> Unit
) {
    val borderColor = when {
        isUnlocked -> SuccessGreen
        !isEpochReached || !isPrereqMet -> Color.Transparent
        canAfford -> AmberFirePrimary
        else -> OchreDark
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isUnlocked || canAfford) 1.5.dp else 0.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .testTag("tech_card_${tech.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) CaveStoneSurface else AshCardSurface
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Icon, Name & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (isUnlocked) SuccessGreen.copy(alpha = 0.2f) else OchreDark.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = tech.iconEmoji, fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = tech.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BoneIvory
                        )
                        Text(
                            text = "${tech.epoch.title} • ${tech.category.title}",
                            style = MaterialTheme.typography.labelSmall,
                            color = AmberFirePrimary,
                            fontSize = 11.sp
                        )
                    }
                }

                if (isUnlocked) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SuccessGreen.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = SuccessGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Освоєно",
                                color = SuccessGreen,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else if (!isEpochReached) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = CaveStoneSurface
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Майбутня епоха", color = Color.Gray, fontSize = 10.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Short Gameplay Effect
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = CaveStoneSurface.copy(alpha = 0.6f)
            ) {
                Text(
                    text = "✨ Ефект: ${tech.shortEffect}",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = SavannahGreen,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Historical context
            Text(
                text = tech.historicalDescription,
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray,
                lineHeight = 18.sp
            )

            if (!isUnlocked && isEpochReached) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "🎯 Досліджується за квести епохи",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = AmberFirePrimary
                        )
                    }

                    Button(
                        onClick = onResearch,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AmberFirePrimary
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("research_btn_${tech.id}")
                    ) {
                        Text(
                            text = "Відкрити зараз ✨",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
