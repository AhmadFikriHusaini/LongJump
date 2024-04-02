package com.example.longjump.routes

sealed class ScreenRoutes(val route: String) {

    object mainScreen: ScreenRoutes("mainScreen")
}