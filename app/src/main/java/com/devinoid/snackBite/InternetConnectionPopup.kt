package com.devinoid.snackBite


import androidx.compose.foundation.layout.*


import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color



import androidx.compose.material3.ButtonDefaults


import androidx.compose.material3.AlertDialog

@Composable
fun InternetConnectionPopup(
    onExitApp: () -> Unit,
    onEnableInternet: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = "No Internet Connection") },
        text = { Text(text = "Please enable internet to use the app. Once you enable your internet, the app should automatically open, if not, click Exit App button and reopen app.") },
        modifier = modifier,
        confirmButton = {
//            Button(
//                onClick = onEnableInternet,
//                colors = ButtonDefaults.buttonColors(Color(0xFFFF8080))
//            ) {
//                Text(text = "Retry")
//            }
        },
        dismissButton = {
            Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onExitApp,
                colors = ButtonDefaults.buttonColors(Color(0xFFFF8080)),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(text = "Exit App")
            }
        }
        }
    )
}