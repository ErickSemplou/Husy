package com.example.ui.minigames

import com.example.data.model.EpochType
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.audio.SoundEffectsManager
import com.example.ui.theme.AmberFirePrimary
import com.example.ui.theme.AshCardSurface
import com.example.ui.theme.BoneIvory
import com.example.ui.theme.CaveStoneSurface
import com.example.ui.theme.OchreTerracotta
import com.example.ui.theme.SuccessGreen

@Composable
fun NutCrackerMiniGame(
    epoch: EpochType = EpochType.DRYOPITHECUS,
    onComplete: (pointsEarned: Int, materialsEarned: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var crackedNutsCount by remember { mutableIntStateOf(0) }
    var currentNutIntegrity by remember { mutableIntStateOf(100) }
    var hitsTotal by remember { mutableIntStateOf(0) }
    var feedbackText by remember { 
        mutableStateOf(
            when (epoch) {
                EpochType.AUSTRALOPITHECUS -> "Удар галечним каменем об основу, щоб сколоти гострий край (чоппер)!"
                EpochType.HOMO_ERECTUS -> "Викреши іскру кременем об пирит для запалювання вогнища!"
                EpochType.HOMO_NEANDERTHALENSIS -> "Використай мустьєрське скребло для очищення та обробки хутряних шкур!"
                EpochType.HOMO_SAPIENS -> "Вирізай кістяні голки та нанось меандровий орнамент (Мізин)!"
                else -> "Поклади горіх на кам'яне ковадло та бий кругляком!"
            }
        ) 
    }
    var isFinished by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 560.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(2.dp, OchreTerracotta, RoundedCornerShape(20.dp))
                    .testTag("nut_cracker_game_modal"),
                color = CaveStoneSurface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                when (epoch) {
                                    EpochType.AUSTRALOPITHECUS -> "🪓 "
                                    EpochType.HOMO_ERECTUS -> "🔥 "
                                    EpochType.HOMO_NEANDERTHALENSIS -> "🧥 "
                                    EpochType.HOMO_SAPIENS -> "🎨 "
                                    else -> "🔨 "
                                },
                                fontSize = 22.sp
                            )
                            Text(
                                text = when (epoch) {
                                    EpochType.AUSTRALOPITHECUS -> "Олдувайські чоппери: Перші знаряддя"
                                    EpochType.HOMO_ERECTUS -> "Приборкання вогню та Ашельське рубило"
                                    EpochType.HOMO_NEANDERTHALENSIS -> "Мустьєрська культура: Обробка шкур"
                                    EpochType.HOMO_SAPIENS -> "Мистецтво та Лук: Культура Мізина"
                                    else -> "Перші знаряддя: Розбивання горіхів"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = OchreTerracotta
                            )
                        }

                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Закрити", tint = BoneIvory)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = when (epoch) {
                                EpochType.AUSTRALOPITHECUS -> "Створено чопперів: $crackedNutsCount / 3"
                                EpochType.HOMO_ERECTUS -> "Запалено багать: $crackedNutsCount / 3"
                                EpochType.HOMO_NEANDERTHALENSIS -> "Очищено шкур: $crackedNutsCount / 3"
                                EpochType.HOMO_SAPIENS -> "Створено виробу: $crackedNutsCount / 3"
                                else -> "Розколото горіхів: $crackedNutsCount / 3"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = OchreTerracotta,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Всього ударів: $hitsTotal",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { (crackedNutsCount / 3f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = OchreTerracotta,
                        trackColor = AshCardSurface
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Anvil & Nut Arena
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = AshCardSurface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (currentNutIntegrity > 0) {
                                    when (epoch) {
                                        EpochType.AUSTRALOPITHECUS -> "🪨"
                                        EpochType.HOMO_ERECTUS -> "🪵"
                                        EpochType.HOMO_NEANDERTHALENSIS -> "🥩"
                                        EpochType.HOMO_SAPIENS -> "🦣"
                                        else -> "🌰"
                                    }
                                } else {
                                    when (epoch) {
                                        EpochType.AUSTRALOPITHECUS -> "✨ 🪓"
                                        EpochType.HOMO_ERECTUS -> "✨ 🔥"
                                        EpochType.HOMO_NEANDERTHALENSIS -> "✨ 🧥"
                                        EpochType.HOMO_SAPIENS -> "✨ 🪡"
                                        else -> "✨ 🥜"
                                    }
                                },
                                fontSize = 52.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (currentNutIntegrity > 0) {
                                    when (epoch) {
                                        EpochType.AUSTRALOPITHECUS -> "Залишилося сколоти края: $currentNutIntegrity%"
                                        EpochType.HOMO_ERECTUS -> "Тління трута та іскри: $currentNutIntegrity%"
                                        EpochType.HOMO_NEANDERTHALENSIS -> "Очищення шкури від жиру: $currentNutIntegrity%"
                                        EpochType.HOMO_SAPIENS -> "Вирізання голки та орнаменту: $currentNutIntegrity%"
                                        else -> "Міцність шкаралупи: $currentNutIntegrity%"
                                    }
                                } else {
                                    when (epoch) {
                                        EpochType.AUSTRALOPITHECUS -> "Чоппер виготовлено! Створено гострий ріжучий край!"
                                        EpochType.HOMO_ERECTUS -> "Вогонь розгорівся! Багаття палає, даруючи тепло і захист!"
                                        EpochType.HOMO_NEANDERTHALENSIS -> "Шкуру вичищено! Отримано теплий хутряний одяг!"
                                        EpochType.HOMO_SAPIENS -> "Створено кістяну голку з вушком та орнаментований виріб!"
                                        else -> "Горіх розбито! Смачне ядро добуто!"
                                    }
                                },
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (currentNutIntegrity > 0) BoneIvory else SuccessGreen
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = feedbackText,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.LightGray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (!isFinished) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Hit Button
                            Button(
                                onClick = {
                                    hitsTotal += 1
                                    SoundEffectsManager.playStoneCrack()
                                    val damage = (30..45).random()
                                    val remaining = (currentNutIntegrity - damage).coerceAtLeast(0)
                                    currentNutIntegrity = remaining

                                    if (remaining == 0) {
                                        SoundEffectsManager.playTribalDrum()
                                        crackedNutsCount += 1
                                        feedbackText = when (epoch) {
                                            EpochType.AUSTRALOPITHECUS -> "💥 БАХ! Відбійником сколото кам'яний відщеп! Олдувайський чоппер готовий до обробки туш!"
                                            EpochType.HOMO_ERECTUS -> "💥 БАХ! Іскра впала на трут! Дмухання роздмухало полум'я! Вогонь захистить плем'я від хижаків."
                                            EpochType.HOMO_NEANDERTHALENSIS -> "💥 ОЧИЩЕНО! Скребло вичистило шкуру тварини! Вона готова до зшивання у теплий одяг."
                                            EpochType.HOMO_SAPIENS -> "💥 ВЕЛИЧНО! Крем'яний різець сформував голку з вушком та меандровий браслет!"
                                            else -> "💥 БАХ! Шкаралупа тріснула під вагою кругляка! Отримано поживні жири та білки."
                                        }
                                        if (crackedNutsCount >= 3) {
                                            isFinished = true
                                            SoundEffectsManager.playQuestSuccess()
                                        } else {
                                            currentNutIntegrity = 100
                                        }
                                    } else {
                                        feedbackText = when (epoch) {
                                            EpochType.AUSTRALOPITHECUS -> "🔨 Точний удар галькою-відбійником по кам'яній основі! Залишилося $remaining% обробки."
                                            EpochType.HOMO_ERECTUS -> "⚡ Удар кременю об пирит викресав пучок іскор! Залишилося $remaining% роздмухування."
                                            EpochType.HOMO_NEANDERTHALENSIS -> "🔪 Точний рух крем'яного скребла по шкурі! Залишилося $remaining% обробки."
                                            EpochType.HOMO_SAPIENS -> "🎨 Обробка ікла мамонта крем'яним різцем! Залишилося $remaining% обробки."
                                            else -> "🔨 Влучний удар кругляком по горіху на пласкому камені! Залишилося $remaining% міцності."
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                                    .testTag("nut_crack_strike_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = OchreTerracotta),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = when (epoch) {
                                        EpochType.AUSTRALOPITHECUS -> "🔨 Вдарити галькою-відбійником!"
                                        EpochType.HOMO_ERECTUS -> "🔥 Викресати іскру кременем!"
                                        EpochType.HOMO_NEANDERTHALENSIS -> "🔪 Обробити шкуру скреблом!"
                                        EpochType.HOMO_SAPIENS -> "🪡 Творити крем'яним різцем!"
                                        else -> "🔨 Вдарити кругляком!"
                                    },
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else {
                        // Summary
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🎉", fontSize = 42.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = when (epoch) {
                                    EpochType.AUSTRALOPITHECUS -> "Знаряддя праці успішно виготовлено!"
                                    EpochType.HOMO_ERECTUS -> "Вогонь успішно приборкано та запалено!"
                                    EpochType.HOMO_NEANDERTHALENSIS -> "Теплий хутряний одяг виготовлено!"
                                    EpochType.HOMO_SAPIENS -> "Кістяні знаряддя та мистецтво створено!"
                                    else -> "Горіхи успішно розколото!"
                                },
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = OchreTerracotta
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Нагорода: +45 очок еволюції 🧠 та +20 природних матеріалів 🪨",
                                style = MaterialTheme.typography.bodyMedium,
                                color = BoneIvory,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = {
                                    SoundEffectsManager.playTribalDrum()
                                    onComplete(45, 20)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("nut_cracker_finish_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = OchreTerracotta),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Забрати нагороду",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
