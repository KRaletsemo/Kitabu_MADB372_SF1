package com.example.kitabusf1.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.example.kitabusf1.data.PlaceholderData
import com.example.kitabusf1.data.matchesSearch


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun CatalogueScreen(modifier: Modifier = Modifier) {
    //this query is used so that it only belongs to this screen
    var query by rememberSaveable { mutableStateOf("") } //used to keep text even when phone is rotated

    val books = PlaceholderData.books.filter { it.matchesSearch(query) } //search function

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

        //book searching logic
        if (books.isEmpty()){ //used to show message of book not being there, better than empty screen
            Text(
                text = "No books match your search",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(16.dp)
            )

        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp), //responsive grid, good for adding more if needed
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
               //One grid cell per book, the key means each card has an identity
                //Alpha modifier is for transparency of the card
                items(books, key = { it.id} ) { book ->
                    BookCard(
                        book = book,
                        modifier = Modifier.alpha(if (book.isAvailable) 1f else 0.5f)
                    )
                }
            }
        }
    }
}
