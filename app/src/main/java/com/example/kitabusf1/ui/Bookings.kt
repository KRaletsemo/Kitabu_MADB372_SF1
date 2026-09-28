package com.example.kitabusf1.ui

import androidx.annotation.OptIn
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kitabusf1.data.Booking
import com.example.kitabusf1.data.BookingStatus
import com.example.kitabusf1.data.PlaceholderData
import com.example.kitabusf1.data.isOverdue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsScreen(modifier: Modifier = Modifier) {
    val bookings = PlaceholderData.bookings
        .filter { it.status != BookingStatus.RETURNED } //keeps note of active and pending books
        .sortedBy { it.returnDeadline }//earliest deadline sorted first

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {Text("My Bookings")},
            windowInsets = WindowInsets(0)
        )

        if (bookings.isEmpty()) {
            Text(
                text = "You have no bookings",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(16.dp)

            )

        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // makes one card per booking
                items(bookings, key = { it.id }) { booking ->
                    BookingCard(booking)

                }
            }
        }
    }
}

@Composable
fun BookingCard(booking: Booking, modifier: Modifier = Modifier){
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = booking.book.title, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = booking.book.author,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.width(8.dp))
                StatusTag(booking.status)
            }

            Spacer(Modifier.height(12.dp))
            Text(
                text = "Reserved ${formatDate(booking.bookingDate)}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Return by ${formatDate(booking.returnDeadline)}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(Modifier.height(4.dp))
            Text(
                text = dueLabel(booking),
                style = MaterialTheme.typography.labelLarge,
                color = if (booking.isOverdue()) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun StatusTag(status: BookingStatus) {
    val isActive = status == BookingStatus.ACTIVE
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isActive) MaterialTheme.colorScheme.tertiaryContainer
        else MaterialTheme.colorScheme.surface,
        contentColor = if (isActive) MaterialTheme.colorScheme.onTertiaryContainer
        else MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Text(
            text = if (isActive) "Active" else "Pending",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

private fun formatDate(millis: Long): String =
    SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(Date(millis))
