package com.example.holaaa.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color // <-- ¡Esta es la importación que faltaba!

private val LightColorScheme = lightColorScheme(
    primary = VerdeEsmeralda,
    secondary = MarronClaro,
    background = BlancoSuave,
    surface = Color.White, // Las tarjetas y barras pueden ser blancas puras
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = GrisOscuro,
    onSurface = GrisOscuro,
    error = RojoError,
    onError = Color.White
)

// Por ahora, no definiremos un tema oscuro, pero la estructura está lista.
private val DarkColorScheme = LightColorScheme

@Composable
fun HuertoHogarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Esto debería funcionar ahora
        content = content
    )
}
