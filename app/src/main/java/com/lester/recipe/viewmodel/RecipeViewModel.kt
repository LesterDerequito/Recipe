package com.lester.recipe.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lester.recipe.data.model.Recipe
import com.lester.recipe.data.model.RecipeInformation
import com.lester.recipe.repository.RecipeRepository
import com.lester.recipe.utils.Network.isConnectionAvailable
import com.lester.recipe.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel
@Inject
constructor(
    private val application: Application,
    private val repository: RecipeRepository): ViewModel() {

    val recipes: MutableLiveData<Resource<Recipe>> = MutableLiveData()
    val recipeInformation: MutableLiveData<Resource<RecipeInformation>> = MutableLiveData()

    init {
        getAllRecipes()
    }

    private fun getAllRecipes() = viewModelScope.launch {
        recipes.postValue(Resource.Loading())
        try {
            if (isConnectionAvailable(application)) {
                val response = repository.getAllRecipes()
                recipes.postValue(handleRecipeResponse(response))
            } else {
                recipes.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> recipes.postValue(Resource.Error("Network Failure"))
                else-> recipes.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun getRecipeInformation(id: Int?) = viewModelScope.launch {
        recipeInformation.postValue(Resource.Loading())
        try {
            if (isConnectionAvailable(application)) {
                val response = repository.getRecipeInformation(id!!)
                recipeInformation.postValue(handleInformationResponse(response))
            } else {
                recipeInformation.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> recipeInformation.postValue(Resource.Error("Network Failure"))
                else-> recipeInformation.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleRecipeResponse(response: Response<Recipe>) : Resource<Recipe> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleInformationResponse(response: Response<RecipeInformation>) : Resource<RecipeInformation> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }
}