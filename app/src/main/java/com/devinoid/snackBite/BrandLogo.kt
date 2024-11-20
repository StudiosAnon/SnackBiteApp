package com.devinoid.snackBite

import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*


import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun BrandLogoWithOrderButton(
    onClickOrder: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),


        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        BrandLogo(modifier = Modifier.size(100.dp))

        Button(onClick = onClickOrder,
                colors = ButtonDefaults.buttonColors(Color(0xFFFF8080))




        ) {
            Text("Order Here",
                fontFamily = FontFamily(Font(R.font.robotoregular)),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,)
        }
    }
}

@Composable
fun BrandLogo(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.splash_logo_three),
        contentDescription = "Brand Logo",
        modifier = modifier
            .padding(start = 10.dp)


    )
}


