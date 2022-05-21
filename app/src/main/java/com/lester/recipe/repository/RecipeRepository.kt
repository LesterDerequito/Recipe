package com.lester.recipe.repository

import com.lester.recipe.data.api.RecipeService
import javax.inject.Inject

class RecipeRepository
@Inject
constructor(private val service: RecipeService) {

    suspend fun getAllRecipes() = service.getAllRecipes()

    suspend fun getRecipeInformation(id: Int) = service.getRecipeInformation(id)
}