package com.cc221043.ccl3_mobileapplications.ui.view

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.cc221043.ccl3_mobileapplications.R
import com.cc221043.ccl3_mobileapplications.data.model.Book
import com.cc221043.ccl3_mobileapplications.ui.theme.Colors
import com.cc221043.ccl3_mobileapplications.ui.view_model.MainViewModel
import androidx.compose.foundation.lazy.items
import com.cc221043.ccl3_mobileapplications.data.BookDao

sealed class Screen(val route: String) {
    object Home : Screen("Home")
    object AddBook : Screen("AddBook")
    object EditBook : Screen("EditBook")
    object BookDetails : Screen("BookDetails")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(
    mainViewModel: MainViewModel, pickImageLauncher: ActivityResultLauncher<String>
) {
    val state = mainViewModel.mainViewState.collectAsState()
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            when (state.value.selectedScreen) {
                is Screen.Home -> {
                    HomeTopBar()
                }

                is Screen.AddBook -> {
                    AddBookTopBar(mainViewModel, navController)
                }

                is Screen.EditBook -> {
                    EditBookTopBar(mainViewModel, navController)
                }

                is Screen.BookDetails -> {
                    BookDetailsTopBar(mainViewModel, navController)
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(it),
        ) {
            composable(Screen.Home.route) {
                mainViewModel.selectedScreen(Screen.Home)
                mainViewModel.getAllBooks()
                mainViewModel.getAllBooksForGenres()
                HomeScreen(mainViewModel, navController)
            }
            composable(Screen.AddBook.route) {
                mainViewModel.selectedScreen(Screen.AddBook)
                mainViewModel.getAllBooks()
                AddBook(
                    mainViewModel,
                    navController,
                    onPickImage = { pickImageLauncher.launch("image/*") })
            }
            composable("${Screen.BookDetails.route}/{bookId}") { backStackEntry ->
                mainViewModel.selectedScreen(Screen.BookDetails)
                val arguments = requireNotNull(backStackEntry.arguments)
                val bookId = arguments.getString("bookId")!!.toInt()

                mainViewModel.selectBookDetails(bookId)
                BookDetails(mainViewModel, navController, bookId)
            }
            composable(Screen.EditBook.route + "/{bookId}") { backStackEntry ->
                mainViewModel.selectedScreen(Screen.EditBook)
                val arguments = requireNotNull(backStackEntry.arguments)
                val bookId = arguments.getString("bookId")!!.toInt()

                mainViewModel.selectBookDetails(bookId)
                EditBook(
                    mainViewModel,
                    navController,
                    bookId,
                    onPickImage = { pickImageLauncher.launch("image/*") })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(mainViewModel: MainViewModel, navController: NavController) {
    val state = mainViewModel.mainViewState.collectAsState()
    val books = state.value.books
    val booksForGenres = state.value.booksForGenres

    val buttonColors = ButtonDefaults.buttonColors(
        contentColor = Colors.OffWhite,
        containerColor = Colors.PrimaryBlueDark,
        disabledContentColor = Colors.OffWhite,
        disabledContainerColor = Colors.Blue0,
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            var tabIndex by remember {
                mutableIntStateOf(0)
            }
            var flag by remember {
                mutableStateOf(false)
            }
            var flag2 by remember {
                mutableStateOf(false)
            }
            val tabs = listOf("All Books", "Categories")

            val pagerState = rememberPagerState {
                tabs.size
            }
            LaunchedEffect(tabIndex) {
                pagerState.animateScrollToPage(tabIndex)
            }
            LaunchedEffect(
                pagerState.currentPage,
            ) {
                tabIndex = pagerState.currentPage
            }

            TabRow(
                selectedTabIndex = tabIndex,
                divider = {
                    Spacer(
                        modifier = Modifier
                            .height(2.dp)
                            .fillMaxWidth()
                            .background(Colors.Blue4)
                    )
                }
            ) {
                Tab(text = { Text("All Books", style = MaterialTheme.typography.displaySmall) },
                    selected = tabIndex == 0,
                    onClick = {
                        tabIndex = 0
                        flag = true
                    }
                )
                Tab(text = { Text("Genres", style = MaterialTheme.typography.displaySmall) },
                    selected = tabIndex == 1,
                    onClick = {
                        tabIndex = 1
                        flag2 = true
                    }
                )
            }
            when (tabIndex) {
                0 -> {
                    if (flag) {
                        mainViewModel.getAllBooks()
                        flag = false
                    }
                }

                1 -> {
                    if (flag2) {
                        mainViewModel.getAllBooks()
                        flag2 = false
//                        searchText = ""
                    }
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { index ->
                when (index) {
                    0 -> {
                        HomeScreenAllBooks(books, navController, mainViewModel)
                    }

                    1 -> {
                        HomeScreenGenres(booksForGenres, navController, mainViewModel)
                    }
                }
            }
        }
        Button(
                onClick = { navController.navigate("AddBook") },
        shape = RoundedCornerShape(8.dp),
        colors = buttonColors,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 10.dp)
            .background(color = Colors.Blue1, shape = RoundedCornerShape(12.dp))
            .padding(vertical = 10.dp, horizontal = 12.dp)

        ) {
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.AddCircle, contentDescription = null)
                Spacer(modifier = Modifier.size(6.dp))
                Text(text = "Add Book", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenAllBooks(books: List<Book>, navController: NavController, mainViewModel: MainViewModel) {
    val gradientColors = listOf(Colors.Blue1, Colors.Blue4, Colors.Blue1)
    var searchText by rememberSaveable { mutableStateOf("") }
val state = mainViewModel.mainViewState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = gradientColors,
                    )
                )
                .padding(start = 14.dp, end = 14.dp, top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                value = searchText,
                onValueChange = {
                    searchText = it
                    mainViewModel.getBooksByQuery(searchText)
                },
                placeholder = { Text("Search") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                searchText = ""
                                mainViewModel.getAllBooks()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        mainViewModel.getBooksByQuery(searchText)
                    }
                ),
            )
            BookGrid(mainViewModel, navController, books)
        }
    }
}

@Composable
fun BookGrid(mainViewModel: MainViewModel, navController: NavController, books: List<Book>) {
    Column (modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            modifier = Modifier
                .heightIn(min = 200.dp)
                .padding(top = 14.dp)
                .weight(1f)
                .fillMaxWidth(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            columns = GridCells.Fixed(3),
            content = {
                items(books.size) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(154.dp)

                            .clickable {
                                navController.navigate("${Screen.BookDetails.route}/${books[it].id}")
                            }
                    ) {
                        AsyncImage(
                            modifier = Modifier.clip(RoundedCornerShape(4.dp)),
                            model = books[it].cover,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                items(3) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(3.0f)
                            .height(100.dp)
                    )
                }
            })
    }

}

@Composable
fun HomeScreenGenres(books: List<Book>, navController: NavController, mainViewModel: MainViewModel) {
    val gradientColors = listOf(Colors.Blue1, Colors.Blue4, Colors.Blue1)
    val genreArray = stringArrayResource(id = R.array.genres)
    var selectedNames by remember { mutableStateOf(emptyList<String>()) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = gradientColors,
                    )
                )
                .padding(start = 14.dp, end = 14.dp, top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyRow {
                items(genreArray) { name ->
                    GenreButton(
                        name = name,
                        isSelected = selectedNames.contains(name),
                        onNameClicked = {
                            selectedNames = if (selectedNames.contains(name)) {
                                selectedNames - name
                            } else {
                                selectedNames + name
                            }
                            println(selectedNames)
                        }
                    )
                }
            }
            BookGrid(mainViewModel, navController, books)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBook(mainViewModel: MainViewModel, navController: NavController, onPickImage: () -> Unit) {
    val state = mainViewModel.mainViewState.collectAsState()
    val genreArray = stringArrayResource(id = R.array.genres)
    var selectedGenres by remember { mutableStateOf(emptyList<String>()) }

    Column(
        Modifier.verticalScroll(rememberScrollState())
    ) {
        var title by rememberSaveable { mutableStateOf("") }
        var author by rememberSaveable { mutableStateOf("") }
        var platformat by rememberSaveable { mutableStateOf("") }
        var synopsis by rememberSaveable { mutableStateOf("") }
        var status by rememberSaveable { mutableStateOf("") }
        var rating by remember { mutableIntStateOf(0) }


        val iconButtonColors = rememberUpdatedState(
            IconButtonDefaults.iconButtonColors(
                contentColor = Colors.OffWhite,
                containerColor = Colors.Blue0,
                disabledContentColor = Colors.OffWhite,
                disabledContainerColor = Colors.Blue0,
            )
        )

        Column {
            if (state.value.selectedImageURI != null) {
                AsyncImage(
                    model = state.value.selectedImageURI,
                    contentDescription = null,
                    modifier = Modifier.width(80.dp)
                )
            }

            if (state.value.selectedImageURI == Uri.parse("")) {
                Button(
                    onClick = {
                        onPickImage()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = null
                    )
                }
            }
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                value = title,
                onValueChange = { newText -> title = newText },
                label = { Text(text = "Title") })

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                value = author,
                onValueChange = { newText -> author = newText },
                label = { Text(text = "Author") })

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                value = platformat,
                onValueChange = { newText -> platformat = newText },
                label = { Text(text = "Platform/Format") })

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 20.dp),
                value = synopsis,
                onValueChange = { newText -> synopsis = newText },
                label = { Text(text = "Synopsis") })

            Row(
                Modifier.horizontalScroll(rememberScrollState())
            ) {
                Button(
                    onClick = {
                        status = "Not started"
                        rating = 0
                    }
                ) {
                    Row {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                        Text(text = "Not started")
                    }
                }
                Button(onClick = {
                    status = "In Progress"
                    rating = 0
                }) {
                    Row {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                        Text(text = "In Progress")
                    }

                }
                Button(onClick = {
                    status = "Finished"
                }) {
                    Row {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        Text(text = "Finished")
                    }
                }
            }
            if (status == "Finished") {
                RatingBar(
                    rating = rating, onRatingChanged = {
                        rating = it
                    },
                    modifier = Modifier.padding(16.dp),
                    small = false
                )
                Button(onClick = { rating = 0 }) {
                    Text(text = "Clear")
                }
            }
            LazyRow {
                items(genreArray) { name ->
                    GenreButton(
                        name = name,
                        isSelected = selectedGenres.contains(name),
                        onNameClicked = {
                            selectedGenres = if (selectedGenres.contains(name)) {
                                selectedGenres - name
                            } else {
                                selectedGenres + name
                            }
                            println(selectedGenres)
                        }
                    )
                }
            }


            Button(onClick = {
                val newBook = Book(
                    title = title,
                    author = author,
                    platformat = platformat,
                    rating = rating,
                    synopsis = synopsis,
                    status = status,
                    genres = selectedGenres
                )

                mainViewModel.previousScreen(state.value.selectedScreen.route)
                val insertedId = mainViewModel.saveBookAndImage(newBook)
                navController.navigate("${Screen.BookDetails.route}/$insertedId")
            }) {
                Text(text = "Save book")
            }
        }
    }
}

@Composable
fun RatingBar(
    rating: Int, onRatingChanged: (Int) -> Unit, modifier: Modifier = Modifier, small: Boolean
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (i in 1..5) {
            val isSelected = i <= rating
            Icon(
                painter = if (isSelected) painterResource(id = R.drawable.bat_filled) else painterResource(
                    id = R.drawable.battybatbat
                ),
                contentDescription = null,
                modifier = if (small) Modifier
                    .size(30.dp)
                    .clickable { onRatingChanged(i) } else Modifier
                    .size(60.dp)
                    .clickable { onRatingChanged(i) },
                tint = Colors.Blue6
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetails(mainViewModel: MainViewModel, navController: NavController, bookId: Int) {
    val state = mainViewModel.mainViewState.collectAsState()
    val book = state.value.selectedBook ?: return

    var isMenuExpanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val iconButtonColors = rememberUpdatedState(
        IconButtonDefaults.iconButtonColors(
            contentColor = Colors.OffWhite,
            containerColor = Colors.Blue0,
            disabledContentColor = Colors.OffWhite,
            disabledContainerColor = Colors.Blue0,
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AsyncImage(
            model = book.cover,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(Color.Gray)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Title: ${book.title}", style = MaterialTheme.typography.displayMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Author: ${book.author}", style = MaterialTheme.typography.displayMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Platform/Format: ${book.platformat}",
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Synopsis: ${book.synopsis}", style = MaterialTheme.typography.displayMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Status: ${book.status}", style = MaterialTheme.typography.displayMedium)
        Spacer(modifier = Modifier.height(8.dp))
        if (book.rating != 0 && book.status == "Finished") {
            RatingBar(rating = book.rating, onRatingChanged = {}, small = true)
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (bookId != null) {
                    navController.navigate(Screen.Home.route)
                    mainViewModel.selectBookDetails(bookId)
                } else {
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Back to Home")
        }

        IconButton(
            onClick = { isMenuExpanded = !isMenuExpanded },
        ) {
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
        }

        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false },
            modifier = Modifier
                .width(IntrinsicSize.Max)
        ) {
            DropdownMenuItem(
                onClick = {
                    isMenuExpanded = false
                    navController.navigate("${Screen.EditBook.route}/$bookId")
                },
                text = { Text("Edit") },
                enabled = true
            )

            DropdownMenuItem(
                onClick = {
                    isMenuExpanded = false
                    showDeleteDialog = true
                },
                text = { Text("Delete") },
                enabled = true
            )
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Book") },
            text = { Text("Are you sure you want to delete this book?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        mainViewModel.deleteBook(book)
                        navController.navigate(Screen.Home.route)
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBook(
    mainViewModel: MainViewModel,
    navController: NavController,
    bookId: Int,
    onPickImage: () -> Unit
) {
    val state = mainViewModel.mainViewState.collectAsState()
    val book = state.value.selectedBook ?: return

    val genreArray = stringArrayResource(id = R.array.genres)
    var selectedGenres by remember { mutableStateOf(book.genres) }

    Column(
        Modifier.verticalScroll(rememberScrollState())
    ) {
        var title by rememberSaveable { mutableStateOf(book.title) }
        var author by rememberSaveable { mutableStateOf(book.author) }
        var platformat by rememberSaveable { mutableStateOf(book.platformat) }
        var synopsis by rememberSaveable { mutableStateOf(book.synopsis) }
        var status by rememberSaveable { mutableStateOf(book.status) }
        var rating by remember { mutableIntStateOf(book.rating) }

        val iconButtonColors = rememberUpdatedState(
            IconButtonDefaults.iconButtonColors(
                contentColor = Colors.OffWhite,
                containerColor = Colors.Blue0,
                disabledContentColor = Colors.OffWhite,
                disabledContainerColor = Colors.Blue0,
            )
        )

        Column {
            if (state.value.selectedImageURI == Uri.parse("")) {
                AsyncImage(
                    model = book.cover,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .background(Color.Gray)
                )
                Button(
                    onClick = {
                        onPickImage()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null
                    )
                }
            } else {
                AsyncImage(
                    model = state.value.selectedImageURI,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .background(Color.Gray)
                )
                Button(
                    onClick = {
                        onPickImage()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null
                    )
                }
            }

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                value = title,
                onValueChange = { newText -> title = newText },
                label = { Text(text = "Title") }
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                value = author,
                onValueChange = { newText -> author = newText },
                label = { Text(text = "Author") }
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                value = platformat,
                onValueChange = { newText -> platformat = newText },
                label = { Text(text = "Platform/Format") }
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                value = synopsis,
                onValueChange = { newText -> synopsis = newText },
                label = { Text(text = "Synopsis") }
            )

            Row(
                Modifier.horizontalScroll(rememberScrollState())
            ) {
                Button(
                    onClick = {
                        status = "Not started"
                        rating = 0
                    }
                ) {
                    Row {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                        Text(text = "Not started")
                    }
                }
                Button(onClick = {
                    status = "In Progress"
                    rating = 0
                }) {
                    Row {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                        Text(text = "In Progress")
                    }

                }
                Button(onClick = {
                    status = "Finished"
                }) {
                    Row {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        Text(text = "Finished")
                    }
                }
            }
            if (status == "Finished") {
                RatingBar(
                    rating = rating, onRatingChanged = {
                        rating = it
                    },
                    modifier = Modifier.padding(16.dp),
                    small = false
                )
                Button(onClick = { rating = 0 }) {
                    Text(text = "Clear")
                }
            }
            LazyRow {
                items(genreArray) { name ->
                    GenreButton(
                        name = name,
                        isSelected = selectedGenres.contains(name),
                        onNameClicked = {
                            selectedGenres = if (selectedGenres.contains(name)) {
                                selectedGenres - name
                            } else {
                                selectedGenres + name
                            }
                            println(selectedGenres)
                        }
                    )
                }
            }


            Button(
                onClick = {
                    val editedBook = Book(
                        id = book.id,
                        title = title,
                        author = author,
                        platformat = platformat,
                        rating = rating,
                        synopsis = synopsis,
                        status = status,
                        cover = book.cover,
                        genres = selectedGenres
                    )
                    mainViewModel.previousScreen(state.value.selectedScreen.route)
                    mainViewModel.updateBookAndImage(editedBook)
                    navController.navigate("${Screen.BookDetails.route}/${editedBook.id}")
                }
            ) {
                Text(text = "Save Changes")
            }
        }
    }
}