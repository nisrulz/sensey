package com.github.nisrulz.senseysample.ui.nav

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.nisrulz.senseysample.navigation.GestureGroup
import com.github.nisrulz.senseysample.navigation.allGroups
import com.github.nisrulz.senseysample.navigation.gestureGroupInfo

@Composable
internal fun GestureNavBar(
    selectedGroup: GestureGroup,
    onGroupSelected: (GestureGroup) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = modifier) {
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
