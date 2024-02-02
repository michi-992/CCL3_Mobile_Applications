package com.cc221043.ccl3_mobileapplications.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.cc221043.ccl3_mobileapplications.R
import com.cc221043.ccl3_mobileapplications.data.model.Book
import com.cc221043.ccl3_mobileapplications.ui.theme.Colors
import com.cc221043.ccl3_mobileapplications.ui.view_model.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
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
fun AddBookTopBar(mainViewModel: MainViewModel, navController: NavController) {
    val state = mainViewModel.mainViewState.collectAsState()

    val iconButtonColors = rememberUpdatedState(
        IconButtonDefaults.iconButtonColors(
            contentColor = Colors.OffWhite,
            containerColor = Color.Transparent,
            disabledContentColor = Colors.OffWhite,
            disabledContainerColor = Color.Transparent,
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
        actions = {
            var showDropDownMenu by remember { mutableStateOf(false) }

            IconButton(
                onClick = { showDropDownMenu = true }) {
                Icon(Icons.Default.MoreVert, null, tint = Colors.OffWhite)
            }

            DropdownMenu(
                showDropDownMenu, { showDropDownMenu = false },
                modifier = Modifier.background(Colors.Blue2),
            ) {
                DropdownMenuItem(
                    text = { Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = Colors.OffWhite)
                        Spacer(Modifier.size(6.dp))
                        Text(text = "Edit", fontSize = 16.sp, color = Colors.OffWhite)
                    } },
                    onClick = {
                        showDropDownMenu = false
                        navController.navigate("${Screen.EditBook.route}/${mainViewModel.mainViewState.value.selectedBook?.id}")
                    }
                )
                DropdownMenuItem(
                    text = { Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Colors.OffWhite)
                        Spacer(Modifier.size(6.dp))
                        Text(text = "Delete", fontSize = 16.sp, color = Colors.OffWhite)
                    } },
                    onClick = {
                        mainViewModel.openDeleteDialog()
                        showDropDownMenu = false
                    })

                DropdownMenuItem(
                    text = { Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Build, contentDescription = null, tint = Colors.OffWhite)
                        Spacer(Modifier.size(6.dp))
                        Text(text = "Status Change", fontSize = 16.sp, color = Colors.OffWhite)
                    } },
                    onClick = {
                        mainViewModel.openChangeStatusDialog()
                        showDropDownMenu = false
                    })
            }
        },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    )
}

