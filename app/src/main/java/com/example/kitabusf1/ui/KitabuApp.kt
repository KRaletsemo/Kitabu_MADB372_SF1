package com.example.kitabusf1.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.kitabusf1.ui.theme.KitabuSF1Theme

@Composable
fun KitabuApp() {

    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { KitabuBottomBar(navController = navController) }
    ) { innerPadding ->
        // Scaffold passes innerPadding (the space taken up by the bottom bar and system bars).
        // Applying it stops the screen content from being drawn underneath the nav bar.
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
