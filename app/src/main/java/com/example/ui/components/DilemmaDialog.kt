package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.DilemmaChoice
import com.example.data.model.EventDilemma
import com.example.ui.theme.AmberFirePrimary
import com.example.ui.theme.AshCardSurface
import com.example.ui.theme.BoneIvory
import com.example.ui.theme.CaveStoneSurface
import com.example.ui.theme.CharcoalBorder
import com.example.ui.theme.FireGlow
import com.example.ui.theme.OchreDark
import com.example.ui.theme.OchreTerracotta
import com.example.ui.theme.SavannahGreen

@Composable
fun DilemmaDialog(
    dilemma: EventDilemma,
    onChoiceSelected: (DilemmaChoice) -> Unit
) {
    Dialog(
        onDismissRequest = { /* Modal must be resolved by choosing */ },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(2.dp, AmberFirePrimary, RoundedCornerShape(20.dp))
                .testTag("dilemma_dialog"),
            color = CaveStoneSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header with icon
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "⚡", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Історична Дилема",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AmberFirePrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = dilemma.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = BoneIvory
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = dilemma.situation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = BoneIvory,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Historical context callout
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = AshCardSurface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "📜 Історична довідка:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = OchreTerracotta
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = dilemma.historicalContext,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Як вчинить ваше плем'я?",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = BoneIvory
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Choices
                val shuffledChoices = remember(dilemma) { dilemma.choices.shuffled() }
                
                shuffledChoices.forEachIndexed { index, choice ->
                    OutlinedButton(
                        onClick = { onChoiceSelected(choice) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .testTag("dilemma_choice_$index"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = AshCardSurface
                        ),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Text(
                                text = "${index + 1}. ${choice.title}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = AmberFirePrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = choice.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DilemmaOutcomeDialog(
    outcomeText: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("✨ ")
                Text(
                    text = "Наслідки Рішення",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = AmberFirePrimary
                )
            }
        },
        text = {
            Text(
                text = outcomeText,
                style = MaterialTheme.typography.bodyMedium,
                color = BoneIvory,
                lineHeight = 22.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = AmberFirePrimary),
                modifier = Modifier.testTag("dismiss_outcome_button")
            ) {
                Text("Продовжити шлях", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = CaveStoneSurface,
        shape = RoundedCornerShape(18.dp)
    )
}
