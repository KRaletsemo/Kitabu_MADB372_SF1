package com.example.kitabusf1.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kitabusf1.di.AppViewModelProvider
import com.example.kitabusf1.domain.Booking
import com.example.kitabusf1.domain.isOverdue

private val fullWidth: LazyGridItemSpanScope.() -> GridItemSpan = { GridItemSpan(maxLineSpan) }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSeeAllBookings: () -> Unit,
    onSeeAllCatalogue: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val query = viewModel.query
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dueSoon = uiState.dueSoon
    val cataloguePreview = uiState.cataloguePreview

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Kitabu") },
            actions = {
                IconButton(onClick = { }) {
                    Icon(Icons.Filled.AccountCircle, contentDescription = "Profile")
                }
            },
            windowInsets = WindowInsets(0)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(span = fullWidth) {
                SearchField(query = query, onQueryChange = viewModel::onQueryChange)
            }

            item(span = fullWidth) {
                SectionHeader(title = "Due soon", onSeeAll = onSeeAllBookings)
            }
            item(span = fullWidth) {
                if (dueSoon.isEmpty()) {
                    if (!uiState.isLoading) EmptyMessage("Nothing due soon")
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(dueSoon, key = { it.id }) { booking ->
                            DueSoonCard(booking)
                        }
                    }
                }
            }

            item(span = fullWidth) {
                SectionHeader(title = "Browse catalogue", onSeeAll = onSeeAllCatalogue)
            }
            if (cataloguePreview.isEmpty()) {
                if (!uiState.isLoading) {
                    item(span = fullWidth) { EmptyMessage("No books match your search") }
                }
            } else {
                items(cataloguePreview, key = { it.id }) { book ->
                    BookCard(book)
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, onSeeAll: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        TextButton(onClick = onSeeAll) { Text("See all") }
    }
}

@Composable
private fun DueSoonCard(booking: Booking, modifier: Modifier = Modifier) {
    Card(modifier = modifier.width(160.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = booking.book.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = booking.book.author,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = dueLabel(booking),
                style = MaterialTheme.typography.labelMedium,
                color = if (booking.isOverdue()) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun EmptyMessage(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}
