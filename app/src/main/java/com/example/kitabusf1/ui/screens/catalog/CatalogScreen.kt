package com.example.kitabusf1.ui.screens.catalog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Catalog tab (full browsing screen).
 *
 * Placeholder for now: the full book grid and the reservation bottom sheet come in a later checklist item.
 */
@Composable
fun CatalogScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Catalog", style = MaterialTheme.typography.headlineMedium)
    }
}
