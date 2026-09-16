package com.example.ui.minigames

import com.example.data.model.EpochType
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.audio.SoundEffectsManager
import com.example.ui.theme.AmberFirePrimary
import com.example.ui.theme.AshCardSurface
import com.example.ui.theme.BoneIvory
import com.example.ui.theme.CaveStoneSurface
import com.example.ui.theme.FireGlow
import com.example.ui.theme.SavannahGreen
import com.example.ui.theme.SuccessGreen
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

data class FruitBranch(
    val id: Int,
    val name: String,
    val emoji: String,
    val points: Int,
    val isSafe: Boolean,
    val tip: String
)

@Composable
fun BrachiationMiniGame(
    epoch: EpochType = EpochType.DRYOPITHECUS,
    onComplete: (pointsEarned: Int, foodEarned: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var score by remember { mutableIntStateOf(0) }
    var turnsLeft by remember { mutableIntStateOf(5) }
    var currentBranchIndex by remember { mutableIntStateOf(0) }
    var message by remember { 
        mutableStateOf(
            when (epoch) {
                EpochType.AUSTRALOPITHECUS -> "Шукай поживні підземні коренеплоди, бульби та дикі злаки у савані!"
                EpochType.HOMO_ERECTUS -> "Вистежуй здобич, узгоджуй дії мисливців та розбирай здобич рубилами!"
                EpochType.HOMO_NEANDERTHALENSIS -> "Заганяй мамонта у сніжну пастку та завдавай ударів крем'яними списами!"
                EpochType.HOMO_SAPIENS -> "Використовуй атлатль, кістяні гарпуни та пастки для комплексного збору ресурсів!"
                else -> "Обирай міцні ліани та смачні плоди в кронах пралісу!"
            }
        ) 
    }
    var isGameOver by remember { mutableStateOf(false) }

    val branches = remember {
        when (epoch) {
            EpochType.AUSTRALOPITHECUS -> listOf(
                listOf(
                    FruitBranch(1, "Солодка дика бульба", "🍠", 10, true, "Чудово! Знайдено поживний підземний корінець."),
                    FruitBranch(2, "Отруйна земляна личинка", "🪱", -5, false, "Небезпечно! Личинка виявилася неїстівною.")
                ),
                listOf(
                    FruitBranch(3, "Дика саванна морква", "🥕", 10, true, "Чудово! Вітаміни для всієї зграї."),
                    FruitBranch(4, "Отруйний скорпіон у траві", "🦂", -10, false, "Обережно! Жало скорпіона у високій траві.")
                ),
                listOf(
                    FruitBranch(5, "Поживний коренеплод", "🥔", 15, true, "Чудова знахідка у сухому ґрунті савани!"),
                    FruitBranch(6, "Блідий неїстівний гриб", "🍄", -5, false, "Схожий на отруйний гриб, краще обійти.")
                ),
                listOf(
                    FruitBranch(7, "Дика цибулина", "🧅", 15, true, "Свіжий та соковитий підземний плід."),
                    FruitBranch(8, "Колюча акація", "🌵", -5, false, "Гострі шипи поранили пальці при спробі розкопати ґрунт.")
                ),
                listOf(
                    FruitBranch(9, "Зерна диких злаків", "🌾", 20, true, "Багате на вуглеводи насіння злаків!"),
                    FruitBranch(10, "Сухий гнилий корінь", "🪵", -5, false, "Зовсім порожній і сухий корінь.")
                )
            )
            EpochType.HOMO_ERECTUS -> listOf(
                listOf(
                    FruitBranch(1, "Сліди стада диких оленів", "🦌", 10, true, "Плем'я вистежило слід! Зграя оточує здобич."),
                    FruitBranch(2, "Напад на носорога наодинці", "🦏", -5, false, "Носоріг занадто небезпечний для одного мисливця!")
                ),
                listOf(
                    FruitBranch(3, "Загнати сарну до скелястого яру", "🏹", 10, true, "Успіх! Сарну загнано у пастку, отримано багатий улов м'яса."),
                    FruitBranch(4, "Шуміти та бігти з криками напролом", "💨", -10, false, "Дичина злякалася і втекла від шуму.")
                ),
                listOf(
                    FruitBranch(5, "Спільне кидання кістяних списів", "🪵", 15, true, "Влучний кидок! М'ясо забезпечить плем'я білками."),
                    FruitBranch(6, "Гніздо розлючених диких бджіл", "🐝", -5, false, "Оси розлютилися!")
                ),
                listOf(
                    FruitBranch(7, "Розбірка здобичі ашельськими рубилами", "🪓", 15, true, "Гострі рубила дозволили швидко розрізати тушу."),
                    FruitBranch(8, "Наступити на колючий терновник", "🌵", -5, false, "Нога пошкоджена!")
                ),
                listOf(
                    FruitBranch(9, "Бенкет мисливців біля багаття", "🥩", 20, true, "Плем'я сите й згуртоване навколо вогнища!"),
                    FruitBranch(10, "Залишити сире м'ясо під сонцем", "🦠", -5, false, "М'ясо зіпсувалося.")
                )
            )
            EpochType.HOMO_NEANDERTHALENSIS -> listOf(
                listOf(
                    FruitBranch(1, "Вистежити мамонта в заметілі біля ярка", "🦣", 10, true, "Мисливці виявили стадо! Мамонта відрізано від зграї."),
                    FruitBranch(2, "Бігти по льоду натовпом без плану", "🧊", -5, false, "Крига тріснула під мисливцями! Дичина втекла.")
                ),
                listOf(
                    FruitBranch(3, "Загнати мамонта у сніжну пастку в яру", "🏔️", 10, true, "Успіх! Гігантського мамонта загнано у снігову пастку."),
                    FruitBranch(4, "Кричати прямо перед мордою мамонта", "📢", -10, false, "Розлючений мамонт розкидав мисливців!")
                ),
                listOf(
                    FruitBranch(5, "Удар важкими списами з крем'яними вістрями", "🗡️", 15, true, "Точний удар важкими мустьєрськими списами! Мамонта здолано."),
                    FruitBranch(6, "Кидати дрібну гальку в тушу", "🪨", -5, false, "Галька не пробиває товсту шкуру мамонта.")
                ),
                listOf(
                    FruitBranch(7, "Обробка туші скреблами та збір жиру", "🥩", 15, true, "Зрізано висококалорійний жир та м'ясо для всього клану."),
                    FruitBranch(8, "Залишити тушу печерним левам", "🦁", -5, false, "Печерні леви забрали здобич собі!")
                ),
                listOf(
                    FruitBranch(9, "Зігрівання клану біля печерного вогнища", "🍲", 20, true, "Клан захищено від лютого холоду і забезпечено м'ясом!"),
                    FruitBranch(10, "Їсти сирий лід замість жиру", "🧊", -5, false, "Обмороження та голод!")
                )
            )
            EpochType.HOMO_SAPIENS -> listOf(
                listOf(
                    FruitBranch(1, "Полювання з атлатля (списокидалки)", "🏹", 10, true, "Далекобійний кидок списокидалкою точно у ціль!"),
                    FruitBranch(2, "Кидати важкі дрючки вручну", "🪵", -5, false, "Дрючок упав неподалік, здобич утекла.")
                ),
                listOf(
                    FruitBranch(3, "Ловля лосося гарпунами з зубцями", "🐟", 10, true, "Кістяний гарпун надійно втримав прудку рибу!"),
                    FruitBranch(4, "Ловити рибу руками у стрімкій річці", "🌊", -10, false, "Риба вислизнула з рук у стрімку воду.")
                ),
                listOf(
                    FruitBranch(5, "Збір зерна диких злаків у кошики", "🌾", 15, true, "Плетені кошики наповнено поживним зерном!"),
                    FruitBranch(6, "Їсти незрілу отруйну блекоту", "🌿", -5, false, "Отруйна рослина викликала нездужання.")
                ),
                listOf(
                    FruitBranch(7, "Використання силець та пасток на зайців", "🐇", 15, true, "Пастки спрацювали! Стабільний видобуток хутра й м'яса."),
                    FruitBranch(8, "Переслідувати зайця бігом без пасток", "🏃", -5, false, "Заєць занадто прудкий для бігу по чагарниках.")
                ),
                listOf(
                    FruitBranch(9, "Збереження сушеного м'яса на зиму", "🧺", 20, true, "Запаси сушеного м'яса та риби гарантують ситу зиму!"),
                    FruitBranch(10, "Залишити рибу під сонцем для мух", "🪰", -5, false, "Здобич зіпсувалася.")
                )
            )
            else -> listOf(
                listOf(
                    FruitBranch(1, "Стиглий дикий інжир", "🍈", 10, true, "Міцна ліана! Зібрано поживний інжир."),
                    FruitBranch(2, "Сухий підгнилий сучок", "🍂", -5, false, "Сучок тріснув! Ледь вдалося втриматися на руках.")
                ),
                listOf(
                    FruitBranch(3, "Соковиті лісові ягоди", "🍒", 10, true, "Чудово! Зібрано ягоди, зап'ястки зміцнюються."),
                    FruitBranch(4, "Гніздо диких ос", "🐝", -10, false, "Обережно! Вчасно відстрибнуто від рою ос.")
                ),
                listOf(
                    FruitBranch(5, "Тропічні горіхи на гілці", "🌰", 15, true, "Прекрасна знахідка! Багата на білок їжа."),
                    FruitBranch(6, "Хитка тонка павутинна гілка", "🕸️", -5, false, "Занадто тонка гілка, краще триматися товстих стовбурів.")
                ),
                listOf(
                    FruitBranch(7, "Свіжі солодкі плоди манго", "🥭", 15, true, "Спритний стрибок брахіацією! Зграю насичено."),
                    FruitBranch(8, "Колюча акація з шипами", "🌵", -5, false, "Шипи вкололи пальці, але ліана витримала.")
                ),
                listOf(
                    FruitBranch(9, "Золотий банан на вершині", "🍌", 20, true, "Вершина крони! Повний кошик їжі для зграї."),
                    FruitBranch(10, "Зігнила кора на стовбурі", "🪵", -5, false, "Кора зсунулася, довелося вчепитися двома руками.")
                )
            )
        }
    }

    // Interactive Animated Monkey Swing Indicator
    val swingOffset = remember { Animatable(0f) }
    LaunchedEffect(currentBranchIndex) {
        swingOffset.animateTo(
            targetValue = 25f,
            animationSpec = tween(300)
        )
        swingOffset.animateTo(
            targetValue = 0f,
            animationSpec = tween(300)
        )
    }

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
                    .border(2.dp, AmberFirePrimary, RoundedCornerShape(20.dp))
                    .testTag("brachiation_game_modal"),
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
                                    EpochType.AUSTRALOPITHECUS -> "🍠 "
                                    EpochType.HOMO_ERECTUS -> "🍖 "
                                    EpochType.HOMO_NEANDERTHALENSIS -> "🦣 "
                                    EpochType.HOMO_SAPIENS -> "🏹 "
                                    else -> "🌴 "
                                },
                                fontSize = 22.sp
                            )
                            Text(
                                text = when (epoch) {
                                    EpochType.AUSTRALOPITHECUS -> "Міні-гра: Викопування бульб"
                                    EpochType.HOMO_ERECTUS -> "Міні-гра: Колективне полювання"
                                    EpochType.HOMO_NEANDERTHALENSIS -> "Міні-гра: Полювання на мамонта"
                                    EpochType.HOMO_SAPIENS -> "Міні-гра: Експедиція кроманьйонців"
                                    else -> "Міні-гра: Брахіація в кронах"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = AmberFirePrimary
                            )
                        }

                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Закрити", tint = BoneIvory)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Stats row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AshCardSurface
                        ) {
                            Text(
                                text = "Зібрано очок: $score 🧠",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AshCardSurface
                        ) {
                            Text(
                                text = "Кроків залишилось: $turnsLeft",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = AmberFirePrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { ((5 - turnsLeft) / 5f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = AmberFirePrimary,
                        trackColor = AshCardSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Message Banner
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = AshCardSurface)
                    ) {
                        Text(
                            text = message,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = BoneIvory,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (!isGameOver) {
                        Text(
                            text = "Обери гілку для наступного стрибка:",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.LightGray
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        val currentChoices = remember(currentBranchIndex) {
                            (branches.getOrNull(currentBranchIndex) ?: emptyList()).shuffled()
                        }

                        currentChoices.forEach { branch ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .border(1.5.dp, AmberFirePrimary.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                                    .clickable {
                                        SoundEffectsManager.playBranchSwing()
                                        if (branch.isSafe) {
                                            score = (score + branch.points).coerceAtLeast(0)
                                            message = branch.tip
                                            SoundEffectsManager.playQuizCorrect()
                                            val next = currentBranchIndex + 1
                                            turnsLeft -= 1
                                            if (next >= branches.size || turnsLeft <= 0) {
                                                isGameOver = true
                                                SoundEffectsManager.playQuestSuccess()
                                            } else {
                                                currentBranchIndex = next
                                            }
                                        } else {
                                            SoundEffectsManager.playQuizWrong()
                                            message = "⚠️ Невірно! Ця гілка небезпечна або гнила. ${branch.tip} Оберіть іншу гілку!"
                                        }
                                    }
                                    .testTag("branch_choice_${branch.id}"),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = AshCardSurface)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(CaveStoneSurface)
                                            .offset { IntOffset(0, swingOffset.value.roundToInt()) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = branch.emoji, fontSize = 22.sp)
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = branch.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = BoneIvory
                                        )
                                        Text(
                                            text = when (epoch) {
                                                EpochType.AUSTRALOPITHECUS -> "Розкопати ґрунт тут"
                                                EpochType.HOMO_ERECTUS -> "Вислідити здобич сюди"
                                                EpochType.HOMO_NEANDERTHALENSIS -> "Полювати на мамонта сюди"
                                                EpochType.HOMO_SAPIENS -> "Зібрати ресурси сюди"
                                                else -> "Стрибнути сюди"
                                            },
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.LightGray
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        // Game Over / Summary
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
                                    EpochType.AUSTRALOPITHECUS -> "Викопування бульб завершено!"
                                    EpochType.HOMO_ERECTUS -> "Загоничне полювання завершено!"
                                    EpochType.HOMO_NEANDERTHALENSIS -> "Полювання на мамонта завершено!"
                                    EpochType.HOMO_SAPIENS -> "Заготівельну експедицію завершено!"
                                    else -> "Квест Брахіації Завершено!"
                                },
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = AmberFirePrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Зароблено: +$score очок еволюції 🧠 та +${score / 2} їжі 🍎",
                                style = MaterialTheme.typography.bodyMedium,
                                color = BoneIvory,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = {
                                    SoundEffectsManager.playTribalDrum()
                                    onComplete(score, score / 2)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("brachiation_complete_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = AmberFirePrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Завершити та отримати нагороду",
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
