// WearApp.kt
package com.capacitor.apptest.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.compose.navigation.composable
import com.capacitor.apptest.UIComponent
import com.capacitor.apptest.models.toUIComponents
import com.capacitor.apptest.presentation.loading.LoadingScreen
import com.capacitor.apptest.presentation.theme.AndroidTheme
import com.capacitor.apptest.viewmodel.MainViewModel

@Composable
fun WearApp(mainViewModel: MainViewModel) {
    val homeViewState by mainViewModel.homeViewState.collectAsStateWithLifecycle()

    AndroidTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()

            when {
                homeViewState.isLoading -> LoadingScreen()
                homeViewState.phoneConnected -> {
                    NavigationController(mainViewModel)
                }

                !homeViewState.phoneConnected -> {
                    WaitingMobileMessage()
                }

                else -> NoPhoneConnectedScreen()
            }
        }
    }
}

@Composable
fun NavigationController(viewModel: MainViewModel) {
    val navController = rememberSwipeDismissableNavController()
    val uiState by viewModel.uiTemplateState.collectAsStateWithLifecycle()

    SwipeDismissableNavHost(navController = navController, startDestination = "home") {
        composable("home") { backStackEntry ->
            val screenContent = uiState.template?.home?.toUIComponents() ?: listOf()
            DynamicUI(components = screenContent) { action ->
                viewModel.updateUITemplateFromScreen(action)
                navController.navigate(action)
            }
        }
        composable("detail") { backStackEntry ->
            val screenContent = uiState.template?.detail?.toUIComponents() ?: listOf()
            DynamicUI(components = screenContent) { action ->
                viewModel.updateUITemplateFromScreen(action)
                navController.navigate(action)
            }
        }
    }
}

@Composable
fun DynamicUI(components: List<UIComponent>, onAction: (String) -> Unit) {
    Scaffold(
        timeText = { TimeText() },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                components.forEach { component ->
                    when (component) {
                        is UIComponent.Text -> {
                            item {
                                Text(
                                    text = component.content,
                                    style = MaterialTheme.typography.body1,
                                    modifier = Modifier.padding(8.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        is UIComponent.Button -> {
                            item {
                                Button(
                                    onClick = { onAction(component.action) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp)
                                ) {
                                    Text(component.label)
                                }
                            }
                        }
                        is UIComponent.Image -> {
                            item {
                                Image(
                                    bitmap = component.toBitmap().asImageBitmap(),
                                    contentDescription = "image description",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize(0.60f)
                                        .clip(RoundedCornerShape(15.dp))
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun PreviewDynamicUI() {
    val uiTemplate = listOf(
        UIComponent.Text("Preview Template!"),
        UIComponent.Button("ok", "")
    )
    DynamicUI(
        components = uiTemplate,
        onAction = { action -> println("Action clicked: $action") }
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
internal fun PreviewNoPhoneConnectedScreen() {
    NoPhoneConnectedScreen()
}