package com.cropyback.cropyback.database.entities.product

import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.time.LocalDateTime.now

@Document(collection = "products")
data class ProductEntity(
    val name: String,
    val description: String,
    val category: String = "Other",
    val image: ByteArray? = null,
    val transactions: List<TransactionEntity> = emptyList(),
)
