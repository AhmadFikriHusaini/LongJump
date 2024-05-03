package com.example.longjump.routes

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.longjump.MainViewModel
import com.example.longjump.screens.MainScreen

@Composable
fun App() {
    val navController: NavHostController = rememberNavController()
    val viewModel = MainViewModel()
    val context = LocalContext.current
    NavHost(navController = navController, startDestination = ScreenRoutes.mainScreen.route){ 
        composable(ScreenRoutes.mainScreen.route){
            MainScreen(viewModel = viewModel, context = context)
        }
    }
}