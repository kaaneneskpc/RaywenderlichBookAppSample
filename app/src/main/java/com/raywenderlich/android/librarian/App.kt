package com.raywenderlich.android.librarian

import android.app.Application
import com.google.gson.Gson
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

  @Inject
  lateinit var repository: LibrarianRepository

  companion object {
    private lateinit var instance: App

    val gson by lazy { Gson() }
  }

  override fun onCreate() {
    super.onCreate()
    instance = this

    GlobalScope.launch {
      if (repository.getGenres().isEmpty()) {
        repository.addGenres(
          listOf(
            Genre(name = "Action"),
            Genre(name = "Adventure"),
            Genre(name = "Classic"),
            Genre(name = "Mystery"),
            Genre(name = "Fantasy"),
            Genre(name = "Sci-Fi"),
            Genre(name = "History"),
            Genre(name = "Horror"),
            Genre(name = "Romance"),
            Genre(name = "Short Story"),
            Genre(name = "Biography"),
            Genre(name = "Poetry"),
            Genre(name = "Self-Help"),
            Genre(name = "Young fiction")
          )
        )
      }
    }
  }
}