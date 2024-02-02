package com.cc221043.ccl3_mobileapplications.ui.view

import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cc221043.ccl3_mobileapplications.R
import com.cc221043.ccl3_mobileapplications.ui.theme.Colors
import com.cc221043.ccl3_mobileapplications.ui.view_model.MainViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.cc221043.ccl3_mobileapplications.ui.view_model.OnboardingViewModel
import kotlinx.coroutines.delay

// set up different screens
sealed class Screen(val route: String) {
    object Home : Screen("Home")
    object Onboarding : Screen("Onboarding")
    object AddBook : Screen("AddBook")
    object EditBook : Screen("EditBook")
    object BookDetails : Screen("BookDetails")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(
    mainViewModel: MainViewModel,
    onboardingViewModel: OnboardingViewModel,
    pickImageLauncher: ActivityResultLauncher<String>
) {
    val state = mainViewModel.mainViewState.collectAsState()
    val navController = rememberNavController()

    val onboardingCompleted by onboardingViewModel.onboardingCompleted.collectAsState()

    Scaffold(
        // displays top bars according to current screen
        topBar = {
            when (state.value.selectedScreen) {
                is Screen.Home -> {
                    HomeTopBar()
                }
                is Screen.Onboarding -> {
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
        // handles navigation
        NavHost(
            navController = navController,
            // sets Onboarding as starting point if it's not completed yet, otherwise it starts on the Home Screen
            startDestination = if (onboardingCompleted) Screen.Home.route else Screen.Onboarding.route,
            modifier = Modifier.padding(it),
        ) {
            // composables for each screen
            composable(Screen.Onboarding.route) {
                mainViewModel.selectedScreen(Screen.Onboarding)
                OnboardingScreen(onboardingViewModel, navController)
            }
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
            // BookDetails & EditBook require the book's ID
            composable("${Screen.BookDetails.route}/{bookId}") { backStackEntry ->
                val bookId = backStackEntry.arguments?.getString("bookId")?.toIntOrNull() ?: -1
                mainViewModel.selectedScreen(Screen.BookDetails)
                mainViewModel.selectBookDetails(bookId)

                BookDetails(
                    mainViewModel = mainViewModel,
                    navController = navController,
                    bookId = bookId
                )
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

// Composable function for the Home Screen
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(mainViewModel: MainViewModel, navController: NavController) {
    val buttonColors = ButtonDefaults.buttonColors(
        contentColor = Colors.OffWhite,
        containerColor = Colors.PrimaryBlue,
        disabledContentColor = Colors.OffWhite,
        disabledContainerColor = Colors.Blue0,
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            var tabIndex by remember { mutableIntStateOf(0) }
            val pagerState = rememberPagerState { 2 }

            // scrolls to selected tab
            LaunchedEffect(tabIndex) {
                pagerState.animateScrollToPage(tabIndex)
            }
            // updates tab index
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
                // tabs for HomeScreenAllBooks and HomeScreenGenres
                Tab(text = { Text("All Books", style = MaterialTheme.typography.displaySmall) },
                    selected = tabIndex == 0,
                    onClick = {
                        tabIndex = 0
                    }
                )
                Tab(text = { Text("Genres", style = MaterialTheme.typography.displaySmall) },
                    selected = tabIndex == 1,
                    onClick = {
                        tabIndex = 1
                    }
                )
            }
            // navigates between pages
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { index ->
                when (index) {
                    0 -> { HomeScreenAllBooks(mainViewModel, navController) }
                    1 -> { HomeScreenGenres(navController, mainViewModel) }
                }
            }
        }
        // Add Book button at the bottom
        Button(
            onClick = { navController.navigate("AddBook") },
            shape = RoundedCornerShape(12.dp),
            colors = buttonColors,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)
                .background(color = Colors.Blue1, shape = RoundedCornerShape(16.dp))
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

// displays all books of the user
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenAllBooks(mainViewModel: MainViewModel, navController: NavController) {
    val gradientColors = listOf(Colors.Blue1, Colors.Blue4, Colors.Blue1)
    val state = mainViewModel.mainViewState.collectAsState()

    var searchText by rememberSaveable { mutableStateOf("") }
    var statusSearch by rememberSaveable { mutableStateOf("") }

    val initialBooks = if(searchText == "") state.value.books else state.value.searchedBooks
    val books = if(searchText == "" && statusSearch == "") state.value.books else if(searchText != "" && statusSearch == "") state.value.searchedBooks else state.value.statusFilteredBooks

    // handles search function and progress filtering
    LaunchedEffect(initialBooks) {
        mainViewModel.changeStatusFilteredBooks(initialBooks, statusSearch)
    }
    LaunchedEffect(statusSearch) {
        mainViewModel.changeStatusFilteredBooks(initialBooks, statusSearch)
    }

    val statusOptions = listOf("Not started", "In Progress", "Finished")

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
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Search bar
            TextField(
                textStyle = MaterialTheme.typography.bodySmall,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, end = 14.dp, start = 14.dp)
                    .shadow(shape = CircleShape, elevation = 6.dp)
                    .border(BorderStroke(2.dp, color = Colors.Blue4), shape = CircleShape),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    containerColor = Colors.Blue1,
                    focusedLeadingIconColor = Colors.OffWhite,
                    textColor = Colors.OffWhite,
                    unfocusedLeadingIconColor = Colors.Blue5,
                    focusedTrailingIconColor = Colors.OffWhite,
                    placeholderColor = Colors.Blue5
                ),
                shape = CircleShape,
                value = searchText,
                onValueChange = {
                    searchText = it
                    mainViewModel.getBooksBySearch(searchText)
                },
                placeholder = { Text("Search") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                searchText = ""
                                mainViewModel.resetSearch()
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
                        mainViewModel.getBooksBySearch(searchText)
                    }
                ),
            )
            // Buttons for filtering by reading status
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, end = 14.dp, start = 14.dp)
                    .horizontalScroll(
                        rememberScrollState()
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                statusOptions.forEach {option ->
                    Button(
                        onClick = {
                            if(statusSearch != option) {
                                statusSearch = option
                            } else {
                                statusSearch = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (statusSearch == option) Colors.PrimaryBlue else Colors.Blue3,
                            contentColor = Colors.OffWhite
                        ),
                        border = if(statusSearch == option) BorderStroke(2.dp, Color.Transparent) else BorderStroke(2.dp, Colors.PrimaryBlue),
                        modifier = Modifier.padding(end = 6.dp),
                        contentPadding = PaddingValues(vertical = 6.dp, horizontal = 18.dp)
                    ) {
                        Text(option)
                    }
                }
            }
            // displays image and message if there are no books added yet
            if (state.value.books.isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.barry_bored),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
                Text(
                    text = "No books yet. Try adding a book so Barry has something to read.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = Colors.Blue5
                )
            } else {
                // displays books in the grid
                BookGrid(navController, books)
            }
        }
    }
}

// filters by genre
@Composable
fun HomeScreenGenres(navController: NavController, mainViewModel: MainViewModel) {
    val gradientColors = listOf(Colors.Blue1, Colors.Blue4, Colors.Blue1)
    val genreArray = stringArrayResource(id = R.array.genres)
    var selectedGenres by rememberSaveable { mutableStateOf(emptyList<String>()) }
    val state = mainViewModel.mainViewState.collectAsState()
    val books = if(selectedGenres.isEmpty()) state.value.booksForGenres else state.value.selectedBooksForGenres

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
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // genre selection buttons
            LazyRow (
                modifier = Modifier.padding(top = 20.dp, start = 14.dp, end = 14.dp)
            ){
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
                            mainViewModel.updateSelectedGenres(selectedGenres)
                        }
                    )
                }
            }
            // writes out which genres have been selected to filter for
            Text(textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth().padding(top = 6.dp, start = 14.dp, end = 14.dp), text = "Genres: ${selectedGenres.joinToString()}", fontSize = 14.sp, color = Colors.Blue5)

            // message if no books have been added yet
            if (state.value.booksForGenres.isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.barry_bored),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
                Text(
                    text = "No books yet. Try adding a book so Barry has something to read.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = Colors.Blue5
                )
            } else {
                // displays books in the grid
                BookGrid(navController, books)
            }
        }
    }
}

// Composable function for onboarding screen
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onboardingViewModel: OnboardingViewModel, navController: NavController) {
    // initializes data
    DisposableEffect(Unit) {
        onDispose {
            onboardingViewModel.initializeData()
        }
    }

    val buttonColors = ButtonDefaults.buttonColors(
        contentColor = Colors.OffWhite,
        containerColor = Colors.PrimaryBlueDark,
        disabledContentColor = Colors.OffWhite,
        disabledContainerColor = Colors.Blue0,
    )

    val buttonColorsSecondary = ButtonDefaults.buttonColors(
        contentColor = Colors.OffWhite,
        containerColor = Color.Transparent,
        disabledContentColor = Colors.OffWhite,
        disabledContainerColor = Colors.Blue0,
    )

    Column {
        var tabIndex by remember { mutableIntStateOf(0) }
        val pagerState = rememberPagerState { 5 }

        // navigates to next page after delay
        LaunchedEffect(tabIndex) {
            if(tabIndex == 0) {
                delay(2000)
                tabIndex++
            }
            pagerState.animateScrollToPage(tabIndex)
            println(tabIndex)
        }

        // handles page change
        LaunchedEffect(pagerState.currentPage) {
            if(pagerState.currentPage == 0) {
                delay(2000)
                tabIndex++
            }
            tabIndex = pagerState.currentPage
            println(tabIndex)
        }

        // navigates between onboarding pages
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { index ->
            when (index) {
                0 -> {
                    LoadingScreen()
                }
                1 -> {
                    FirstOnboardingScreen()
                    LaunchedEffect(Unit) {
                    }
                }
                2 -> {
                    SecondOnboardingScreen()
                }
                3 -> {
                    ThirdOnboardingScreen()
                }
                4 -> {
                    FourthOnboardingScreen()
                }
            }
        }

        // buttons for navigating
        Column(
            modifier = Modifier.fillMaxWidth().height(160.dp).padding(horizontal = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ){
                // back button
                if (tabIndex != 0 && tabIndex != 1) {
                    Button(onClick = {
                        tabIndex--
                    },
                        border = BorderStroke(2.dp, Colors.PrimaryBlueDark),
                        colors = buttonColorsSecondary,
                        modifier = Modifier.weight(1f)
                    )  {
                        Text("Back")
                    }
                }
                // next button
                if (tabIndex != 0 && tabIndex != 4) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            tabIndex++
                        },
                        colors = buttonColors
                    ) {
                        Text(text = if(tabIndex == 1) "Let's go" else "Next")
                    }
                }
                // finish button
                if (tabIndex == 4) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onboardingViewModel.completeOnboarding()
                            navController.navigate(Screen.Home.route)
                        },
                        colors = buttonColors
                    ) {
                        Text("Finish")
                    }
                }

            }

            Spacer(
                Modifier.padding(10.dp)
            )

            //skip button
            if (tabIndex != 0 && tabIndex != 4) {
                Button(
                    onClick = {
                        onboardingViewModel.completeOnboarding()
                        navController.navigate(Screen.Home.route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Colors.OffWhite,
                        containerColor = Color.Transparent,
                        disabledContentColor = Colors.OffWhite,
                        disabledContainerColor = Color.Transparent,
                    )
                ) {
                    Text("Skip Introduction", style = MaterialTheme.typography.bodySmall, textDecoration = TextDecoration.Underline)
                }
            }
        }
    }
}

// onboarding screens with respective contents
@Composable
fun FirstOnboardingScreen() {
    Column {
        Image(
            painter = painterResource(id = R.drawable.barry_happy),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        )
        Text(
            "Hello! I'm Barry the BiblioBat.",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )
        Text(
            "I'll help you keep track of all your reads.",
            style = MaterialTheme.typography.bodyLarge.copy(color = Colors.OffWhite),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )
    }

}

@Composable
fun SecondOnboardingScreen() {
    Column {
        Image(
            painter = painterResource(id = R.drawable.onboarding_one),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        )
        Text(
            "Easily find your books",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )
        Text(
            "You can search for a specific book you've added and view its details.",
            style = MaterialTheme.typography.bodyLarge.copy(color = Colors.OffWhite),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )
    }

}

@Composable
fun ThirdOnboardingScreen() {
    Column {
        Image(
            painter = painterResource(id = R.drawable.onboarding_two),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        )
        Text(
            "Keep track of your books",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )
        Text(
            "Organize your books for an easy overview of all your reads.",
            style = MaterialTheme.typography.bodyLarge.copy(color = Colors.OffWhite),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun FourthOnboardingScreen() {
    Column {
        Image(
            painter = painterResource(id = R.drawable.onboarding_three),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        )
        Text(
            "Track your status & rate your reads",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )
        Text(
            "Update your reading status and rate your book to keep up with your reading journey!",
            style = MaterialTheme.typography.bodyLarge.copy(color = Colors.OffWhite),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )
    }
}
