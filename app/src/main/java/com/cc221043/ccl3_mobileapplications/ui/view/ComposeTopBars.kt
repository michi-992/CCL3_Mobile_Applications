package com.cc221043.ccl3_mobileapplications.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cc221043.ccl3_mobileapplications.R
import com.cc221043.ccl3_mobileapplications.ui.theme.Colors
import com.cc221043.ccl3_mobileapplications.ui.view_model.MainViewModel

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
                            navController.navigate("Home")
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
    fun EditBookTopBar(mainViewModel: MainViewModel, navController: NavController) {
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
                            navController.navigate("Home")
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
                    text = "Edit Book",
                    style = MaterialTheme.typography.titleSmall
                )
            },
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BookDetailsTopBar(mainViewModel: MainViewModel, navController: NavController) {
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
                            navController.navigate("Home")
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
                    text = "Book Details",
                    style = MaterialTheme.typography.titleSmall
                )
            },
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeTopBar() {
        CenterAlignedTopAppBar(
            title = {
                Column {
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
                }
            },
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        )
    }
