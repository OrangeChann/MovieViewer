package com.it2161.a210297h.a21029hmovieviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.it2161.a210297h.a21029hmovieviewer.ui.components.LandingScreen
import com.it2161.a210297h.a21029hmovieviewer.ui.components.LoginScreen
import com.it2161.a210297h.a21029hmovieviewer.ui.components.MovieDetailScreen
import com.it2161.a210297h.a21029hmovieviewer.ui.components.RegisterScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.it2161.a210297h.a21029hmovieviewer.ui.MovieViewModel
import com.it2161.a210297h.a21029hmovieviewer.ui.UserViewModel
import com.it2161.a210297h.a21029hmovieviewer.ui.components.FavoriteMoviesScreen
import com.it2161.a210297h.a21029hmovieviewer.ui.components.ProfileScreen


data class AppBarState(
    val title: String = "Default Title",
    val navigationIcon: @Composable (() -> Unit) = { },
    val actions: @Composable RowScope.() -> Unit = { },
    val modifier: Modifier = Modifier,
)

enum class AppScreen {
    Login,
    Register,
    LandingScreen,
    MovieDetail,
}

class MovieViewer : ComponentActivity() { // Renamed class from MainActivity to MovieViewer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enables edge-to-edge rendering
        setContent {
            AppMain() // Starts the app's main composable
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppMain() {
    val navController = rememberNavController()
    var appBarState by remember { mutableStateOf(AppBarState()) }
    var currentScreen by remember { mutableStateOf(AppScreen.Login.name) }

    // ✅ Correctly update currentScreen when navigation happens
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentScreen = destination.route ?: AppScreen.Login.name
        }
    }

    Scaffold(
        topBar = {
            if (currentScreen != AppScreen.Login.name && currentScreen != AppScreen.Register.name) {
                BaseAppBar(appBarState)
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.fillMaxSize().padding(innerPadding)
        AppNavHost(
            navController = navController,
            modifier = modifier,
            onAppBarChange = { appBarState = it } // ✅ Ensure AppBar updates correctly
        )
    }
}



//new
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onAppBarChange: (AppBarState) -> Unit
) {
    val movieViewModel: MovieViewModel = viewModel() // ✅ Fix the red error
    val userViewModel: UserViewModel = viewModel() // ✅ Fix the red error

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AppScreen.Login.name
    ) {
        composable(AppScreen.Login.name) {
            LoginScreen(
                modifier = modifier,
                navController = navController
            )
        }

        composable(AppScreen.Register.name) {
            RegisterScreen(
                modifier = modifier,
                navController = navController
            )
        }

        composable("profile") {
            ProfileScreen(
                navController = navController,
                viewModel = userViewModel,
                onAppBarChange = onAppBarChange // ✅ Pass App Bar update function
            )
        }



        composable(AppScreen.LandingScreen.name) {
            LandingScreen(
                viewModel = movieViewModel, // ✅ Pass ViewModel
                navController = navController,
                onAppBarChange = onAppBarChange
            )
        }

        composable("MovieDetail/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull() ?: return@composable
            MovieDetailScreen(movieId = movieId, navController = navController, onAppBarChange = onAppBarChange)
        }


        // ✅ Fixing FavoriteMoviesScreen Navigation
        composable("favorites") {
            FavoriteMoviesScreen(navController = navController, viewModel = movieViewModel, onAppBarChange = onAppBarChange)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseAppBar(appBarState: AppBarState) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = { Text(appBarState.title) },
        actions = appBarState.actions, // ✅ This will now show the menu if LandingScreen sets it
        navigationIcon = appBarState.navigationIcon,
        modifier = appBarState.modifier
    )
}
