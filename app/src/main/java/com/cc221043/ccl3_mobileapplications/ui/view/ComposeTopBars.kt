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
fun BookDetailsTopBar(
    mainViewModel: MainViewModel,
    navController: NavController
) {
    val state = mainViewModel.mainViewState.collectAsState()

    val book = state.value.selectedBook ?: return

    var showDeleteDialog by remember { mutableStateOf(false) }

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
                showDropDownMenu, { showDropDownMenu = false }
            ) {
                DropdownMenuItem(text = { Text(text = "Edit") }, onClick = {
                    showDropDownMenu = false
                    navController.navigate("${Screen.EditBook.route}/${mainViewModel.mainViewState.value.selectedBook?.id}")
                })

                DropdownMenuItem(text = { Text(text = "Delete") }, onClick = {
                    showDeleteDialog = true
                    showDropDownMenu = false
                })

                DropdownMenuItem(text = { Text(text = "Status Change") }, onClick = {
                    mainViewModel.openChangeStatusDialog()
                    showDropDownMenu = false
                })
            }
        },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    )

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Book", style = TextStyle(color = Colors.Blue5)) },
            text = { Text("Are you sure you want to delete this book?", style = TextStyle(color = Colors.Blue4)) },
            confirmButton = {
                Button(colors = ButtonDefaults.buttonColors(
                    contentColor = Colors.OffWhite,
                    containerColor = Colors.PrimaryBlue
                ),
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
                Button(onClick = { showDeleteDialog = false },
                    modifier = Modifier
                        .border(BorderStroke(2.dp, Colors.PrimaryBlue), shape = CircleShape),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Colors.OffWhite,
                        containerColor = Color.Transparent
                    )) {
                    Text("Cancel")
                }
            }
        )
    }

    if (state.value.showChangeStatusDialog) {
        var status by remember {
            mutableStateOf(book.status)
        }
        var rating by remember { mutableStateOf(book.rating) }
        Dialog(
            onDismissRequest = {
                mainViewModel.dismissChangeStatusDialog()
                status = book.status
                rating = book.rating
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.98f)
                        .background(Colors.Blue0, RoundedCornerShape(20.dp))
                        .border(BorderStroke(2.dp, Colors.PrimaryBlue))
                        .padding(14.dp)
                ) {
                    Text(modifier = Modifier.padding(start= 14.dp), text = "Reading Status", fontSize = 14.sp, color = Colors.Blue5)
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(BorderStroke(2.dp, Colors.PrimaryBlue), CircleShape),
                        onClick = {
                            status = "Not started"
                            rating = 0
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if(status == "Not started") Colors.PrimaryBlueDark else Colors.Blue3,
                            contentColor = Colors.OffWhite,
                            disabledContainerColor = Colors.Blue3,
                            disabledContentColor = Colors.OffWhite,
                        )
                    ) {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ){
                            Icon(
                                painterResource(id = R.drawable.slash),
                                contentDescription = null,
                                tint = Colors.OffWhite
                            )
                            Spacer(modifier = Modifier.size(6.dp))
                            Text(text = "Not started", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Spacer(modifier = Modifier.size(12.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(BorderStroke(2.dp, Colors.PrimaryBlue), CircleShape),
                        onClick = {
                            status = "In Progress"
                            rating = 0
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if(status == "In Progress") Colors.PrimaryBlueDark else Colors.Blue3,
                            contentColor = Colors.OffWhite,
                            disabledContainerColor = Colors.Blue3,
                            disabledContentColor = Colors.OffWhite,
                        )) {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ){
                            Icon(
                                painterResource(id = R.drawable.clock),
                                contentDescription = null,
                                tint = Colors.OffWhite)
                            Spacer(modifier = Modifier.size(6.dp))
                            Text(text = "In Progress", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Spacer(modifier = Modifier.size(12.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(BorderStroke(2.dp, Colors.PrimaryBlue), CircleShape),
                        onClick = {
                            status = "Finished"
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if(status == "Finished") Colors.PrimaryBlueDark else Colors.Blue3,
                            contentColor = Colors.OffWhite,
                            disabledContainerColor = Colors.Blue3,
                            disabledContentColor = Colors.OffWhite,
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                painterResource(id = R.drawable.checkcircle),
                                contentDescription = null,
                                tint = Colors.OffWhite)
                            Spacer(modifier = Modifier.size(6.dp))
                            Text(text = "Finished", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    if (status == "Finished") {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                            .background(Colors.Blue3, RoundedCornerShape(14.dp))) {
                            Column (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                Text(modifier = Modifier.padding(start= 14.dp), text = "Rating (optional)", fontSize = 14.sp, color = Colors.Blue5)
                                RatingBar( rating = rating, onRatingChanged = { rating = it },
                                    modifier = Modifier.padding(bottom = 6.dp),
                                    small = false
                                )
                                Text(
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .clickable { rating = 0 },
                                    text = "Clear Rating",
                                    fontSize = 14.sp,
                                    color = Colors.Blue6,
                                    style = TextStyle(textDecoration = TextDecoration.Underline)
                                )

                            }
                        }
                    }
                    Button(colors = ButtonDefaults.buttonColors(
                        contentColor = Colors.OffWhite,
                        containerColor = Colors.PrimaryBlue
                    ),
                        onClick = {
                            val book = Book(
                                id = book.id,
                                title = book.title,
                                author = book.author,
                                platformat = book.platformat,
                                rating = rating,
                                synopsis = book.synopsis,
                                status = status,
                                cover = book.cover,
                                genres = book.genres
                            )

                            mainViewModel.updateBookAndImage(book)
                        }
                    ) {
                        Text("Save")
                    }

                }
            }
        )
    }
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
