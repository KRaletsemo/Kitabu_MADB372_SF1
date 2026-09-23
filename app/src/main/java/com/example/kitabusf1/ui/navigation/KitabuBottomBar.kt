package com.example.kitabusf1.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

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
        // Without this, tapping Home -> Catalog -> Home -> Catalog would need 4 back presses to exit.
        popUpTo(graph.findStartDestination().id) {
            // Remember the state (scroll position, etc.) of the tab we're leaving...
            saveState = true
        }
        // ...don't stack a second copy of a tab if the user taps the tab they are already on...
        launchSingleTop = true
        // ...and restore the saved state when the user comes back to a tab.
        restoreState = true
    }
}
