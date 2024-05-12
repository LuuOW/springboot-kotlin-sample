package com.cropyback.cropyback.database.entities.product

import com.google.gson.annotations.SerializedName
import org.bson.types.ObjectId
import java.time.LocalDateTime
import java.time.LocalDateTime.now

data class TransactionEntity(
    val origin: ObjectId,
    val destiniy: ObjectId,
    @SerializedName("created_at")
    val createdAt: LocalDateTime = now(),
    @SerializedName("updated_at")
    val updatedAt: LocalDateTime? = null,
    val cost : Double,
)
