package com.capacitor.apptest.presentation.detector

import com.capacitor.apptest.presentation.models.DeviceInfo
import kotlinx.coroutines.flow.Flow

interface PhoneDetector {
    val phoneStatusFlow: Flow<DeviceInfo>
}