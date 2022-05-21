package com.lester.recipe.data.api

import com.lester.recipe.data.model.Recipe
import com.lester.recipe.data.model.RecipeInformation
import com.lester.recipe.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeService {

    @GET("recipes/random?")
    suspend fun getAllRecipes(
        @Query("number") number: Int = 30,
        @Query("apiKey") apiKey: String = API_KEY): Response<Recipe>

    @GET("recipes/{id}/information?")
    suspend fun getRecipeInformation(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String = API_KEY): Response<RecipeInformation>
}