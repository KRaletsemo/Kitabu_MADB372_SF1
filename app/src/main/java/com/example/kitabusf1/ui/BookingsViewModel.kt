package com.example.kitabusf1.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kitabusf1.data.repository.LibraryRepository
import com.example.kitabusf1.domain.Booking
import com.example.kitabusf1.domain.DAY_MS
import com.example.kitabusf1.domain.RENEW_DAYS
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

//Updated code to suit now using the DB and having a viewModel
data class BookingsUiState(
    val bookings: List<Booking> = emptyList(),
    val isLoading: Boolean = true
)

class BookingsViewModel(
    private val repository: LibraryRepository
) : ViewModel() {

    val uiState: StateFlow<BookingsUiState> = repository.getCurrentBookings()
        .map { bookings -> BookingsUiState(bookings = bookings, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BookingsUiState()
        )

    private val _messages = Channel<String>(Channel.BUFFERED)
    val messages = _messages.receiveAsFlow()

    fun collectBooking(booking: Booking) {
        viewModelScope.launch {
            if (repository.collectBooking(booking.id)) {
                _messages.send("Collected ${booking.book.title}")
            }
        }
    }

    fun renewBooking(booking: Booking) {
        viewModelScope.launch {
            if (repository.renewBooking(booking.id)) {
                val newDeadline = booking.returnDeadline + RENEW_DAYS * DAY_MS
                _messages.send("Renewed ${booking.book.title} until ${formatDate(newDeadline)}")
            }
        }
    }

    fun returnBooking(booking: Booking) {
        viewModelScope.launch {
            if (repository.returnBooking(booking.id)) {
                _messages.send("Returned ${booking.book.title}")
            }
        }
    }

    fun cancelBooking(booking: Booking) {
        viewModelScope.launch {
            if (repository.cancelBooking(booking.id)) {
                _messages.send("Cancelled booking for ${booking.book.title}")
            }
        }
    }
}
