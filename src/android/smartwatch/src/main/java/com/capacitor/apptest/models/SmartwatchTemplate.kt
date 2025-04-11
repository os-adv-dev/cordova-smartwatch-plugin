package com.capacitor.apptest.models

import kotlinx.serialization.Serializable

@Serializable
data class SmartwatchTemplate(
    val home: TemplateScreen? = null,
    val detail: TemplateScreen? = null,
    val screens: Map<String, TemplateScreen>? = null
)

@Serializable
data class TemplateScreen(
    val content: ScreenContent
)

@Serializable
data class ScreenContent(
    val title: String? = null,
    val description: String? = null,
    val image: String? = null,
    val space: String? = null,
    val list: List<ListItem>? = null
)

@Serializable
data class ListItem(
    val text: String,
    val image: String? = null,
    val action: String? = null
)

fun TemplateScreen.toUIComponents(): List<com.capacitor.apptest.UIComponent> {
    val result = mutableListOf<com.capacitor.apptest.UIComponent>()

    content.title?.let {
        result.add(com.capacitor.apptest.UIComponent.Text(it))
    }

    content.description?.let {
        result.add(com.capacitor.apptest.UIComponent.Text(it))
    }

    content.image?.let {
        val imgData = if (it.startsWith("data:image")) it else "data:image/png;base64,$it"
        result.add(com.capacitor.apptest.UIComponent.Image(imgData))
    }

    content.list?.forEach { item ->
        result.add(com.capacitor.apptest.UIComponent.Button(
            label = item.text,
            action = item.action ?: ""
        ))
    }

    return result
}