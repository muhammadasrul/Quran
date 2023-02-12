package com.acun.quranicplus.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColors(
    primary = Mariner,
    onPrimary = AliceBlue,
    onSecondary = GrayChateau,
    primaryVariant = HavelockBlue,
    background = EbonyClay,
    surface = BigStone,
    onBackground = NileBlue,
    onSurface = AliceBlue,
)

private val LightColorScheme = lightColors(
    primary = Mariner,
    onPrimary = Mariner,
    onSecondary = WildBlueYonder,
    primaryVariant = HavelockBlue,
    background = WhiteLilac,
    surface = Color.White,
    onBackground = GhostWhite,
    onSurface = Dune,
)

@Composable
fun QuranicPlusTheme(
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
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colors = colorScheme,
        content = content
    )
}