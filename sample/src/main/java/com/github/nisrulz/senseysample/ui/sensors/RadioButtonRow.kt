package com.github.nisrulz.senseysample.ui.sensors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.sp
import com.github.nisrulz.senseysample.ui.core.Paddings
import com.github.nisrulz.senseysample.ui.core.PreviewTheme

@Composable
internal fun RadioButtonRow(
    label: String,
    helperText: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    showHelperText: Boolean = true,
    content: @Composable () -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .padding(horizontal = Paddings.sm, vertical = Paddings.md)
                .then(
                    if (selected) {
                        Modifier
                            .clip(RoundedCornerShape(Paddings.md))
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                    } else {
                        Modifier
                    },
                ).clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) { onSelect() },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = Paddings.sm, vertical = Paddings.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f).padding(vertical = Paddings.md),
            )
            RadioButton(
                selected = selected,
                onClick = null,
                colors =
                    RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    ),
            )
        }

        if (showHelperText && helperText.isNotBlank()) {
            Text(
                text = helperText,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                fontSize = 14.sp,
                modifier =
                    Modifier.padding(
                        start = Paddings.sm,
                        top = Paddings.none,
                        end = Paddings.sm,
                        bottom = Paddings.sm,
                    ),
            )
        }

        content()
    }
}

@PreviewLightDark
@Composable
private fun RadioButtonRowPreview() {
    PreviewTheme {
        RadioButtonRow(
            label = "Shake Gesture",
            helperText = "Shake the device",
            selected = true,
            onSelect = {},
        )
    }
}
