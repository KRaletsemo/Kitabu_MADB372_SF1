package com.example.kitabusf1.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kitabusf1.ui.screens.bookings.BookingsScreen
import com.example.kitabusf1.ui.screens.catalog.CatalogScreen
import com.example.kitabusf1.ui.screens.home.HomeScreen

/**
 * The navigation graph: a map from each route string to the composable screen it shows.
 *
 * NavHost is a container that displays whichever screen matches the controller's current route.
 * This is what makes the app "single activity": we never start a new Activity to change screens,
 * we just tell the NavController to show a different composable inside this one host.
 */
@Composable
fun KitabuNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = KitabuDestination.startDestination.route,
        modifier = modifier
    ) {
        // Each composable(route) { ... } block registers one destination in the graph.
        composable(KitabuDestination.Home.route) { HomeScreen() }
        composable(KitabuDestination.Catalog.route) { CatalogScreen() }
        composable(KitabuDestination.Bookings.route) { BookingsScreen() }
    }
}
