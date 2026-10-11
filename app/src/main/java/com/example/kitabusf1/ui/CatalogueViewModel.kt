package com.example.kitabusf1.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kitabusf1.data.repository.LibraryRepository
import com.example.kitabusf1.domain.Book
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
//ViewModel is used to hold the screens data and logic outside the composable
data class CatalogueUiState(
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = true
)

class CatalogueViewModel(
    private val repository: LibraryRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<CatalogueUiState> = _query
        .flatMapLatest { query -> repository.getBooks(query) } //drops old db query when search text changes and makes a new one
        .map { books -> CatalogueUiState(books = books, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), //Keeps db stream going while the page is open + a 5 sec delay
            initialValue = CatalogueUiState() //what the screen gets before the db has answered
        )

    private val _messages = Channel<String>(Channel.BUFFERED)
    val messages = _messages.receiveAsFlow()

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    fun reserveBook(book: Book, days: Int) {
        viewModelScope.launch {
            if (repository.reserveBook(book.id, days)) {
                _messages.send("Reserved ${book.title} for $days days")
            }
        }
    }
}
