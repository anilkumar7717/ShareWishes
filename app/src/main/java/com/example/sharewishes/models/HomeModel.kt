package com.example.sharewishes.models

data class HomeModel(
    val categoryId: String,
    val documentId: String,
    val viewType: Int,
    val content: String,
    val favourite: String
)