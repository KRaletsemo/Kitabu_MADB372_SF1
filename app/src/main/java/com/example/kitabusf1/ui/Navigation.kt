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

// This file holds everything to do with moving between screens:
//   1. KitabuDestination - the list of tabs
//   2. KitabuBottomBar   - the bar the user taps
//   3. KitabuNavHost     - the container that shows the screen for the selected tab

/**
 * The three top-level tabs in the bottom navigation bar.
 *
 * Why an enum? The set of tabs is fixed and known at compile time, so an enum gives us:
 *  - one single place that defines every tab (route + label + icon stay together),
 *  - `entries` to loop over when drawing the bottom bar, so adding a tab later is a one-line change,
 *  - type safety: code refers to `KitabuDestination.Home`, not a raw "home" string that could be misspelled.
 *
 * @property route the unique string key Navigation Compose uses to identify this screen in the NavHost.
 * @property label the text shown under the icon in the bottom bar.
 * @property icon the Material icon shown in the bottom bar.
 */
enum class KitabuDestination(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    Home(route = "home", label = "Home", icon = Icons.Filled.Home),

    // AutoMirrored means the list icon flips automatically for right-to-left languages.
    Catalogue(route = "catalogue", label = "Catalogue", icon = Icons.AutoMirrored.Filled.List),

    // A calendar icon fits Bookings, since every booking has a reservation date and a return deadline.
    Bookings(route = "bookings", label = "Bookings", icon = Icons.Filled.DateRange);

    companion object {
        /** The tab the app opens on: Home is the landing screen in the chosen layout. */
        val startDestination = Home
    }
}

/**
 * Material 3 bottom navigation bar with one item per [KitabuDestination].
 *
 * It takes the [NavHostController] as a parameter instead of creating its own, because the bar
 * and the NavHost must share the *same* controller: the bar tells it where to go, and the
 * NavHost reacts by swapping the visible screen.
 */
@Composable
fun KitabuBottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // currentBackStackEntryAsState() turns the controller's back stack into Compose State.
    // Whenever the user navigates, this value changes and the bar recomposes, so the
    // highlighted (selected) tab always matches the screen actually on display.
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(modifier = modifier) {
        KitabuDestination.entries.forEach { destination ->
            // `hierarchy` walks up from the current screen through its parent graphs.
            // Checking the whole hierarchy (not just the exact route) keeps the tab selected
            // later on, if we add nested screens inside a tab (e.g. a book detail under Catalog).
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

/**
 * Standard "switch bottom tab" navigation. It is an extension function so other code (e.g. the
 * "See all" links on the Home screen later) can jump to a tab with exactly the same behaviour.
 */
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
        // Each composable(route) { ... } block registers one destination in the graph but the screens themselves live in Home.kt, Catalogue.kt and Bookings.kt.
        composable(KitabuDestination.Home.route) { HomeScreen() }
        composable(KitabuDestination.Catalogue.route) { CatalogueScreen() }
        composable(KitabuDestination.Bookings.route) { BookingsScreen() }
    }
}
