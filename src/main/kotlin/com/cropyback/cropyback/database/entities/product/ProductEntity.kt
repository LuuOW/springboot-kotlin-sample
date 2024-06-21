package com.cropyback.cropyback.database.entities.product

data class ProductEntity(
    val name: String,
    val description: String,
    val category: String = "Other",
    val image: ByteArray? = null,
    val transactions: List<TransactionEntity> = emptyList(),
)
