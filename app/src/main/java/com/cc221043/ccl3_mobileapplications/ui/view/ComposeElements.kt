package com.cc221043.ccl3_mobileapplications.ui.view

import android.graphics.BlurMaskFilter
import android.graphics.Typeface.NORMAL
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.cc221043.ccl3_mobileapplications.R
import com.cc221043.ccl3_mobileapplications.data.model.Book
import com.cc221043.ccl3_mobileapplications.ui.theme.Colors
import com.cc221043.ccl3_mobileapplications.ui.view_model.MainViewModel
import com.google.android.material.search.SearchBar

// Composable function for the genre pills
@Composable
fun GenreButton(name: String, isSelected: Boolean, onNameClicked: () -> Unit) {
    Button(
        onClick = onNameClicked,
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .shadow(elevation = 2.dp, CircleShape),
        contentPadding = PaddingValues(horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Colors.PrimaryBlue else Color.Transparent,
            contentColor = if(isSelected) Color.Transparent else Colors.OffWhite
        ),
        border = if(isSelected) BorderStroke(2.dp, Color.Transparent) else BorderStroke(2.dp, Colors.PrimaryBlue)
    ) {
        Text(text = name, color = Color.White, style = MaterialTheme.typography.bodySmall)
    }
}

// Composable function for the book grid
@Composable
fun BookGrid(navController: NavController, books: List<Book>) {
    Column (modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            modifier = Modifier
                .heightIn(min = 200.dp)
                .padding(top = 16.dp, end = 14.dp, start = 14.dp)
                .weight(1f)
                .fillMaxWidth(1f),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            columns = GridCells.Fixed(3),
            content = {
                items(books.size) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(154.dp)
                            .clickable {
                                navController.navigate("${Screen.BookDetails.route}/${books[it].id}")
                            }
                    ) {
                        // shows cover, no other info
                        AsyncImage(
                            modifier = Modifier.clip(RoundedCornerShape(6.dp)),
                            model = books[it].cover,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                items(3) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(3.0f)
                            .height(100.dp)
                    )
                }
            })
    }

}

// Composable function for ratings
@Composable
fun RatingBar(
    rating: Int, onRatingChanged: (Int) -> Unit, modifier: Modifier = Modifier, small: Boolean
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // for loop to generate rating icons, determines whether icon needs to be filled or not
        for (i in 1..5) {
            val isSelected = i <= rating
            Icon(
                painter = if (isSelected) painterResource(id = R.drawable.bat_filled) else painterResource(
                    id = R.drawable.battybatbat
                ),
                contentDescription = null,
                modifier = if (small) Modifier
                    .size(40.dp)
                    .clickable { onRatingChanged(i) } else Modifier
                    .size(50.dp)
                    .clickable { onRatingChanged(i) },
                tint = Colors.PrimaryBlue
            )
        }
    }
}

// displays circular loading indicator
@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.Blue0),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(100.dp),
            color = Colors.PrimaryBlue,
            strokeWidth = 15.dp)
    }
}