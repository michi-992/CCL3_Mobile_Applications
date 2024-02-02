package com.cc221043.ccl3_mobileapplications.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.cc221043.ccl3_mobileapplications.R
import com.cc221043.ccl3_mobileapplications.data.model.Book
import com.cc221043.ccl3_mobileapplications.ui.theme.Colors
import com.cc221043.ccl3_mobileapplications.ui.view_model.MainViewModel

// displays the details of a specific book
@Composable
fun BookDetails(mainViewModel: MainViewModel, navController: NavController, bookId: Int) {
    val state = mainViewModel.mainViewState.collectAsState()
    val book = state.value.selectedBook ?: return
    val gradientColors = listOf(Colors.Blue1, Colors.Blue4, Colors.Blue1)
    val genreString =
        book.genres.joinToString().removePrefix("[").removePrefix(",").removeSuffix("]")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //displays image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = gradientColors,
                    )
                )
                .padding(top = 20.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        color = Colors.Blue0,
                        shape = RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp)
                    )
            )
            AsyncImage(
                model = book.cover,
                contentDescription = null,
                modifier = Modifier
                    .width(154.dp)
                    .height(240.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(Colors.Blue4),
                contentScale = ContentScale.Crop
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        // displays title
        Text(
            text = book.title,
            style = MaterialTheme.typography.titleSmall.copy(
                textAlign = TextAlign.Center
            ),
            color = Colors.OffWhite,
            modifier = Modifier
                .padding(22.dp)
        )

        // only displays optional information if it was input
        if (book.author.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = book.author,
                style = MaterialTheme.typography.bodySmall.copy(
                    textAlign = TextAlign.Center
                ),
                color = Colors.Blue5,
                modifier = Modifier
                    .padding(22.dp)
            )
        }

        Spacer(modifier = Modifier.size(24.dp))

        Row(
            modifier = Modifier.padding(bottom = 12.dp)
                .background(color = Colors.PrimaryBlueDark, shape = CircleShape)
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // displays reading status
            if (book.status == "Finished") {
                Icon(
                    painterResource(id = R.drawable.checkcircle),
                    contentDescription = null,
                    tint = Colors.OffWhite
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    text = "Finished",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Colors.OffWhite
                )
            } else if (book.status == "Not started") {
                Icon(
                    painterResource(id = R.drawable.slash),
                    contentDescription = null,
                    tint = Colors.OffWhite
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    text = "Not started",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Colors.OffWhite
                )

            } else if (book.status == "In Progress") {
                Icon(
                    painterResource(id = R.drawable.clock),
                    contentDescription = null,
                    tint = Colors.OffWhite
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    text = "In Progress",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Colors.OffWhite
                )
            }
        }

        // displays rating (if applicable and if status is 'Finished')
        if (book.rating != 0 && book.status == "Finished") {
            RatingBar(rating = book.rating, onRatingChanged = {}, small = false)
        }

        Spacer(modifier = Modifier.height(18.dp))

        // displays other info (genres, platform/format,synopsis)
        Column (
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
        ){
            Text(
                text = "Genres",
                style = MaterialTheme.typography.bodySmall,
                color = Colors.Blue5
            )
            Text(
                text = genreString,
                style = MaterialTheme.typography.bodyMedium,
                color = Colors.OffWhite
            )
            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Platform/Format",
                style = MaterialTheme.typography.bodySmall,
                color = Colors.Blue5
            )

            // changes text/styling based on whether platform was provided
            Text(
                text = if(book.platformat.isNotEmpty()) book.platformat else "No Platform or Format was provided",
                style = MaterialTheme.typography.bodyMedium,
                color = if(book.platformat.isNotEmpty()) Colors.OffWhite else Colors.Blue6
            )
            Spacer(modifier = Modifier.size(18.dp))

            Text(
                text = "Synopsis",
                style = MaterialTheme.typography.bodySmall,
                color = Colors.Blue5
            )
            // changes text/styling based on whether synopsis was provided
            Text(
                text = if(book.synopsis.isNotEmpty()) book.synopsis else "No Synopsis was provided",
                style = MaterialTheme.typography.bodyMedium,
                color = if(book.synopsis.isNotEmpty()) Colors.OffWhite else Colors.Blue6
            )

            Spacer(modifier = Modifier.size(54.dp))

        }
    }

    // displays dialog for quick status change
    if (state.value.showChangeStatusDialog) {
        StatusChangeDialog(mainViewModel)
    }

    // displays delete dialog
    if (state.value.showDeleteDialog) {
        DeleteDialog(mainViewModel, navController)
    }
}

// Composable function for delete dialog
@Composable
fun DeleteDialog(mainViewModel: MainViewModel, navController: NavController) {
    val state = mainViewModel.mainViewState.collectAsState()
    val book = state.value.selectedBook ?: return

    val backgroundColor = Colors.Blue0.copy(alpha = 0.8f)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // asks for confirmation
        AlertDialog(
            onDismissRequest = { mainViewModel.dismissDeleteDialog() },
            title = { Text("Delete Book", style = MaterialTheme.typography.displayMedium, color = Colors.Blue5) },
            text = {
                Text(
                    "Are you sure you want to delete this book?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Colors.OffWhite
                )
            },
            // button for delete confirmation
            confirmButton = {
                Button(colors = ButtonDefaults.buttonColors(
                    contentColor = Colors.OffWhite,
                    containerColor = Colors.PrimaryBlue
                ),
                    onClick = {
                        mainViewModel.dismissDeleteDialog()
                        mainViewModel.deleteBook(book)
                        navController.navigate(Screen.Home.route)
                    }
                ) {
                    Text("Delete")
                }
            },
            // button for cancelling
            dismissButton = {
                Button(
                    onClick = { mainViewModel.dismissDeleteDialog() },
                    border = BorderStroke(2.dp, Colors.PrimaryBlue),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Colors.OffWhite,
                        containerColor = Color.Transparent
                    )
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

// Composable function for quick status change dialog
@Composable
fun StatusChangeDialog(mainViewModel: MainViewModel) {
    val state = mainViewModel.mainViewState.collectAsState()

    val book = state.value.selectedBook ?: return
    var status by remember {
        mutableStateOf(book.status)
    }
    var rating by remember { mutableStateOf(book.rating) }
    val backgroundColor = Colors.Blue0.copy(alpha = 0.8f)

    var options = listOf("Not started", "In Progress", "Finished")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Dialog(
            onDismissRequest = {
                mainViewModel.dismissChangeStatusDialog()
                status = book.status
                rating = book.rating
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Colors.Blue1, RoundedCornerShape(20.dp))
                        .padding(20.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 14.dp),
                        text = "Change Reading Status",
                        style = MaterialTheme.typography.displayMedium,
                        color = Colors.Blue5
                    )
                    // buttons for each status option
                    options.forEach {option ->
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
                            border = if(status == option) BorderStroke(2.dp, Color.Transparent) else BorderStroke(2.dp, Colors.PrimaryBlue),
                            onClick = {
                                status = option
                                if (option == "Not started" || option == "Finished") {
                                    rating = 0
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (status == option) Colors.PrimaryBlue else Colors.Blue1,
                                contentColor = Colors.OffWhite
                            ),
                        ) {
                            Row (
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ){
                                Icon(
                                    painterResource(id =
                                    if(option == "Not started") R.drawable.slash else if(option == "In Progress") R.drawable.clock else R.drawable.checkcircle
                                    ),
                                    contentDescription = null,
                                    tint = Colors.OffWhite
                                )
                                Spacer(modifier = Modifier.size(6.dp))
                                Text(text = option, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Spacer(Modifier.size(12.dp))
                    }

                    // rating if status is 'Finished'
                    if (status == "Finished") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp)
                                .background(Colors.Blue3, RoundedCornerShape(14.dp))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    modifier = Modifier.padding(start = 14.dp),
                                    text = "Rating (optional)",
                                    fontSize = 14.sp,
                                    color = Colors.Blue5
                                )
                                RatingBar(
                                    rating = rating, onRatingChanged = { rating = it },
                                    modifier = Modifier.padding(bottom = 6.dp),
                                    small = true
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
                    // buttons for saving/cancelling
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { mainViewModel.dismissChangeStatusDialog() },
                            border = BorderStroke(2.dp, Colors.PrimaryBlue),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Colors.OffWhite,
                                containerColor = Color.Transparent
                            )
                        ) {
                            Text("Cancel")
                        }
                        Spacer(Modifier.size(12.dp))
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
            }
        )
    }
}