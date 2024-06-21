package com.cropyback.cropyback.api.ui.models

import jakarta.persistence.*

@Entity
@Table(name="ui")
data class UIModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val model_title: String = "model.title",
    val model_description: String = "model.description",
    val model_image: String = "",
    val model_buttons_titles: Array<String> = arrayOf("model.button.title"),
    val model_buttons_images: Array<String> = arrayOf(),
    val model_buttons_actions: Array<String> = arrayOf(),
    val model_primary_color: String = "",
    val model_secondary_color: String = ""
)