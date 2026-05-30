package com.github.nisrulz.senseysample.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun AppHeader() {
    Surface(
        shadowElevation = 4.dp,
        color = PrimaryBlue,
    ) {
        Column {
            Text(
                text = "Sensey",
                color = Color(0xFFFF9800),
                fontSize = 40.sp,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            )
            HorizontalDivider()
        }
    }
}
