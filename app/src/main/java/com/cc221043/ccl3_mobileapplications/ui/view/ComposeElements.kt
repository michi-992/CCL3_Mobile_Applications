package com.cc221043.ccl3_mobileapplications.ui.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GenreButton(name: String, isSelected: Boolean, onNameClicked: () -> Unit) {
    Button(
        onClick = onNameClicked,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color.Green else Color.Gray,
        )
    ) {
        Text(text = name, color = Color.White)
    }
}