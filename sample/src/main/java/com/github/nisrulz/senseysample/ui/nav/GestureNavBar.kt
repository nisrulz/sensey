package com.github.nisrulz.senseysample.ui.nav

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.github.nisrulz.senseysample.navigation.GestureGroup
import com.github.nisrulz.senseysample.navigation.TouchGroup
import com.github.nisrulz.senseysample.navigation.allGroups
import com.github.nisrulz.senseysample.navigation.gestureGroupInfo
import com.github.nisrulz.senseysample.ui.core.PreviewTheme

@Composable
internal fun GestureNavBar(
    selectedGroup: GestureGroup,
    onGroupSelected: (GestureGroup) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier.shadow(8.dp),
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
    ) {
        allGroups.forEach { group ->
            val info = gestureGroupInfo[group]!!
            NavigationBarItem(
                selected = group == selectedGroup,
                onClick = { onGroupSelected(group) },
                icon = { Text(text = info.emoji) },
                label = { Text(info.label) },
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun GestureNavBarPreview() {
    PreviewTheme {
        GestureNavBar(
            selectedGroup = TouchGroup,
            onGroupSelected = {},
        )
    }
}
