package br.ufms.assistente.navegacao.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1565C0),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD6E4FF),
    secondary = Color(0xFF2E7D32),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF424242),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF424242),
    onSurfaceVariant = Color(0xFF757575),
    error = Color(0xFFB71C1C),
    onError = Color(0xFFFFFFFF)
)

@Composable
fun AssistenteNavegacaoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}