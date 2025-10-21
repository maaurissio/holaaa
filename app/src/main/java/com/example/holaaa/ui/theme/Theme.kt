package com.example.holaaa.ui.theme

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

// Paleta para Modo Claro (Huerto Hogar)
private val LightColorScheme = lightColorScheme(
    primary = VerdePrincipal,
    secondary = VerdeClaro,
    tertiary = VerdeOscuro,
    background = CremaFondo,
    surface = CremaFondo,
    onPrimary = TextoClaro,
    onSecondary = TextoOscuro,
    onTertiary = TextoClaro,
    onBackground = TextoOscuro,
    onSurface = TextoOscuro,
    error = RojoEliminar
)

// (Usaremos la misma paleta para el modo oscuro por simplicidad)
private val DarkColorScheme = lightColorScheme(
    primary = VerdePrincipal,
    secondary = VerdeClaro,
    tertiary = VerdeOscuro,
    background = CremaFondo,
    surface = CremaFondo,
    onPrimary = TextoClaro,
    onSecondary = TextoOscuro,
    onTertiary = TextoClaro,
    onBackground = TextoOscuro,
    onSurface = TextoOscuro,
    error = RojoEliminar
)

@Composable
fun HolaaaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.tertiary.toArgb() // Color de la barra de estado
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false // Íconos claros
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}