package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.audio.SoundEffectsManager
import com.example.data.model.EpochType
import com.example.ui.theme.AmberFirePrimary
import com.example.ui.theme.AshCardSurface
import com.example.ui.theme.BoneIvory
import com.example.ui.theme.CaveStoneSurface
import com.example.ui.theme.FireGlow
import com.example.ui.theme.OchreDark
import com.example.ui.theme.SavannahGreen

@Composable
fun AscensionCelebrationDialog(
    newEpoch: EpochType,
    onContinue: () -> Unit
) {
    Dialog(
        onDismissRequest = onContinue,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 560.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(
                    2.dp,
                    Brush.verticalGradient(listOf(FireGlow, AmberFirePrimary, OchreDark)),
                    RoundedCornerShape(24.dp)
                )
                .testTag("ascension_celebration_dialog"),
            color = CaveStoneSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Banner Image of the New Epoch
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                ) {
                    Image(
                        painter = painterResource(id = newEpoch.imageRes),
                        contentDescription = newEpoch.title,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        CaveStoneSurface.copy(alpha = 0.85f),
                                        CaveStoneSurface
                                    )
                                )
                            )
                    )
                    // Hominid Avatar Centerpiece
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(AmberFirePrimary)
                            .border(2.5.dp, AmberFirePrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = newEpoch.avatarRes),
                            contentDescription = "Аватар ${newEpoch.title}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🌟 ЕВОЛЮЦІЙНИЙ СТРИБОК!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = FireGlow,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = newEpoch.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = BoneIvory,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = newEpoch.scientificName,
                        style = MaterialTheme.typography.titleSmall,
                        color = AmberFirePrimary,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Epoch Stats Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = AshCardSurface),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            InfoRow(icon = "⏳", label = "Часова епоха", value = newEpoch.timePeriod)
                            Spacer(modifier = Modifier.height(6.dp))
                            InfoRow(icon = "🧠", label = "Об'єм мозку", value = newEpoch.brainVolume)
                            Spacer(modifier = Modifier.height(6.dp))
                            InfoRow(icon = "📍", label = "Ареал розселення", value = newEpoch.location)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Ваше плем'я успішно подолало кліматичні та біологічні бар'єри, відкривши нову главу в історії роду людей! Отримано +50 очок Еволюції та максимальний бойовий дух.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BoneIvory,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            SoundEffectsManager.playTribalDrum()
                            onContinue()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("ascension_confirm_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = AmberFirePrimary),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = "Вступити в нову еру!",
                            color = Color.Black,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun InfoRow(icon: String, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.labelMedium,
            color = Color.LightGray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = BoneIvory
        )
    }
}
