package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PrehistoricDarkColorScheme = darkColorScheme(
    primary = AmberFirePrimary,
    onPrimary = Color(0xFF1A0C02),
    primaryContainer = OchreDark,
    onPrimaryContainer = BoneIvory,
    secondary = OchreTerracotta,
    onSecondary = Color(0xFF1E0A02),
    secondaryContainer = OchreSurfaceVariant,
    onSecondaryContainer = BoneIvory,
    tertiary = SavannahGreen,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF334220),
    onTertiaryContainer = BoneIvory,
    background = ObsidianBackground,
    onBackground = BoneIvory,
    surface = CaveStoneSurface,
    onSurface = BoneIvory,
    surfaceVariant = AshCardSurface,
    onSurfaceVariant = Color(0xFFD4C8BE),
    outline = CharcoalBorder,
    error = DangerRed,
    onError = Color.White
)

private val PrehistoricLightColorScheme = lightColorScheme(
    primary = OchreDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDBC9),
    onPrimaryContainer = Color(0xFF3B1000),
    secondary = AmberFireDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE082),
    onSecondaryContainer = Color(0xFF261A00),
    tertiary = SavannahGreen,
    onTertiary = Color.White,
    background = Color(0xFFFBF8F4),
    onBackground = Color(0xFF201A17),
    surface = Color(0xFFF3ECE4),
    onSurface = Color(0xFF201A17),
    surfaceVariant = Color(0xFFE5DDD5),
    onSurfaceVariant = Color(0xFF4D443E),
    outline = Color(0xFF857368),
    error = DangerRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // default to rich prehistoric dark atmosphere
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) PrehistoricDarkColorScheme else PrehistoricLightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun EvolutionTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) = MyApplicationTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)


