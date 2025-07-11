package com.example.chillileafdiseasedetectionapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = OnPrimaryWhite,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryGreen,
    onSecondary = OnSecondaryWhite,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = TertiaryBlue,
    onTertiary = OnTertiaryWhite,
    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = OnTertiaryContainerDark,
    error = ErrorRed,
    onError = OnErrorWhite,
    background = BackgroundLight,
    onBackground = OnBackgroundDark,
    surface = SurfaceLight,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantDark
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlueDark,
    onPrimary = OnPrimaryContainerDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerLight,
    secondary = SecondaryGreenDark,
    onSecondary = OnSecondaryContainerDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerLight,
    tertiary = TertiaryBlue,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerLight,
    error = ErrorRed,
    onError = OnErrorWhite,
    background = BackgroundDark,
    onBackground = OnBackgroundLight,
    surface = SurfaceDark,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantLight
)

@Composable
fun ChilliLeafDiseaseDetectionAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}