package com.raywenderlich.android.librarian.ui.bookReviewDetails.animations

sealed class BookReviewDetailsScreenState

object Initial : BookReviewDetailsScreenState()

object Loaded : BookReviewDetailsScreenState()