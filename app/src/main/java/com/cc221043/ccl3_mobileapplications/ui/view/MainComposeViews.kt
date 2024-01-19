package com.cc221043.ccl3_mobileapplications.ui.view

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.contentColorFor
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
import androidx.compose.ui.UiComposable
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cc221043.ccl3_mobileapplications.R
import com.cc221043.ccl3_mobileapplications.ui.theme.Colors
import com.cc221043.ccl3_mobileapplications.ui.view_model.MainViewModel


sealed class Screen(val route: String) {
    object HomeAll : Screen("HomeAll")
    object HomeCategories : Screen("HomeCategories")
    object AddBook : Screen("AddBook")
    object EditBook : Screen("AddBook")
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
                    HomeAllTopBar()
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
fun HomeAllTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.hanging_bat),
                    contentDescription = null, // Content description for accessibility
                    modifier = Modifier
                        .height(46.dp)
                        .width(42.dp),
                    tint = Colors.Blue6// Adjust the size as needed
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


@Composable
fun HomeScreenAll(mainViewModel: MainViewModel, navController: NavController) {
    val state = mainViewModel.mainViewState.collectAsState()
    val books = state.value.books
    val gradientColors = listOf(Colors.Blue1, Colors.Blue4, Colors.Blue1)

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
            .padding(vertical = 14.dp, horizontal = 14.dp)
            .fillMaxHeight()
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = gradientColors
                )
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        columns = GridCells.Fixed(3),
        content = {
            items(15) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(3.0f)
                        .height(154.dp)
                        .background(Colors.OffWhite)
                )
            }
        })
//    }
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

            IconButton(onClick = {

            }) {

            }

            Button(onClick = {
                println(title)
                println(author)
                println(state.value.selectedImageURI)
                println(platformat)
                println(synopsis)
            }) {
                Text(text = "Save book")
            }
        }
    }
}