package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.EncyclopediaCatalog
import com.example.data.model.EncyclopediaEntry
import com.example.data.model.EpochType
import com.example.game.EvolutionUiState
import com.example.game.EvolutionViewModel
import com.example.ui.theme.AmberFirePrimary
import com.example.ui.theme.AshCardSurface
import com.example.ui.theme.BoneIvory
import com.example.ui.theme.CaveStoneSurface
import com.example.ui.theme.OchreDark
import com.example.ui.theme.OchreTerracotta

@Composable
fun EncyclopediaScreen(
    state: EvolutionUiState,
    viewModel: EvolutionViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery = state.encyclopediaFilter
    val epochFilter = state.encyclopediaEpochFilter
    val selectedArticle = state.selectedEncyclopediaEntry
    val maxUnlockedOrder = state.progress.maxUnlockedEpochOrder

    // Detail dialog (only shown if article is unlocked)
    selectedArticle?.let { article ->
        if (article.epoch.order <= maxUnlockedOrder) {
            ArticleDetailDialog(
                article = article,
                onDismiss = { viewModel.selectEncyclopediaEntry(null) }
            )
        } else {
            LockedArticleDialog(
                article = article,
                onDismiss = { viewModel.selectEncyclopediaEntry(null) }
            )
        }
    }

    val filteredEntries = EncyclopediaCatalog.allEntries.filter { entry ->
        val matchesEpoch = epochFilter == null || entry.epoch == epochFilter
        val matchesQuery = searchQuery.isBlank() ||
                entry.title.contains(searchQuery, ignoreCase = true) ||
                entry.summary.contains(searchQuery, ignoreCase = true) ||
                entry.fullText.contains(searchQuery, ignoreCase = true)
        matchesEpoch && matchesQuery
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("encyclopedia_scroll"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Header
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CaveStoneSurface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📚 ", fontSize = 22.sp)
                        Text(
                            text = "База Знань & Енциклопедія Еволюції",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = BoneIvory
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Поступове відкриття знань: наукові статті стають доступними у міру еволюції вашого племені та переходу між історичними епохами!",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setEncyclopediaSearch(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("encyclopedia_search_input"),
                placeholder = { Text("Пошук фактів, стоянок, термінів...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = AmberFirePrimary)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setEncyclopediaSearch("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Очистити", tint = Color.LightGray)
                        }
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AmberFirePrimary,
                    unfocusedBorderColor = AshCardSurface,
                    focusedContainerColor = CaveStoneSurface,
                    unfocusedContainerColor = CaveStoneSurface
                ),
                singleLine = true
            )
        }

        // Epoch Filter Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                item {
                    FilterChip(
                        selected = epochFilter == null,
                        onClick = { viewModel.setEncyclopediaEpochFilter(null) },
                        label = { Text("Усі статті") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AmberFirePrimary,
                            selectedLabelColor = Color.Black
                        )
                    )
                }
                items(EpochType.entries) { epoch ->
                    val isEpochUnlocked = epoch.order <= maxUnlockedOrder
                    FilterChip(
                        selected = epochFilter == epoch,
                        onClick = { viewModel.setEncyclopediaEpochFilter(epoch) },
                        label = {
                            Text(
                                text = if (isEpochUnlocked) "${epoch.iconEmoji} ${epoch.title}" else "🔒 ${epoch.title}"
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AmberFirePrimary,
                            selectedLabelColor = Color.Black,
                            containerColor = if (isEpochUnlocked) CaveStoneSurface else AshCardSurface.copy(alpha = 0.5f)
                        )
                    )
                }
            }
        }

        // Article Items
        items(filteredEntries) { entry ->
            val isUnlocked = entry.epoch.order <= maxUnlockedOrder
            if (isUnlocked) {
                EncyclopediaCard(
                    entry = entry,
                    onClick = { viewModel.selectEncyclopediaEntry(entry) }
                )
            } else {
                LockedEncyclopediaCard(
                    entry = entry,
                    onClick = { viewModel.selectEncyclopediaEntry(entry) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun EncyclopediaCard(
    entry: EncyclopediaEntry,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("encyclopedia_card_${entry.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CaveStoneSurface),
        border = CardDefaults.outlinedCardBorder()
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
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(AshCardSurface),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = entry.category.iconEmoji, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = entry.category.title,
                        style = MaterialTheme.typography.labelMedium,
                        color = AmberFirePrimary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = AshCardSurface
                ) {
                    Text(
                        text = "${entry.epoch.iconEmoji} ${entry.epoch.title}",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = BoneIvory,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = entry.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = BoneIvory
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = entry.summary,
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Читати повністю →",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = OchreTerracotta
            )
        }
    }
}

@Composable
fun LockedEncyclopediaCard(
    entry: EncyclopediaEntry,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = AshCardSurface,
                shape = RoundedCornerShape(16.dp)
            )
            .testTag("locked_encyclopedia_card_${entry.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AshCardSurface.copy(alpha = 0.45f))
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
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(AshCardSurface),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🔒", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Розділ заблоковано",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = AshCardSurface
                ) {
                    Text(
                        text = "Епоха: ${entry.epoch.title}",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "🔒 ${entry.title}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Ця стаття бази знань відкриється після переходу в епоху «${entry.epoch.title}» (${entry.epoch.timePeriod}).",
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null,
                    tint = OchreDark,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Потрібно еволюціонувати до етапу ${entry.epoch.order}",
                    style = MaterialTheme.typography.labelSmall,
                    color = OchreDark,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun LockedArticleDialog(
    article: EncyclopediaEntry,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CaveStoneSurface),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(AshCardSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🔒", fontSize = 28.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Стаття заблокована",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BoneIvory
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Матеріал «${article.title}» відкриється автоматично, коли ви завершите еволюційні квести та перейдете до епохи «${article.epoch.title}» (${article.epoch.timePeriod}).",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                androidx.compose.material3.Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = AmberFirePrimary)
                ) {
                    Text("Зрозуміло", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ArticleDetailDialog(
    article: EncyclopediaEntry,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp, horizontal = 12.dp),
            shape = RoundedCornerShape(24.dp),
            color = CaveStoneSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = article.category.iconEmoji, fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = article.category.title,
                            style = MaterialTheme.typography.labelLarge,
                            color = AmberFirePrimary
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Закрити", tint = BoneIvory)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = article.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = BoneIvory
                )

                Text(
                    text = "Епоха: ${article.epoch.title} (${article.epoch.timePeriod})",
                    style = MaterialTheme.typography.bodySmall,
                    color = OchreTerracotta
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Full Text
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = AshCardSurface)
                ) {
                    Text(
                        text = article.fullText,
                        modifier = Modifier.padding(14.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = BoneIvory,
                        lineHeight = 22.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Key Facts Box
                Text(
                    text = "📌 Ключові наукові факти:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AmberFirePrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                article.keyFacts.forEach { fact ->
                    Row(modifier = Modifier.padding(vertical = 3.dp)) {
                        Text("• ", color = AmberFirePrimary, fontWeight = FontWeight.Bold)
                        Text(
                            text = fact,
                            style = MaterialTheme.typography.bodySmall,
                            color = BoneIvory,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Scientific Importance
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CaveStoneSurface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "🔬 Значення для антропогенезу:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = AmberFirePrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = article.scientificImportance,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Close Button
                androidx.compose.material3.Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("article_close_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = AmberFirePrimary)
                ) {
                    Text("Зрозуміло", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
