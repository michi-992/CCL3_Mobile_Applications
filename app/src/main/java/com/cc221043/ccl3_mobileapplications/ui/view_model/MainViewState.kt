package com.cc221043.ccl3_mobileapplications.ui.view_model

import android.net.Uri
import com.cc221043.ccl3_mobileapplications.data.model.Book
import com.cc221043.ccl3_mobileapplications.ui.view.Screen

data class MainViewState (
    val selectedScreen: Screen = Screen.Onboarding,
    val books: List<Book> = emptyList(),
    val searchedBooks: List<Book> = emptyList(),
    val statusFilteredBooks: List<Book> = emptyList(),
    val booksForGenres: List<Book> = emptyList(),
    val selectedBooksForGenres: List<Book> = emptyList(),
    val selectedImageURI: Uri = Uri.parse(""),
    val selectedBook: Book = Book(title = "", status = "", platformat = "", synopsis = ""),
    val previousScreen: String = "",
    val showChangeStatusDialog: Boolean = false,
    val showDeleteDialog: Boolean = false
)