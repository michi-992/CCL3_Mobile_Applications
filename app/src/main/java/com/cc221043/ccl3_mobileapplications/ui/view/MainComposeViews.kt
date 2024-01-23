package com.cc221043.ccl3_mobileapplications.ui.view

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.google.android.material.search.SearchBar


sealed class Screen(val route: String) {
    object HomeAll : Screen("HomeAll")
    object HomeCategories : Screen("HomeCategories")
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
                    HomeAllTopBar(mainViewModel) {newSearchQuery ->
                        mainViewModel.updateSearchText(newSearchQuery)
                    }
//                    var tabIndex by remember {
//                        mutableIntStateOf(0)
//                    }
//                    val tabs = listOf("All Books", "Categories")
//
//                    TabRow(
//                        selectedTabIndex = tabIndex,
//                        divider = {
//                            // Customize the divider appearance here
//                            Spacer(
//                                modifier = Modifier
//                                    .height(2.dp)
//                                    .fillMaxWidth() // Width of the divider
//                                    .background(Colors.Blue4) // Color of the divider
//                            )
//                        }
//                    ) {
//                        tabs.forEachIndexed { index, title ->
//                            Tab(text = { Text(title) },
//                                selected = tabIndex == index,
//                                onClick = { tabIndex = index }
//                            )
//                        }
//                    }
//// Use the updated tabIndex to determine the selected tab
//                    when (tabIndex) {
//                        0 -> {
//                            println("Selected Tab: All Books")
//                            // Handle the content for the "All Books" tab
//                        }
//
//                        1 -> {
//                            println("Selected Tab: Categories")
//                            // Handle the content for the "Categories" tab
//                        }
//                    }
                }

                is Screen.HomeCategories -> {
                }

                is Screen.AddBook -> {
                    AddBookTopBar(navController)
                }

                is Screen.EditBook -> {
                }

                is Screen.BookDetails -> {

                }
            }
        },
        bottomBar = {
            if (state.value.selectedScreen == Screen.HomeAll || state.value.selectedScreen == Screen.HomeCategories) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Colors.Blue0)
                ) {
                    Button(
                        onClick = {
                            navController.navigate("AddBook")
                        },
                        shape = RoundedCornerShape(6.dp),
                        colors = iconButtonColors.value,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Icon(imageVector = Icons.Default.AddCircle, contentDescription = null)
                        Text(text = "Add book")
                    }
                }
            }
        }
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
            composable(Screen.AddBook.route) {
                mainViewModel.selectedScreen(Screen.AddBook)
                mainViewModel.getAllBooks()
                AddBook(
                    mainViewModel,
                    navController,
                    onPickImage = { pickImageLauncher.launch("image/*") })
            }
            composable("${Screen.BookDetails.route}/{bookId}") { backStackEntry ->
                val arguments = requireNotNull(backStackEntry.arguments)
                val bookId = arguments.getString("bookId")!!.toInt()

                mainViewModel.selectBookDetails(bookId)
                BookDetails(mainViewModel, navController, bookId)
            }
            composable(Screen.EditBook.route + "/{bookId}") { backStackEntry ->
                val arguments = requireNotNull(backStackEntry.arguments)
                val bookId = arguments.getString("bookId")!!.toInt()

                mainViewModel.selectBookDetails(bookId)
                EditBook(mainViewModel, navController, bookId, onPickImage = { pickImageLauncher.launch("image/*") })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookTopBar(navController: NavController) {
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
                onClick = { navController.navigateUp() },
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
fun HomeAllTopBar(mainViewModel: MainViewModel, onSearch: (String) -> Unit) {
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

        }, actions = {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchText,
                onValueChange = {
                    searchText = it
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
                                mainViewModel.updateSearchText("")
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
//                        onSearch.invoke(searchText)
                        mainViewModel.updateSearchText(searchText)
                        print("Search in HomeAllTopBar: ")
                        println(mainViewModel.mainViewState.value.searchText)

                    }
                )
            )
        },

        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}


@Composable
fun HomeScreenAll(mainViewModel: MainViewModel, navController: NavController) {
    val state = mainViewModel.mainViewState.collectAsState()
    val books = state.value.books
    val gradientColors = listOf(Colors.Blue1, Colors.Blue4, Colors.Blue1)

//    val searchText by mainViewModel.searchText.collectAsState()

    val filteredBooks = if (state.value.searchText.isNotEmpty()) {
        print("Search in HomeScreenAll: ")
        println(state.value.searchText)

        books.filter { it.title.contains(state.value.searchText, ignoreCase = true)}
    } else {
        books
    }
//    Box(
//        Modifier
//            .fillMaxHeight(9.0f)
//            .fillMaxWidth()
//            .background(
//                brush = Brush.verticalGradient(
//                    colors = gradientColors
//                )
//            )
//            .verticalScroll(rememberScrollState())
//
//    ) {

    LazyVerticalGrid(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = gradientColors,
                )
            )
            .padding(vertical = 14.dp, horizontal = 14.dp)
            .fillMaxHeight()
            .fillMaxWidth(),

        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        columns = GridCells.Fixed(3),
        content = {
            items(filteredBooks.size) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(3.0f)
                        .height(154.dp)
                        .clickable {
                            navController.navigate("${Screen.BookDetails.route}/${books[it].id}")
                        }
                ) {
                    AsyncImage(
                        model = books[it].cover,
                        contentDescription = null)
                }
            }
        })
//    }

    //HomeAllTopBar { newSearchQuery ->
    //    HomeScreenAll(mainViewModel, navController, newSearchQuery)
    //}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBook(mainViewModel: MainViewModel, navController: NavController, onPickImage: () -> Unit) {
    val state = mainViewModel.mainViewState.collectAsState()
    Column(
        Modifier.verticalScroll(rememberScrollState())
    ) {
        var title by rememberSaveable { mutableStateOf("") }
        var author by rememberSaveable { mutableStateOf("") }
        var platformat by rememberSaveable { mutableStateOf("") }
        var synopsis by rememberSaveable { mutableStateOf("") }
        var status by rememberSaveable { mutableStateOf("") }

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
                label = { Text(text = "Author") })

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 20.dp),
                value = synopsis,
                onValueChange = { newText -> synopsis = newText },
                label = { Text(text = "Synopsis") })

            Row (
                Modifier.horizontalScroll(rememberScrollState())
            ){
                Button(
                    onClick = { status = "Not started" }
                ) {
                    Row{
                        Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                        Text(text = "Not started")
                    }
                }
                Button(onClick = {
                    status = "In Progress"
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


            Button(onClick = {
                val newBook = Book(
                    title = title,
                    author = author,
                    platformat = platformat,
                    rating = 3,
                    synopsis = synopsis,
                    status = status
                )

                val insertedId = mainViewModel.saveBookAndImage(newBook)
                navController.navigate("${Screen.BookDetails.route}/$insertedId")
            }) {
                Text(text = "Save book")
            }
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
        Text(text = "Platform/Format: ${book.platformat}", style = MaterialTheme.typography.displayMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Synopsis: ${book.synopsis}", style = MaterialTheme.typography.displayMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Status: ${book.status}", style = MaterialTheme.typography.displayMedium)

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

    Column(
        Modifier.verticalScroll(rememberScrollState())
    ) {
        var title by rememberSaveable { mutableStateOf(book.title) }
        var author by rememberSaveable { mutableStateOf(book.author) }
        var platformat by rememberSaveable { mutableStateOf(book.platformat) }
        var synopsis by rememberSaveable { mutableStateOf(book.synopsis) }
        var status by rememberSaveable { mutableStateOf(book.status) }

        val iconButtonColors = rememberUpdatedState(
            IconButtonDefaults.iconButtonColors(
                contentColor = Colors.OffWhite,
                containerColor = Colors.Blue0,
                disabledContentColor = Colors.OffWhite,
                disabledContainerColor = Colors.Blue0,
            )
        )

        Column {
            AsyncImage(
                model = book.cover,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(Color.Gray)
            )

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

            Row (
                Modifier.horizontalScroll(rememberScrollState())
            ){
                Button(
                    onClick = { status = "Not started" }
                ) {
                    Row{
                        Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                        Text(text = "Not started")
                    }
                }
                Button(onClick = {
                    status = "In Progress"
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

            Button(
                onClick = {
                    val editedBook = Book(
                        id = book.id,
                        title = title,
                        author = author,
                        platformat = platformat,
                        rating = 3,
                        synopsis = synopsis,
                        status = status
                    )

                    mainViewModel.updateBookAndImage(editedBook)

                    navController.navigate("${Screen.BookDetails.route}/${editedBook.id}")
                }
            ) {
                Text(text = "Save Changes")
            }
        }
    }
}
