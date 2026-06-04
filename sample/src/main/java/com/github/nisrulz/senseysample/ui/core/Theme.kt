package com.github.nisrulz.senseysample.ui.core

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
        primary = Color(0xFFFF6D00),
        onPrimary = Color(0xFFFFFFFF),
        secondary = Color(0xFFFFB74D),
        onSecondary = Color(0xFF3A1E00),
        secondaryContainer = Color(0xFF5C2E00),
        onSecondaryContainer = Color(0xFFFFDDB3),
        tertiary = Color(0xFF4DB6AC),
        onTertiary = Color(0xFF00332E),
        background = Color(0xFF1A1C1E),
        onBackground = Color(0xFFE2E2E6),
        surface = Color(0xFF1A1C1E),
        onSurface = Color(0xFFE2E2E6),
        surfaceVariant = Color(0xFF44474E),
        onSurfaceVariant = Color(0xFFC4C6CF),
        outline = Color(0xFF8E9099),
    )

private val LightColorScheme =
    lightColorScheme(
        primary = Color(0xFFFF6D00),
        onPrimary = Color(0xFFFFFFFF),
        secondary = Color(0xFFFF8F00),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFFFE0B2),
        onSecondaryContainer = Color(0xFF3A1E00),
        tertiary = Color(0xFF006A4E),
        onTertiary = Color(0xFFFFFFFF),
        background = Color(0xFFFFFBFE),
        onBackground = Color(0xFF1C1B1F),
        surface = Color(0xFFFFFBFE),
        onSurface = Color(0xFF1C1B1F),
        surfaceVariant = Color(0xFFE7E0EC),
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
