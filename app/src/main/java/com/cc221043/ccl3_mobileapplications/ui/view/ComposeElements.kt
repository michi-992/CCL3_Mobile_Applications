package com.cc221043.ccl3_mobileapplications.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.cc221043.ccl3_mobileapplications.ui.theme.Colors

@Composable
fun GenreButton(name: String, isSelected: Boolean, onNameClicked: () -> Unit) {
    val shadows = Shadow(Color.Black, offset = Offset(0.4f, 0.4f), blurRadius = 0.4f)

    Button(
        onClick = onNameClicked,
//        shape = RoundedCornerShape(6.dp),
        modifier = Modifier
            .padding(horizontal = 4.dp)
//            .graphicsLayer(shadow = shadows)
        ,
        contentPadding = PaddingValues(horizontal = 12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Colors.PrimaryBlue else Color.Transparent,
            contentColor = if(isSelected) Color.Transparent else Colors.OffWhite
        ),
        border = if(isSelected) BorderStroke(2.dp, Color.Transparent) else BorderStroke(2.dp, Colors.PrimaryBlue)
    ) {
        Text(text = name, color = Color.White,)
    }
}

