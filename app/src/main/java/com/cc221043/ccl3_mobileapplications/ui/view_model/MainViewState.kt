package com.cc221043.ccl3_mobileapplications.ui.view_model

import android.net.Uri
import com.cc221043.ccl3_mobileapplications.data.model.Book
import com.cc221043.ccl3_mobileapplications.ui.view.Screen

data class MainViewState (
    val selectedScreen: Screen = Screen.HomeAll,
    val books: List<Book> = emptyList(),
    val selectedImageURI: Uri = Uri.parse(""),
    val selectedBook: Book? = null,
    val previousScreen: String = ""
)