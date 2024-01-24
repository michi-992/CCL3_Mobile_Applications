package com.cc221043.ccl3_mobileapplications.ui.view

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.cc221043.ccl3_mobileapplications.R
import com.cc221043.ccl3_mobileapplications.data.model.Book
import com.cc221043.ccl3_mobileapplications.ui.theme.Colors
import com.cc221043.ccl3_mobileapplications.ui.view_model.MainViewModel

sealed class Screen(val route: String) {
    object HomeAll : Screen("HomeAll")
    object HomeGenres : Screen("HomeGenres")
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
    val iconButtonColors = rememberUpdatedState(
        ButtonDefaults.buttonColors(
            contentColor = Colors.OffWhite,
            containerColor = Colors.PrimaryBlueDark,
            disabledContentColor = Colors.OffWhite,
            disabledContainerColor = Colors.Blue0,
        )
    )

    Scaffold(
        topBar = {
            when (state.value.selectedScreen) {
                is Screen.HomeAll -> {
                    HomeAllTopBar(mainViewModel)
                }

                is Screen.HomeGenres -> {
                    HomeAllTopBar(mainViewModel)
                }

                is Screen.HomeGenres -> {
                }

                is Screen.AddBook -> {
                    AddBookTopBar(mainViewModel, navController)
                }

                is Screen.EditBook -> {
                }

                is Screen.BookDetails -> {
                    AddBookTopBar(mainViewModel, navController)
                }
            }
        },
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.HomeAll.route,
            modifier = Modifier.padding(it),
        ) {
            composable(Screen.HomeAll.route) {
                mainViewModel.selectedScreen(Screen.HomeAll)
                mainViewModel.getAllBooks()
                HomeScreenAll(mainViewModel, navController)
            }
            composable(Screen.HomeGenres.route) {
                mainViewModel.selectedScreen(Screen.HomeGenres)
                mainViewModel.getAllBooks()
                HomeScreenGenres(mainViewModel, navController)
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookTopBar(mainViewModel: MainViewModel, navController: NavController) {
    val state = mainViewModel.mainViewState.collectAsState()
    val iconButtonColors = rememberUpdatedState(
        IconButtonDefaults.iconButtonColors(
            contentColor = Colors.OffWhite,
            containerColor = Colors.Blue0,
            disabledContentColor = Colors.OffWhite,
            disabledContainerColor = Colors.Blue0,
        )
    )
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = {
                    if (state.value.previousScreen == Screen.AddBook.route || state.value.previousScreen == Screen.EditBook.route) {
                        navController.navigate("HomeAll")
                        mainViewModel.previousScreen("")
                    } else {
                        navController.navigateUp()
                    }
                },
                colors = iconButtonColors.value
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        title = {
            Text(
                text = "Add Book",
                style = MaterialTheme.typography.titleSmall
            )
        },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAllTopBar(mainViewModel: MainViewModel) {
    var searchText by rememberSaveable { mutableStateOf("") }

    CenterAlignedTopAppBar(
        title = {
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.hanging_bat),
                    contentDescription = null,
                    modifier = Modifier
                        .height(46.dp)
                        .width(42.dp),
                    tint = Colors.Blue6
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.titleMedium
                )
            }

        },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenAll(mainViewModel: MainViewModel, navController: NavController) {
    val state = mainViewModel.mainViewState.collectAsState()
    val books = state.value.books
    val gradientColors = listOf(Colors.Blue1, Colors.Blue4, Colors.Blue1)
    var searchText by rememberSaveable { mutableStateOf("") }

    val buttonColors = ButtonDefaults.buttonColors(
        contentColor = Colors.OffWhite,
        containerColor = Colors.PrimaryBlueDark,
        disabledContentColor = Colors.OffWhite,
        disabledContainerColor = Colors.Blue0,
    )

    Box(modifier = Modifier.fillMaxSize()) {
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
//            maxLines = 1
            )

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

@Composable
fun HomeScreenGenres(mainViewModel: MainViewModel, navController: NavHostController) {
    val state = mainViewModel.mainViewState.collectAsState()
    val genres = stringArrayResource(id = R.array.genres)

    LazyVerticalGrid(
        modifier = Modifier
            .padding(vertical = 14.dp, horizontal = 14.dp)
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        columns = GridCells.Fixed(3),
        content = {
            items(genres.size) { index ->
                GenreButton(
                    genre = genres[index],
                    onClick = {
//                        mainViewModel.getBooksByGenre(genres[index])
                        navController.navigate(Screen.HomeGenres.route)
                    }
                )
            }
        }
    )
}


@Composable
fun GenreButton(genre: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Colors.OffWhite,
            containerColor = Colors.PrimaryBlueDark,
            disabledContentColor = Colors.OffWhite,
            disabledContainerColor = Colors.Blue0,
        ),
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth()
    ) {
        Text(text = genre)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBook(
    mainViewModel: MainViewModel,
    navController: NavController,
    onPickImage: () -> Unit
) {
    val state = mainViewModel.mainViewState.collectAsState()
    var title by rememberSaveable { mutableStateOf("") }
    var author by rememberSaveable { mutableStateOf("") }
    var platformat by rememberSaveable { mutableStateOf("") }
    var synopsis by rememberSaveable { mutableStateOf("") }
    var status by rememberSaveable { mutableStateOf("") }
    var selectedGenres by remember { mutableStateOf(emptySet<String>()) }
    val genres = stringArrayResource(id = R.array.genres)

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

            Row {
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

            genres.forEach { genre ->
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
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null
                            )
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


                Button(onClick = {
                    val newBook = Book(
                        title = title,
                        author = author,
                        platformat = platformat,
                        rating = rating,
                        synopsis = synopsis,
                        status = status
                    )

                    mainViewModel.previousScreen(state.value.selectedScreen.route)
                    val insertedId = mainViewModel.saveBookAndImage(newBook)
                    navController.navigate("${Screen.BookDetails.route}/$insertedId")
                }) {
                    Text(text = "Save book")
                    Checkbox(
                        checked = selectedGenres.contains(genre),
                        onCheckedChange = {
                            selectedGenres = if (selectedGenres.contains(genre)) {
                                selectedGenres - genre
                            } else {
                                selectedGenres + genre
                            }
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = genre)
                }

                Row(
                    Modifier.horizontalScroll(rememberScrollState())
                ) {
                    Button(
                        onClick = { status = "Not started" }
                    ) {
                        Row {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                            Text(text = "Not started")
                        }
                    }
                    Button(onClick = {
                        status = "In Progress"
                    }) {
                        Row {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null
                            )
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

                Button(onClick = {
//            val newBook = Book(
//                title = title,
//                author = author,
//                platformat = platformat,
//                rating = 3,
//                synopsis = synopsis,
//                status = status,
//                genres = selectedGenres.toList() // Convert set to list
//            )
//
//            val insertedId = mainViewModel.saveBookAndImage(newBook)
//            navController.navigate("${Screen.BookDetails.route}/$insertedId")
                }) {
                    Text(text = "Save book")
                }
            }
        }
    }
}

@Composable
fun RatingBar(
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    small: Boolean
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
fun BookDetails(
    mainViewModel: MainViewModel,
    navController: NavController,
    bookId: Int
) {
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

        Text(
            text = "Title: ${book.title}",
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Author: ${book.author}",
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Platform/Format: ${book.platformat}",
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Synopsis: ${book.synopsis}",
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Status: ${book.status}",
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (book.rating != 0 && book.status == "Finished") {
            RatingBar(rating = book.rating, onRatingChanged = {}, small = true)
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (bookId != null) {
                    navController.navigate(Screen.HomeAll.route)
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
                        navController.navigate(Screen.HomeAll.route)
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

    var title by rememberSaveable { mutableStateOf(book.title) }
    var author by rememberSaveable { mutableStateOf(book.author) }
    var platformat by rememberSaveable { mutableStateOf(book.platformat) }
    var synopsis by rememberSaveable { mutableStateOf(book.synopsis) }
    var status by rememberSaveable { mutableStateOf(book.status) }
    var selectedGenres by remember { mutableStateOf(book.genres.toSet()) }

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

            val genres = stringArrayResource(id = R.array.genres)

            genres.forEach { genre ->
                Row(
                    Modifier.horizontalScroll(rememberScrollState())
                ) {
                    Checkbox(
                        checked = selectedGenres.contains(genre),
                        onCheckedChange = {
                            selectedGenres = if (selectedGenres.contains(genre)) {
                                selectedGenres - genre
                            } else {
                                selectedGenres + genre
                            }
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = genre)
                }
            }

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
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null
                        )
                        Text(text = "Not started")
                    }
                }
                Button(onClick = {
                    status = "In Progress"
                    rating = 0
                }) {
                    Row {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null
                        )
                        Text(text = "In Progress")
                    }
                }
                Button(onClick = {
                    status = "Finished"
                }) {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null
                        )
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


            Button(
                onClick = {

                }
            ) {
                Text(text = "Save Changes")
            }
        }
    }
}
