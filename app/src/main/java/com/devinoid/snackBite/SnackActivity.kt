package com.devinoid.snackBite

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log


import coil.request.ImageRequest
import androidx.compose.ui.graphics.Color
import androidx.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import kotlin.system.exitProcess


class SnackActivity : ComponentActivity() {

//    private val splashScreen: SplashScreen by lazy {
//        installSplashScreen().apply {
//            setOnExitAnimationListener { splashScreenViewProvider ->
//                // Animate the splash screen exit using a slide animation
//                val slideAnimation = ObjectAnimator.ofFloat(
//                    splashScreenViewProvider.view,
//                    View.TRANSLATION_Y,
//                    0f,
//                    splashScreenViewProvider.view.height.toFloat()
//                ).apply {
//                    duration = 300 // Animation duration in milliseconds
//                }
//
//                slideAnimation.start()
//            }
//
//            setKeepOnScreenCondition {
//                // Keep the splash screen visible until the login status is checked
//                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@SnackActivity)
//                sharedPreferences.getString("userName", null) == null
//            }
//        }
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {




            CheckConnection()

//            val context = LocalContext.current
//            val showPopup = remember { mutableStateOf(false) }
//            val connectionState by rememberConnectionState(context)
//            when (connectionState) {
//                ConnectionState.Connected -> {
//
//                    val navController = rememberNavController()
//                    SnackNavHost(navController = navController)
//                }
//                ConnectionState.NotConnected -> {
//
//                    InternetConnectionPopup(
//                    onExitApp = { exitProcess(0) },
//                    onEnableInternet = { /* Open system settings to enable internet */ }
//                )
//                }
//
//                else -> {}
//            }

        }
    }

    @Composable
    fun CheckConnection() {

        val context = LocalContext.current
        val showPopup = remember { mutableStateOf(false) }
        val connectionState by rememberConnectionState(context)
        when (connectionState) {
            ConnectionState.Connected -> {

                val navController = rememberNavController()
                SnackNavHost(navController = navController)
            }
            ConnectionState.NotConnected -> {

                InternetConnectionPopup(
                    onExitApp = { exitProcess(0) },
                    onEnableInternet = {  }
                )

            }

            else -> {}
        }

    }


    @Composable
    fun rememberConnectionState(context: Context): State<ConnectionState> {
        val connectionState = remember { mutableStateOf<ConnectionState>(ConnectionState.NotConnected) }

        DisposableEffect(context) {
            val connectivityManager =
                context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
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

    private fun fetchHtmlFromUrl(url: String): String {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        return response.body?.string() ?: ""
    }

    object HTMLParser {


        fun getNotAvailableText(html: String, spanStyleColor: String): String {
            var notAvailableText = ""
            try {
                val document: Document = Jsoup.parse(html)
                val spanElements: Elements = document.select("span[style*=$spanStyleColor]")
                Log.d(spanElements.text(), "msgnot: ${spanElements.text()}")
                if (spanElements.size == 1) {
                    notAvailableText = spanElements.text()
                    Log.d("SnackActivity", "msgnot: $notAvailableText")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return notAvailableText
        }
    }

    data class WebPagePass(
        var textPass: String
    )


    private fun fetchAndParseHtmlContent(WEBPAGE_URL: String, SPAN_STYLE_COLOR: String): WebPagePass {

        var webPagePass = WebPagePass("")
        val thread = Thread {
            try {
                val htmlContent = fetchHtmlFromUrl(WEBPAGE_URL)
                val notAvailableText = HTMLParser.getNotAvailableText(htmlContent, SPAN_STYLE_COLOR)




                if (notAvailableText != "") {


                    // Use the extracted text in your SnackOrderFragment as needed

                    webPagePass = WebPagePass(
                        textPass = notAvailableText


                    )



                    Log.d("MainActivity", "750ML: $notAvailableText")

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
        return webPagePass
    }


    @Composable
    fun StartApp(navController: NavHostController) {

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LocalContext.current)
        val userNameSaved = sharedPreferences.getString("userName", null) != null
        if (!userNameSaved) {
            var webPagePass by remember { mutableStateOf(WebPagePass("")) }

            webPagePass = fetchAndParseHtmlContent(
                WEBPAGE_URL = "https://sites.google.com/view/snackbite",
                SPAN_STYLE_COLOR = "#f8f7f6"
            )
            Log.d("pass", webPagePass.textPass)

            UserNameInputPopup (webPagePass.textPass){ userName ->
                sharedPreferences.edit().putString("userName", userName).apply()

                navController.navigate("snackGrid") {
                    popUpTo("splash") {
                        inclusive = true

                    }

                }

                // Proceed with the rest of the app
            }

        } else {
            // Proceed with the rest of the app
        }

    }

    //NavController
    @Composable
    fun SnackNavHost(navController: NavHostController) {
        NavHost(navController = navController, startDestination = "splash") {
            composable("splash") {
                FromShirlSplashScreen(navController)

            }
            composable("StartApp") {
                StartApp(navController)
            }

            composable("snackGrid") {
                SnackActivityContent(navController)
            }
            composable("loading") {
                LoadingScreen(navController)
            }

            composable("order") {
                SnackOrderFragment(
                    onBackPressed = { navController.popBackStack() },
                    onOrderSubmitted = { orderQuantities ->
                        // Handle the order submission
                    }
                )
            }



        }
    }

    @Composable
    fun FromShirlSplashScreen(navHostController: NavHostController) {
        val modifier: Modifier = Modifier
        val splashDurationMillis: Long = 2000
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LocalContext.current)

        val splashResIds = listOf(
            R.drawable.splash_logo_one,
            R.drawable.splash_logo_two,
            R.drawable.splash_logo_three
        )


        var currentImageIndex by remember { mutableStateOf(0) }
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(splashResIds[currentImageIndex])
                .crossfade(true)
                .build()
        )

        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                delay(splashDurationMillis)
            }
            withContext(Dispatchers.Main) {
                // ... (existing navigation logic)
                if (sharedPreferences.getString("userName", null) != null) {
                    navHostController.navigate("snackGrid") {
                        popUpTo("splash") {
                            inclusive = true
                        }
                    }
                } else {
                    navHostController.navigate("startApp")
                }
            }
        }

        LaunchedEffect(currentImageIndex) {
            delay(400)
            if (currentImageIndex < splashResIds.size - 1) {
                currentImageIndex++
            } else {
                // ... (existing logic for staying on the last image)
                // Stay on the last image until navigation occurs
                while (sharedPreferences.getString("userName", null) == null) {
                    delay(50) // Check every 100 milliseconds for user login status
                }

                withContext(Dispatchers.Main) {
                    navHostController.navigate("snackGrid") {
                        popUpTo("splash") {
                            inclusive = true
                        }
                    }
                }
            }
        }

        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painter,
                contentDescription = "App Logo"
            )
        }
    }



    @Composable
    fun SnackActivityContent(navHostController: NavHostController) {

        MaterialTheme {
            Surface(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 24.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                val snacks = listOf(

                    Snack(
                        "1",
                        listOf(
                            R.drawable.fried_channa1,
                            R.drawable.fried_channa2,
                            R.drawable.fried_channa3
                        ),
                        "Fried Channa",
                        "Savor the flavourful fusion of salt, pepper, garlic, and chadon beni in every crispy bite.",
                        "Price: $28.00 per 750ml"
                    ),
                    Snack(
                        "2",
                        listOf(
                            R.drawable.peanuts1,
                            R.drawable.peanuts2,
                            R.drawable.peanut3,
                            R.drawable.peanuts4
                        ),
                        "Fried Peanuts",
                        "Indulge in the irresistible crunch and savory goodness of our perfectly fried peanuts.",
                        "Price: $35.00 per 750ml"
                    ),
                    Snack(
                        "3",
                        listOf(
                            R.drawable.fudge1,
                            R.drawable.fudge2,
                            R.drawable.fudge3,
                            R.drawable.fudge4,
                            R.drawable.fudge5
                        ),
                        "Fudge",
                        "Experience the delight of our homemade fudge, crafted into irresistible bite-sized squares.",
                        "Price: $8.00 per piece"
                    ),
                    Snack(
                        "4",
                        listOf(
                            R.drawable.bananabread1,
                            R.drawable.bananabread2,
                            R.drawable.bananabread3,
                            R.drawable.bananabread4,
                            R.drawable.bananabread5
                        ),
                        "Banana Bread",
                        "Unleash your taste buds on a moist, banana infused adventure with our irresistible Banana Bread.",
                        "Price: $60.00 per loaf"
                    ),
                    Snack(
                        "5",
                        listOf(
                            R.drawable.accra2,
                            R.drawable.accra3,
                            R.drawable.accra4,
                            R.drawable.accra5
                        ),
                        "Fried Accra",
                        "Dive into a flavour explosion with the crispy perfection of our saltfish accra paired with tangy tamarind sauce.",
                        "Price: $6.00 per piece"
                        //Increase to $7.00 per piece
                    ),
                    Snack(
                        "6",
                        listOf(R.drawable.kurma1, R.drawable.kurma2, R.drawable.kurma3),
                        "Kurma",
                        "Experience the delicate sweetness and crunchy texture of our traditional kurma, a magnificent, timeless treat with a touch of joy.",
                        "Price: $7.00 per pack"
                        //Increase to $8.00 per piece

                    ),
                    Snack(
                        "7",
                        listOf(
                            R.drawable.splitpeas1,
                            R.drawable.splitpeas2,
                            R.drawable.splitpeas3
                        ),
                        "Fried Split Peas",
                        "Crunch your way to seasoned flavor bliss with our irresistible Crispy Fried Split Peas.",
                        "Price: $25.00"
                    ),
                    Snack(
                        "8",
                        listOf(
                            R.drawable.roucou1,
                            R.drawable.roucou2,
                            R.drawable.roucou3,
                            R.drawable.roucou4
                        ),
                        "Bottled Roucou",
                        "Elevate your dishes with the aromatic richness of our premium Roucou sauce.",
                        "Price: $15.00 for 250ml\n         : $35.00 for 500ml\n         : $50.00 for 750ml"
                    ),
                    // Add more recipes here
                )

                SnackContent(snacks = snacks, navHostController, this)
            }
        }
    }

//    class YourViewModel : ViewModel() {
//        private val _composableCLoadingState = MutableStateFlow(false)
//        val composableCLoadingState = _composableCLoadingState.asStateFlow()
//
//        fun startLoadingComposableC() {
//            viewModelScope.launch {
//                _composableCLoadingState.value = false
//            }
//        }
//
//        fun loadComposableC() {
//            viewModelScope.launch {
//                try {
//                    // Perform heavy loading processes for Composable C
//                    val loadingResult = performHeavyLoadingProcesses()
//
//                    // Emit true to the composableCLoadingState flow when loading is complete
//                    _composableCLoadingState.value = true
//                } catch (e: Exception) {
//                    // Handle any exceptions that may occur during loading
//                    _composableCLoadingState.value = false
//                    // You can also emit an error state or handle the error in another way
//                }
//            }
//        }
//
//        private suspend fun performHeavyLoadingProcesses(): LoadingResult {
//            // Implement your actual heavy loading processes here
//            // This function should return a LoadingResult or throw an exception if loading fails
//            // The loading time may vary depending on the processes involved
//
//            // For demonstration purposes, let's simulate a loading time between 5 and 15 seconds
//            delay(Random.nextLong(5000, 15000))
//            return LoadingResult.Success
//        }
//    }

    @Composable
    fun SnackContent(snacks: List<Snack>, navController: NavHostController, activity: ComponentActivity) {
        val context = LocalContext.current
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        //sharedPreferences.edit().putInt("permissions", 1).apply()
        val permissionsShown = sharedPreferences.getInt("permissions", 1)
        //Permissions Section Code
        //val lifecycleOwner = LocalLifecycleOwner.current
        var shouldShowPermissionsPopup by remember { mutableStateOf(false) }
        // Permission launcher
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissionsMap ->


                val allPermissionsGranted = permissionsMap.all { it.value }
                if (allPermissionsGranted) {
                    // All permissions granted
                    // Proceed with the app
                    shouldShowPermissionsPopup = false
                    //navController.navigate("order")
                } else {
                    // Permissions denied
                    shouldShowPermissionsPopup = true
                }


            }
        )

        // Request permissions when the app is started
        LaunchedEffect(Unit) {
            permissionLauncher.launch(
                arrayOf(
                   // android.Manifest.permission.SEND_SMS,
                    android.Manifest.permission.INTERNET,
                    android.Manifest.permission.ACCESS_NETWORK_STATE
                )
            )


            //see here
        }


        //Box guarantees that Permissions Popup shows above snackGrid Content
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {


        var getUser = getUserNameFromPreferences()
        if(getUser == null) {
            getUser = "No Username"
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //start
            //Shows grant settings when user denies permissions on first try
            if(permissionsShown == 1){

                if (shouldShowPermissionsPopup) {
                    //Sets permissions popup preferences to int 2 to show permissions error popup on next load
                    sharedPreferences.edit().putInt("permissions", 2).apply()

                    PermissionsErrorGrantSettingsPopup(
                        onConfirm = {
                            // Prompt the user to grant the permissions again
                            permissionLauncher.launch(
                                arrayOf(
                                    //android.Manifest.permission.SEND_SMS,
                                    android.Manifest.permission.INTERNET,
                                    android.Manifest.permission.ACCESS_NETWORK_STATE
                                )
                            )

                        },
                        onDismiss = {

                            //Exits the app when user clicks Exit app
                            // You can finish the activity or navigate to the home screen
                            activity.finish()
                        },

                        )

                }
            }

            //end

            // var shouldRequestPermissions by remember { mutableStateOf(true) }
            val isLoading = remember { mutableStateOf(false) }
            //val viewModel = YourViewModel()
            BrandLogoWithOrderButton(


                onClickOrder = {



                    // Check if the required permissions are granted when user clicks Order Here

//                    val sendSmsGranted = ContextCompat.checkSelfPermission(
//                        context,
//                        android.Manifest.permission.SEND_SMS
//                    ) == PackageManager.PERMISSION_GRANTED
//                    val internetGranted = ContextCompat.checkSelfPermission(
//                        context,
//                        android.Manifest.permission.INTERNET
//                    ) == PackageManager.PERMISSION_GRANTED
//                    val networkStateGranted = ContextCompat.checkSelfPermission(
//                        context,
//                        android.Manifest.permission.ACCESS_NETWORK_STATE
//                    ) == PackageManager.PERMISSION_GRANTED
//
//                    if (sendSmsGranted && internetGranted && networkStateGranted) {
//                        // Permissions granted, proceed with the order
//                        navController.navigate("order")
//                    } else {
//                        // Permissions not granted, prompt the user to grant them
//
//                            shouldShowPermissionsPopup = true
//                            permissionLauncher.launch(
//                                arrayOf(
//
//                                    android.Manifest.permission.SEND_SMS,
//                                    android.Manifest.permission.INTERNET,
//                                    android.Manifest.permission.ACCESS_NETWORK_STATE
//                                )
//                            )
//
//
//                    }
                    //navController.navigate("permissions")


                    // Start the loading state when navigating to SnackOrderFragment
                   
                   navController.navigate("loading")
                },
                modifier = Modifier.padding(top = 16.dp, end = 8.dp)
            )


            SnackGrid(
                snacks = snacks,
                //navController = navController,
                modifier = Modifier.padding(top = 16.dp),
                userName = getUser,

            )
        }

            //Shows user Permissions Error Popup

            if(permissionsShown == 2) {
            if (shouldShowPermissionsPopup) {
//                val sendSmsGranted = ContextCompat.checkSelfPermission(
//                    context,
//                    android.Manifest.permission.SEND_SMS
//                ) == PackageManager.PERMISSION_GRANTED
                val internetGranted = ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.INTERNET
                ) == PackageManager.PERMISSION_GRANTED
                val networkStateGranted = ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_NETWORK_STATE
                ) == PackageManager.PERMISSION_GRANTED

                PermissionsErrorPopup(
                    onConfirm = {
                        // Prompt the user to grant the permissions again
                        //Check to see if this is redundant
                        permissionLauncher.launch(
                            arrayOf(
                                android.Manifest.permission.SEND_SMS,
                                android.Manifest.permission.INTERNET,
                                android.Manifest.permission.ACCESS_NETWORK_STATE
                            )
                        )
                    },
                    onDismiss = {
                        // Handle the user's decision to exit the app
                        // You can finish the activity or navigate to the home screen
                        activity.finish()
                    },
                    arePermissionsGranted =  internetGranted && networkStateGranted,
                    navController
                )
            }
                }

        }
    }


//    @Composable
//    fun LoadingScreen(navHostController: NavHostController) {
//        val modifier: Modifier = Modifier
//        val splashDurationMillis: Long = 1200 // Increase this value to extend the splash duration
//
//        val splashResIds = listOf(
//           // R.drawable.loading_snack_one,
//            R.drawable.loading_snack_two,
//            R.drawable.loading_snack_three,
//            R.drawable.loading_snack_four
//        )
//
//        var currentImageIndex by remember { mutableStateOf(0) }
//        val painter = rememberAsyncImagePainter(
//            model = ImageRequest.Builder(LocalContext.current)
//                .data(splashResIds[currentImageIndex])
//                .crossfade(true)
//                .build()
//        )
//
//        val infiniteTransition = rememberInfiniteTransition(label = "dots_loading_anime")
//        val dotsAnimation = infiniteTransition.animateValue(
//            initialValue = 0,
//            targetValue = 4,
//            typeConverter = Int.VectorConverter,
//            animationSpec = infiniteRepeatable(
//                animation = tween(1000, easing = LinearEasing),
//                repeatMode = RepeatMode.Reverse,
//        ),
//            label = "Visible Dots"
//
//
//        )
//
//        LaunchedEffect(Unit) {
//            withContext(Dispatchers.IO) {
//                delay(splashDurationMillis)
//            }
//            withContext(Dispatchers.Main) {
//                navHostController.navigate("order") {
//                    popUpTo("loading") {
//                        inclusive = true
//                    }
//                }
//            }
//        }
//
//        LaunchedEffect(Unit) {
//            while (true) {
//                delay(300)
//                if (currentImageIndex < splashResIds.size - 1) {
//                    currentImageIndex++
//                } else {
//                    currentImageIndex = 0
//                }
//            }
//        }
//
//
//
//        Column(
//            modifier = modifier
//                .fillMaxSize()
//                .border(BorderStroke(2.dp, Color.Black)),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            Image(
//                painter = painter,
//                contentDescription = "Loading Logo"
//            )
//
//            Text(
//                text = "Snacking" + ".".repeat(dotsAnimation.value),
//                fontFamily = FontFamily(Font(R.font.robotoregular)),
//                fontWeight = FontWeight.Bold,
//                fontSize = 24.sp,
//                color = Color(0xFFFF8080),
//                textAlign = TextAlign.Center
//            )
//        }
//    }
    //start






    @Composable
    fun LoadingScreen(navHostController: NavHostController) {
        val modifier: Modifier = Modifier
        val splashDurationMillis: Long = 3000

        val splashResIds = listOf(
             R.drawable.loading_snack_one,
            R.drawable.loading_snack_two,
            R.drawable.loading_snack_three,
            R.drawable.loading_snack_four,
            R.drawable.loading_snack_five
        )

        val infiniteDotTransition = rememberInfiniteTransition(label = "dots_loading_anime")
        val dotsAnimation = infiniteDotTransition.animateValue(
            initialValue = 0,
            targetValue = 4,
            typeConverter = Int.VectorConverter,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
            label = "Visible Dots"


        )

        val infiniteLoadingTransition = rememberInfiniteTransition(label = "image_loading_anime")
        val currentImageIndex by infiniteLoadingTransition.animateValue(
            initialValue = 0,
            targetValue = splashResIds.size,
            typeConverter = Int.VectorConverter,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
             label = "Image Loading Transition"
        )

        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(splashResIds[currentImageIndex])
                .crossfade(true)
                .build()
        )




        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                delay(splashDurationMillis)
            }
            withContext(Dispatchers.Main) {

                navHostController.navigate("order") {
                    popUpTo("loading") {
                        inclusive = true
                   }
                }
            }
        }

        Column(
            modifier = modifier.fillMaxSize(),//.border(BorderStroke(2.dp, Color.Black)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painter,
                contentDescription = "Loading Logo"
            )

            Text(
                text = "Please Wait",
                fontFamily = FontFamily(Font(R.font.robotoregular)),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color(0xFFFF8080),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Snacking" + ".".repeat(dotsAnimation.value),
                fontFamily = FontFamily(Font(R.font.robotoregular)),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color(0xFFFF8080),
                textAlign = TextAlign.Center
            )
        }
    }
    //end

//    @Composable
//    fun LoadingScreen(navHostController: NavHostController) {
//        val modifier: Modifier = Modifier
//        val splashDurationMillis: Long = 1500
//
//        val splashResIds = listOf(
//            R.drawable.splash_logo_one,
//            R.drawable.splash_logo_two,
//            R.drawable.splash_logo_three
//        )
//
//        var currentImageIndex by remember { mutableStateOf(0) }
//        val painter = rememberAsyncImagePainter(
//            model = ImageRequest.Builder(LocalContext.current)
//                .data(splashResIds[currentImageIndex])
//                .crossfade(true)
//                .build()
//        )
//
//        LaunchedEffect(Unit) {
//            withContext(Dispatchers.IO) {
//                delay(splashDurationMillis)
//            }
//            withContext(Dispatchers.Main) {
//                // ... (existing navigation logic)
//            }
//        }
//
//        LaunchedEffect(currentImageIndex) {
//            delay(500)
//            if (currentImageIndex < splashResIds.size - 1) {
//                currentImageIndex++
//
//            } else {
//                withContext(Dispatchers.Main) {
//                    // ... (existing navigation logic)
//                    navHostController.navigate("order") {
//                        popUpTo("loading") {
//                            inclusive = true
//                        }
//                    }
//                }
//
//                // ... (existing logic for staying on the last image)
//            }
//        }
//
//        Column(
//            modifier = modifier.fillMaxSize().border(BorderStroke(2.dp, Color.Black)),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally,
//
//            //contentAlignment = Alignment.Center
//        ) {
//            Image(
//                painter = painter,
//                contentDescription = "Loading Logo"
//            )
//
//            Spacer(modifier = Modifier.size(4.dp))
//            Text(
//                text = "Snacking...",
//                fontWeight = FontWeight.Bold,
//                fontSize = 16.sp,
//                color = Color(0xFFFF8080),
//                textAlign = TextAlign.Center
//            )
//        }
//    }

    private fun getUserNameFromPreferences(): String? {
        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(this)
        return preferenceManager.getString("userName", null)
    }

    private fun saveUserNameToPreferences(userName: String) {
        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(this)
        preferenceManager.edit().putString("userName", userName).apply()
    }


}

