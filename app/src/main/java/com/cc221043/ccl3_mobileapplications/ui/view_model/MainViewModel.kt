package com.cc221043.ccl3_mobileapplications.ui.view_model

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cc221043.ccl3_mobileapplications.MainActivity
import com.cc221043.ccl3_mobileapplications.data.BookDao
//import com.cc221043.ccl3_mobileapplications.data.datastore.MyPreferencesDataStore
import com.cc221043.ccl3_mobileapplications.data.model.Book
import com.cc221043.ccl3_mobileapplications.ui.view.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

class MainViewModel(
    private val dao: BookDao,
    private val mainActivity: MainActivity
) : ViewModel() {

    private val _mainViewState = MutableStateFlow(MainViewState())
    val mainViewState: StateFlow<MainViewState> = _mainViewState.asStateFlow()

    fun selectedScreen(screen: Screen) {
        _mainViewState.update { it.copy(selectedScreen = screen) }
    }

    fun previousScreen(screenName: String) {
        _mainViewState.update { it.copy(previousScreen = screenName) }
    }

    fun getAllBooks() {
        viewModelScope.launch {
            dao.getAllBooks().collect() {books ->
                _mainViewState.update { it.copy(books = books) }
            }
        }
    }

    fun getBooksBySearch(searchText: String) {
        viewModelScope.launch {
            dao.getBooksBySearch(searchText).collect() {books ->
                _mainViewState.update { it.copy(searchedBooks = books) }
            }
        }
    }

    fun resetSearch() {
        _mainViewState.update { it.copy(searchedBooks = emptyList()) }
    }

    fun getAllBooksForGenres() {
        viewModelScope.launch {
            dao.getAllBooks().collect() {books ->
                _mainViewState.update { it.copy(booksForGenres = books) }
            }
        }
    }

    fun changeStatusFilteredBooks(books: List<Book>, status: String) {
        val filteredBooks = books.filter { it.status == status }
        _mainViewState.update { it.copy(statusFilteredBooks = filteredBooks) }
    }

    fun updateSelectedGenres(genres: List<String>) {
        val selectedBooks = _mainViewState.value.booksForGenres.filter { book ->
            book.genres.containsAll(genres)
        }
        _mainViewState.update { it.copy(selectedBooksForGenres = selectedBooks) }
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
            clearSelectedImageURI()
        }
        clearSelectedImageURI()
        runBlocking {
            insertedId = dao.insertBook(book)
            updateImageURI(Uri.parse(""))
        }
        return insertedId


    }

    fun updateImageURI(imageURI: Uri) {
        _mainViewState.update { it.copy(selectedImageURI = imageURI) }
    }

    fun clearSelectedImageURI() {
        _mainViewState.update { it.copy(selectedImageURI = Uri.parse("")) }
    }

    fun selectBookDetails(id: Int) {
        viewModelScope.launch {
            dao.getBookById(id).take(1).collect() {book ->
                _mainViewState.update { it.copy(selectedBook = book) }
            }
        }
    }

    fun updateBookAndImage(editedBook: Book) {
        viewModelScope.launch {
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
                editedBook.cover = imagePath

                clearSelectedImageURI()
            }
            dao.updateBook(editedBook)
            selectBookDetails(editedBook.id)
            dismissChangeStatusDialog()
            updateImageURI(Uri.parse(""))
        }
    }
    fun openChangeStatusDialog() {
        _mainViewState.update { it.copy(showChangeStatusDialog = true) }
    }

    fun dismissChangeStatusDialog() {
        _mainViewState.update { it.copy(showChangeStatusDialog = false) }
    }

    fun openDeleteDialog() {
        _mainViewState.update { it.copy(showDeleteDialog = true) }
    }

    fun dismissDeleteDialog() {
        _mainViewState.update { it.copy(showDeleteDialog = false) }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            dao.deleteBook(book)
        }
    }
}