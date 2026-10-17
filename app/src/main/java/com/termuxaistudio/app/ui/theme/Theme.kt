package com.termuxaistudio.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4FC3F7),
    onPrimary = Color(0xFF00344A),
    primaryContainer = Color(0xFF004D6B),
    onPrimaryContainer = Color(0xFFC5E7FF),
    secondary = Color(0xFF80CBC4),
    onSecondary = Color(0xFF003731),
    background = Color(0xFF0D1117),
    onBackground = Color(0xFFE6EDF3),
    surface = Color(0xFF161B22),
    onSurface = Color(0xFFE6EDF3),
    surfaceVariant = Color(0xFF21262D),
    onSurfaceVariant = Color(0xFFC9D1D9),
    outline = Color(0xFF30363D)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF00668A),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC5E7FF),
    onPrimaryContainer = Color(0xFF001E2C),
    secondary = Color(0xFF4A6360),
    onSecondary = Color.White,
    background = Color(0xFFF6F8FA),
    onBackground = Color(0xFF1F2328),
    surface = Color.White,
    onSurface = Color(0xFF1F2328),
    surfaceVariant = Color(0xFFEEF2F5),
    onSurfaceVariant = Color(0xFF42474E),
    outline = Color(0xFFD0D7DE)
)

@Composable
fun TermuxAIStudioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
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
