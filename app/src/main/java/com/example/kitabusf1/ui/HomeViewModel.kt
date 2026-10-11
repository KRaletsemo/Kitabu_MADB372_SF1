package com.example.kitabusf1.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kitabusf1.data.repository.LibraryRepository
import com.example.kitabusf1.domain.Book
import com.example.kitabusf1.domain.Booking
import com.example.kitabusf1.domain.BookingStatus
import com.example.kitabusf1.domain.matchesSearch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
//Now that I have a db connected and Repo, I'm using ViewModels to show screens 
private const val CATALOGUE_PREVIEW_SIZE = 6

data class HomeUiState(
    val dueSoon: List<Booking> = emptyList(),
    val cataloguePreview: List<Book> = emptyList(),
    val isLoading: Boolean = true
)

class HomeViewModel(
    private val repository: LibraryRepository
) : ViewModel() {

    var query by mutableStateOf("")
        private set //This is used to make sure that the screen can read the query but only the ViewModel can change it

    private val queryFlow = snapshotFlow { query }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<HomeUiState> = combine(
        queryFlow,
        queryFlow.flatMapLatest { query -> repository.getBooks(query) },
        repository.getCurrentBookings()
    ) { query, books, bookings ->
        HomeUiState(
            dueSoon = bookings.filter {
                it.status == BookingStatus.ACTIVE && it.book.matchesSearch(query)
            },
            cataloguePreview = books.take(CATALOGUE_PREVIEW_SIZE),
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    fun onQueryChange(newQuery: String) {
        query = newQuery
    }
}
