package com.example.chillileafdiseasedetectionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chillileafdiseasedetectionapp.ui.NavigationItem
import com.example.chillileafdiseasedetectionapp.ui.screen.AboutScreen
import com.example.chillileafdiseasedetectionapp.ui.screen.AnalyzeScreen
import com.example.chillileafdiseasedetectionapp.ui.screen.DetailHistoryScreen
import com.example.chillileafdiseasedetectionapp.ui.screen.HistoryScreen
import com.example.chillileafdiseasedetectionapp.ui.screen.HomeScreen
import com.example.chillileafdiseasedetectionapp.ui.screen.OnboardingScreen
import com.example.chillileafdiseasedetectionapp.ui.screen.ResultScreen
import com.example.chillileafdiseasedetectionapp.ui.theme.ChilliLeafDiseaseDetectionAppTheme
import com.example.chillileafdiseasedetectionapp.ui.viewmodel.AnalyzeViewModel
import com.example.chillileafdiseasedetectionapp.ui.viewmodel.HistoryViewModel
import com.example.chillileafdiseasedetectionapp.ui.viewmodel.HomeViewModel
import com.example.chillileafdiseasedetectionapp.ui.viewmodel.MainViewModel
import com.example.chillileafdiseasedetectionapp.ui.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            ChilliLeafDiseaseDetectionAppTheme {
                val application = LocalContext.current.applicationContext as ChilliApplication
                val factory = ViewModelFactory(
                    analysisResultRepository = application.analysisResultRepository,
                    userPreferencesRepository = application.userPreferencesRepository,
                    application = application
                )
                val mainViewModel: MainViewModel = viewModel(factory = factory)
                val uiState by mainViewModel.uiState.collectAsState()

                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize())
                } else {
                    val startDestination =
                        if (uiState.hasCompletedOnboarding) "main_app" else "onboarding"
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("onboarding") {
                            OnboardingScreen(
                                onOnboardingFinished = {
                                    mainViewModel.setOnboardingCompleted()
                                    navController.navigate("main_app") {
                                        popUpTo("onboarding") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("main_app") {
                            MainScreen(factory = factory)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(factory: ViewModelFactory) {
    val navController = rememberNavController()
    AppNavHost(navController = navController, factory = factory)
}

@Composable
fun AppNavHost(navController: NavHostController, factory: ViewModelFactory) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val analyzeViewModel: AnalyzeViewModel = viewModel(factory = factory)
    val historyViewModel: HistoryViewModel = viewModel(factory = factory)
    val homeViewModel: HomeViewModel = viewModel(factory = factory)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val items = listOf(
                    NavigationItem.Home,
                    NavigationItem.Analyze,
                    NavigationItem.History
                )
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentDestination?.hierarchy?.any { it.route?.startsWith(item.route) == true } == true,
                        onClick = {
                            if (item.route == NavigationItem.History.route) {
                                historyViewModel.setFilter("Semua")
                            }
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationRoute!!) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavigationItem.Home.route) {
                HomeScreen(
                    navController = navController,
                    viewModel = homeViewModel,
                    factory = factory,
                    historyViewModel = historyViewModel
                )
            }
            composable(NavigationItem.Analyze.route) {
                AnalyzeScreen(navController = navController, viewModel = analyzeViewModel)
            }
            composable(NavigationItem.History.route) {
                HistoryScreen(
                    viewModel = historyViewModel,
                    navController = navController
                )
            }
            composable("result") {
                ResultScreen(navController = navController, viewModel = analyzeViewModel)
            }
            composable(
                route = "history/detail/{resultId}",
                arguments = listOf(navArgument("resultId") { type = NavType.StringType })
            ) { navBackStackEntry ->
                val id = navBackStackEntry.arguments?.getString("resultId") ?: return@composable
                DetailHistoryScreen(resultId = id, navController = navController, factory = factory)
            }
            composable("about") {
                AboutScreen(navController = navController)
            }
        }
    }
}