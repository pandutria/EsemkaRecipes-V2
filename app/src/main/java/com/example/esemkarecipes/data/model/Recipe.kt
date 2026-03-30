package com.example.esemkarecipes.data.model

data class Recipe(
    val category: Category,
    val cookingTimeEstimate: Int,
    val description: String,
    val id: Int,
    val image: String,
    val ingredients: MutableList<String>,
    val isLike: Boolean,
    val priceEstimate: Int,
    val steps: MutableList<String>,
    val title: String
)