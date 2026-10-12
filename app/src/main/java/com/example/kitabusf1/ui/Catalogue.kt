package com.example.kitabusf1.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kitabusf1.di.AppViewModelProvider
import com.example.kitabusf1.domain.Book
import com.example.kitabusf1.domain.RENTAL_OPTIONS

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun CatalogueScreen(
    modifier: Modifier = Modifier,
    viewModel: CatalogueViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val query = viewModel.query
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val books = uiState.books

    var selectedBook by remember { mutableStateOf<Book?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.messages.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        //Column used to stack the top bar and then the Search bar
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {Text("Catalogue")},
                windowInsets = WindowInsets(0)

            )

            SearchField(
                query = query,
                onQueryChange = viewModel::onQueryChange,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            //book searching logic
            if (!uiState.isLoading && books.isEmpty()){ //used to show message of book not being there, better than empty screen
                Text(
                    text = "No books match your search",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )

            } else if (books.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 160.dp), //responsive grid, good for adding more if needed
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    //One grid cell per book, the key means each card has an identity
                    //Alpha modifier is for transparency of the card
                    items(books, key = { it.id }) { book ->
                        BookCard(
                            book = book,
                            modifier = Modifier
                                .alpha(if (book.isAvailable) 1f else 0.5f)
                                .clip(CardDefaults.shape)
                                .clickable(enabled = book.isAvailable) { selectedBook = book }
                        )
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    //Book that's been selected will trigger the sheet
    selectedBook?.let { book ->
        ReservationSheet(
            book = book,
            onDismiss = { selectedBook = null },
            onReserve = { days ->
                viewModel.reserveBook(book, days)
                selectedBook = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable

//Bottom Sheet to show and reserve books
private fun ReservationSheet(
    book: Book,
    onDismiss: () -> Unit,
    onReserve: (days: Int) -> Unit
) {
    var selectedDays by rememberSaveable { mutableIntStateOf(RENTAL_OPTIONS.first()) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
        ) {
            Text(text = book.title, style = MaterialTheme.typography.titleLarge)
            Text(
                text = book.author,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            //rental selection
            Spacer(Modifier.height(16.dp))
            Text(text = "Rental duration", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))

            //Rental logic selection
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RENTAL_OPTIONS.forEach { days ->
                    FilterChip(
                        selected = selectedDays == days,
                        onClick = { selectedDays = days },
                        label = { Text("$days days") }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { onReserve(selectedDays) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reserve")
            }
        }
    }
}