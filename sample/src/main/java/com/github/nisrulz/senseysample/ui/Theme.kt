package com.github.nisrulz.senseysample.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DarkColorScheme =
    darkColorScheme(
        primary = Color(0xFFFF9800),
        secondary = Color(0xFF16BCD9),
        tertiary = Color(0xFFFF1D7F),
        background = Color(0xFF000000),
        surface = Color(0xFF121212),
        onPrimary = Color(0xFFFFFFFF),
        onSecondary = Color(0xFFFFFFFF),
        onTertiary = Color(0xFFFFFFFF),
        onBackground = Color(0xFFFFFFFF),
        onSurface = Color(0xFFFFFFFF),
        onSurfaceVariant = Color(0xFFB6B6B6),
        outline = Color(0xFFB6B6B6),
    )

private val LightColorScheme =
    lightColorScheme(
        primary = Color(0xFFFF6F00),
        secondary = Color(0xFF0097A7),
        tertiary = Color(0xFFD81B60),
        background = Color(0xFFFFFFFF),
        surface = Color(0xFFF5F5F5),
        onPrimary = Color(0xFFFFFFFF),
        onSecondary = Color(0xFFFFFFFF),
        onTertiary = Color(0xFFFFFFFF),
        onBackground = Color(0xFF1C1B1F),
        onSurface = Color(0xFF1C1B1F),
        onSurfaceVariant = Color(0xFF49454F),
        outline = Color(0xFF79747E),
    )

@Composable
fun SenseyTheme(content: @Composable () -> Unit) {
    val colorScheme = if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}

@Composable
fun PreviewTheme(content: @Composable () -> Unit) {
    SenseyTheme {
        Surface(
            modifier = Modifier.wrapContentSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}
