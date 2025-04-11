package com.outsystems.smartwatch.plugin

import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.apache.cordova.CordovaPlugin
import org.apache.cordova.CallbackContext
import org.apache.cordova.CordovaInterface
import org.apache.cordova.CordovaWebView
import org.json.JSONArray

class SmartwatchPlugin : CordovaPlugin() {

    private lateinit var messageClient: MessageClient
    private lateinit var capabilityClient: CapabilityClient
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    override fun initialize(cordova: CordovaInterface?, webView: CordovaWebView?) {
        super.initialize(cordova, webView)

        cordova?.context?.let { context ->
            messageClient = Wearable.getMessageClient(context)
            capabilityClient = Wearable.getCapabilityClient(context)
        }
    }


    override fun execute(
        action: String,
        args: JSONArray,
        callbackContext: CallbackContext
    ): Boolean {
        if (action == "sendTemplate") {
            scope.launch {
                val message: String = args.getString(0)
                sendTemplate(message, callbackContext)
            }
            return true
        }

        return false
    }

    private suspend fun sendTemplate(message: String, callbackContext: CallbackContext) {
        if (message.isNotEmpty()) {
            try {
                val capabilityInfo: CapabilityInfo = withContext(Dispatchers.IO) {
                    capabilityClient.getCapability(SMARTWATCH_CAPABILITY, CapabilityClient.FILTER_ALL).await()
                }

                val connectedNodes = capabilityInfo.nodes
                if (connectedNodes.isNotEmpty()) {
                    val node: Node = connectedNodes.iterator().next()

                    try {
                        withContext(Dispatchers.IO) {
                            messageClient.sendMessage(node.id, ACTION_MESSAGE_DATA, message.toByteArray()).await()
                        }
                        callbackContext.success()
                    } catch (e: Exception) {
                        callbackContext.error(e.message.toString())
                    }
                } else {
                    callbackContext.error("No nodes found with the required capability.")
                }
            } catch (e: Exception) {
                callbackContext.error("Failed to get capability: ${e.message}")
            }
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    companion object {
        private const val SMARTWATCH_CAPABILITY = "outsystems_smartwatch_app"
        private const val ACTION_MESSAGE_DATA = "ACTION_MESSAGE_DATA"
    }
}