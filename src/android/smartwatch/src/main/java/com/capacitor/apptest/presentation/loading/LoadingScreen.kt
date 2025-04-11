package com.capacitor.apptest.presentation.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier =
        modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            indicatorColor = Color.White,
            trackColor = MaterialTheme.colors.onBackground.copy(alpha = 0.1f),
        )
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
private fun LoadingScreenRoundedPreview() {
    LoadingScreen()
}

@Preview(device = Devices.WEAR_OS_SQUARE, showSystemUi = true)
@Composable
private fun LoadingScreenSquarePreview() {
    LoadingScreen()
}
