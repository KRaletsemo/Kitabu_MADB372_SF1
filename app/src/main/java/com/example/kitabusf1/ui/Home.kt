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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.kitabusf1.data.Booking
import com.example.kitabusf1.data.BookingStatus
import com.example.kitabusf1.data.PlaceholderData
import com.example.kitabusf1.data.daysRemaining
import com.example.kitabusf1.data.isOverdue
import com.example.kitabusf1.data.matchesSearch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSeeAllBookings: () -> Unit,
    modifier: Modifier = Modifier
) {
    var query by rememberSaveable { mutableStateOf("") }

    //Data will be hard coded and use as placeholders till actual database is made 
    val dueSoon = PlaceholderData.bookings
        .filter { it.status == BookingStatus.ACTIVE && it.book.matchesSearch(query) }
        .sortedBy { it.returnDeadline }

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

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SearchField(query = query, onQueryChange = { query = it })
            }

            item {
                SectionHeader(title = "Due soon", onSeeAll = onSeeAllBookings)
            }
            item {
                if (dueSoon.isEmpty()) {
                    EmptyMessage("Nothing due soon")
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(dueSoon, key = { it.id }) { booking ->
                            DueSoonCard(booking)
                        }
                    }
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

private fun dueLabel(booking: Booking): String {
    if (booking.isOverdue()) return "Overdue"
    val days = booking.daysRemaining()
    return if (days == 1) "1 day left" else "$days days left"
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
