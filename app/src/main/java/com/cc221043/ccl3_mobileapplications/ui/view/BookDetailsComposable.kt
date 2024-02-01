package com.cc221043.ccl3_mobileapplications.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.cc221043.ccl3_mobileapplications.R
import com.cc221043.ccl3_mobileapplications.ui.theme.Colors
import com.cc221043.ccl3_mobileapplications.ui.view_model.MainViewModel



//, isMenuExpanded: MutableState<Boolean>, showDeleteDialog: Boolean



@Composable
fun BookDetails(mainViewModel: MainViewModel, navController: NavController, bookId: Int) {
    val state = mainViewModel.mainViewState.collectAsState()
    val book = state.value.selectedBook ?: return
    val gradientColors = listOf(Colors.Blue1, Colors.Blue4, Colors.Blue1)
//    var isMenuExpanded by remember { mutableStateOf(false) }
    val genreString = book.genres.joinToString().removePrefix("[").removePrefix(",").removeSuffix("]")

//    var showDeleteDialog by remember { mutableStateOf(false) }

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


//        Spacer(modifier = Modifier.height(16.dp))
//        IconButton(
//            onClick = { isMenuExpanded = !isMenuExpanded },
//        ) {
//            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
//        }

//        DropdownMenu(
//            expanded = isMenuExpanded,
//            onDismissRequest = { isMenuExpanded = false },
//            modifier = Modifier
//                .width(IntrinsicSize.Max)
//        ) {
//            DropdownMenuItem(
//                onClick = {
//                    isMenuExpanded = false
//                    navController.navigate("${Screen.EditBook.route}/$bookId")
//                },
//                text = { Text("Edit") },
//                enabled = true
//            )
//
//            DropdownMenuItem(
//                onClick = {
//                    isMenuExpanded = false
//                    showDeleteDialog = true
//                },
//                text = { Text("Delete") },
//                enabled = true
//            )
//        }
    }

//    if (showDeleteDialog) {
//        AlertDialog(
//            onDismissRequest = { showDeleteDialog = false },
//            title = { Text("Delete Book") },
//            text = { Text("Are you sure you want to delete this book?") },
//            confirmButton = {
//                Button(colors = ButtonDefaults.buttonColors(
//                    contentColor = Colors.OffWhite,
//                    containerColor = Colors.PrimaryBlue
//                ),
//                    onClick = {
//                        showDeleteDialog = false
//                        mainViewModel.deleteBook(book)
//                        navController.navigate(Screen.Home.route)
//                    }
//                ) {
//                    Text("Delete")
//                }
//            },
//            dismissButton = {
//                Button(onClick = { showDeleteDialog = false },
//                    modifier = Modifier
//                        .border(BorderStroke(2.dp, Colors.PrimaryBlue), shape = CircleShape),
//                    colors = ButtonDefaults.buttonColors(
//                        contentColor = Colors.OffWhite,
//                        containerColor = Color.Transparent
//                    )) {
//                    Text("Cancel")
//                }
//            }
//        )
//    }
}
