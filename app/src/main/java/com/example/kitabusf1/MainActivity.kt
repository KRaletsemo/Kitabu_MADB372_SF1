package com.example.kitabusf1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.kitabusf1.ui.KitabuApp
import com.example.kitabusf1.ui.theme.KitabuSF1Theme

/**
 * The app's only Activity (single-activity architecture).
 *
 * It does as little as possible: apply the theme and hand over to [KitabuApp].
 * All screen switching happens inside Compose via Navigation, not by launching new Activities.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Draw behind the status and navigation bars; Scaffold's innerPadding keeps content clear of them.
        enableEdgeToEdge()
        setContent {
            KitabuSF1Theme {
                KitabuApp()
            }
        }
    }
}
