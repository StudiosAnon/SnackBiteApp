package com.devinoid.snackBite

import androidx.annotation.DrawableRes

data class Snack(
    val id: String,
    @DrawableRes val imageResIds: List<Int>,
    val title: String,
    val description: String,
    val price: String
)

