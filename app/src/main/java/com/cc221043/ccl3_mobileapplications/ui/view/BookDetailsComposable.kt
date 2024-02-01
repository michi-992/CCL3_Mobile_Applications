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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.cc221043.ccl3_mobileapplications.R
import com.cc221043.ccl3_mobileapplications.data.model.Book
import com.cc221043.ccl3_mobileapplications.ui.theme.Colors
import com.cc221043.ccl3_mobileapplications.ui.view_model.MainViewModel


@Composable
fun BookDetails(mainViewModel: MainViewModel, navController: NavController, bookId: Int) {
    val state = mainViewModel.mainViewState.collectAsState()
    var book = state.value.selectedBook
    val gradientColors = listOf(Colors.Blue1, Colors.Blue4, Colors.Blue1)
    val genreString = book.genres.joinToString().removePrefix("[").removePrefix(",").removeSuffix("]")

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showStatusChangeDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = gradientColors,
                        )
                    )
                    .padding(top = 20.dp),
                contentAlignment = Alignment.BottomCenter
            ) {Box(
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
                        .background(Color.Gray)
                )

        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = book.title, style = MaterialTheme.typography.displayMedium, color = Colors.OffWhite)


        if(book.author.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = book.author, style = MaterialTheme.typography.displayMedium, color = Colors.Blue5)
        }

        Spacer(modifier = Modifier.size(20.dp))
        Row(
            modifier = Modifier
                .padding(vertical = 20.dp)
                .background(color = Colors.PrimaryBlueDark, shape = CircleShape)
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            if(book.status == "Finished") {
                    Icon(
                        painterResource(id = R.drawable.checkcircle),
                        contentDescription = null,
                        tint = Colors.OffWhite)
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(text = "Finished", style = MaterialTheme.typography.bodyMedium, color = Colors.OffWhite)
                } else if (book.status == "Not started") {
                    Icon(
                        painterResource(id = R.drawable.slash),
                        contentDescription = null,
                        tint = Colors.OffWhite
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(text = "Not started", style = MaterialTheme.typography.bodyMedium, color = Colors.OffWhite)

                } else if(book.status == "In Progress") {
                    Icon(
                        painterResource(id = R.drawable.clock),
                        contentDescription = null,
                        tint = Colors.OffWhite
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(text = "In Progress", style = MaterialTheme.typography.bodyMedium, color = Colors.OffWhite)
            }
        }
        if (book.rating != 0 && book.status == "Finished") {
            RatingBar(rating = book.rating, onRatingChanged = {}, small = false)
        }

        Spacer(modifier = Modifier.height(8.dp))
            Spacer(modifier = Modifier.size(20.dp))
            Text(
                text = "Genres",
                style = MaterialTheme.typography.bodySmall,
                color = Colors.Blue5
            )
            Text(
                text = genreString,
                style = MaterialTheme.typography.bodyMedium,
                color = Colors.Blue6
            )



        Spacer(modifier = Modifier.height(8.dp))
        if(book.platformat.isNotEmpty()) {
            Spacer(modifier = Modifier.size(20.dp))
            Text(
                text = "Platform/Format",
                style = MaterialTheme.typography.bodySmall,
                color = Colors.Blue5
            )
            Text(
                text = book.platformat,
                style = MaterialTheme.typography.bodyMedium,
                color = Colors.Blue6
            )
        }


        if(book.synopsis.isNotEmpty()) {
            Spacer(modifier = Modifier.size(20.dp))

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Synopsis",
                style = MaterialTheme.typography.bodySmall,
                color = Colors.Blue5
            )
            Text(
                text = book.synopsis,
                style = MaterialTheme.typography.bodyMedium,
                color = Colors.Blue6
            )
        }


        Spacer(modifier = Modifier.height(16.dp))
        Row{
            IconButton(
                onClick = { navController.navigate("${Screen.EditBook.route}/$bookId") },
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = Colors.OffWhite)
            }
            Spacer(modifier = Modifier.size(34.dp))
            IconButton(
                onClick = { showDeleteDialog = true },
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Colors.OffWhite)
            }
            Spacer(modifier = Modifier.size(34.dp))
            IconButton(
                onClick = {
                    showStatusChangeDialog = true
                          },
            ) {
                Icon(imageVector = Icons.Default.Build, contentDescription = null, tint = Colors.OffWhite)
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Book") },
            text = { Text("Are you sure you want to delete this book?") },
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

    if (showStatusChangeDialog) {

        var status by rememberSaveable {
            mutableStateOf(book.status)
        }
        var rating by remember { mutableStateOf(book.rating) }

        Dialog(
            onDismissRequest = {
                showStatusChangeDialog = false
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
                        showStatusChangeDialog = false
                        book = Book(
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
                        mainViewModel.selectBookDetails(bookId)
                    }
                    ) {
                    Text("Save")
                }

            }
            }
        )
    }
}
