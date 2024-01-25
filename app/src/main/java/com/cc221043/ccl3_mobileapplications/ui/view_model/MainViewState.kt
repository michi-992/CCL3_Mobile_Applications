package com.cc221043.ccl3_mobileapplications.ui.view_model

import android.net.Uri
import com.cc221043.ccl3_mobileapplications.data.model.Book
import com.cc221043.ccl3_mobileapplications.ui.view.Screen

data class MainViewState (
    val selectedScreen: Screen = Screen.Home,
    val books: List<Book> = emptyList(),
    val booksForGenres: List<Book> = emptyList(),
    val selectedBooksForGenres: List<Book> = emptyList(),
    val selectedImageURI: Uri = Uri.parse(""),
    val selectedBook: Book? = null,
    val previousScreen: String = "",
    val test: List<Book> = emptyList(),
)