package com.example.kitabusf1.di

import android.content.Context
import com.example.kitabusf1.data.local.AppDatabase
import com.example.kitabusf1.data.repository.LibraryRepository

class AppContainer(context: Context) {


    //"lazy" means it will be created once then used forever, this means the db will only be opened with a screen needs the data
    //container is private so nothing can access the db from the outside, only the repo is public
    private val database: AppDatabase by lazy {
        AppDatabase.getDatabase(context)
    }

    val libraryRepository: LibraryRepository by lazy {
        LibraryRepository(database.bookDao(), database.bookingDao())
    }
}
