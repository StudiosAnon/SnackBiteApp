package com.devinoid.snackBite

import android.util.Log
import android.Manifest
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


import androidx.compose.material3.ButtonDefaults

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign

import androidx.navigation.NavController

@Composable
fun PermissionsHandler(
    shouldRequestPermissions: Boolean = true,
    onPermissionsGranted: () -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val areAllPermissionsGranted = permissions.all { it.value }
        if (areAllPermissionsGranted) {
            onPermissionsGranted()
        } else {
            //PermissionsErrorPopup()
            // Display an error message to the user
            Log.e("SnackOrderFragment", "Some permissions were denied")
        }
    }

    LaunchedEffect(shouldRequestPermissions) {
        if (shouldRequestPermissions) {
            val requiredPermissions = arrayOf(
                //Manifest.permission.SEND_SMS,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE
            )

            permissionLauncher.launch(requiredPermissions)
        }
    }
}


@Composable
fun PermissionsErrorPopup(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    arePermissionsGranted: Boolean,
    navController: NavController,
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xCC000000))
            .clickable(onClick = { /* dismiss on background click */ }),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(0.85f),
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(bottom = 18.dp, top = 18.dp, start = 8.dp, end = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Permissions Required",

                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (arePermissionsGranted) {
                    Text(
                        text = "All required permissions have been granted.",
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(Color(0xFFFF8080)),
                    ) {
                        Text("Proceed",
                            fontFamily = FontFamily(Font(R.font.robotoregular)),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,)
                    }
                } else {
                    Text(
                        text = "The app requires the following permissions to function properly:\n\n" +
                                "- Send SMS\n" +
                                "- Internet Access\n" +
                                "- Network State Access\n\n" +
                                "Please go to the app settings to grant these permissions.",
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(Color(0xFFFF8080)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Exit App",
                                fontFamily = FontFamily(Font(R.font.robotoregular)),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Button(
                            onClick = {
                                // Open the app settings

                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri = Uri.fromParts("package", context.packageName, null)
                                intent.data = uri
                                context.startActivity(intent)
                                navController.navigate("snackGrid")
                            },
                            colors = ButtonDefaults.buttonColors(Color(0xFFFF8080)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("App Settings",
                                textAlign = TextAlign.Center,
                                fontFamily = FontFamily(Font(R.font.robotoregular)),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,)
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun PermissionsErrorGrantSettingsPopup(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
   // val shouldShowPopup by remember { mutableStateOf(true) }

   // if (shouldShowPopup) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xCC000000))
                .clickable(onClick = { /* dismiss on background click */ }),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(0.85f),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(bottom = 18.dp, top = 18.dp, start = 8.dp, end = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Permissions Required",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "The app requires the following permissions to function properly:\n\n" +
                                "- Send SMS\n" +
                                "- Internet Access\n" +
                                "- Network State Access\n\n" +
                                "Please grant these permissions to continue using the app.",
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(Color(0xFFFF8080)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Exit App")
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Button(
                            onClick = onConfirm,
                            colors = ButtonDefaults.buttonColors(Color(0xFFFF8080)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Accept")
                        }
                    }
                }
            }
        }
    //}
}





/*
//Permission hANDLER #!
fun PermissionsHandler(
    shouldRequestPermissions: Boolean = true,
    onPermissionsGranted: () -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionsGranted()
        } else {
            // Display an error message to the user
            Log.e("SnackOrderFragment", "SMS permission denied")
        }
    }

    LaunchedEffect(shouldRequestPermissions) {
        if (shouldRequestPermissions) {
            val smsPermission = Manifest.permission.SEND_SMS
            when (ContextCompat.checkSelfPermission(context, smsPermission)) {
                PackageManager.PERMISSION_GRANTED -> {
                    onPermissionsGranted()
                }
                else -> {
                    permissionLauncher.launch(smsPermission)
                }
            }
        }
    }
}

 */