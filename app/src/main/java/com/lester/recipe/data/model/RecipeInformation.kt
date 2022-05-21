package com.lester.recipe.data.model

data class RecipeInformation(
    val id: Int,
    val image: String,
    val title: String,
    val aggregateLikes: Int,
    val pricePerServing: Double,
    val dishTypes: List<String>,
    val servings: Int,
    val summary: String,
    val readyInMinutes: Int,
    val extendedIngredients: List<ExtendedIngredient>
)