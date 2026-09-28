package com.example.kitabusf1.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun CatalogueScreen(modifier: Modifier = Modifier) {
    //this query is used so that it only belongs to this screen
    var query by rememberSaveable { mutableStateOf("") } //used to keep text even when phone is rotated

    //Column used to stack the top bar and then the Search bar
    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = {Text("Catalogue")},
            windowInsets = WindowInsets(0)

        )

        SearchField(
            query = query,
            onQueryChange = { query = it },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
