package com.example.kitabusf1.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

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
    Catalog(route = "catalog", label = "Catalog", icon = Icons.AutoMirrored.Filled.List),

    // A calendar icon fits Bookings, since every booking has a reservation date and a return deadline.
    Bookings(route = "bookings", label = "Bookings", icon = Icons.Filled.DateRange);

    companion object {
        /** The tab the app opens on: Home is the landing screen in the chosen layout. */
        val startDestination = Home
    }
}
