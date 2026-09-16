package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.audio.SoundEffectsManager
import com.example.data.local.EvolutionDatabase
import com.example.data.model.EpochType
import com.example.data.repository.EvolutionRepository
import com.example.game.EvolutionViewModel
import com.example.game.GameScreen
import com.example.ui.components.AscensionCelebrationDialog
import com.example.ui.screens.CampScreen
import com.example.ui.screens.EncyclopediaScreen
import com.example.ui.screens.EvolutionEpochMapScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.TechTreeScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.AmberFirePrimary
import com.example.ui.theme.AshCardSurface
import com.example.ui.theme.BoneIvory
import com.example.ui.theme.CaveStoneSurface
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EvolutionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = EvolutionDatabase.getInstance(applicationContext)
        val repository = EvolutionRepository(database.evolutionDao())

        setContent {
            EvolutionTheme {
                val viewModel: EvolutionViewModel = viewModel(
                    factory = EvolutionViewModel.provideFactory(repository)
                )
                EvolutionGameApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvolutionGameApp(
    viewModel: EvolutionViewModel
) {
    val state by viewModel.uiState.collectAsState()
    val isSoundOn by SoundEffectsManager.isSoundEnabled.collectAsState()
    var showResetDialog by remember { mutableStateOf(false) }
    var showTribeRenameDialog by remember { mutableStateOf(false) }
    var renameInput by remember { mutableStateOf(state.progress.tribeName) }

    // Ascension Celebration Modal
    if (state.showEpochAscensionDialog && state.ascendedEpoch != null) {
        AscensionCelebrationDialog(
            newEpoch = state.ascendedEpoch!!,
            onContinue = { viewModel.dismissAscensionDialog() }
        )
    }

    // Reset Confirmation Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = {
                Text(
                    text = "Почати еволюцію знову?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = AmberFirePrimary
                )
            },
            text = {
                Text(
                    text = "Весь прогрес, відкриті квести та результати тестування будуть скинуті до етапу Дріопітека.",
                    color = BoneIvory
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        SoundEffectsManager.playTribalDrum()
                        viewModel.resetEntireGame()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed)
                ) {
                    Text("Скинути все", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showResetDialog = false }) {
                    Text("Скасувати", color = BoneIvory)
                }
            },
            containerColor = CaveStoneSurface
        )
    }

    // Tribe Rename Dialog
    if (showTribeRenameDialog) {
        AlertDialog(
            onDismissRequest = { showTribeRenameDialog = false },
            title = {
                Text(
                    text = "Назва вашої зграї/племені",
                    style = MaterialTheme.typography.titleLarge,
                    color = AmberFirePrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                OutlinedTextField(
                    value = renameInput,
                    onValueChange = { renameInput = it },
                    label = { Text("Назва") },
                    singleLine = true,
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AmberFirePrimary,
                        unfocusedBorderColor = AshCardSurface
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        SoundEffectsManager.playTribalDrum()
                        viewModel.setTribeName(renameInput)
                        showTribeRenameDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AmberFirePrimary)
                ) {
                    Text("Зберегти", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showTribeRenameDialog = false }) {
                    Text("Скасувати", color = BoneIvory)
                }
            },
            containerColor = CaveStoneSurface
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.testTag("app_top_bar_title")
                    ) {
                        Text(
                            text = "Еволюція",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = BoneIvory
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = AmberFirePrimary
                        ) {
                            Text(
                                text = state.currentEpoch.iconEmoji,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                fontSize = 12.sp
                            )
                        }
                    }
                },
                actions = {
                    // Sound Toggle Button
                    IconButton(
                        onClick = {
                            val newStatus = SoundEffectsManager.toggleSound()
                            if (newStatus) {
                                SoundEffectsManager.playTribalDrum()
                            }
                        },
                        modifier = Modifier.testTag("sound_toggle_button")
                    ) {
                        Icon(
                            if (isSoundOn) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                            contentDescription = if (isSoundOn) "Звук увімкнено" else "Звук вимкнено",
                            tint = if (isSoundOn) AmberFirePrimary else Color.Gray
                        )
                    }

                    IconButton(
                        onClick = {
                            renameInput = state.progress.tribeName
                            showTribeRenameDialog = true
                        },
                        modifier = Modifier.testTag("rename_tribe_button")
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Перейменувати", tint = BoneIvory)
                    }
                    IconButton(
                        onClick = { showResetDialog = true },
                        modifier = Modifier.testTag("reset_game_button")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Почати спочатку", tint = BoneIvory)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = CaveStoneSurface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = CaveStoneSurface,
                modifier = Modifier.testTag("main_navigation_bar")
            ) {
                NavigationBarItem(
                    selected = state.currentScreen == GameScreen.WELCOME,
                    onClick = {
                        SoundEffectsManager.playTribalDrum()
                        viewModel.setScreen(GameScreen.WELCOME)
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Головна") },
                    label = { Text("Головна") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = AmberFirePrimary,
                        indicatorColor = AmberFirePrimary,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    ),
                    modifier = Modifier.testTag("nav_item_welcome")
                )

                NavigationBarItem(
                    selected = state.currentScreen == GameScreen.CAMP,
                    onClick = {
                        SoundEffectsManager.playTribalDrum()
                        viewModel.setScreen(GameScreen.CAMP)
                    },
                    icon = { Icon(Icons.Default.PlayCircle, contentDescription = "Гра") },
                    label = { Text("Квести") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = AmberFirePrimary,
                        indicatorColor = AmberFirePrimary,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    ),
                    modifier = Modifier.testTag("nav_item_camp")
                )

                NavigationBarItem(
                    selected = state.currentScreen == GameScreen.ENCYCLOPEDIA,
                    onClick = {
                        SoundEffectsManager.playTribalDrum()
                        viewModel.setScreen(GameScreen.ENCYCLOPEDIA)
                    },
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = "База") },
                    label = { Text("База") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = AmberFirePrimary,
                        indicatorColor = AmberFirePrimary,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    ),
                    modifier = Modifier.testTag("nav_item_encyclopedia")
                )

                NavigationBarItem(
                    selected = state.currentScreen == GameScreen.EPOCH_MAP,
                    onClick = {
                        SoundEffectsManager.playTribalDrum()
                        viewModel.setScreen(GameScreen.EPOCH_MAP)
                    },
                    icon = { Icon(Icons.Default.Map, contentDescription = "Етапи") },
                    label = { Text("Етапи") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = AmberFirePrimary,
                        indicatorColor = AmberFirePrimary,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    ),
                    modifier = Modifier.testTag("nav_item_epoch_map")
                )

                NavigationBarItem(
                    selected = state.currentScreen == GameScreen.QUIZ,
                    onClick = {
                        SoundEffectsManager.playTribalDrum()
                        viewModel.setScreen(GameScreen.QUIZ)
                    },
                    icon = { Icon(Icons.Default.Quiz, contentDescription = "Тести") },
                    label = { Text("Тести") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = AmberFirePrimary,
                        indicatorColor = AmberFirePrimary,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    ),
                    modifier = Modifier.testTag("nav_item_quiz")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 680.dp)
            ) {
                when (state.currentScreen) {
                    GameScreen.WELCOME -> WelcomeScreen(state = state, viewModel = viewModel)
                    GameScreen.CAMP -> CampScreen(state = state, viewModel = viewModel)
                    GameScreen.TECH_TREE -> TechTreeScreen(state = state, viewModel = viewModel)
                    GameScreen.ENCYCLOPEDIA -> EncyclopediaScreen(state = state, viewModel = viewModel)
                    GameScreen.EPOCH_MAP -> EvolutionEpochMapScreen(state = state, viewModel = viewModel)
                    GameScreen.QUIZ -> QuizScreen(state = state, viewModel = viewModel)
                }
            }
        }
    }
}
