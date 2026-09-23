package com.example.kitabusf1.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.kitabusf1.ui.theme.KitabuSF1Theme

/**
 * Root composable of the whole app: the bottom bar and the screen area underneath it.
 *
 * Every screen plugs into this skeleton through [KitabuNavHost], so this file rarely needs
 * to change as new screens and features are added.
 */
@Composable
fun KitabuApp() {
    // rememberNavController() creates the NavController once and keeps it across recompositions.
    // It is created here, at the top, and passed down to both the bar and the NavHost
    // (this is "state hoisting"): both children need the same controller, so their common
    // parent owns it.
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { KitabuBottomBar(navController = navController) }
    ) { innerPadding ->
        // Scaffold passes innerPadding = the space taken up by the bottom bar and system bars.
        // Applying it stops the screen content from being drawn underneath the navigation bar.
        KitabuNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KitabuAppPreview() {
    KitabuSF1Theme {
        KitabuApp()
    }
}
