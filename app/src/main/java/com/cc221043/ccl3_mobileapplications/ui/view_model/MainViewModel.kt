package com.cc221043.ccl3_mobileapplications.ui.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cc221043.ccl3_mobileapplications.MainActivity
import com.cc221043.ccl3_mobileapplications.data.BookDao
import com.cc221043.ccl3_mobileapplications.data.model.Book
import com.cc221043.ccl3_mobileapplications.ui.view.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

class MainViewModel (private val dao: BookDao, private val mainActivity: MainActivity) : ViewModel() {
    private val _mainViewState = MutableStateFlow(MainViewState())
    val mainViewState: StateFlow<MainViewState> = _mainViewState.asStateFlow()

    fun selectedScreen(screen: Screen) {
        _mainViewState.update { it.copy(selectedScreen = screen) }
    }

    fun getAllBooks() {
        viewModelScope.launch {
            dao.getAllBooks().collect() {books ->
                _mainViewState.update { it.copy(books = books) }
            }
        }
    }

    fun saveBookAndImage(book: Book): Long {
        var insertedId: Long = -1

        if (_mainViewState.value.selectedImageURI != Uri.parse("")) {
            val contentResolver = mainActivity.contentResolver
            val inputStream = contentResolver.openInputStream(_mainViewState.value.selectedImageURI)

            val fileName = "image_${System.currentTimeMillis()}.jpg"
            val file = File(mainActivity.filesDir, fileName)

            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            val imagePath = file.absolutePath
            book.cover = imagePath
        }

        runBlocking {
            insertedId = dao.insertBook(book)
            println("Saved book ID: $insertedId")
            updateImageURI(Uri.parse(""))
        }

        return insertedId
    }

    fun updateImageURI(imageURI: Uri) {
        _mainViewState.update { it.copy(selectedImageURI = imageURI) }
    }

    fun selectBookDetails(id: Int) {
        viewModelScope.launch {
            val book = dao.getBookById(id)
            _mainViewState.update { it.copy(selectedBook = book) }
        }
    }
}