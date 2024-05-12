package com.cropyback.cropyback.database.entities.user

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.web.multipart.MultipartFile


@Document(collection = "images")
data class ImageEntity(
    val id: ObjectId = ObjectId.get(), // MongoDB's ObjectID for automatic generation
    val fileId: String,
    val filename: String,
    val metadata: Map<String, String>? = null
)