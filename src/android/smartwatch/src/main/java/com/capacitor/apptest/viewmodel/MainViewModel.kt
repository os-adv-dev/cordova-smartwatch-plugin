package com.capacitor.apptest.viewmodel

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capacitor.apptest.UIComponent
import com.capacitor.apptest.models.SmartwatchTemplate
import com.capacitor.apptest.models.toUIComponents
import com.capacitor.apptest.presentation.models.DeviceInfo
import com.capacitor.apptest.presentation.state.HomeViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _homeViewState = MutableStateFlow(HomeViewState())
    val homeViewState: StateFlow<HomeViewState> = _homeViewState

    private val _uiTemplateState = MutableStateFlow(MessageData())
    val uiTemplateState: StateFlow<MessageData> = _uiTemplateState

    fun updateDeviceInfo(deviceInfo: DeviceInfo) {
        viewModelScope.launch {
            if (deviceInfo.nodeId != null) {
                _homeViewState.update {
                    it.copy(
                        isLoading = false,
                        nodeDeviceId = deviceInfo.nodeId,
                        phoneConnected = true
                    )
                }
            } else {
                _homeViewState.update {
                    it.copy(
                        isLoading = false,
                        nodeDeviceId = null,
                        phoneConnected = false
                    )
                }
            }
        }
    }

    fun updateUITemplateFromScreen(screenKey: String) {
        viewModelScope.launch {
            val screen = when (screenKey) {
                "home" -> _uiTemplateState.value.template?.home
                "detail" -> _uiTemplateState.value.template?.detail
                else -> _uiTemplateState.value.template?.screens?.get(screenKey)
            }

            screen?.let {
                val components = it.toUIComponents()
                _uiTemplateState.update { current ->
                    current.copy(message = components)
                }
            }
        }
    }

    fun updateFullTemplate(template: SmartwatchTemplate) {
        viewModelScope.launch {
            val firstScreen = template.home ?: template.detail ?: template.screens?.values?.firstOrNull()
            val components = firstScreen?.toUIComponents() ?: listOf()
            _uiTemplateState.update {
                it.copy(
                    message = components,
                    template = template
                )
            }
        }
    }

    @Stable
    data class MessageData(
        val message: List<UIComponent>? = listOf(),
        val template: SmartwatchTemplate? = null
    )
}