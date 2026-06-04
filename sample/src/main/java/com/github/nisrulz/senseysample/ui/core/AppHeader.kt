@file:OptIn(ExperimentalMaterial3Api::class)

package com.github.nisrulz.senseysample.ui.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.github.nisrulz.senseysample.R

@Composable
internal fun AppHeader(scrollBehavior: TopAppBarScrollBehavior? = null) {
    Surface(
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface,
    ) {
        TopAppBar(
            modifier = Modifier.padding(bottom = Paddings.md),
            scrollBehavior = scrollBehavior,
            title = {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Sensey",
                    modifier = Modifier.height(60.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                )
            },
            colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
        )
    }
}

@PreviewLightDark
@Composable
private fun AppHeaderPreview() {
    PreviewTheme {
        AppHeader()
    }
}
