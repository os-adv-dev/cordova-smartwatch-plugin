package com.capacitor.apptest.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition

@Composable
fun NoPhoneConnectedScreen(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier
) {
    val scalingLazyListState = rememberScalingLazyListState()
    val context = LocalContext.current

    val drawableId = context.resources.getIdentifier("no_phone_connected", "drawable", context.packageName)
    val errorMessageId = context.resources.getIdentifier("error_connect_device", "string", context.packageName)

    Scaffold(
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scalingLazyListState = scalingLazyListState) },
    ) {
        ScalingLazyColumn(
            modifier = Modifier.background(Color.Black),
            state = scalingLazyListState,
            contentPadding = PaddingValues(
                horizontal = 8.dp,
                vertical = 8.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Image(
                    painter = painterResource(id = drawableId),
                    alignment = Alignment.Center,
                    modifier = iconModifier.size(24.dp),
                    contentDescription = "no phone connected!",
                )
            }
            item {
                Text(
                    modifier = modifier.padding(top = 8.dp, start = 12.dp, end = 12.dp),
                    text = stringResource(id = errorMessageId),
                    color = Color.White,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
private fun NoPhoneConnectedScreenRoundedPreview() {
    NoPhoneConnectedScreen()
}