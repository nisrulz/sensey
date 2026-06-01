package com.github.nisrulz.senseysample.ui.nav

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.github.nisrulz.senseysample.navigation.GestureGroup
import com.github.nisrulz.senseysample.navigation.TouchGroup
import com.github.nisrulz.senseysample.navigation.allGroups
import com.github.nisrulz.senseysample.navigation.gestureGroupInfo
import com.github.nisrulz.senseysample.ui.core.PreviewTheme

@Composable
internal fun GestureNavRailHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Sensey",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
internal fun GestureNavRailItems(
    selectedGroup: GestureGroup,
    onGroupSelected: (GestureGroup) -> Unit,
) {
    allGroups.forEach { group ->
        val info = gestureGroupInfo[group]!!
        NavigationRailItem(
            selected = group == selectedGroup,
            onClick = { onGroupSelected(group) },
            icon = { Text(text = info.emoji) },
            label = { Text(info.label) },
        )
    }
}

@PreviewLightDark
@Composable
private fun GestureNavRailPreview() {
    PreviewTheme {
        Column(Modifier.width(80.dp)) {
            GestureNavRailHeader()
            GestureNavRailItems(
                selectedGroup = TouchGroup,
                onGroupSelected = {},
            )
        }
    }
}
