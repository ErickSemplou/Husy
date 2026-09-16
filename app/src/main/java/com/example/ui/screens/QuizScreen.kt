package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.School
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EpochType
import com.example.data.model.QuizCatalog
import com.example.game.EvolutionUiState
import com.example.game.EvolutionViewModel
import com.example.game.GameScreen
import com.example.ui.theme.AmberFirePrimary
import com.example.ui.theme.AshCardSurface
import com.example.ui.theme.BoneIvory
import com.example.ui.theme.CaveStoneSurface
import com.example.ui.theme.DangerRed
import com.example.ui.theme.FireGlow
import com.example.ui.theme.OchreTerracotta
import com.example.ui.theme.SavannahGreen
import com.example.ui.theme.SuccessGreen

@Composable
fun QuizScreen(
    state: EvolutionUiState,
    viewModel: EvolutionViewModel,
    modifier: Modifier = Modifier
) {
    val quizEpoch = state.currentQuizEpoch
    val quiz = QuizCatalog.getQuizForEpoch(quizEpoch)
    val questionIndex = state.currentQuizQuestionIndex
    val currentQuestion = quiz.questions.getOrNull(questionIndex)
    val isFinished = state.isQuizFinished

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("quiz_screen_scroll"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Top Bar with Epoch Selector or Indicator
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = quizEpoch.iconEmoji, fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = quiz.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = BoneIvory
                            )
                            Text(
                                text = "Тестування знань етапу",
                                style = MaterialTheme.typography.bodySmall,
                                color = AmberFirePrimary
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = AshCardSurface
                    ) {
                        Text(
                            text = "+${quiz.rewardEvolutionPoints} 🧠",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = AmberFirePrimary
                        )
                    }
                }
            }
        }

        if (!isFinished && currentQuestion != null) {
            // Question Progress Bar
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Питання ${questionIndex + 1} з ${quiz.questions.size}",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.LightGray
                        )
                        Text(
                            text = "Правильних: ${state.currentQuizScore}",
                            style = MaterialTheme.typography.labelMedium,
                            color = SuccessGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { (questionIndex + 1).toFloat() / quiz.questions.size.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = AmberFirePrimary,
                        trackColor = AshCardSurface
                    )
                }
            }

            // Question Box
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("quiz_question_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CaveStoneSurface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = currentQuestion.question,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BoneIvory,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 4 Multiple Choice Options
                        val shuffledIndices = androidx.compose.runtime.remember(currentQuestion) {
                            currentQuestion.options.indices.shuffled()
                        }

                        shuffledIndices.forEachIndexed { displayIndex, originalIndex ->
                            val optionText = currentQuestion.options[originalIndex]
                            val isSelected = state.selectedAnswerIndex == originalIndex
                            val isSubmitted = state.isAnswerSubmitted
                            val isCorrect = originalIndex == currentQuestion.correctIndex

                            val containerColor = when {
                                !isSubmitted && isSelected -> AmberFirePrimary.copy(alpha = 0.25f)
                                isSubmitted && isCorrect -> SuccessGreen.copy(alpha = 0.3f)
                                isSubmitted && isSelected && !isCorrect -> DangerRed.copy(alpha = 0.3f)
                                else -> AshCardSurface
                            }

                            val borderColor = when {
                                !isSubmitted && isSelected -> AmberFirePrimary
                                isSubmitted && isCorrect -> SuccessGreen
                                isSubmitted && isSelected && !isCorrect -> DangerRed
                                else -> Color.Transparent
                            }

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
                                    .clickable(enabled = !isSubmitted) {
                                        viewModel.selectQuizAnswer(originalIndex)
                                    }
                                    .testTag("quiz_option_$originalIndex"),
                                color = containerColor
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) AmberFirePrimary else CaveStoneSurface),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${('А'.code + displayIndex).toChar()}",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.Black else BoneIvory
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Text(
                                        text = optionText,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = BoneIvory,
                                        modifier = Modifier.weight(1f)
                                    )

                                    if (isSubmitted) {
                                        if (isCorrect) {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = "Правильно",
                                                tint = SuccessGreen
                                            )
                                        } else if (isSelected) {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = "Неправильно",
                                                tint = DangerRed
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Explanation & Next Button
            item {
                if (state.isAnswerSubmitted) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = AshCardSurface),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "📖 Наукове пояснення:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = AmberFirePrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentQuestion.scientificExplanation,
                                style = MaterialTheme.typography.bodySmall,
                                color = BoneIvory,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { viewModel.nextQuizQuestion() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("quiz_next_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AmberFirePrimary)
                    ) {
                        Text(
                            text = if (questionIndex + 1 < quiz.questions.size) "Наступне питання →" else "Завершити тест!",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Button(
                        onClick = { viewModel.submitQuizAnswer() },
                        enabled = state.selectedAnswerIndex != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("quiz_submit_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AmberFirePrimary,
                            disabledContainerColor = AshCardSurface
                        )
                    ) {
                        Text(
                            text = "Підтвердити відповідь",
                            color = if (state.selectedAnswerIndex != null) Color.Black else Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else if (isFinished) {
            // Quiz Results Report Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("quiz_results_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CaveStoneSurface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val isPassed = state.currentQuizScore >= (quiz.questions.size / 2 + 1)

                        Text(
                            text = if (isPassed) "🏆 Тріумф Знань!" else "📚 Тест пройдено!",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isPassed) AmberFirePrimary else FireGlow
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Результат: ${state.currentQuizScore} з ${quiz.questions.size} правильних відповідей",
                            style = MaterialTheme.typography.titleMedium,
                            color = BoneIvory,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = AshCardSurface
                        ) {
                            Text(
                                text = "🎁 Нагорода: +${state.quizEvolutionRewardEarned} Очок Еволюції",
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = SavannahGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = if (isPassed)
                                "Вітаємо! Ви успішно підтвердили глибокі знання про епоху ${quizEpoch.title}."
                            else
                                "Непоганий результат! Ви можете переглянути матеріали в Енциклопедії та пройти тест знову.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.LightGray,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.startQuizForEpoch(quizEpoch) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Replay, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Ще раз")
                            }

                            Button(
                                onClick = { viewModel.setScreen(GameScreen.CAMP) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AmberFirePrimary)
                            ) {
                                Text("У табір", color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
