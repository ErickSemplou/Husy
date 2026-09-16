package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.data.local.GameProgressEntity
import com.example.data.model.EpochType
import com.example.ui.components.ResourceHUD
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun evolution_hud_screenshot() {
        composeTestRule.setContent {
            MyApplicationTheme {
                ResourceHUD(
                    progress = GameProgressEntity(),
                    currentEpoch = EpochType.DRYOPITHECUS
                )
            }
        }
    }
}
