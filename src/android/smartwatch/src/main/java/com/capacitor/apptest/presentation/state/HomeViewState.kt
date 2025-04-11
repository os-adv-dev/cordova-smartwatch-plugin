package com.capacitor.apptest.presentation.state

import androidx.compose.runtime.Stable

@Stable
data class HomeViewState(
    val isLoading: Boolean = true,
    val phoneConnected: Boolean = false,
    val nodeDeviceId: String? = null
)