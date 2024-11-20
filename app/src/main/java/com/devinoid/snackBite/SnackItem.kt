package com.devinoid.snackBite



import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Surface

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow

import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SnackItem(
    snack: Snack,
    modifier: Modifier = Modifier
) {


    Column(
        modifier = modifier
            .padding(8.dp)
            .shadow(elevation = 0.dp, shape = RoundedCornerShape(2.dp))

    ) {
        Surface(
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(16.dp)
        ) {


            ImageSlideshow(
                context = LocalContext.current,
                imageResIds = snack.imageResIds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            }
            Text(
                text = snack.title,
                modifier = Modifier.padding(top = 8.dp),
                fontFamily = FontFamily(Font(R.font.robotolight)),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = snack.description,
                fontFamily = FontFamily(Font(R.font.robotothin)),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 5.dp),
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                modifier = Modifier.padding(top = 5.dp)
            ) {

                //Check why this is in a Row
                Text(
                    text = snack.price,
                    modifier = Modifier.padding(top = 5.dp),
                    fontFamily = FontFamily(Font(R.font.robotobold)),
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold
                    )


                )
            }



    }


}


