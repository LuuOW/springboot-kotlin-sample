package com.cropyback.cropyback.api.ui.models

import com.google.gson.annotations.SerializedName
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.annotation.Id

@Document(collection = "ui")
data class UIModel(
    @Id val id: ObjectId = ObjectId(),
    var model_title: String = "model.title",
    var model_description: String = "model.description",
    var model_image: String = "",
    var model_button_title: String = "model.button.title",
    var model_button_action: String = "",
    var model_primary_color: String = "",
    var model_secondary_color: String = ""
)