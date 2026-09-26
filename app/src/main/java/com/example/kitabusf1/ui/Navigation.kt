package com.example.kitabusf1.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState

// This file holds all navigation logic, cleaner format


/**
 * The three top-level tabs in the bottom navigation bar.
 *
 * Enum is used so the set of tabs is fixed and known at compile time, it gives:
 *  - one single place that defines every tab (route + label + icon stay together),
 *  - `entries` to loop over when drawing the bottom bar, so adding a tab later is a one-line change thus making it easy to expand,
 *  - type safety: code refers to `KitabuDestination.Home`, not a raw "home" string that could be misspelled so you always know exactly where its going.
 */
enum class KitabuDestination(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    Home(route = "home", label = "Home", icon = Icons.Filled.Home),


    Catalogue(route = "catalogue", label = "Catalogue", icon = Icons.AutoMirrored.Filled.List),

    // A calendar icon
    Bookings(route = "bookings", label = "Bookings", icon = Icons.Filled.DateRange);

    companion object {
        // The tab the app opens on: Home is the landing screen
        val startDestination = Home
    }
}

@Composable
fun KitabuBottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // currentBackStackEntryAsState() turns the controller's back stack into Compose State,
    // this means highlighted (selected) tab always matches the screen actually on display.

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(modifier = modifier) {
        KitabuDestination.entries.forEach { destination ->

            val selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true

            NavigationBarItem(
                selected = selected,
                onClick = { navController.navigateToTab(destination) },
                icon = { Icon(imageVector = destination.icon, contentDescription = destination.label) },
                label = { Text(destination.label) }
            )
        }
    }
}

fun NavHostController.navigateToTab(destination: KitabuDestination) {
    navigate(destination.route) {
        // Pop back to the start tab so the back stack doesn't grow every time a tab is tapped.

        popUpTo(graph.findStartDestination().id) {
            // Remember the state (scroll position, etc.) of the tab being left
            saveState = true
        }
        // don't stack a second copy of a tab if the user taps the tab they are already on
        launchSingleTop = true
        // restore the saved state when the user comes back to a tab
        restoreState = true
    }
}
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
        // Each composable(route) { ... } block registers one destination in the graph but the screens themselves live in Home.kt, Catalogue.kt and Bookings.kt.
        composable(KitabuDestination.Home.route) {
            HomeScreen(
                onSeeAllBookings = { navController.navigateToTab(KitabuDestination.Bookings) }
            )
        }
        composable(KitabuDestination.Catalogue.route) { CatalogueScreen() }
        composable(KitabuDestination.Bookings.route) { BookingsScreen() }
    }
}
