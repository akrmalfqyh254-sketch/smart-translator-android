package com.smarttranslator.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.smarttranslator.app.ui.screens.CameraScreen
import com.smarttranslator.app.ui.screens.FavoritesScreen
import com.smarttranslator.app.ui.screens.HistoryScreen
import com.smarttranslator.app.ui.screens.HomeScreen
import com.smarttranslator.app.ui.screens.SettingsScreen

@Composable
fun SmartTranslatorApp() {
    val navController: NavHostController = rememberNavController()
    AppNavHost(navController)
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("history") { HistoryScreen(navController) }
        composable("favorites") { FavoritesScreen(navController) }
        composable("camera") { CameraScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
    }
}
