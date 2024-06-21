package com.cropyback.cropyback.database.entities.product

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.LocalDateTime.now

data class TransactionEntity(
    val origin: Long,
    val destiniy: Long,
    @SerializedName("created_at")
    val createdAt: LocalDateTime = now(),
    @SerializedName("updated_at")
    val updatedAt: LocalDateTime? = null,
    val cost : Double,
)
