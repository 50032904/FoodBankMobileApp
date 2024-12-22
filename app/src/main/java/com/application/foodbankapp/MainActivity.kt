package com.application.foodbankapp

import android.Manifest
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.application.theme.token.MyAliasTokens
import com.microsoft.fluentui.compose.Scaffold
import com.microsoft.fluentui.theme.FluentTheme
import com.microsoft.fluentui.theme.token.controlTokens.AppBarSize
import com.microsoft.fluentui.tokenized.AppBar
import com.microsoft.fluentui.tokenized.navigation.TabBar
import com.microsoft.fluentui.tokenized.navigation.TabData
import com.microsoft.fluent.mobile.icons.R
import com.microsoft.fluentui.icons.SearchBarIcons
import com.microsoft.fluentui.icons.searchbaricons.Arrowback
import com.microsoft.fluentui.icons.searchbaricons.Dismisscircle
import com.microsoft.fluentui.theme.token.AliasTokens
import com.microsoft.fluentui.theme.token.FluentAliasTokens
import com.microsoft.fluentui.theme.token.FluentGlobalTokens
import com.microsoft.fluentui.theme.token.FluentIcon
import com.microsoft.fluentui.theme.token.FluentStyle
import com.microsoft.fluentui.theme.token.Icon
import com.microsoft.fluentui.theme.token.controlTokens.AppBarTokens
import com.microsoft.fluentui.theme.token.controlTokens.BehaviorType
import com.microsoft.fluentui.theme.token.controlTokens.BorderType
import com.microsoft.fluentui.theme.token.controlTokens.ButtonStyle
import com.microsoft.fluentui.theme.token.controlTokens.DrawerTokens
import com.microsoft.fluentui.tokenized.controls.BasicCard
import com.microsoft.fluentui.tokenized.controls.Button
import com.microsoft.fluentui.tokenized.controls.TextField
import com.microsoft.fluentui.tokenized.controls.ToggleSwitch
import com.microsoft.fluentui.tokenized.divider.Divider
import com.microsoft.fluentui.tokenized.drawer.Drawer
import com.microsoft.fluentui.tokenized.drawer.rememberDrawerState
import com.microsoft.fluentui.tokenized.listitem.ListItem
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    var tempUserIDStore = 0
    var titlePublic = "none"

    var loggedInAccount = Account("Guest", "", "")

    var accountArray =
        MutableList<Account>(10) { Account("", "", "") }

    var datePublic = ""
    var timePublic = ""
    var selectedDatePublic = Date()
    var fullDatePublic = ""
    var notifCheckPublic = false


    var address1Public = ""
    var address2Public = ""
    var cityPublic = ""
    var postCodePublic = ""

    class NotificationText {
        var notificationText = ""
    }

    class NotificationWorker(context: Context, workerParams: WorkerParameters) :
        Worker(context, workerParams) {

        var notificationtext = ""


        override fun doWork(): Result {
            // Create an explicit intent for an Activity in your app
            val intent = Intent(applicationContext, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent,
                PendingIntent.FLAG_IMMUTABLE)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create the NotificationChannel.
                val name = "Name"
                val descriptionText = "Description"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val mChannel = NotificationChannel("CHANNEL_ID", name, importance)
                mChannel.description = descriptionText
                // Register the channel with the system. You can't change the importance
                // or other notification behaviors after this.
                val notificationManager = ContextCompat.getSystemService(
                    applicationContext,
                    NotificationManager::class.java
                ) as NotificationManager
                notificationManager.createNotificationChannel(mChannel)
            }

            val builder = NotificationCompat.Builder(applicationContext, "CHANNEL_ID")
                .setSmallIcon(com.application.foodbankapp.R.drawable.ic_launcher_foreground)
                .setContentTitle("Food Bank")
                .setContentText("Your package will be ready to collect in 60 minutes.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                //.setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(applicationContext)) {
                // notificationId is a unique int for each notification that you must define
                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return Result.failure()
                }
                notify(1, builder.build())
            }


            return Result.success()
        }
    }

    class NotificationWorker2(context: Context, workerParams: WorkerParameters) :
        Worker(context, workerParams) {

        var notificationtext = ""


        override fun doWork(): Result {
            // Create an explicit intent for an Activity in your app
            val intent = Intent(applicationContext, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent,
                PendingIntent.FLAG_IMMUTABLE)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create the NotificationChannel.
                val name = "Name"
                val descriptionText = "Description"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val mChannel = NotificationChannel("CHANNEL_ID", name, importance)
                mChannel.description = descriptionText
                // Register the channel with the system. You can't change the importance
                // or other notification behaviors after this.
                val notificationManager = ContextCompat.getSystemService(
                    applicationContext,
                    NotificationManager::class.java
                ) as NotificationManager
                notificationManager.createNotificationChannel(mChannel)
            }

            val builder = NotificationCompat.Builder(applicationContext, "CHANNEL_ID")
                .setSmallIcon(com.application.foodbankapp.R.drawable.ic_launcher_foreground)
                .setContentTitle("Food Bank")
                .setContentText("Your package has been delivered.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                //.setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(applicationContext)) {
                // notificationId is a unique int for each notification that you must define
                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return Result.failure()
                }
                notify(2, builder.build())
            }


            return Result.success()
        }
    }

    enum class AppScreen() {
        Home,
        Booking,
        Package,
        Contact,
        Login,
        Pickup,
        DateTime,
        Signin,
        Register,
        LoginHandler,
        AddItem,
        ConfirmationPickup,
        Delivery,
        ConfirmationDelivery,
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val context = this

            val navController = rememberNavController()
            AppNavHost(
                context = context,
                modifier = Modifier,
                navController = navController,
                startDestination = AppScreen.Home.name
            )


        }
    }

    @Composable
    fun AppNavHost(
        context: Context,
        modifier: Modifier,
        navController: NavHostController = rememberNavController(),
        startDestination: String = "home"
    ) {
        var selectedTab by rememberSaveable { mutableStateOf(0) }
        var title by rememberSaveable { mutableStateOf("Main Menu") }
        titlePublic = title

        var profileIcon by rememberSaveable { mutableStateOf(R.drawable.ic_fluent_person_24_filled) }



        accountArray.add(
            Account(
                "TestAccount1",
                "Password1",
                "Beans#Canned-Produce#3#Pasta#Dried-Produce#2#Apples#FruitVeg#1#",

                )
        )
        accountArray.add(
            Account(
                "a",
                "aaaaaaaa",
                "Beans#Canned-Produce#3#Pasta#Dried-Produce#2#Apples#FruitVeg#1#",

                )
        )

        val tabDataList = arrayListOf(
            TabData(
                title = "Home",
                icon = ImageVector.vectorResource(id = R.drawable.ic_fluent_home_24_regular),
                selectedIcon = ImageVector.vectorResource(id = R.drawable.ic_fluent_home_24_filled),
                onClick = {
                    selectedTab = 0
                    title = "Main Menu"
                    navController.navigate("home")
                },
            ),
            TabData(
                title = "Booking",
                icon = ImageVector.vectorResource(id = R.drawable.ic_fluent_book_clock_24_regular),
                selectedIcon = ImageVector.vectorResource(id = R.drawable.ic_fluent_book_clock_24_filled),
                onClick = {
                    selectedTab = 1
                    title = "Booking/Pick Up"
                    navController.navigate("booking")
                },
            ),
            TabData(
                title = "Package",
                icon = ImageVector.vectorResource(id = R.drawable.ic_fluent_box_search_24_regular),
                selectedIcon = ImageVector.vectorResource(id = R.drawable.ic_fluent_box_search_24_filled),
                onClick = {
                    selectedTab = 2
                    title = "Package"
                    navController.navigate("package")
                }
            ),
            TabData(
                title = "Contact",
                icon = ImageVector.vectorResource(id = R.drawable.ic_fluent_communication_person_24_regular),
                selectedIcon = ImageVector.vectorResource(id = R.drawable.ic_fluent_communication_person_24_filled),
                onClick = {
                    selectedTab = 3
                    title = "Contact"
                    navController.navigate("contact")
                }
            )

        )


        FluentTheme(aliasTokens = MyAliasTokens()) {
            Scaffold(
                modifier = Modifier.background(
                    FluentTheme.aliasTokens.neutralBackgroundColor[FluentAliasTokens.NeutralBackgroundColorTokens.Background1].value(
                        FluentTheme.themeMode
                    ), RectangleShape
                ),
                topBar = {
                    AppBar(
                        title = "Food Bank",
                        appBarSize = AppBarSize.Small,
                        subTitle = titlePublic,
                        navigationIcon = FluentIcon(
                            SearchBarIcons.Arrowback,
                            contentDescription = "Navigate Back",
                            onClick = {
                                selectedTab = 0
                                title = "Main Menu"
                                navController.navigate("home")
                            },
                            flipOnRtl = true,
                        ),
                        logo = {
                            Image(
                                painter = painterResource(id = com.application.foodbankapp.R.mipmap.colourclogoicon),
                                "Icon",
                                Modifier
                                    .width(32.dp)
                                    .height(32.dp)
                            )
                        },
                        rightAccessoryView = {
                            Box(
                                Modifier
                                    .size(44.dp)
                                    .clickable(
                                        onClick = {
                                            title = "Account"
                                            navController.navigate("login")
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_fluent_person_24_filled),
                                    "Add",
                                    tint = FluentTheme.aliasTokens.neutralForegroundColor[FluentAliasTokens.NeutralForegroundColorTokens.Foreground2].value(
                                        FluentTheme.themeMode
                                    )
                                )
                            }
                            GetContentHelp(context = context, tint = FluentTheme.aliasTokens.neutralForegroundColor[FluentAliasTokens.NeutralForegroundColorTokens.Foreground2].value(
                                FluentTheme.themeMode
                            ))

                        }
                    )
                },
                bottomBar = {
                    TabBar(
                        tabDataList = tabDataList,
                        selectedIndex = selectedTab
                    )
                },
            ) {
                NavHost(
                    modifier = modifier,
                    navController = navController,
                    startDestination = AppScreen.Home.name,
                ) {
                    composable(route = AppScreen.Home.name) {
                        MainMenu(
                            onNavigateToBooking = {

                                selectedTab = 1
                                title = "Booking/Pick Up"
                                navController.navigate("booking")
                            },
                            onNavigateToPackage = {
                                selectedTab = 2
                                title = "Package"
                                navController.navigate("package")
                            },
                            onNavigateToLogin = {
                                title = "Account"
                                navController.navigate("login")
                            },
                            onNavigateToContact = {
                                selectedTab = 3
                                title = "Contact"
                                navController.navigate("contact")
                            },
                        )
                    }
                    composable(route = AppScreen.Booking.name) {
                        Booking(
                            onNavigateToHome = { navController.navigate("home") },
                            onNavigateToPackage = { navController.navigate("package") },
                            onNavigateToDelivery = { navController.navigate("delivery") },
                            onNavigateToPickup = { navController.navigate("pickup") },
                        )
                    }
                    composable(AppScreen.Package.name) {
                        Package(
                            onNavigateToHome = { navController.navigate("home") },
                            onNavigateToBooking = { navController.navigate("booking") },
                            onNavigateToContact = { navController.navigate("contact") },
                            onNavigateToAddItem = { navController.navigate("additem") },
                            navController = navController
                        )
                    }
                    composable(route = AppScreen.Login.name) {
                        LoginChoice(
                            onNavigateToSignin = { navController.navigate("signin") },
                            onNavigateToRegister = { navController.navigate("register") }
                        )
                    }
                    composable(route = AppScreen.Contact.name) {
                        Contact(
                            onNavigateToHome = { navController.navigate("home") },
                            onNavigateToBooking = { navController.navigate("booking") },
                            onNavigateToPackage = { navController.navigate("package") },
                        )
                    }
                    composable(route = AppScreen.Pickup.name) {
                        Pickup(
                            navController = navController,
                            onNavigateToDateTime = { navController.navigate("datetime") },
                            onNavigateToConfirmationPickup = { navController.navigate("confirmationpickup") },
                            context = context
                        )
                    }
                    composable(route = AppScreen.Signin.name) {
                        LoginScreen(
                            navController = navController,
                            context = context,
                            onNavigateToHome = {
                                navController.navigate("home")
                            }
                        )
                    }
                    composable(route = AppScreen.Register.name) {
                        RegisterScreen(
                            navController = navController,
                            context = context
                        )
                    }
                    composable(route = AppScreen.AddItem.name) {
                        AddItem(
                            onNavigateToPackage = { navController.navigate("package") },
                            navController = navController
                        )
                    }
                    composable(route = AppScreen.ConfirmationPickup.name) {
                        ConfirmationPickup(
                            context = context,
                            navController = navController
                        )
                    }
                    composable(route = AppScreen.Delivery.name) {
                        Delivery(
                            navController = navController,
                            onNavigateToConfirmationDelivery = { navController.navigate("confirmationdelivery") },
                        )
                    }
                    composable(route = AppScreen.ConfirmationDelivery.name) {
                        ConfirmationDelivery(
                            context = context,
                            navController = navController
                        )
                    }
                }

            }
        }




    }

    @Composable
    fun CreateListHelp(size: Int, context: Context) {

        val question =
            ImageVector.vectorResource(id = R.drawable.ic_fluent_question_28_filled)

        var helpView by rememberSaveable { mutableStateOf(false) }


        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        {
            repeat(size) {
                item {
                    if (!helpView) {
                        ListItem.Item(text = "Get Help",
                            onClick = {
                                helpView = true

                            },
                            leadingAccessoryContent = {
                                Image(
                                    question,
                                    "Help"
                                )
                            })
                    } else if(titlePublic == "Main Menu"){
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Help (" + titlePublic + ")",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Light,
                                modifier = Modifier
                                    .padding(top = 8.dp, bottom = 32.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                            Text(
                                text = "If you wish to create an account to save your package, select \"Log into your account\", otherwise you can continue as a guest.\n\nTo begin, select \"View and modify the contents of your package\".\n\nOnce you have created the package, the next step is to arrange a collection method by selecting \"Create a booking or arrange a pick up\".",
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp
                            )
                        }
                    } else if(titlePublic == "Booking/Pick Up"){
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Help (" + titlePublic + ")",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Light,
                                modifier = Modifier
                                    .padding(top = 8.dp, bottom = 32.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                            Text(
                                text = "Choose how you would like to obtain your package.\n\nIf you are nearby, you can arrive at our collection center to collect your package, otherwise, you can have the package delivered to your house.",
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp
                            )
                        }
                    } else if(titlePublic == "Package"){
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Help (" + titlePublic + ")",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Light,
                                modifier = Modifier
                                    .padding(top = 8.dp, bottom = 32.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                            Text(
                                text = "To add items to your supply package, select \"Add item\". Then select the category of the item you wish to add. You can then select the specific item you want from the \"Item\" dropdown bar, and choose a quantity.\n\nTo remove an item, press the Bin Icon next to the item.",
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp
                            )
                        }
                    } else if(titlePublic == "Contact"){
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Help (" + titlePublic + ")",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Light,
                                modifier = Modifier
                                    .padding(top = 8.dp, bottom = 32.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                            Text(
                                text = "If you are having trouble using this app, or placing an order, please use one of the following contact methods on this screen to get in touch.",
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp
                            )
                        }
                    } else if(titlePublic == "Account"){
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Help (" + titlePublic + ")",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Light,
                                modifier = Modifier
                                    .padding(top = 8.dp, bottom = 32.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                            Text(
                                text = "Create an account to save your package contents so that you can re-order it faster.\n\nIf you have created an account before, select \"Sign in to your food bank account\".",
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun GetContentHelp(context: Context, tint: Color) {

        val drawerState = rememberDrawerState()
        val scope = rememberCoroutineScope()
        Drawer(
            drawerState = drawerState,
            drawerContent = { CreateListHelp(size = 1, context = context) },
            //expandable = true,
            drawerTokens = DrawerTokens(),
            behaviorType = BehaviorType.BOTTOM_SLIDE_OVER
        )
        Box(
            Modifier
                .size(44.dp)
                .clickable(
                    onClick = { scope.launch { drawerState.open() } }
                ),
            contentAlignment = Alignment.Center
        )
        {
            Icon(
                painter = painterResource(id = R.drawable.ic_fluent_more_vertical_24_regular),
                "More",
                tint = tint
            )
        }
    }

    @Composable
    private fun MainMenu(
        onNavigateToBooking: () -> Unit,
        onNavigateToPackage: () -> Unit,
        onNavigateToContact: () -> Unit,
        onNavigateToLogin: () -> Unit,
    ){
        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp, vertical = 58.dp)) {
            item {
                //Spacer(modifier = Modifier.height(64.dp))
                Text(
                    text = "Welcome to the Food Bank App",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.height(28.dp))
                Text(
                    text = "Please make a selection: " + loggedInAccount.username,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(12.dp))
                BasicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    content = {
                        Button(
                            modifier = Modifier.height(80.dp),
                            onClick = onNavigateToBooking,
                            style = ButtonStyle.TextButton,
                            text = "Create a booking or arrange a pick up                                                                                                                                               ",
                            icon = ImageVector.vectorResource(id = R.drawable.ic_fluent_book_clock_24_filled)
                        )
                    }
                )
                Spacer(Modifier.height(16.dp))
                BasicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    content = {
                        Button(
                            modifier = Modifier.height(80.dp),
                            onClick = onNavigateToPackage,
                            style = ButtonStyle.TextButton,
                            text = "View and modify the contents of your supply package                                                                                                                                   ",
                            icon = ImageVector.vectorResource(id = R.drawable.ic_fluent_box_search_24_filled)
                        )
                    },
                )
                Spacer(Modifier.height(16.dp))
                BasicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    content = {
                        Button(
                            modifier = Modifier.height(80.dp),
                            onClick = onNavigateToLogin,
                            style = ButtonStyle.TextButton,
                            text = "Log in to your account                                                                                                                                                               ",
                            icon = ImageVector.vectorResource(id = R.drawable.ic_fluent_key_24_filled)
                        )
                    }
                )
                Spacer(Modifier.height(16.dp))
                BasicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    content = {
                        Button(
                            modifier = Modifier.height(80.dp),
                            onClick = onNavigateToContact,
                            style = ButtonStyle.TextButton,
                            text = "Contact us for help                                                                                                                                                                     ",
                            icon = ImageVector.vectorResource(id = R.drawable.ic_fluent_communication_person_24_filled)
                        )
                    }
                )
                Spacer(Modifier.height(16.dp))

            }
        }
    }

    @Composable
    private fun Booking(
        onNavigateToHome: () -> Unit,
        onNavigateToPackage: () -> Unit,
        onNavigateToDelivery: () -> Unit,
        onNavigateToPickup: () -> Unit,
    ) {

        val pickup =
            ImageVector.vectorResource(id = R.drawable.ic_fluent_box_arrow_up_24_filled)
        val delivery =
            ImageVector.vectorResource(id = R.drawable.ic_fluent_box_arrow_left_24_filled)

        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp, vertical = 58.dp)) {
            item {
                Column {
                    Spacer(modifier = Modifier.height(48.dp))

                    Text(
                        text = "Are you picking up or having a package delivered?",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(36.dp))
                    BasicCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        content = {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp),
                                onClick = onNavigateToPickup,
                                style = ButtonStyle.TextButton,
                                text = "Pick up a package                                                                                                                                               ",
                                icon = pickup
                            )
                        }
                    )

                    Spacer(Modifier.height(16.dp))

                    BasicCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        content = {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp),
                                onClick = onNavigateToDelivery,
                                style = ButtonStyle.TextButton,
                                text = "Have a package delivered                                                                                                                                   ",
                                icon = delivery
                            )
                        }
                    )
                    Spacer(Modifier.height(4.dp))
                }
            }
        }
    }

    @Composable
    private fun Package(
        onNavigateToHome: () -> Unit,
        onNavigateToBooking: () -> Unit,
        onNavigateToContact: () -> Unit,
        onNavigateToAddItem: () -> Unit,
        navController: NavHostController
    ) {

        val add =
            ImageVector.vectorResource(id = com.microsoft.fluent.mobile.icons.R.drawable.ic_fluent_add_24_regular)

        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp, vertical = 58.dp)) {
            item {
                Column {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Select the contents of your supply package:",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    if (loggedInAccount.contents != "") {
                        ContentGetterV2(navController = navController)
                    } else {
                        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                            ) {

                                Text(
                                    text = "None selected",
                                    textAlign = TextAlign.Start,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
                                )

                            }
                        }
                    }
                    Spacer(modifier = Modifier.padding(bottom = 8.dp))
                    Divider()
                    Button(
                        text = "Add Item",
                        icon = add,
                        modifier = Modifier.align(Alignment.End),
                        onClick = onNavigateToAddItem
                    )
                }
            }
        }
    }

    @Composable
    private fun Contact(
        onNavigateToHome: () -> Unit,
        onNavigateToBooking: () -> Unit,
        onNavigateToPackage: () -> Unit,
    ) {

    }

    @Composable
    private fun Pickup(
        onNavigateToDateTime: () -> Unit,
        onNavigateToConfirmationPickup: () -> Unit,
        navController: NavHostController,
        context: Context
    ) {

        val pen =
            ImageVector.vectorResource(id = R.drawable.ic_fluent_edit_24_regular)


        var date by rememberSaveable { mutableStateOf("01/01/23") }
        var time by rememberSaveable { mutableStateOf("15:00") }
        var fullDate by rememberSaveable { mutableStateOf("Sunday, 1st January 2023\n3:00pm") }

        var confirmEnabled by rememberSaveable { mutableStateOf(false) }


        var selectedDate by remember { mutableStateOf(Date()) }
        val datePickerDialog = remember { DatePickerDialog(context) }
        datePickerDialog.setOnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, monthOfYear, dayOfMonth)
            selectedDate = calendar.time
        }


        val selectedTime = remember { mutableStateOf(Calendar.getInstance()) }
        val timePickerDialog = remember { TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                selectedTime.value.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.value.set(Calendar.MINUTE, minute)
            },
            selectedTime.value.get(Calendar.HOUR_OF_DAY),
            selectedTime.value.get(Calendar.MINUTE),
            true
        ) }

        fun localCalendar() {

            val tempCalendar = Calendar.getInstance()
            tempCalendar.time = selectedDate

            val calendar2 = Calendar.getInstance()

            calendar2.set(Calendar.YEAR, tempCalendar.get(Calendar.YEAR))
            calendar2.set(Calendar.MONTH, tempCalendar.get(Calendar.MONTH))
            calendar2.set(Calendar.DAY_OF_MONTH, tempCalendar.get(Calendar.DAY_OF_MONTH))
            calendar2.set(Calendar.HOUR_OF_DAY, selectedTime.value.get(Calendar.HOUR_OF_DAY))
            calendar2.set(Calendar.MINUTE, selectedTime.value.get(Calendar.MINUTE))
            selectedDate = calendar2.time

        }


        val formatter = SimpleDateFormat("dd/MM/yy")
        val formattedDate = formatter.format(selectedDate)
        date = formattedDate



        var timeCompose = "${selectedTime.value.get(Calendar.HOUR_OF_DAY)}:${selectedTime.value.get(
            Calendar.MINUTE)}"

        if(selectedTime.value.get(Calendar.HOUR_OF_DAY) < 10){
            timeCompose = "0${selectedTime.value.get(Calendar.HOUR_OF_DAY)}:"
        } else {
            timeCompose = "${selectedTime.value.get(Calendar.HOUR_OF_DAY)}:"
        }
        if(selectedTime.value.get(Calendar.MINUTE) < 10){
            timeCompose = timeCompose + "0${selectedTime.value.get(Calendar.MINUTE)}"
        } else {
            timeCompose = timeCompose + "${selectedTime.value.get(Calendar.MINUTE)}"
        }


        time = timeCompose

        val fullDateFormatter = SimpleDateFormat("EEEE, d MMMM yyyy")
        val formattedFullDate = fullDateFormatter.format(selectedDate)
        val fullTimeFormatter = SimpleDateFormat("h:mmaa")
        val formattedFullTime = fullTimeFormatter.format(selectedDate)

        fullDate = formattedFullDate + "\n" + formattedFullTime

        if((selectedDate.time - System.currentTimeMillis()) > 3600000){
            confirmEnabled = true
        } else {
            confirmEnabled = false
        }

        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp, vertical = 58.dp)) {
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(36.dp))

                    Text(
                        text = "Select when you can pick up the package:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Date",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Pickup date:   " + date,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Button(
                        modifier = Modifier,
                        onClick = {datePickerDialog.show()
                            datePickerDialog.setOnDismissListener({localCalendar()})},
                        text = "Choose a date",
                        icon = pen
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Time",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Pickup time:   " + time,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Button(
                        modifier = Modifier,
                        onClick = {timePickerDialog.show()
                            timePickerDialog.setOnDismissListener({localCalendar()})},
                        text = "Choose a time",
                        icon = pen
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Confirm pickup?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 24.dp)
                    )

                    Text(
                        text = fullDate,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(Modifier.height(8.dp))

                    if((selectedDate.time - System.currentTimeMillis()) < 3600000) {
                        Text(
                            text = "Selected date must be at least 1 hour from the current time",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Button(
                        text = "Confirm",
                        modifier = Modifier.align(Alignment.End),
                        enabled = confirmEnabled,
                        onClick = {
                            datePublic = date
                            timePublic = time
                            selectedDatePublic = selectedDate
                            fullDatePublic = fullDate
                            navController.navigate("confirmationpickup")
                        }
                    )

                }
            }
        }
    }

    @Composable
    private fun Delivery(navController: NavHostController, onNavigateToConfirmationDelivery: () -> Unit){

        var address1 by rememberSaveable{ mutableStateOf("")}
        var address2 by rememberSaveable{ mutableStateOf("")}
        var city by rememberSaveable{ mutableStateOf("")}
        var postCode by rememberSaveable{ mutableStateOf("")}

        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp, vertical = 58.dp)) {
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(36.dp))

                    Text(
                        text = "Confirm your address for delivery",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    TextField(
                        value = address1,
                        onValueChange = { address1 = it },
                        hintText = "Address 1",
                        label = "Address 1",
                        modifier = Modifier.padding(horizontal = 34.dp)
                    )
                    TextField(
                        value = address2,
                        onValueChange = { address2 = it },
                        hintText = "Address 2",
                        label = "Address 2",
                        modifier = Modifier.padding(horizontal = 34.dp)
                    )
                    TextField(
                        value = city,
                        onValueChange = { city = it },
                        hintText = "City",
                        label = "City",
                        modifier = Modifier.padding(horizontal = 34.dp)
                    )
                    TextField(
                        value = postCode,
                        onValueChange = { postCode = it },
                        hintText = "Post Code",
                        label = "Post Code",
                        modifier = Modifier.padding(horizontal = 34.dp)
                    )
                    Button(
                        text = "Confirm",
                        modifier = Modifier.align(Alignment.End),
                        onClick = { address1Public = address1
                            address2Public = address2
                            cityPublic = city
                            postCodePublic = postCode
                            navController.navigate("confirmationdelivery")
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun LoginChoice(
        onNavigateToSignin: () -> Unit,
        onNavigateToRegister: () -> Unit,
    ) {

        val key =
            ImageVector.vectorResource(id = R.drawable.ic_fluent_person_key_20_filled)
        val add =
            ImageVector.vectorResource(id = R.drawable.ic_fluent_person_add_20_filled)

        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp, vertical = 58.dp)) {
            item {
                Column {
                    Spacer(modifier = Modifier.height(48.dp))

                    Text(
                        text = "Log into an existing account, or create a new one:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(36.dp))
                    BasicCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        content = {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp),
                                onClick = onNavigateToSignin,
                                style = ButtonStyle.TextButton,
                                text = "Sign in to your food bank account                                                                                                                                               ",
                                icon = key
                            )
                        }
                    )

                    Spacer(Modifier.height(16.dp))
                    BasicCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        content = {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp),
                                onClick = onNavigateToRegister,
                                style = ButtonStyle.TextButton,
                                text = "Register a new account                                                                                                                                   ",
                                icon = add
                            )
                        }
                    )
                    Spacer(Modifier.height(4.dp))

                }
            }
        }
    }

    @Composable
    private fun LoginScreen(
        onNavigateToHome: () -> Unit,
        navController: NavHostController,
        context: Context
    ) {


        var secondAttempt by rememberSaveable { mutableStateOf(false) }

        var username by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }

        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp, vertical = 58.dp)) {
            item {

                Box(
                    modifier = Modifier
                        .padding(all = 50.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(7.dp),
                            ambientColor = Color.Black,
                            spotColor = Color.Black
                        )
                        .background(Color.White)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(7.dp)),
                ) {
                    Column {

                        Text(
                            text = "Food Bank Sign-in",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 34.dp, bottom = 79.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        TextField(
                            value = username,
                            onValueChange = { username = it },
                            hintText = "Username",
                            label = "Username",
                            errorString = if (verifyUsername(username) == false && secondAttempt) "Username does not exist" else "",
                            modifier = Modifier.padding(horizontal = 34.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            hintText = "Password",
                            label = "Password",
                            trailingAccessoryIcon = FluentIcon(
                                SearchBarIcons.Dismisscircle
                            ),
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.padding(horizontal = 34.dp),
                        )

                        Button(
                            text = "Login",
                            onClick =
                            {
                                LoginUser(username, context)
                                onNavigateToHome()
                            },
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(horizontal = 33.dp, vertical = 33.dp),
                            enabled = if (verifyLogin(
                                    username,
                                    password
                                ) == true && username != ""
                            ) true else false
                        )
                        Spacer(modifier = Modifier.height(11.dp))
                    }
                }
            }
        }
    }

    @Composable
    private fun RegisterScreen(navController: NavController, context: Context) {
        var username by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }

        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp, vertical = 58.dp)) {
            item {

                Box(
                    modifier = Modifier
                        .padding(all = 50.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(7.dp),
                            ambientColor = Color.Black,
                            spotColor = Color.Black
                        )
                        .background(Color.White)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(7.dp)),
                ) {
                    Column {

                        Text(
                            text = "Food Bank Sign-up",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 34.dp, bottom = 79.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        TextField(
                            value = username,
                            onValueChange = { username = it },
                            hintText = "Username",
                            label = "Username",
                            errorString = if (username == "") "Enter a Username" else if (verifyUsername(
                                    username
                                ) == true
                            ) "Name already registered" else "",
                            modifier = Modifier.padding(horizontal = 34.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            hintText = "Password",
                            label = "Password",
                            errorString = if (password.length < 8) "Password must contain 8 characters" else "",
                            trailingAccessoryIcon = FluentIcon(
                                SearchBarIcons.Dismisscircle
                            ),
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.padding(horizontal = 34.dp),
                        )

                        Button(
                            text = "Register",
                            onClick = {
                                RegisterAccount(username, password, context)
                                navController.navigate("home")
                            },
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(horizontal = 33.dp, vertical = 33.dp),
                            enabled = if (password.length < 8 || username == "" || verifyUsername(
                                    username
                                ) == true
                            ) false else true
                        )
                        Spacer(modifier = Modifier.height(11.dp))
                    }
                }
            }
        }

    }

    private fun RegisterAccount(username: String, password: String, context: Context) {
        for (i in 0 until accountArray.size) {
            if (accountArray.get(i).username == "") {
                accountArray.get(i).username = username
                accountArray.get(i).password = password
                LoginUser(username, context)
                break
            }
        }
    }

    private fun verifyLogin(username: String, password: String): Boolean {
        var loginSucceed = false
        var usernameLocation = 0

        for (i in 0 until accountArray.size) {
            if (username == accountArray.get(i).username) {
                usernameLocation = i
                break
            }
        }

        if ((accountArray.get(usernameLocation).password == password) && (password != "")) {
            loginSucceed = true
        } else {
            loginSucceed = false
        }

        return loginSucceed
    }

    private fun verifyUsername(username: String): Boolean {
        var usernameExist = false
        for (i in 0 until accountArray.size) {
            if (username == accountArray.get(i).username) {
                usernameExist = true
                tempUserIDStore = i
            }
        }
        return usernameExist
    }

    private fun LoginUser(username: String, context: Context) {
        for (i in 0 until accountArray.size) {
            if (username == accountArray.get(i).username) {
                loggedInAccount = accountArray.get(i)
                break
            }
        }

    }

    @Composable
    private fun ContentGetterV2(navController: NavHostController) {

        var packageContents = loggedInAccount.contents
        var iterator = packageContents.iterator()
        var currentChar: Char
        var word = ""
        var wordList = mutableListOf<String>("", "", "")
        val maxLength = wordList.maxOf { it.length }
        var name = ""
        var category = ""
        var qty = ""
        var part = 0

        while (iterator.hasNext()) {
            currentChar = iterator.nextChar()

            if (currentChar != '#') {
                word = word + currentChar
            } else {
                wordList.add(word)
                word = ""
            }

        }

        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 58.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    modifier = Modifier.weight(1f),
                    text = "Name",
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    modifier = Modifier.weight(1.5f),
                    text = "Category",
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    modifier = Modifier.weight(0.5f),
                    text = "Quantity",
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(text = "Remove")
            }
        }

        for (i in wordList.indices) {
            if (i % 3 == 0) {
                name = wordList.get(i)
            } else if (i % 3 == 1) {
                category = wordList.get(i)
            } else if (i % 3 == 2) {
                qty = wordList.get(i)
                Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            modifier = Modifier.weight(1f),
                            text = name,
                            textAlign = TextAlign.Start,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Text(
                            modifier = Modifier.weight(1.5f),
                            text = category,
                            textAlign = TextAlign.Start,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Text(
                            modifier = Modifier.weight(0.5f),
                            text = qty,
                            textAlign = TextAlign.Start,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                        val itemString = name + "#" + category + "#" + qty + "#"
                        if(name != "") {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_fluent_delete_24_regular),
                                "Remove item",
                                modifier = Modifier.clickable(
                                    enabled = true,
                                    onClick = {
                                        ContentRemover(
                                            itemString
                                        ); navController.navigate("package")
                                    })
                            )
                        }
                    }
                }
            }
        }

    }

    @Composable
    private fun AddItem(onNavigateToPackage: () -> Unit, navController: NavHostController) {

        var expandedCategory by remember { mutableStateOf(false) }
        var expandedItems by remember { mutableStateOf(false) }
        var expandedQty by remember { mutableStateOf(false) }
        var selectedIndexCategory by remember { mutableStateOf(0) }
        var selectedIndexItems by remember { mutableStateOf(0) }
        var selectedIndexQty by remember { mutableStateOf(0) }
        var selectedCategory by remember { mutableStateOf("") }
        var selectedItems by remember { mutableStateOf("") }
        var selectedQty by remember { mutableStateOf("") }

        val categories = listOf(
            "Baby",
            "DriedProduce",
            "Preserves",
            "Tinned-FruitVeg",
            "Tinned-Meat",
            "Tinned-Other",
            "Toiletries"
        )

        val stock = listOf(
            stockItem("Baby", "Baby Food", "5"),
            stockItem("Baby", "Diapers", "3"),
            stockItem("DriedProduce", "Pasta", "4"),
            stockItem("Preserves", "Jam", "7"),
            stockItem("Preserves", "Marmalade", "7"),
            stockItem("Tinned-FruitVeg", "Peaches", "1"),
            stockItem("Tinned-FruitVeg", "Pineapples", "1"),
            stockItem("Tinned-FruitVeg", "Pears", "1"),
            stockItem("Tinned-FruitVeg", "Corn", "1"),
            stockItem("Tinned-FruitVeg", "Carrots", "1"),
            stockItem("Tinned-FruitVeg", "Peas", "1"),
            stockItem("Tinned-Meat", "Beef", "2"),
            stockItem("Tinned-Meat", "Pork", "2"),
            stockItem("Tinned-Meat", "Chicken", "2"),
            stockItem("Tinned-Other", "Cake Mix", "8"),
            stockItem("Tinned-Other", "Yogurt", "8"),
            stockItem("Tinned-Other", "Custard", "8"),
            stockItem("Toiletries", "Toilet Paper", "9"),
            stockItem("Toiletries", "Soap", "9"),
            stockItem("Toiletries", "Shampoo", "9"),
            stockItem("Toiletries", "Conditioner", "9"),
        )

        val items = mutableListOf<stockItem>()

        for(i in 0 until stock.size){
            if (stock.get(i).category == categories.get(selectedIndexCategory)){
                items.add(stock.get(i))
            }
        }



        val qty = listOf("1", "2", "3", "4", "5")


        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp, vertical = 58.dp)) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {

                    Text(
                        text = "Select the items you wish to add to your package",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(36.dp))

                    Text(
                        text = "Category",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(bottom = 12.dp, start = 8.dp)

                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .border(1.dp, Color.Gray, RoundedCornerShape(20))
                            .clickable(onClick = { expandedCategory = true })
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = categories[selectedIndexCategory],
                                modifier = Modifier
                            )
                            Icon(
                                modifier = Modifier.padding(vertical = 4.dp),
                                painter = painterResource(R.drawable.ic_fluent_chevron_down_12_regular),
                                contentDescription = "Chevron down"
                            )
                        }
                        DropdownMenu(
                            expanded = expandedCategory,
                            onDismissRequest = { expandedCategory = false }) {
                            categories.forEachIndexed { index, s ->
                                DropdownMenuItem(text = {Text(text = categories[index])}, onClick = {
                                    selectedIndexItems = 0
                                    selectedIndexCategory = index
                                    expandedCategory = false
                                })
                            }
                        }
                    }

                    Text(
                        text = "Name",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(top = 28.dp, bottom = 12.dp, start = 8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .border(1.dp, Color.Gray, RoundedCornerShape(20))
                            .clickable(onClick = { expandedItems = true })
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = items[selectedIndexItems].itemName,
                                modifier = Modifier
                            )
                            Icon(
                                modifier = Modifier.padding(vertical = 4.dp),
                                painter = painterResource(com.microsoft.fluent.mobile.icons.R.drawable.ic_fluent_chevron_down_12_regular),
                                contentDescription = "Chevron down"
                            )
                        }
                        DropdownMenu(
                            expanded = expandedItems,
                            onDismissRequest = { expandedItems = false }) {
                            items.forEachIndexed { index, s ->
                                DropdownMenuItem(text = {Text(text = items[index].itemName)}, onClick = {
                                    selectedIndexItems = index
                                    expandedItems = false
                                })
                            }
                        }
                    }

                    Text(
                        text = "Quantity",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(top = 28.dp, bottom = 12.dp, start = 8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .border(1.dp, Color.Gray, RoundedCornerShape(20))
                            .clickable(onClick = { expandedQty = true })
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = qty[selectedIndexQty],
                                modifier = Modifier
                            )
                            Icon(
                                modifier = Modifier.padding(vertical = 4.dp),
                                painter = painterResource(com.microsoft.fluent.mobile.icons.R.drawable.ic_fluent_chevron_down_12_regular),
                                contentDescription = "Chevron down"
                            )
                        }
                        DropdownMenu(
                            modifier = Modifier,
                            expanded = expandedQty,
                            onDismissRequest = { expandedQty = false }) {
                            qty.forEachIndexed { index, s ->
                                DropdownMenuItem(text = {Text(text = qty[index])}, onClick = {
                                    selectedIndexQty = index
                                    expandedQty = false
                                })
                            }
                        }
                    }
                    Button(
                        text = "Add",
                        onClick = { ContentAdder(categories.get(selectedIndexCategory), items.get(selectedIndexItems).itemName, qty.get(selectedIndexQty))
                            navController.navigate("package")},
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(horizontal = 12.dp, vertical = 60.dp)
                    )

                }
            }
        }
    }

    private fun ContentAdder(category: String, item: String, qty: String){
        val currentContents = loggedInAccount.contents
        val addedItems = item + "#" + category + "#" + qty + "#"
        val newContents = currentContents + addedItems

        loggedInAccount.contents = newContents

        for (i in 0 until accountArray.size) {
            if (loggedInAccount.username == accountArray.get(i).username) {
                accountArray.get(i).contents = loggedInAccount.contents
                break
            }
        }
    }

    private fun ContentRemover(itemString: String){
        val currentContents = loggedInAccount.contents
        val removeItems = itemString
        val newContents = currentContents.replace(removeItems, "")

        loggedInAccount.contents = newContents

        for (i in 0 until accountArray.size) {
            if (loggedInAccount.username == accountArray.get(i).username) {
                accountArray.get(i).contents = loggedInAccount.contents
                break
            }
        }
    }

    @Composable
    private fun ConfirmationPickup(context: Context, navController: NavController) {

        var notifCheck by rememberSaveable { mutableStateOf(true) }
        NotificationText().notificationText = "Your package will be ready to collect in 60 minutes."

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 58.dp)
                .fillMaxWidth()
        ) {
            item {
                Column(modifier = Modifier.fillMaxSize()) {

                    Spacer(modifier= Modifier.height(16.dp))
                    Text(
                        text = "Your pickup request has been sent",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(36.dp))

                    Text(
                        text = fullDatePublic,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(64.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Receive a notification before pickup?",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start,

                            )
                        ToggleSwitch(checkedState = notifCheck, onValueChange = {
                            notifCheck = it

                            notifCheckPublic = notifCheck
                        })


                    }
                    Text(
                        text = "You will receive a notification an hour before the pickup time, reminding you to collect the package",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.width(340.dp)
                    )
                    Button(
                        text = "Complete",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 44.dp),
                        onClick = {

                            if(notifCheck) {
                                val schedule =
                                    ((selectedDatePublic.time - 3600000) - System.currentTimeMillis())
                                val notificationWork =
                                    OneTimeWorkRequestBuilder<NotificationWorker>()
                                        .setInitialDelay(schedule, TimeUnit.MILLISECONDS)
                                        .build()



                                WorkManager.getInstance(context).enqueue(notificationWork)
                            }
                            navController.navigate("home")
                        }
                    )

                }
            }
        }
    }

    @Composable
    private fun ConfirmationDelivery(context: Context, navController: NavController) {

        NotificationText().notificationText = "Your package has been delivered to:\n" + address1Public + "\n" + address2Public + "\n" + cityPublic + "\n" + postCodePublic

        var notifCheck by rememberSaveable { mutableStateOf(true) }
        var fullDate by rememberSaveable { mutableStateOf("") }

        val selectedDate = System.currentTimeMillis() + (2 * 24 * 60 * 60 * 1000)

        val fullDateFormatter = SimpleDateFormat("EEEE, d MMMM yyyy")
        val formattedFullDate = fullDateFormatter.format(selectedDate)
        val fullTimeFormatter = SimpleDateFormat("k:mmaa")
        val formattedFullTime = fullTimeFormatter.format(selectedDate)

        fullDate = formattedFullDate + "\n" + "1:00pm"

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 58.dp)
                .fillMaxWidth()
        ) {
            item {
                Column(modifier = Modifier.fillMaxSize()) {

                    Spacer(modifier= Modifier.height(16.dp))
                    Text(
                        text = "Your pickup request has been sent",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(36.dp))

                    Text(text = "Estimated delivery date:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally))
                    Text(
                        text = fullDate,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Package will be delivered to:\n" + address1Public + "\n" + address2Public + "\n" + cityPublic + "\n" + postCodePublic,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Receive a notification after delivery?",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start,

                            )
                        ToggleSwitch(checkedState = notifCheck, onValueChange = {
                            notifCheck = it

                            notifCheckPublic = notifCheck
                        })


                    }
                    Text(
                        text = "You will receive a notification after the package has been dropped off.",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.width(340.dp)
                    )
                    Button(
                        text = "Complete",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 44.dp),
                        onClick = {
                            if(notifCheck) {
                                val schedule =
                                    (selectedDate - System.currentTimeMillis())
                                val notificationWork =
                                    OneTimeWorkRequestBuilder<NotificationWorker2>()
                                        .setInitialDelay(schedule, TimeUnit.MILLISECONDS)
                                        .build()

                                WorkManager.getInstance(context).enqueue(notificationWork)
                            }
                            navController.navigate("home")
                        }
                    )

                }
            }
        }
    }

    class stockItem(category: String, itemName: String, itemQuantity: String) {


        var category: String = category
            get() = field
            set(value) {
                field = value
            }

        var itemName: String = itemName
            get() = field
            set(value) {
                field = value
            }

        var itemQuantity: String = itemQuantity
            get() = field
            set(value) {
                field = value
            }

        init {

        }

    }


    class Account(username: String, password: String, contents: String) {

        var username: String = username
            get() = field
            set(value) {
                field = value
            }

        var password: String = password
            get() = field
            set(value) {
                field = value
            }

        var contents: String = contents
            get() = field
            set(value) {
                field = value
            }

    }
}