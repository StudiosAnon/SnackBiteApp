package com.devinoid.snackBite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text

import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.ui.unit.sp


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.AccountCircle

import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults

import androidx.compose.ui.text.font.FontWeight


import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import java.util.Locale


@Composable
fun UserNameInputPopup(
    textPass: String,
    onNameSubmitted: (String) -> Unit

) {

    var firstName by remember { mutableStateOf("") }
    val isfirstNameValid by remember { derivedStateOf { firstName.isNotBlank() } }
    var lastName by remember { mutableStateOf("") }
    val islastNameValid by remember { derivedStateOf { lastName.isNotBlank() } }
    var password by remember { mutableStateOf("") }
    val ispasswordValid by remember { derivedStateOf { password.equals(textPass, ignoreCase = false) } }

    Box(
        modifier = Modifier
            .fillMaxSize()
           // .background(color = Color(0xFFFF8080))
            .background(color = Color(0xCC000000))
            //.verticalScroll(scrollState)
            .clickable(onClick = { /* dismiss on background click */ }),
            //.padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth()
               // .background(color = Color(0xFFFF8080))
                //.verticalScroll(scrollState)
                .fillMaxHeight(),

           // shape = RoundedCornerShape(16.dp),
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))


                AsyncImage(
                    modifier = Modifier.size(124.dp),
                    model = R.drawable.splash_logo_three,
                    contentDescription = "App Logo"
                )

                Spacer(modifier = Modifier.height(8.dp))


                Text(

                    text = "Your name will be used to process your orders.",
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.robotolight)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = {  Row (

                       verticalAlignment = Alignment.CenterVertically,





                    ){
                        Icon(Icons.Default.Person,
                            contentDescription = "Üsername Icon",
                            tint = Color(0xFFFF8080))
                        Text("Enter Your First Name",

                        modifier = Modifier.padding(start = 16.dp),
                            color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.robotolight)),
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,)}
                            },
                    isError = !isfirstNameValid,
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Color.DarkGray,
                        focusedBorderColor = Color(0xFFFF8080),
                        unfocusedBorderColor = Color.Gray,
                    ),
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp),

                    textStyle = TextStyle(
                        fontFamily = FontFamily(Font(R.font.robotolight)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    // fontSize = 13.sp, // Increase the font size
                        color = Color.DarkGray
                )
                )
                if (!isfirstNameValid) {
                    Text(
                        text = "Please enter your first name",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Start).padding(start = 10.dp),
                        fontFamily = FontFamily(Font(R.font.robotolight)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,

                    )
                }
                //surname
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = {  Row (

                        verticalAlignment = Alignment.CenterVertically,





                        ){
                        Icon(Icons.Default.Person,
                            contentDescription = "Üsername Icon",
                            tint = Color(0xFFFF8080))
                        Text("Enter Your Last Name",

                            modifier = Modifier.padding(start = 16.dp),
                            color = Color.Gray,
                            fontFamily = FontFamily(Font(R.font.robotolight)),
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,)}
                    },
                    isError = !islastNameValid,
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Color.DarkGray,
                        focusedBorderColor = Color(0xFFFF8080),
                        unfocusedBorderColor = Color.Gray,
                    ),
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp),

                    textStyle = TextStyle(
                        fontFamily = FontFamily(Font(R.font.robotolight)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        // fontSize = 13.sp, // Increase the font size
                        color = Color.DarkGray
                    )
                )
                if (!islastNameValid) {
                    Text(
                        text = "Please enter your last name",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Start).padding(start = 10.dp),
                        fontFamily = FontFamily(Font(R.font.robotolight)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,

                        )
                }


                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = {  Row (

                        verticalAlignment = Alignment.CenterVertically,





                        ){
                        Icon(Icons.Default.AccountCircle,
                            contentDescription = "Password Icon",
                            tint = Color(0xFFFF8080))
                        Text("Enter Your Password",

                            modifier = Modifier.padding(start = 16.dp),
                            color = Color.Gray,
                            fontFamily = FontFamily(Font(R.font.robotolight)),
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,)}
                    },
                    isError = !ispasswordValid,
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Color.DarkGray,
                        focusedBorderColor = Color(0xFFFF8080),
                        unfocusedBorderColor = Color.Gray,
                    ),
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp),

                    textStyle = TextStyle(
                        fontFamily = FontFamily(Font(R.font.robotolight)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        // fontSize = 13.sp, // Increase the font size
                        color = Color.DarkGray
                    )
                )
                if (!ispasswordValid) {
                    Text(
                        text = "Please enter correct password",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Start).padding(start = 10.dp),
                        fontFamily = FontFamily(Font(R.font.robotolight)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,

                        )
                }
                Spacer(modifier = Modifier.height(28.dp))
                Button(
                    onClick = {
                        if (isfirstNameValid and islastNameValid) {
                            onNameSubmitted(firstName.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.getDefault()
                                ) else it.toString()
                            } +" "+ lastName.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.getDefault()
                                ) else it.toString()
                            })
                        }

                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFFFF8080)),
                    modifier = Modifier.fillMaxWidth().padding(start = 4.dp, end = 4.dp),
                    enabled = isfirstNameValid and islastNameValid and ispasswordValid

                ) {
                    Text("Done",
                        fontFamily = FontFamily(Font(R.font.robotoregular)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,)
                }
            }
        }
    }
}

