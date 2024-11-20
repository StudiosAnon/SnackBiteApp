package com.devinoid.snackBite



import androidx.compose.foundation.Image
import androidx.compose.foundation.background

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults

import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat

// For WhatsApp sharing

// For default messaging app sharing
import androidx.preference.PreferenceManager
import coil.compose.AsyncImage
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.net.URLEncoder
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.TimeZone


data class WebPageTexts(
    var text750ml: String,
    var text500ml: String,
    var text250ml: String,
)

@Composable
fun SnackOrderFragment(

    onBackPressed: () -> Unit,
    onOrderSubmitted: (Map<String, Int>) -> Unit,

    ) {

    //Section New

    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val connectionState by rememberConnectionState(context)


    var webPageTexts by remember { mutableStateOf(WebPageTexts("", "", "")) }
    val internetMessage = "No Internet Connection"


//    LaunchedEffect(connectionState) {
//        when (connectionState) {
//            ConnectionState.Connected -> {
//                fetchAndParseHtmlContentAsync()
//            }
//            ConnectionState.NotConnected -> {
//                webPageTexts = WebPageTexts(internetMessage, internetMessage, internetMessage)
//            }
//        }
//    }
    LaunchedEffect(connectionState) {
        when (connectionState) {
            ConnectionState.Connected -> {
                try {
                    if (checkStrings(
                            webPageTexts.text750ml,
                            webPageTexts.text500ml,
                            webPageTexts.text250ml,

                        )
                    ) {
                        webPageTexts = fetchAndParseHtmlContent(
                            WEBPAGE_URL = "https://sites.google.com/view/snackbite",
                            SPAN_STYLE_COLOR = "#990000"
                        )
                    }
                } catch (e: Exception) {
                    Log.e("SnackActivityOrderFragment", "Error fetching HTML content: ${e.message}", e)
                }
            }

            ConnectionState.NotConnected -> {
                webPageTexts = WebPageTexts(internetMessage, internetMessage, internetMessage)
            }
        }
    }

    val (orderQuantities, setOrderQuantities) = remember { mutableStateOf(emptyMap<String, Int>()) }
    //val (saltiness, setSaltiness) = remember { mutableStateOf(0f) }
    // val (pepperiness, setPepperiness) = remember { mutableStateOf(0f) }
    // var goToShareOrder = remember { mutableStateOf(false) }






    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(
                    start = 4.dp,
                    end = 4.dp,
                    top = 16.dp,
                    bottom = 100.dp // Increase bottom padding to make room for the bottom bar
                )
        ) {
            Row(
                modifier = Modifier//.border(BorderStroke(2.dp,Color.Black))
                    .fillMaxWidth()
                    .padding(top = 2.dp, bottom = 8.dp)
                    .clickable { onBackPressed() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.backsign),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(start = 8.dp)

                )
                Text(
                    "Snack Order Menu",
                    fontFamily = FontFamily(Font(R.font.robotolight)),
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    modifier = Modifier

                        .fillMaxWidth()
                        .padding(start = 52.dp)

                )



            }

            OrderItem(
                title = "Fried Channa",
                price = 28.00,
                //saltinessOptions = listOf("None", "Slight", "Medium", "High"),
                //peppernessOptions = listOf("None", "Slight", "Medium", "High"),
                //saltiness = saltiness,
                // pepperiness = pepperiness,
                // onSaltnessChange = { setSaltiness(it) },
                //onPeppernessChange = { setPepperiness(it) },
                onQuantityChange = { quantity ->

                    setOrderQuantities(orderQuantities.toMutableMap().apply {
                        if (quantity > 0) {
                            this["Fried Channa"] = quantity
                        } else {
                            remove("Fried Channa")
                        }

                    })
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            OrderItem(
                title = "Fried Peanuts",
                price = 35.00,
                //saltinessOptions = listOf("None", "Slight", "Medium", "High"),
                //saltiness = saltiness,
                //onSaltnessChange = { setSaltiness(it) },
                onQuantityChange = { quantity ->
                    setOrderQuantities(orderQuantities.toMutableMap().apply {
                        if (quantity > 0) {
                            this["Fried Peanuts"] = quantity
                        } else {
                            remove("Fried Peanuts")
                        }

                    })
                }
            )

            // Add more order items here

            Spacer(modifier = Modifier.height(24.dp))

            OrderItem(
                title = "Fudge",
                price = 8.00,
                onQuantityChange = {quantity ->
                        setOrderQuantities(orderQuantities.toMutableMap().apply {
                            if (quantity > 0) {
                                this["Fudge"] = quantity
                            } else {
                                remove("Fudge")
                            }
                        })

                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            OrderItem(
                title = "Banana Bread",
                price = 60.00,
                onQuantityChange = { quantity ->
                    setOrderQuantities(orderQuantities.toMutableMap().apply {
                        if (quantity > 0) {
                            this["Banana Bread"] = quantity
                        } else {
                            remove("Banana Bread")
                        }

                    })
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            OrderItem(
                title = "Fried Accra",
                price = 6.00,
                onQuantityChange = { quantity ->
                    setOrderQuantities(orderQuantities.toMutableMap().apply {
                        if (quantity > 0) {
                            this["Fried Accra"] = quantity
                        } else {
                            remove("Fried Accra")
                        }

                    })
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            OrderItem(
                title = "Kurma",
                price = 7.00,
                onQuantityChange = { quantity ->
                    setOrderQuantities(orderQuantities.toMutableMap().apply {
                        if (quantity > 0) {
                            this["Kurma"] = quantity
                        } else {
                            remove("Kurma")
                        }

                    })
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            OrderItem(
                title = "Fried Split Peas",
                price = 35.00,
                onQuantityChange = { quantity ->
                    setOrderQuantities(orderQuantities.toMutableMap().apply {
                        if (quantity > 0) {
                            this["Fried Split Peas"] = quantity
                        } else {
                            remove("Fried Split Peas")
                        }



                    })
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            OrderItem(
                title = "Bottled Roucou 750ml",
                price = 50.0,
                availablilty = webPageTexts.text750ml,
                onQuantityChange = { quantity ->
                    setOrderQuantities(orderQuantities.toMutableMap().apply {
                        if (quantity > 0) {
                            this["Bottled Roucou 750ml"] = quantity
                        } else {
                            remove("Bottled Roucou 750ml")
                        }

                    })
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            OrderItem(
                title = "Bottled Roucou 500ml",
                price = 35.0,
                availablilty = webPageTexts.text500ml,
                onQuantityChange = { quantity ->
                    setOrderQuantities(orderQuantities.toMutableMap().apply {
                        if (quantity > 0) {
                            this["Bottled Roucou 500ml"] = quantity
                        } else {
                            remove("Bottled Roucou 500ml")
                        }

                    })
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            OrderItem(
                title = "Bottled Roucou 250ml",
                price = 15.0,
                availablilty = webPageTexts.text250ml,
                onQuantityChange = { quantity ->
                    setOrderQuantities(orderQuantities.toMutableMap().apply {
                        if (quantity > 0) {
                            this["Bottled Roucou 250ml"] = quantity
                        } else {
                            remove("Bottled Roucou 250ml")
                        }

                    })
                }
            )

            // Add your existing order items here

            Spacer(modifier = Modifier.height(4.dp))

            Spacer(modifier = Modifier.height(48.dp))
        }
        //orderQuantities.filterValues { it != 0 }




        BottomBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .absoluteOffset(y = (-50).dp), // Adjust the offset value as needed
            orderQuantities = orderQuantities,
            onOrderSubmitted = onOrderSubmitted
        )
    }
}

//@Composable
//fun SnackOrderFragment(
//
//    onBackPressed: () -> Unit,
//    onOrderSubmitted: (Map<String, Int>) -> Unit,
//
//    ) {
//
//    //Section New
//
//    val scrollState = rememberScrollState()
//    val context = LocalContext.current
//    val connectionState by rememberConnectionState(context)
//
//
//    var webPageTexts by remember { mutableStateOf(WebPageTexts("", "", "")) }
//    val internetMessage = "No Internet Connection"
//
//
////    LaunchedEffect(connectionState) {
////        when (connectionState) {
////            ConnectionState.Connected -> {
////                fetchAndParseHtmlContentAsync()
////            }
////            ConnectionState.NotConnected -> {
////                webPageTexts = WebPageTexts(internetMessage, internetMessage, internetMessage)
////            }
////        }
////    }
//LaunchedEffect(connectionState) {
//    when (connectionState) {
//        ConnectionState.Connected -> {
//            try {
//                if (checkStrings(
//                        webPageTexts.text750ml,
//                        webPageTexts.text500ml,
//                        webPageTexts.text250ml
//                    )
//                ) {
//                    webPageTexts = fetchAndParseHtmlContent(
//                        WEBPAGE_URL = "https://sites.google.com/view/snackbite",
//                        SPAN_STYLE_COLOR = "#990000"
//                    )
//                }
//            } catch (e: Exception) {
//                Log.e("SnackActivityOrderFragment", "Error fetching HTML content: ${e.message}", e)
//            }
//        }
//
//        ConnectionState.NotConnected -> {
//            webPageTexts = WebPageTexts(internetMessage, internetMessage, internetMessage)
//        }
//    }
//}
//
//    val (orderQuantities, setOrderQuantities) = remember { mutableStateOf(emptyMap<String, Int>()) }
//    //val (saltiness, setSaltiness) = remember { mutableStateOf(0f) }
//   // val (pepperiness, setPepperiness) = remember { mutableStateOf(0f) }
//   // var goToShareOrder = remember { mutableStateOf(false) }
//
//
//
//
//
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(scrollState)
//                .padding(
//                    start = 4.dp,
//                    end = 4.dp,
//                    top = 16.dp,
//                    bottom = 100.dp // Increase bottom padding to make room for the bottom bar
//                )
//        ) {
//            Row(
//                modifier = Modifier//.border(BorderStroke(2.dp,Color.Black))
//                    .fillMaxWidth()
//                    .padding(top = 2.dp, bottom = 8.dp)
//                    .clickable { onBackPressed() },
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.backsign),
//                    contentDescription = "Back",
//                    modifier = Modifier
//                        .size(64.dp)
//                        .padding(start = 8.dp)
//
//                )
//                Text(
//                    "Snack Order Menu",
//                    fontFamily = FontFamily(Font(R.font.robotolight)),
//                    fontWeight = FontWeight.Normal,
//                    fontSize = 18.sp,
//                    modifier = Modifier
//
//                        .fillMaxWidth()
//                        .padding(start = 52.dp)
//
//                )
//
//
//
//            }
//
//            OrderItem(
//                title = "Fried Channa",
//                price = 28.00,
//                //saltinessOptions = listOf("None", "Slight", "Medium", "High"),
//                //peppernessOptions = listOf("None", "Slight", "Medium", "High"),
//                //saltiness = saltiness,
//                // pepperiness = pepperiness,
//                // onSaltnessChange = { setSaltiness(it) },
//                //onPeppernessChange = { setPepperiness(it) },
//                onQuantityChange = { quantity ->
//                    setOrderQuantities(orderQuantities.toMutableMap().apply {
//                        this["Fried Channa"] = quantity
//                    })
//                }
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            OrderItem(
//                title = "Fried Peanuts",
//                price = 35.00,
//                //saltinessOptions = listOf("None", "Slight", "Medium", "High"),
//                //saltiness = saltiness,
//                //onSaltnessChange = { setSaltiness(it) },
//                onQuantityChange = { quantity ->
//                    setOrderQuantities(orderQuantities.toMutableMap().apply {
//                        this["Fried Peanuts"] = quantity
//                    })
//                }
//            )
//
//            // Add more order items here
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            OrderItem(
//                title = "Fudge",
//                price = 8.00,
//                onQuantityChange = { quantity ->
//                    setOrderQuantities(orderQuantities.toMutableMap().apply {
//                        this["Fudge"] = quantity
//                    })
//                }
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            OrderItem(
//                title = "Banana Bread",
//                price = 60.00,
//                onQuantityChange = { quantity ->
//                    setOrderQuantities(orderQuantities.toMutableMap().apply {
//                        this["Banana Bread"] = quantity
//                    })
//                }
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            OrderItem(
//                title = "Fried Accra",
//                price = 6.00,
//                onQuantityChange = { quantity ->
//                    setOrderQuantities(orderQuantities.toMutableMap().apply {
//                        this["Fried Accra"] = quantity
//                    })
//                }
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            OrderItem(
//                title = "Kurma",
//                price = 7.00,
//                onQuantityChange = { quantity ->
//                    setOrderQuantities(orderQuantities.toMutableMap().apply {
//                        this["Kurma"] = quantity
//                    })
//                }
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            OrderItem(
//                title = "Fried Split Peas",
//                price = 35.00,
//                onQuantityChange = { quantity ->
//                    setOrderQuantities(orderQuantities.toMutableMap().apply {
//                        this["Fried Split Peas"] = quantity
//                    })
//                }
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            OrderItem(
//                title = "Bottled Roucou 750ml",
//                price = 50.0,
//                availablilty = webPageTexts.text750ml,
//                onQuantityChange = { quantity ->
//                    setOrderQuantities(orderQuantities.toMutableMap().apply {
//                        this["Bottled Roucou 750ml"] = quantity
//                    })
//                }
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            OrderItem(
//                title = "Bottled Roucou 500ml",
//                price = 35.0,
//                availablilty = webPageTexts.text500ml,
//                onQuantityChange = { quantity ->
//                    setOrderQuantities(orderQuantities.toMutableMap().apply {
//                        this["Bottled Roucou 500ml"] = quantity
//                    })
//                }
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            OrderItem(
//                title = "Bottled Roucou 250ml",
//                price = 15.0,
//                availablilty = webPageTexts.text250ml,
//                onQuantityChange = { quantity ->
//                    setOrderQuantities(orderQuantities.toMutableMap().apply {
//                        this["Bottled Roucou 250ml"] = quantity
//                    })
//                }
//            )
//
//            // Add your existing order items here
//
//            Spacer(modifier = Modifier.height(4.dp))
//
//            Spacer(modifier = Modifier.height(48.dp))
//        }
//
//        BottomBar(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .absoluteOffset(y = (-50).dp), // Adjust the offset value as needed
//            orderQuantities = orderQuantities,
//            onOrderSubmitted = onOrderSubmitted
//        )
//    }
//}


@Composable
fun rememberConnectionState(context: Context): State<ConnectionState> {
    val connectionState = remember { mutableStateOf<ConnectionState>(ConnectionState.NotConnected) }

    DisposableEffect(context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                connectionState.value = ConnectionState.Connected
            }

            override fun onLost(network: Network) {
                connectionState.value = ConnectionState.NotConnected
            }
        }

        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build(),
            networkCallback
        )

        onDispose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

    return connectionState
}

//private fun fetchAndParseHtmlContentAsync() {
//    viewModelScope.launch(Dispatchers.IO) {
//        try {
//            val fetchedWebPageTexts = fetchAndParseHtmlContent(
//                WEBPAGE_URL = "https://sites.google.com/view/snackbite",
//                SPAN_STYLE_COLOR = "#990000"
//            )
//            withContext(Dispatchers.Main) {
//                webPageTexts = fetchedWebPageTexts
//            }
//        } catch (e: Exception) {
//            Log.e("SnackActivityOrderFragment", "Error fetching HTML content: ${e.message}", e)
//        }
//    }
//}

sealed class ConnectionState {
    object Connected : ConnectionState()
    object NotConnected : ConnectionState()
}


fun checkStrings(str1: String, str2: String, str3: String): Boolean {
    return str1.isEmpty() || str1 == "No Internet Connection" &&
            str2.isEmpty() || str2 == "No Internet Connection" &&
            str3.isEmpty() || str3 == "No Internet Connection"
}




@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    orderQuantities: Map<String, Int>,
    onOrderSubmitted: (Map<String, Int>) -> Unit
) {

    //orderQuantities.filterValues { it != 0 }
    //var goToShareOrder by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {

        } else {
            // Display an error message to the user
            Log.e("SnackOrderFragment", "SMS permission denied")
        }
    }

    val totalCost = orderQuantities.entries.sumOf { (item, quantity) ->
        when (item) {
            "Fried Channa" -> 28.0 * quantity
            "Fried Peanuts" -> 35.0 * quantity
            "Fudge" -> 8.0 * quantity
            "Banana Bread" -> 60.0 * quantity
            "Fried Accra" -> 6.0 * quantity
            "Kurma" -> 7.0 * quantity
            "Fried Split Peas" -> 25.0 * quantity
            "Bottled Roucou 750ml" -> 50.0 * quantity
            "Bottled Roucou 500ml" -> 35.0 * quantity
            "Bottled Roucou 250ml" -> 15.0 * quantity
            else -> 0.0
        }

    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(84.dp)
            .shadow(elevation = 12.dp, shape = RoundedCornerShape(24.dp))
            .padding(start = 4.dp, end = 4.dp)
            .background(Color(0xFFFF8080), shape = RoundedCornerShape(16.dp)),


        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column() {
            Text(
                "Total Price",
                modifier = Modifier.padding(bottom = 0.dp, start = 24.dp),
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.robotolight)),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,

                )
            Text(
                "$${totalCost}0",

                modifier = Modifier.padding(bottom = 0.dp, start = 24.dp),
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.robotoblack)),
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,

                )

        }

        var isShowingConfirmPopup by remember { mutableStateOf(false) }
        Button(modifier = Modifier.padding(end = 16.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(2.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFFFFFFF)),
                shape = RoundedCornerShape(24.dp),

            onClick = {
                //checks if order was made, if not confirm order popup does not open
                if(totalCost != 0.0) {
                    isShowingConfirmPopup = true
                }


               // checkAndRequestSmsPermission(permissionLauncher, orderQuantities, context, "$${totalCost}0")
                //onOrderSubmitted(orderQuantities)


                 }) {

            Text("Make Order",
                modifier = Modifier.padding(4.dp),
                color = Color(0xFFFF8080),
                fontFamily = FontFamily(Font(R.font.robotolight)),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,

                )

            if(isShowingConfirmPopup) {
                ConfirmOrderDatePopup(
                    onConfirm = { confirmedDate -> checkAndRequestSmsPermission(permissionLauncher, orderQuantities, context, "$${totalCost}0", confirmedDate); onOrderSubmitted(orderQuantities);    }, onDismiss = { isShowingConfirmPopup = false }, orderQuantities, "$${totalCost}0")
            }


        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmOrderDatePopup(
    onConfirm: (date: String) -> Unit,
    onDismiss: () -> Unit,
    orderQuantities: Map<String, Int>,
    totalCost: String,
) {

    val dateState = rememberDatePickerState()
    val selectedDate = dateState.selectedDateMillis?.let {
        Instant.ofEpochMilli(it).atZone(TimeZone.getDefault().toZoneId()).toLocalDate()
    }
    var dateToString =
        selectedDate?.plusDays(1)?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) ?: "Choose Date"


    var showDialog by remember { mutableStateOf(false) }


    AlertDialog(
        onDismissRequest = {},
        title = {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//
//                AsyncImage(
//                    modifier = Modifier.size(80.dp),
//                    model = R.drawable.splash_logo_one,
//                    contentDescription = "App Logo"
//                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    AsyncImage(
                        modifier = Modifier.size(80.dp),
                        model = R.drawable.splash_logo_one,
                        contentDescription = "App Logo"
                    )
//                    Text(
//                        text = "Confirm Order",
//                        fontSize = 24.sp,
//                        fontWeight = FontWeight.Bold,
//                        fontFamily = FontFamily(Font(R.font.robotoregular)),
//                        color = Color(0xFFFF8080)
//
//
//                        )

                }
          //  }
                },



        text = {
            Column(modifier = Modifier
                .fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Receipt",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.robotoregular)),
                    color = Color(0xFFFF8080)

                )

            }

          //here

            Row(
                modifier = Modifier.padding(top = 10.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                //horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Click Calendar for Delivery Date: ",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.robotoregular)),
                    color = Color(0xFFFF8080)

                )



                Icon(
                    Icons.Default.DateRange,
                    modifier = Modifier
                        .padding(start = 4.dp)//fillMaxWidth()
                        .clickable(onClick = {
                            showDialog = true
                        }),
                    contentDescription = "Calendar Icon",
                    tint = Color(0xFFFF8080)
                )



            }

            Row(
                modifier = Modifier.padding(top = 10.dp).fillMaxWidth(),
                //horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
//                    modifier = Modifier
//                        .padding(start = 4.dp)//fillMaxWidth()
//                        .clickable(onClick = {
//                            showDialog = true
//                        }),
                    text = "Delivery Date: $dateToString",
                    //textAlign = TextAlign.Center,
                    fontSize = 12.sp
                    // style = MaterialTheme.typography.headlineMedium
                )

                if (showDialog) {
                    DatePickerDialog(
                        onDismissRequest = { showDialog = false },
                        confirmButton = {
                            Button(
                                onClick = { showDialog = false },
                                colors = ButtonDefaults.buttonColors(Color(0xFFFF8080))
                            ) {
                                Text(text = "OK")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { showDialog = false },
                                colors = ButtonDefaults.buttonColors(Color(0xFFFF8080))
                            ) {
                                Text(text = "Cancel")
                            }
                        }
                    ) {
                        DatePicker(
                            state = dateState,
                            showModeToggle = true
                        )
                    }
                }

            }
        }



            // Spacer(modifier = Modifier.height(20.dp))
            val scrollState = rememberScrollState()
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp)
                .verticalScroll(scrollState),
                //.border(BorderStroke(2.dp, Color.Black)),

                //horizontalArrangement = Arrangement.SpaceEvenly

                ) {

                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = "Please review and confirm your order, or go back to make adjustments. When Confirm Order button is clicked, you'll be navigated to Whatsapp or SMS. Your Order will already be loaded just press send.",
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily(Font(R.font.robotothin))
                )

                Text(
                    modifier = Modifier.padding(top = 20.dp, bottom = 10.dp).fillMaxWidth(),
                    text = "Order Details",
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.robotothin))
                )
                orderQuantities.forEach { (item, quantity) ->
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween) {


                    Text(
                        text = "$quantity  $item",
                        modifier = Modifier.padding(start = 4.dp),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.robotolight)),
                        fontWeight = FontWeight.Normal,
                        //textAlign = TextAlign.Start
                    )
                    var price = 0.0
                    when (item) {
                        "Fried Channa" -> price =  28.0 * quantity
                        "Fried Peanuts" -> price = 35.0 * quantity
                        "Fudge" -> price = 8.0 * quantity
                        "Banana Bread" -> price = 60.0 * quantity
                        "Fried Accra" -> price = 6.0 * quantity
                        "Kurma" -> price = 7.0 * quantity
                        "Fried Split Peas" -> price = 25.0 * quantity
                        "Bottled Roucou 750ml" -> price = 50.0 * quantity
                        "Bottled Roucou 500ml" -> price = 35.0 * quantity
                        "Bottled Roucou 250ml" -> price = 15.0 * quantity
                        //else -> 0.0
                    }
                    Text(
                        text = "$${price}0",
                        modifier = Modifier.padding(end = 4.dp),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.robotoregular)),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    )


                    //Spacer(modifier = Modifier.height(8.dp))


                }
                }

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "Total Cost: ",
                        modifier = Modifier.padding(end = 4.dp),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.robotoregular)),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    )

                    Text(
                        text = totalCost,
                        modifier = Modifier.padding(end = 4.dp),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.robotoregular)),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    )
                }
            }
            //Spacer(modifier = Modifier.height(12.dp))
//            Text(
//                text = "Total Cost: $totalCost",
//                fontSize = 16.sp,
//                fontFamily = FontFamily(Font(R.font.robotoregular)),
//                fontWeight = FontWeight.Bold,
//                //textAlign = TextAlign.Right
//            )
               },
        modifier = Modifier,

        confirmButton = {
            Button(
                modifier = Modifier.padding(start = 8.dp),
                onClick = { if(dateToString == "Choose Date") {
                    dateToString = "No Date Chosen"
                }

                    onConfirm(dateToString) },
                colors = ButtonDefaults.buttonColors(Color(0xFFFF8080))
            ) {
                Text(text = "Confirm Order")
            }
        },
        dismissButton = {
           // Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(Color(0xFFFF8080)),
                    //modifier = Modifier.align(Alignment.Center)
                ) {
                    Text(text = "Go Back")
                }
          //  }
        }

    )


}




private fun fetchAndParseHtmlContent(WEBPAGE_URL: String, SPAN_STYLE_COLOR: String): WebPageTexts  {

    var webPageTexts = WebPageTexts("", "", "")
    val thread = Thread {
        try {
            val htmlContent = fetchHtmlFromUrl(WEBPAGE_URL)
            val notAvailableText = HTMLParser.getNotAvailableText(htmlContent, SPAN_STYLE_COLOR)




            if (notAvailableText.size == 3) {
                val text750ml = notAvailableText[0]
                val text500ml = notAvailableText[1]
                val text250ml = notAvailableText[2]




                // Use the extracted text in your SnackOrderFragment as needed

                    webPageTexts = WebPageTexts(
                        text750ml = text750ml,
                        text500ml = text500ml,
                        text250ml = text250ml,

                    )

                Log.d("MainActivity", "750MLWebPage: $webPageTexts.text750ml")

                Log.d("MainActivity", "750ML: $text750ml")
                Log.d("MainActivity", "500ML: $text500ml")
                Log.d("MainActivity", "250ML: $text250ml")
            } else {
                // Handle the case when the expected number of "NOT AVAILABLE" texts is not found
                Log.e("MainActivity", "Error: Unable to extract 'NOT AVAILABLE' text from HTML")
            }

        } catch (e: Exception) {
            Log.e("MainActivity", "Error fetching HTML content: ${e.message}")
        }
    }
        thread.start()
    thread.join()
     return webPageTexts
}

private fun fetchHtmlFromUrl(url: String): String {
    val client = OkHttpClient()
    val request = Request.Builder().url(url).build()
    val response = client.newCall(request).execute()
    return response.body?.string() ?: ""
}

object HTMLParser {


    fun getNotAvailableText(html: String, spanStyleColor: String): Array<String> {
        val notAvailableText = arrayOf("", "", "")
        try {
            val document: Document = Jsoup.parse(html)
            val spanElements: Elements = document.select("span[style*=$spanStyleColor]")
            if (spanElements.size == 3) {
                notAvailableText[0] = spanElements[0].text()
                notAvailableText[1] = spanElements[1].text()
                notAvailableText[2] = spanElements[2].text()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return notAvailableText
    }
}

//Order over User Messaging App

//@Composable
private fun checkAndRequestSmsPermission(permissionLauncher: ActivityResultLauncher<String>,orderQuantities: Map<String, Int>, context: Context, totalCost: String, confirmedDate: String) {
   // val context = LocalContext.current
    shareOrderDetails(orderQuantities, context, totalCost, confirmedDate)
//    val smsPermission = Manifest.permission.SEND_SMS
//    when (ContextCompat.checkSelfPermission(context, smsPermission)) {
//        PackageManager.PERMISSION_GRANTED -> {
//
//            shareOrderDetails(orderQuantities, context, totalCost, confirmedDate)
//        }
//        else -> {
//            permissionLauncher.launch(smsPermission)
//        }
//    }
}


private fun shareOrderDetails(orderQuantities: Map<String, Int>, context: Context, totalCost: String, confirmedDate: String) {

    try {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val userName = sharedPreferences.getString("userName", "") ?: ""
        val businessPhone = "18682637554"
        //395 - 0759

        val message = "SnackBite Receipt\n\nDelivery Date: $confirmedDate\n\nTotal: $totalCost\n\nCustomer: $userName\n\nOrder Details:\n\nSnacks Ordered: ${orderQuantities.size}\n\n" + orderQuantities.entries.joinToString("\n") { (item, quantity) ->
                "$item: $quantity"

        }

        val encodedMessage = URLEncoder.encode(message, "UTF-8")


        try {
            val url = "https://wa.me/$businessPhone?text=$encodedMessage"
            val whatsAppIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

            whatsAppIntent.`package` = "com.whatsapp"

            context.startActivity(whatsAppIntent)
        } catch (e: Exception) {

            try {
                val url = "https://wa.me/$businessPhone?text=$encodedMessage"
                val whatsAppBusinessIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

                whatsAppBusinessIntent.`package` = "com.whatsapp.w4b"

                context.startActivity(whatsAppBusinessIntent)

             } catch (e: Exception) {
                val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("smsto:$businessPhone")
                    putExtra(Intent.EXTRA_TEXT, message)


            }
                Log.d("ShareOrderDetails", "Creating SMS intent: $smsIntent")
                ContextCompat.startActivity(context, Intent.createChooser(smsIntent, null), null)
        }

        }

    } catch (e: Exception) {
        Log.e("SnackOrderFragment", "Error sharing order details: ${e.message}", e)
        // Display an error message to the user
    }

}

//End

//@Composable
//fun OrderItem(
//    title: String,
//    availablilty: String = "",
//    saltinessOptions: List<String> = emptyList(),
//    peppernessOptions: List<String> = emptyList(),
//    saltiness: (Float) = 0F,
//    pepperiness: (Float) = 0F,
//    onSaltnessChange: (Float) -> Unit = {},
//    onPeppernessChange: (Float) -> Unit = {},
//    price: Double = 0.0,
//    onQuantityChange: (Int) -> Unit,
//
//    ) {


@Composable
fun OrderItem(
    title: String,
    availablilty: String = "",
    price: Double = 0.0,
    onQuantityChange: (Int) -> Unit,

) {


    var quantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(shadowElevation = 8.dp,
            shape = RoundedCornerShape(8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()

                    .height(100.dp)
                    .clip(RoundedCornerShape(8.dp)),

                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shadowElevation = 12.dp,
                    shape = RoundedCornerShape(8.dp))  {
                    Image( //Image modify
                        painter = painterResource(
                            id = when (title) {
                                "Fried Channa" -> R.drawable.fried_channa2
                                "Fried Peanuts" -> R.drawable.peanuts1
                                "Fudge" -> R.drawable.fudge3
                                "Banana Bread" -> R.drawable.bananabread2
                                "Fried Accra" -> R.drawable.accra5
                                "Kurma" -> R.drawable.kurma2
                                "Fried Split Peas" -> R.drawable.splitpeas2
                                "Bottled Roucou 750ml" -> R.drawable.roucou2
                                "Bottled Roucou 500ml" -> R.drawable.roucou2
                                "Bottled Roucou 250ml" -> R.drawable.roucou2

                                // Add more cases for other items
                                else -> R.drawable.shirleylogo
                            }
                        ),
                        contentDescription = title,
                        modifier = Modifier
                            .fillMaxWidth(0.3f)
                            .fillMaxHeight()

                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Fit
                    )
                }

                Column {

                    Row(modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically

                    ) {
                        Text(
                            title,
                            modifier = Modifier.padding(top = 2.dp, start = 14.dp),
                            fontFamily = FontFamily(Font(R.font.robotoregular)),
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                        )



                        Text(
                            "$${price}0",
                            modifier = Modifier
                                .padding(top = 2.dp, end = 6.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.End,
                            fontFamily = FontFamily(Font(R.font.robotoregular)),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            //fontWeight = FontWeight.SemiBold,
                            color = Color.DarkGray
                        )




                    }

                    Text(
                        availablilty,
                        modifier = Modifier
                            .padding(top = 2.dp, end = 6.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.robotoregular)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        //fontWeight = FontWeight.SemiBold,
                        color = Color(0x99990000)
                    )

                   // AddedToOrderImage(quantity.toIntOrNull() ?: 0)
                    /*
                    if (saltinessOptions.isNotEmpty()) {
                        // Spacer(modifier = Modifier.height(2.dp))
                        Text("Saltiness:")
                        Spacer(modifier = Modifier.height(4.dp))
                        Slider(
                            //1F - saltiness
                            value = saltiness,
                            onValueChange = onSaltnessChange,
                            modifier = Modifier.fillMaxWidth(),
                            valueRange = 0f..3f,
                            steps = 3,
                            onValueChangeFinished = {
                                // Handle final saltiness value
                            }
                        )
                    }
                    if (peppernessOptions.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Pepperiness:")
                        Spacer(modifier = Modifier.height(4.dp))
                        Slider(
                            //2F - pepperiness
                            value = pepperiness,
                            onValueChange = onPeppernessChange,
                            modifier = Modifier.fillMaxWidth(),
                            valueRange = 0f..3f,
                            steps = 3,
                            onValueChangeFinished = {
                                // Handle final pepperiness value
                            }
                        )
                    }

                     */
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 14.dp, bottom = 2.dp, top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if (price > 0) {

                            Text(
                                "Quantity: ",
                                color = Color.DarkGray
                                )

                            OutlinedTextField(
                                value = quantity,


//                                onValueChange = { newQuantity ->
//                                    val filteredQuantity = newQuantity.filter { it.isDigit() }
//                                    onQuantityChange(filteredQuantity.toIntOrNull() ?: 0)
//                                },

//                                onValueChange = { newQuantity ->
//                                    val filteredQuantity = newQuantity.filter { it.isDigit() }
//                                    if ((filteredQuantity.toIntOrNull() ?: 0) <= 100) {
//                                        onQuantityChange(filteredQuantity.toIntOrNull() ?: 0)
//                                    } else {
//                                        onQuantityChange(100)
//                                    }
//                                },


                                onValueChange =  { newQuantity ->
                                    quantity = newQuantity
                                    onQuantityChange(newQuantity.toIntOrNull() ?: 0)
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),

                                colors = OutlinedTextFieldDefaults.colors(
                                    cursorColor = Color.DarkGray,
                                    focusedBorderColor = Color(0xFFFF8080),
                                    unfocusedBorderColor = Color.Gray,
                                ),
                                modifier = Modifier
                                    .padding(start = 4.dp)
                                    //.height(90.dp)
                                    .fillMaxWidth(0.4f)
                                    .fillMaxHeight(),
                                textStyle = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.robotoregular)),
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                   // fontSize = 13.sp, // Increase the font size
                                    color = Color.Black
                            )
                            )

                            AddedToOrderImage(quantity.toIntOrNull() ?: 0)






                        }



                    }

                }
            }
        }
    }


}




@Composable
fun AddedToOrderImage(quantity: Int) {
    if (quantity > 0) {
        Image(
            painter = painterResource(id = R.drawable.addedorderpng),
            contentDescription = "Added to order",
            modifier = Modifier
                .fillMaxWidth()

                .padding(top = 0.dp, start = 0.dp, end = 2.dp)

        )
    }
}


