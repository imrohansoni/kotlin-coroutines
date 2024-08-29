package com.imrohansoni.kotlin_coroutines.models

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val categoryId: Int,
    val available: Boolean
)