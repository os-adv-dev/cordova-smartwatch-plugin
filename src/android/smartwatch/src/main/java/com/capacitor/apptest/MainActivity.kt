package com.capacitor.apptest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.capacitor.apptest.presentation.WearApp
import com.capacitor.apptest.presentation.detector.PhoneDetectorImpl
import com.capacitor.apptest.viewmodel.MainViewModel
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.launch
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.capacitor.apptest.models.SmartwatchTemplate
import com.capacitor.apptest.models.toUIComponents
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity(), MessageClient.OnMessageReceivedListener {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)

        val capabilityClient = lazy { Wearable.getCapabilityClient(this) }
        val phoneDetector = lazy { PhoneDetectorImpl(capabilityClient.value) }

        lifecycleScope.launch {
            phoneDetector.value.phoneStatusFlow.collect { deviceInfo ->
                mainViewModel.updateDeviceInfo(deviceInfo)
            }
        }

        setContent {
            WearApp(mainViewModel)
        }
    }

    override fun onResume() {
        super.onResume()
        Wearable.getMessageClient(this).addListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Wearable.getMessageClient(this).removeListener(this)
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        try {
            val jsonString = String(messageEvent.data, Charsets.UTF_8)
            val unescapedJson = org.json.JSONObject("{\"data\":$jsonString}").getString("data")
            val template = Json.decodeFromString<SmartwatchTemplate>(unescapedJson)

            val screen = template.home ?: template.detail ?: template.screens?.values?.firstOrNull()
            if (screen != null) {
                val components = screen.toUIComponents()
                mainViewModel.updateFullTemplate(template)
            }
        } catch (e: Exception) {
            Log.v("Smartwatch", ">>>>> Erro ao processar JSON do template: ${e.message}")
        }
    }
}

sealed class UIComponent {
    data class Text(val content: String) : UIComponent(), java.io.Serializable
    data class Button(val label: String, val action: String) : UIComponent(), java.io.Serializable
    data class Image(val base64: String) : UIComponent(), java.io.Serializable {
        fun toBitmap(): Bitmap {
            try {
                val cleanBase64 = if (base64.contains("base64,")) {
                    base64.substringAfter("base64,")
                } else {
                    base64
                }
                val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
                return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                    ?: throw IllegalArgumentException("Bitmap decode returned null.")
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("Invalid Base64 string: ${e.message}", e)
            }
        }
    }
}