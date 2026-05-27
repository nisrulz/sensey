package com.github.nisrulz.senseysample.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SensorItem(
    val label: String,
    val isSelected: Boolean,
    val onSelect: () -> Unit,
)

@Composable
fun MainScreen(
    sensors: List<SensorItem>,
    resultText: String,
    onTouchDetectorClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        color = PrimaryBlue,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Select a gesture detector",
                    color = Color(0xFF5D9CEC),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
                sensors.forEach { sensor ->
                    SenseyRadioButton(
                        label = sensor.label,
                        selected = sensor.isSelected,
                        onSelect = sensor.onSelect,
                    )
                }
            }

            ResultArea(
                text = resultText,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
                    .padding(horizontal = 16.dp),
            )

            Button(
                onClick = onTouchDetectorClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentOrange,
                    contentColor = White,
                ),
                shape = RoundedCornerShape(0.dp),
            ) {
                Text(
                    text = "Touch Detector",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}
