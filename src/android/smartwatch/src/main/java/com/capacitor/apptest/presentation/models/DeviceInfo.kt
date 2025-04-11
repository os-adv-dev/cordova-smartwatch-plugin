package com.capacitor.apptest.presentation.models

import androidx.compose.runtime.Stable

@Stable
data class DeviceInfo(
    val nodeId: String?,
    val deviceName: String?
)