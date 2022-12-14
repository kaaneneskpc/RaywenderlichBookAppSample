package com.raywenderlich.android.librarian.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.ui.books.BooksFragment
import com.raywenderlich.android.librarian.ui.readingList.ReadingListFragment
import com.raywenderlich.android.librarian.ui.reviews.BookReviewsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private var reviewsFragment: BookReviewsFragment? = null
  private var readingListFragment: ReadingListFragment? = null
  private var booksFragment: BooksFragment? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.AppTheme)
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    initUi()

    if (savedInstanceState == null) {
      displayNextFragment(R.id.books)
    }
  }

  private fun initUi() {
    val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)

    val colorDrawable = R.color.bottom_view_selector

    bottomNavigation.itemTextColor =
      ResourcesCompat.getColorStateList(resources, colorDrawable, theme)

    bottomNavigation.itemIconTintList =
      ResourcesCompat.getColorStateList(resources, colorDrawable, theme)

    bottomNavigation.setOnItemSelectedListener { menuItem ->
      displayNextFragment(menuItem.itemId)
      true
    }
  }

  private fun displayNextFragment(itemId: Int) {
    when (itemId) {
      R.id.readingList -> {
        if (readingListFragment == null) {
          readingListFragment = ReadingListFragment()
        }

        displayFragment(readingListFragment!!)
      }

      R.id.bookReviews -> {
        if (reviewsFragment == null) {
          reviewsFragment = BookReviewsFragment()
        }

        displayFragment(reviewsFragment!!)
      }

      R.id.books -> {
        if (booksFragment == null) {
          booksFragment = BooksFragment()
        }

        displayFragment(booksFragment!!)
      }
    }
  }

  private fun displayFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
      .replace(R.id.fragmentContainer, fragment, null)
      .commit()
  }
}
