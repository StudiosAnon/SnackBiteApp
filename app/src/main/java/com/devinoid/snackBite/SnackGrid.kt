package com.devinoid.snackBite

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar


@Composable
fun SnackGrid(
    snacks: List<Snack>,
    modifier: Modifier = Modifier,
    userName: String
) {

    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    val greeting = when (currentHour) {
        in 0 .. 11 -> "Good morning"
        in 12 .. 17 -> "Good afternoon"
        in 18 .. 20 -> "Good evening"
        else -> "Good night"
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "$greeting, $userName",
            modifier = Modifier.padding(start = 16.dp, top = 2.dp),
            fontFamily = FontFamily(Font(R.font.robotolight)),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,

            color = Color(0xFFFF8080),

        )
        Text(
            text = "Delightful Bites, Delectable Delights",//Snacks:
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 2.dp),
            fontFamily = FontFamily(Font(R.font.robotothin)),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.Gray

        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp, 16.dp)
        ) {
            items(snacks) { recipe ->
                SnackItem(snack = recipe)
            }
        }
    }
}
