package com.cc221043.ccl3_mobileapplications.ui.view

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.cc221043.ccl3_mobileapplications.R
import com.cc221043.ccl3_mobileapplications.data.model.Book
import com.cc221043.ccl3_mobileapplications.ui.theme.Colors
import com.cc221043.ccl3_mobileapplications.ui.view_model.MainViewModel

// Composable function for adding a book
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBook(mainViewModel: MainViewModel, navController: NavController, onPickImage: () -> Unit) {
    val state = mainViewModel.mainViewState.collectAsState()
    val genreArray = stringArrayResource(id = R.array.genres)
    val gradientColors = listOf(Colors.Blue1, Colors.Blue4, Colors.Blue1)

    val iconButtonColors =
        IconButtonDefaults.iconButtonColors(
            contentColor = Colors.Blue5,
            containerColor = Color.Transparent,
            disabledContentColor = Colors.Blue5,
            disabledContainerColor = Color.Transparent,
        )

    val inputFieldColors = TextFieldDefaults.textFieldColors(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        containerColor = Colors.Blue1,
        focusedLeadingIconColor = Colors.OffWhite,
        textColor = Colors.OffWhite,
        unfocusedLeadingIconColor = Colors.Blue5,
        focusedTrailingIconColor = Colors.OffWhite,
        placeholderColor = Colors.Blue5,
        unfocusedLabelColor = Colors.Blue5,
        focusedLabelColor = Colors.PrimaryBlue
    )
    val buttonColors = ButtonDefaults.buttonColors(
        contentColor = Colors.OffWhite,
        containerColor = Colors.PrimaryBlue,
        disabledContentColor = Colors.OffWhite,
        disabledContainerColor = Colors.Blue0,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(
                brush = Brush.verticalGradient(
                    colors = gradientColors,
                )
            )
            .padding(top = 16.dp, start = 14.dp, end = 14.dp)
    ) {
        // default values for all variables
        var title by rememberSaveable { mutableStateOf("") }
        var author by rememberSaveable { mutableStateOf("") }
        var platformat by rememberSaveable { mutableStateOf("") }
        var synopsis by rememberSaveable { mutableStateOf("") }
        var status by rememberSaveable { mutableStateOf("") }
        var rating by remember { mutableIntStateOf(0) }
        var selectedGenres by remember { mutableStateOf(emptyList<String>()) }
        var genresString by remember {
            mutableStateOf("")
        }

        var options = listOf("Not started", "In Progress", "Finished")

        val context = LocalContext.current

        // input fields for title, cover, status etc.
        TextField(
            singleLine = true,
            colors = inputFieldColors,
            shape = CircleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .shadow(shape = CircleShape, elevation = 6.dp)
                .border(BorderStroke(2.dp, color = Colors.PrimaryBlue), shape = CircleShape),
            value = title,
            onValueChange = { newText -> title = newText },
            label = { Text(text = "Title", modifier = Modifier.padding(horizontal = 6.dp), fontSize = 14.sp)
            })

        TextField(
            singleLine = true,
            colors = inputFieldColors,
            shape = CircleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .shadow(shape = CircleShape, elevation = 6.dp)
                .border(BorderStroke(2.dp, color = Colors.PrimaryBlue), shape = CircleShape),
            value = author,
            onValueChange = { newText -> author = newText },
            label = { Text(text = "Author (optional)", modifier = Modifier.padding(horizontal = 6.dp), fontSize = 14.sp) })

        // user can upload cover here
        Row (
            modifier = Modifier.padding(bottom = 20.dp)
        ){
            Column {
                Text(modifier = Modifier.padding(start= 6.dp), text = "Cover Image", fontSize = 14.sp, color = Colors.Blue5)
                Box(modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(200.dp)
                    .background(Colors.Blue5, RoundedCornerShape(8.dp))
                    .border(BorderStroke(2.dp, Colors.PrimaryBlue), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center) {
                    if (state.value.selectedImageURI != null) {
                        AsyncImage(
                            model = state.value.selectedImageURI,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    IconButton(
                        onClick = { onPickImage() },
                        modifier = Modifier
                            .border(BorderStroke(4.dp, Colors.PrimaryBlue), shape = CircleShape)
                            .background(color = Colors.Blue3, shape = CircleShape)
                            .padding(6.dp)
                            .shadow(4.dp, shape = CircleShape), // Apply CircleShape here
                        colors = iconButtonColors
                    ) {
                        // if condition to change icon based on whether an image has been uploaded already
                        if (state.value.selectedImageURI == Uri.parse("")) {
                            Icon(
                                painterResource(id = R.drawable.upload),
                                contentDescription = null,
                                tint = Colors.Blue6,
                                modifier = Modifier.size(30.dp)
                            )
                        } else {
                            Icon(
                                painterResource(id = R.drawable.update),
                                contentDescription = null,
                                tint = Colors.Blue6,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.size(14.dp))

            // status input
            Column( modifier = Modifier.fillMaxWidth()) {
                Text(modifier = Modifier.padding(start= 14.dp), text = "Reading Status", fontSize = 14.sp, color = Colors.Blue5)
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
            }
        }

        // if condition for enabling user rating if the book has been read already
        if (status == "Finished") {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .background(Colors.Blue1, RoundedCornerShape(14.dp))) {
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

        TextField(
            singleLine = true,
            colors = inputFieldColors,
            shape = CircleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .shadow(shape = CircleShape, elevation = 6.dp)
                .border(BorderStroke(2.dp, color = Colors.PrimaryBlue), shape = CircleShape),
            value = platformat,
            onValueChange = { newText -> platformat = newText },
            label = { Text(text = "Platform/Format (optional)", modifier = Modifier.padding(horizontal = 6.dp), fontSize = 14.sp) })

        TextField(
            maxLines = 6,
            colors = inputFieldColors,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .shadow(shape = RoundedCornerShape(28.dp), elevation = 6.dp)
                .border(
                    BorderStroke(2.dp, color = Colors.PrimaryBlue),
                    shape = RoundedCornerShape(28.dp)
                ),
            value = synopsis,
            onValueChange = { newText -> synopsis = newText },
            label = { Text(text = "Synopsis (optional)", modifier = Modifier.padding(horizontal = 6.dp), fontSize = 14.sp) })

        Text(modifier = Modifier.padding(start= 14.dp), text = "Genres: $genresString", fontSize = 14.sp, color = Colors.Blue5)

        // genre selection; user can select multiple genres
        LazyRow (modifier = Modifier.padding(bottom = 20.dp)){
            items(genreArray) { name ->
                GenreButton(
                    name = name,
                    isSelected = selectedGenres.contains(name),
                    onNameClicked = {
                        // if statement to either select or deselect
                        selectedGenres = if (selectedGenres.contains(name)) {
                            selectedGenres - name
                        } else {
                            selectedGenres + name
                        }
                        genresString = selectedGenres.toString().removePrefix("[").removeSuffix("]")
                    }
                )
            }
        }

        // buttons for saving/cancelling
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    contentColor = Colors.OffWhite,
                    containerColor = Color.Transparent,
                    disabledContentColor = Colors.OffWhite,
                    disabledContainerColor = Color.Transparent,
                ),
                onClick = { navController.navigateUp() }
            ) {
                Text(text = "Cancel", style = MaterialTheme.typography.bodySmall, textDecoration = TextDecoration.Underline)
            }

            Button(
                shape = RoundedCornerShape(12.dp),
                colors = buttonColors,
                onClick = {
                    // if condition to ensure all necessary inputs are there
                    if(title.isEmpty() || status.isEmpty() || state.value.selectedImageURI == Uri.parse("") || selectedGenres.isEmpty()) {
                        Toast.makeText(context, "Input the necessary information", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        val newBook = Book(
                            title = title,
                            author = author,
                            platformat = platformat,
                            rating = rating,
                            synopsis = synopsis,
                            status = status,
                            genres = selectedGenres
                        )

                        // navigation to the Details screen for the new book
                        mainViewModel.previousScreen(state.value.selectedScreen.route)
                        val insertedId = mainViewModel.saveBookAndImage(newBook)
                        navController.navigate("${Screen.BookDetails.route}/$insertedId")
                    }
                }) {
                Text(text = "Save book", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}