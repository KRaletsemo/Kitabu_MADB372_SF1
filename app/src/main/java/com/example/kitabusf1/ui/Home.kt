package com.example.kitabusf1.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Home tab (landing screen).
 *
 * Placeholder for now: it only proves the navigation scaffold works. The real layout
 * (search bar, "Due soon" rail, "Browse catalog" preview grid) is built in the next checklist item.
 */
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    // Box with contentAlignment = Center puts the label in the middle of the screen.
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Home", style = MaterialTheme.typography.headlineMedium)
    }
}
