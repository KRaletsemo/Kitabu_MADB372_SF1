package com.example.kitabusf1.di

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.kitabusf1.data.repository.LibraryRepository
import com.example.kitabusf1.ui.BookingsViewModel
import com.example.kitabusf1.ui.CatalogueViewModel
//ViewModel is used to hold the screens data and logic outside the composable
object AppViewModelProvider {

    val Factory = viewModelFactory {
        initializer {
            CatalogueViewModel(libraryRepository())
        }
        initializer {
            BookingsViewModel(libraryRepository())
        }
    }
}

private fun CreationExtras.libraryRepository(): LibraryRepository =
    (this[APPLICATION_KEY] as KitabuApplication).container.libraryRepository
