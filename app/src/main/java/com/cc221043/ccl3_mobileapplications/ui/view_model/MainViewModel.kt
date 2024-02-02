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

    // updates selected screen
    fun selectedScreen(screen: Screen) {
        _mainViewState.update { it.copy(selectedScreen = screen) }
    }

    // saves previous screen
    fun previousScreen(screenName: String) {
        _mainViewState.update { it.copy(previousScreen = screenName) }
    }

    // retrieves all books and updates main view state
    fun getAllBooks() {
        viewModelScope.launch {
            dao.getAllBooks().collect() {books ->
                _mainViewState.update { it.copy(books = books) }
            }
        }
    }

    // retrieves books based on the search text
    fun getBooksBySearch(searchText: String) {
        viewModelScope.launch {
            dao.getBooksBySearch(searchText).collect() {books ->
                _mainViewState.update { it.copy(searchedBooks = books) }
            }
        }
    }

    // resets list of searched books
    fun resetSearch() {
        _mainViewState.update { it.copy(searchedBooks = emptyList()) }
    }

    // retrieves all books and copies them to the list of books filtered by genre
    fun getAllBooksForGenres() {
        viewModelScope.launch {
            dao.getAllBooks().collect() {books ->
                _mainViewState.update { it.copy(booksForGenres = books) }
            }
        }
    }

    // filters books by status
    fun changeStatusFilteredBooks(books: List<Book>, status: String) {
        val filteredBooks = books.filter { it.status == status }
        _mainViewState.update { it.copy(statusFilteredBooks = filteredBooks) }
    }

    // filters books by genre
    fun updateSelectedGenres(genres: List<String>) {
        val selectedBooks = _mainViewState.value.booksForGenres.filter { book ->
            book.genres.containsAll(genres)
        }
        _mainViewState.update { it.copy(selectedBooksForGenres = selectedBooks) }
    }

    // saves book
    fun saveBookAndImage(book: Book): Long {
        var insertedId: Long = -1

        // checks if image is already selected
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
        // clears image uri and inserts book into the database
        clearSelectedImageURI()
        runBlocking {
            insertedId = dao.insertBook(book)
            updateImageURI(Uri.parse(""))
        }
        return insertedId
    }

    // updates image uri
    fun updateImageURI(imageURI: Uri) {
        _mainViewState.update { it.copy(selectedImageURI = imageURI) }
    }

    // clears image uri
    fun clearSelectedImageURI() {
        _mainViewState.update { it.copy(selectedImageURI = Uri.parse("")) }
    }

    // retrieves details of specific book
    fun selectBookDetails(id: Int) {
        viewModelScope.launch {
            dao.getBookById(id).take(1).collect() {book ->
                _mainViewState.update { it.copy(selectedBook = book) }
            }
        }
    }

    // updates a book after editing
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

    // opens status change dialog
    fun openChangeStatusDialog() {
        _mainViewState.update { it.copy(showChangeStatusDialog = true) }
    }

    // dismisses status change dialog
    fun dismissChangeStatusDialog() {
        _mainViewState.update { it.copy(showChangeStatusDialog = false) }
    }

    // opens delete dialog
    fun openDeleteDialog() {
        _mainViewState.update { it.copy(showDeleteDialog = true) }
    }

    // dismisses delete dialog
    fun dismissDeleteDialog() {
        _mainViewState.update { it.copy(showDeleteDialog = false) }
    }

    // deletes book
    fun deleteBook(book: Book) {
        viewModelScope.launch {
            dao.deleteBook(book)
        }
    }
}