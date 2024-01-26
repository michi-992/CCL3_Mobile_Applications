package com.cc221043.ccl3_mobileapplications.ui.view

import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.cc221043.ccl3_mobileapplications.data.BookDao
import com.cc221043.ccl3_mobileapplications.ui.view_model.OnboardingViewModel
import kotlinx.coroutines.delay

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

    observeOnboardingCompletion(onboardingViewModel, navController)
    val onboardingCompleted by onboardingViewModel.onboardingCompleted.collectAsState()
    if (onboardingCompleted) {
        navController.navigate(Screen.Home.route)
    }


    Scaffold(
        topBar = {
            when (state.value.selectedScreen) {
                is Screen.Home -> {
                    HomeTopBar()
                }
                is Screen.Onboarding -> {
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
            startDestination = if (onboardingCompleted) Screen.Home.route else Screen.Onboarding.route,
            modifier = Modifier.padding(it),
        ) {
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

@Composable
private fun observeOnboardingCompletion(
    onboardingViewModel: OnboardingViewModel,
    navController: NavController
) {
    val onboardingCompleted by onboardingViewModel.onboardingCompleted.collectAsState()

    if (onboardingCompleted) {
        LaunchedEffect(navController) {
            navController.navigate(Screen.Home.route)
        }
    } else {
        LaunchedEffect(navController) {
            navController.navigate(Screen.Onboarding.route)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(mainViewModel: MainViewModel, navController: NavController) {
    val buttonColors = ButtonDefaults.buttonColors(
        contentColor = Colors.OffWhite,
        containerColor = Colors.PrimaryBlueDark,
        disabledContentColor = Colors.OffWhite,
        disabledContainerColor = Colors.Blue0,
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            var tabIndex by remember { mutableIntStateOf(0) }
            var flag by remember { mutableStateOf(false) }
            var flag2 by remember { mutableStateOf(false) }
            val pagerState = rememberPagerState { 2 }

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
                0 -> { if (flag) {
                    mainViewModel.getAllBooks()
                        flag = false
                    }
                }

                1 -> { if (flag2) {
                        mainViewModel.getAllBooks()
                        flag2 = false
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
                    0 -> { HomeScreenAllBooks(mainViewModel, navController) }
                    1 -> { HomeScreenGenres(navController, mainViewModel) }
                }
            }
        }
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

@Composable
fun HomeScreenAllBooks(mainViewModel: MainViewModel, navController: NavController) {
    val gradientColors = listOf(Colors.Blue1, Colors.Blue4, Colors.Blue1)
    val state = mainViewModel.mainViewState.collectAsState()
    val books = if(state.value.searchedBooks.isEmpty()) state.value.books else state.value.searchedBooks

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
            SearchBar(mainViewModel)
            BookGrid(navController, books)
        }
    }
}

@Composable
fun HomeScreenGenres(navController: NavController, mainViewModel: MainViewModel) {
    val gradientColors = listOf(Colors.Blue1, Colors.Blue4, Colors.Blue1)
    val genreArray = stringArrayResource(id = R.array.genres)
    var selectedNames by remember { mutableStateOf(emptyList<String>()) }
    val state = mainViewModel.mainViewState.collectAsState()
    val books = if(state.value.selectedBooksForGenres.isEmpty() && selectedNames.isEmpty()) state.value.booksForGenres else state.value.selectedBooksForGenres

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
            LazyRow (
                modifier = Modifier.padding(top = 20.dp, start = 14.dp, end = 14.dp)
            ){
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
                            mainViewModel.updateSelectedGenres(selectedNames)
                        }
                    )
                }
            }
            BookGrid(navController, books)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onboardingViewModel: OnboardingViewModel, navController: NavController) {
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
//        borderColor = Colors.PrimaryBlueDark,
        disabledContentColor = Colors.OffWhite,
        disabledContainerColor = Colors.Blue0,
    )

    val pagerState = rememberPagerState { 5 }
    var index by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(index) {
        pagerState.animateScrollToPage(index)
    }
    LaunchedEffect(
        pagerState.currentPage
    ) {
        index = pagerState.currentPage
    }

    HorizontalPager(
        state = pagerState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (index) {
                0 -> {
                    LoadingScreen()
                    LaunchedEffect(Unit) {
                        delay(2000)
                        index++
                    }
                }
                1 -> {
                    Image(
                        painter = painterResource(id = R.drawable.barry_happy),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                    Text("Hello! I'm Barry the BiblioBat.",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                    )
                    Text("I'll help you keep track of all your reads.",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Colors.OffWhite),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                    )

                }
                2 -> {
                    Image(
                        painter = painterResource(id = R.drawable.onboarding_one),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                    Text("Easily find your books",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                    )
                    Text("You can search for a specific book you've added and view its details.",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Colors.OffWhite),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                    )

                }
                3 -> {
                    Image(
                        painter = painterResource(id = R.drawable.onboarding_two),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                    Text("Keep track of your books",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                    )
                    Text("Organize your books for an easy overview of all your reads.",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Colors.OffWhite),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                    )

                }
                4 -> {
                    Image(
                        painter = painterResource(id = R.drawable.onboarding_three),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                    Text("Track your status & rate your reads",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                    )
                    Text("Update your reading status and rate your book to keep up with your reading journey!",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Colors.OffWhite),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                    )

                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                if (index != 0 && index != 4) {
                    Button(onClick = {
                        onboardingViewModel.completeOnboarding()
                        navController.navigate(Screen.Home.route)
                    },
                        colors = buttonColorsSecondary,
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Colors.PrimaryBlueDark,
                                shape = CircleShape
                            )
                    ) {
                        Text("Skip")
                    }
                }

                Spacer(
                    Modifier.padding(10.dp)
                )

                if (index != 0 && index != 1) {
                    Button(onClick = {
                        index--
                    },
                        colors = buttonColorsSecondary,
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Colors.PrimaryBlueDark,
                                shape = CircleShape
                            )
                    )  {
                        Text("Back")
                    }
                }

                Spacer(
                    Modifier.padding(10.dp)
                )

                if (index != 0 && index != 4) {
                    Button(onClick = {
                        index++
                    },
                        colors = buttonColors
                    ) {
                        Text("Next")
                    }
                }

                Spacer(
                    Modifier.padding(10.dp)
                )

                if (index == 4) {
                    Button(onClick = {
                        onboardingViewModel.completeOnboarding()
                        navController.navigate(Screen.Home.route)
                    },
                        colors = buttonColors
                    ) {
                        Text("Finish")
                    }
                }
            }
        }
    }
}

