package com.raywenderlich.android.librarian.ui.books.filter

sealed class Filter

class ByGenre(val genreId: String) : Filter()

class ByRating(val rating: Int) : Filter()