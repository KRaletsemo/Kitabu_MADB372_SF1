package com.example.kitabusf1.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.kitabusf1.R
import com.example.kitabusf1.domain.Book
import com.example.kitabusf1.ui.theme.AvailableGreen
import com.example.kitabusf1.ui.theme.BorrowedOrange
import com.example.kitabusf1.domain.Booking
import com.example.kitabusf1.domain.daysRemaining
import com.example.kitabusf1.domain.isOverdue

// Components.kt will hold all reusable parts/ parts that will be used across all screens. This means simply calling the part makes code cleaner

@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("Search by title or author") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Filled.Clear, contentDescription = "Clear search")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(50)
    )
}

@Composable
fun BookCard(book: Book, modifier: Modifier = Modifier) {
    val icon = categoryIcon(book.category)

    Card(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(if (icon != null) Color.White else MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            if (icon != null) {
                Image(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                Text(
                    text = book.title.first().uppercase(),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            AvailabilityBadge(
                isAvailable = book.isAvailable,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            )
        }
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = book.author,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(8.dp))
            CategoryTag(book.category)
        }
    }
}

@DrawableRes
private fun categoryIcon(category: String): Int? = when (category) {
    "Game Design" -> R.drawable.icon_game_design
    "3D Animation" -> R.drawable.icon_animation
    "Motorsport Engineering" -> R.drawable.icon_motorsport
    "Cyber Security" -> R.drawable.icon_cyber_security
    "Software Engineering" -> R.drawable.icon_software_engineering
    else -> null
}

@Composable
fun AvailabilityBadge(isAvailable: Boolean, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = if (isAvailable) AvailableGreen else BorrowedOrange,
        contentColor = Color.White
    ) {
        Text(
            text = if (isAvailable) "Available" else "Borrowed",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun CategoryTag(category: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

fun dueLabel(booking: Booking): String {
    if (booking.isOverdue()) return "Overdue"
    val days = booking.daysRemaining()
    return if (days == 1) "1 day left" else "$days days left"
}

