package com.devinoid.snackBite

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import androidx.compose.foundation.Image
import coil.ImageLoader
import coil.request.ImageRequest
import android.content.Context
import androidx.compose.animation.Crossfade

import androidx.compose.animation.core.tween


import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import coil.transform.RoundedCornersTransformation


import androidx.compose.material3.Surface

@Composable
fun ImageSlideshow(
    context: Context,
    imageResIds: List<Int>,
    modifier: Modifier = Modifier,
    delayMillis: Long = 8000,
    transitionDurationMillis: Int = 2000 // Duration of the transition animation
) {
    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = currentIndex) {
        while (true) {
            delay(delayMillis)
            currentIndex = (currentIndex + 1) % imageResIds.size
        }
    }

    Surface(modifier = modifier.fillMaxSize()) {
        Crossfade(
            targetState = imageResIds[currentIndex],
            animationSpec = tween(transitionDurationMillis), label = "CrossFade ImageSlideShow"
        ) { targetImageResId ->
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = targetImageResId)
                        .apply(block = fun ImageRequest.Builder.() {
                            transformations(RoundedCornersTransformation(8f))
                        }).build(), imageLoader = ImageLoader.Builder(context).build()
                ),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
        }
    }
}

