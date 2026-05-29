package com.github.nisrulz.senseysample.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

data class SensorItem(
    val label: String,
    val isSelected: Boolean,
    val onSelect: () -> Unit,
)

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen(
        sensors =
            listOf(
                SensorItem("Shake Detector", true, {}),
                SensorItem("Flip Detector", false, {}),
            ),
        eventCount = 5,
        resultText = "",
        onTouchDetectorClick = {},
    )
}

@Composable
fun MainScreen(
    sensors: List<SensorItem>,
    eventCount: Int,
    resultText: String,
    onTouchDetectorClick: () -> Unit,
) {
    var detected by remember { mutableStateOf(false) }
    LaunchedEffect(eventCount) {
        if (eventCount > 0) {
            detected = true
            delay(3000)
            detected = false
        }
    }
    Surface(
        modifier =
            Modifier
                .fillMaxSize()
                .systemBarsPadding(),
        color = PrimaryBlue,
    ) {
        Column {
            Text(
                text = "Sensey",
                color = Color(0xFFFF9800),
                fontSize = 40.sp,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Column(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState()),
                ) {
                    sensors.forEach { sensor ->
                        SenseyRadioButton(
                            label = sensor.label,
                            selected = sensor.isSelected,
                            onSelect = sensor.onSelect,
                        )
                    }
                }
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(color = White)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Status",
                        color = DividerGray,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = resultText,
                        color = if (detected) AccentOrange else DividerGray,
                        fontSize = 14.sp,
                        fontWeight = if (detected) FontWeight.Bold else FontWeight.Normal,
                    )
                }
                Row(
                    modifier =
                        Modifier
                            .background(color = White)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    DetectionIndicator(
                        detected = detected,
                    )

                    Button(
                        onClick = onTouchDetectorClick,
                        modifier =
                            Modifier
                                .weight(1f)
                                .height(48.dp),
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = NavigationCyan,
                                contentColor = White,
                            ),
                        shape = RoundedCornerShape(12.dp),
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
    }
}
