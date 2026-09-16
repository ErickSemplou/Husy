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
import com.example.ui.theme.DangerRed
import com.example.ui.theme.OchreDark
import com.example.ui.theme.SavannahGreen
import com.example.ui.theme.SuccessGreen

@Composable
fun SavannahStepMiniGame(
    epoch: EpochType = EpochType.DRYOPITHECUS,
    onComplete: (pointsEarned: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var stageIndex by remember { mutableIntStateOf(0) }
    var evolutionScore by remember { mutableIntStateOf(0) }
    var isStandingUp by remember { mutableStateOf(false) }
    var storyLog by remember { 
        mutableStateOf(
            when (epoch) {
                EpochType.AUSTRALOPITHECUS -> "Праліс залишився позаду. Попереду — відкрита савана з високою травою та небезпеками."
                EpochType.HOMO_ERECTUS -> "Вихід з Африки. Попереду — Євроазіатський континент та нові території."
                EpochType.HOMO_NEANDERTHALENSIS -> "Настав Льодовиковий період. Суворі замети й морози вимагають надійного прихистку."
                EpochType.HOMO_SAPIENS -> "Кроманьйонці опанували нові технології й готові заселити всі континенти Землі."
                else -> "Праліс закінчився. Попереду — безкрайній степ з високою сухою травою."
            }
        ) 
    }
    var isFinished by remember { mutableStateOf(false) }

    val scenarios = remember(epoch) {
        when (epoch) {
            EpochType.AUSTRALOPITHECUS -> listOf(
                "1. Шелест у високій траві: Не видно хижаків та стежки. Як оглянути околиці?",
                "2. Перехід відкритої савани до водопою під палючим сонцем.",
                "3. Наближення хижаків (гієн та шаблезубих кішок) до зграї."
            )
            EpochType.HOMO_ERECTUS -> listOf(
                "1. Вихід з Африки: Плем'я Homo Erectus наближається до Близькосхідного коридору. Як рухатися далі?",
                "2. Прибуття в Закарпаття (Україна): Знайдено долину річки Тиса поблизу селища Королево з покладами ашельського кременю.",
                "3. Настання сезонних холодів в Європі: Зміна пір року вимагає активного захисту від морозів."
            )
            EpochType.HOMO_NEANDERTHALENSIS -> listOf(
                "1. Сувора заметіль: Насувається льодовиковий шторм у передгір'ях Криму. Де шукати порятунок?",
                "2. Печера Киїк-Коба (Крим): Знайдено глибокий грот, але там живуть печерні ведмеді.",
                "3. Турбота про травмованого мисливця клану зі зламаною ногою."
            )
            EpochType.HOMO_SAPIENS -> listOf(
                "1. Перехід Берингії: Настало зледеніння і оголився сухопутний міст між Азією та Америкою.",
                "2. Стоянка Мізин (Чернігівщина, Україна): Будівництво зимових жител у суворих умовах тундростепу.",
                "3. Опанування океанічних шляхів: Освоєння островів та заселення нових континентів."
            )
            else -> listOf(
                "1. Шелест у траві: Не видно, що ховається за пагорбом. Що зробити?",
                "2. Перехід до джерела води: Потрібно перетнути відкриту галявину під палючим сонцем.",
                "3. Поява хижака: На горизонті помічено силует шаблезубого кота (Махайрода)!"
            )
        }
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
                    .border(2.dp, SavannahGreen, RoundedCornerShape(20.dp))
                    .testTag("savannah_game_modal"),
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
                                    EpochType.AUSTRALOPITHECUS -> "👣 "
                                    EpochType.HOMO_ERECTUS -> "🗺️ "
                                    EpochType.HOMO_NEANDERTHALENSIS -> "🏔️ "
                                    EpochType.HOMO_SAPIENS -> "🌐 "
                                    else -> "🌾 "
                                },
                                fontSize = 22.sp
                            )
                            Text(
                                text = when (epoch) {
                                    EpochType.AUSTRALOPITHECUS -> "Міні-гра: Прямоходіння в савані"
                                    EpochType.HOMO_ERECTUS -> "Міні-гра: Велике розселення (Королево)"
                                    EpochType.HOMO_NEANDERTHALENSIS -> "Міні-гра: Пошук печер (Киїк-Коба)"
                                    EpochType.HOMO_SAPIENS -> "Міні-гра: Заселення континентів (Мізин)"
                                    else -> "Міні-гра: Спуск у Савану"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SavannahGreen
                            )
                        }

                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Закрити", tint = BoneIvory)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Progress
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Етап ${stageIndex + 1} з ${scenarios.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AmberFirePrimary
                        )
                        Text(
                            text = "Очки: +$evolutionScore 🧠",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { ((stageIndex + 1).toFloat() / scenarios.size.toFloat()).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = SavannahGreen,
                        trackColor = AshCardSurface
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Story Narrative Box
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = AshCardSurface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = scenarios.getOrElse(stageIndex) { "Подорож завершена!" },
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = BoneIvory
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = storyLog,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.LightGray,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (!isFinished) {
                        // Action Buttons
                        val actionOptions = remember(stageIndex) { listOf(true, false).shuffled() }
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            actionOptions.forEach { isStandOption ->
                                if (isStandOption) {
                                    Button(
                                        onClick = {
                                            SoundEffectsManager.playSavannahStep()
                                            isStandingUp = true
                                            evolutionScore += 15
                                            storyLog = when (epoch) {
                                                EpochType.AUSTRALOPITHECUS -> when (stageIndex) {
                                                    0 -> "🧍 Австралопітек (Люсі) впевнено став на дві ноги! Висока саванна трава більше не закриває огляд, джерело води помічено!"
                                                    1 -> "🚶 Двонога хода (біпедалізм) заощаджує воду та енергію на довгих дистанціях савани!"
                                                    2 -> "🛡️ Вільні руки дозволяють тримати палиці та каміння для захисту від шаблезубих хижаків!"
                                                    else -> "🧍 Успішний крок уперед на двох ногах!"
                                                }
                                                EpochType.HOMO_ERECTUS -> when (stageIndex) {
                                                    0 -> "🧭 Успішно пройдено Близькосхідний коридор! Homo Erectus першим серед гомінідів вийшов з Африки."
                                                    1 -> "⛰️ Встановлено найдавнішу відому стоянку первісної людини в Європі — Королево на Закарпатті (1.4 млн років)!"
                                                    2 -> "🔥 Вогонь та ашельські рубила допомогли племені підкорити помірний клімат Європи!"
                                                    else -> "🗺️ Успішний крок розселення!"
                                                }
                                                EpochType.HOMO_NEANDERTHALENSIS -> when (stageIndex) {
                                                    0 -> "⛰️ Знайдено надійний грот! Вапнякові печерні гроти Криму захищають клан від снігового бурану."
                                                    1 -> "🐻 Факели з вогнем вигнали хижаків! Стоянка Киїк-Коба в Криму стала безпечним прихистком неандертальців."
                                                    2 -> "🩹 Турбота й альтруїзм! Клан доглядав і годував пораненого мисливця до його повного одужання."
                                                    else -> "🏔️ Успішне виживання у печері!"
                                                }
                                                EpochType.HOMO_SAPIENS -> when (stageIndex) {
                                                    0 -> "🧊 Перетято Берингійський сухопутний міст! Кроманьйонці першими з людей заселили Америку."
                                                    1 -> "🛖 На Чернігівщині засновано стоянку Мізин! Споруджено міцні зимові житла з кісток та бивнів мамонта."
                                                    2 -> "🛶 Використання морських плотів та човнів дозволило Homo Sapiens освоїти Австралію та далекі острови!"
                                                    else -> "🌐 Успішне глобальне розселення!"
                                                }
                                                else -> "🧍 Примат піднявся на дві задні кінцівки! Висота огляду збільшилася — небезпеку помічено заздалегідь."
                                            }
                                            SoundEffectsManager.playQuizCorrect()
                                            val next = stageIndex + 1
                                            if (next >= scenarios.size) {
                                                isFinished = true
                                                SoundEffectsManager.playQuestSuccess()
                                            } else {
                                                stageIndex = next
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(52.dp)
                                            .testTag("savannah_action_stand_btn"),
                                         colors = ButtonDefaults.buttonColors(containerColor = AshCardSurface),
                                        shape = RoundedCornerShape(12.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, AmberFirePrimary.copy(alpha = 0.5f))
                                    ) {
                                        Text(
                                            text = when (epoch) {
                                                EpochType.AUSTRALOPITHECUS -> "🧍 Впевнений прямохідний крок (біпедалізм)"
                                                EpochType.HOMO_ERECTUS -> when (stageIndex) {
                                                    0 -> "🧭 Рухатися через Близький Схід уздовж річок"
                                                    1 -> "⛰️ Заснувати стоянку Королево на Закарпатті"
                                                    2 -> "🔥 Обігрівати стоянки приборканим вогнем"
                                                    else -> "🧭 Продовжувати розселення"
                                                }
                                                EpochType.HOMO_NEANDERTHALENSIS -> when (stageIndex) {
                                                    0 -> "⛰️ Шукати печерні гроти у скелях Криму"
                                                    1 -> "🐻 Вигнати ведмедів з печери Киїк-Коба вогнем"
                                                    2 -> "🩹 Доглядати й лікувати травмованого мисливця"
                                                    else -> "⛰️ Захистити клан у печері"
                                                }
                                                EpochType.HOMO_SAPIENS -> when (stageIndex) {
                                                    0 -> "🧊 Перейти Берингію за стадами мамонтів"
                                                    1 -> "🛖 Збудувати житло з кісток мамонта (Мізин)"
                                                    2 -> "🛶 Освоїти плоти та заселити нові континенти"
                                                    else -> "🌐 Розселятися по всьому світу"
                                                }
                                                else -> "🧍 Піднятися на дві кінцівки та озирнутися"
                                            },
                                            color = BoneIvory,
                                            fontSize = 13.sp
                                        )
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            SoundEffectsManager.playSavannahStep()
                                            SoundEffectsManager.playQuizWrong()
                                            storyLog = when (epoch) {
                                                EpochType.AUSTRALOPITHECUS -> "⚠️ Пересування на чотирьох у високій саванні звужує огляд і перегріває тіло! Використовуй двоногу ходу (біпедалізм)."
                                                EpochType.HOMO_ERECTUS -> "⚠️ Помилковий маршрут! Без доступу до прісної води та джерел кременю плем'я не зможе просунутися далі."
                                                EpochType.HOMO_NEANDERTHALENSIS -> "⚠️ Фатальна помилка! Спати на морозному вітрі без печери чи кидати хворих сородичів означає загибель клану."
                                                EpochType.HOMO_SAPIENS -> "⚠️ Невірно! Без житла з кісток мамонта (як у Мізині) та човнів людина розумна не зможе подолати крижані пустки й океани."
                                                else -> "⚠️ Невірно! Повзти на чотирьох у високій саванні небезпечно — поганий огляд і загроза від хижаків."
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(52.dp)
                                            .testTag("savannah_action_crawl_btn"),
                                        colors = ButtonDefaults.buttonColors(containerColor = AshCardSurface),
                                        shape = RoundedCornerShape(12.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, AmberFirePrimary.copy(alpha = 0.5f))
                                    ) {
                                        Text(
                                            text = when (epoch) {
                                                EpochType.AUSTRALOPITHECUS -> "🐾 Пересуватися на чотирьох кінцівках"
                                                EpochType.HOMO_ERECTUS -> when (stageIndex) {
                                                    0 -> "🌊 Плити у відкритий океан без човнів"
                                                    1 -> "🏜️ Повернутися назад у пустелю Сахара"
                                                    2 -> "❄️ Заснути в крижаній саванні без вогню"
                                                    else -> "⚠️ Відступити назад"
                                                }
                                                EpochType.HOMO_NEANDERTHALENSIS -> when (stageIndex) {
                                                    0 -> "⛺ Спати на відкритому снігу під вітром"
                                                    1 -> "🪵 Будувати курінь з сухого листя на морозі"
                                                    2 -> "❄️ Кинути травмованого сородича на морозі"
                                                    else -> "⚠️ Залишитися на відкритому морозі"
                                                }
                                                EpochType.HOMO_SAPIENS -> when (stageIndex) {
                                                    0 -> "🏜️ Повернутися назад у пустелю"
                                                    1 -> "⛺ Ночувати під відкритим дощем без даху"
                                                    2 -> "🌊 Стрибати у глибінь океану без плотів"
                                                    else -> "⚠️ Відступити назад"
                                                }
                                                else -> "🐾 Повзти на чотирьох у траві"
                                            },
                                            color = BoneIvory,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        // Summary Completion
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🌟", fontSize = 42.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = when (epoch) {
                                    EpochType.AUSTRALOPITHECUS -> "Савану подолано!"
                                    EpochType.HOMO_ERECTUS -> "Велике розселення успішно здійснено!"
                                    EpochType.HOMO_NEANDERTHALENSIS -> "Печерний прихисток Киїк-Коба здобуто!"
                                    EpochType.HOMO_SAPIENS -> "Континенти Землі успішно заселено!"
                                    else -> "Шлях подолано!"
                                },
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = SavannahGreen
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Ви заробили: +$evolutionScore очок еволюції прямоходіння 🧠",
                                style = MaterialTheme.typography.bodyMedium,
                                color = BoneIvory,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = {
                                    SoundEffectsManager.playTribalDrum()
                                    onComplete(evolutionScore)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("savannah_finish_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = SavannahGreen),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Завершити квест",
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
