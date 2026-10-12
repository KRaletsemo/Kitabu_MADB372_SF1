package com.example.kitabusf1.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val CottageCoreColorScheme = lightColorScheme(
    primary = CottageOliveDeep,
    onPrimary = CottageCream,
    primaryContainer = CottageSage,
    onPrimaryContainer = CottageInk,
    secondary = CottageOlive,
    onSecondary = CottageCream,
    secondaryContainer = CottageTan,
    onSecondaryContainer = CottageBrown,
    tertiary = CottageRose,
    onTertiary = CottageRoseDeep,
    tertiaryContainer = CottageRose,
    onTertiaryContainer = CottageRoseDeep,
    background = CottageCream,
    onBackground = CottageInk,
    surface = CottageCream,
    onSurface = CottageInk,
    surfaceVariant = CottageTan,
    onSurfaceVariant = CottageInkSoft,
    surfaceContainerLowest = CottagePaper,
    surfaceContainerLow = CottageSurfaceLow,
    surfaceContainer = CottageSurfaceMid,
    surfaceContainerHigh = CottageSurfaceHigh,
    surfaceContainerHighest = CottageSurfaceHighest,
    inverseSurface = CottageInk,
    inverseOnSurface = CottageCream,
    inversePrimary = CottageSage,
    outline = CottageOutline,
    outlineVariant = CottageOutlineSoft,
    error = CottageBrick,
    onError = CottageCream
)

@Composable
fun KitabuSF1Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CottageCoreColorScheme,
        typography = Typography,
        content = content
    )
}
