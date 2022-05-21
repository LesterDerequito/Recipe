package com.lester.recipe.ui

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.lester.recipe.R
import com.lester.recipe.databinding.ActivityInformationBinding
import com.lester.recipe.utils.Common.showNetworkError
import com.lester.recipe.utils.Constants.IMAGE_URL
import com.lester.recipe.utils.Constants.RECIPE_ID
import com.lester.recipe.utils.Resource
import com.lester.recipe.viewmodel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class InformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInformationBinding
    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBackButton()
        setIngredients()
        getRecipes()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setBackButton() {
        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setIngredients() {
        val intent = intent
        val recipeId: Int? = intent.getStringExtra(RECIPE_ID)?.toInt()
        viewModel.getRecipeInformation(recipeId)
    }

    private fun getRecipes() {
        viewModel.recipeInformation.observe(this) { response ->
            when(response) {
                is Resource.Success-> {
                    showProgressBar(false)
                    response.data?.let { recipe ->

                        Glide
                            .with(this)
                            .load(recipe.image)
                            .into(binding.imageViewRecipe)

                        binding.textViewTitle.text = recipe.title
                        binding.textViewLikes.text = recipe.aggregateLikes.toString()
                        binding.textViewAmount.text = recipe.pricePerServing.toString()
                        binding.textViewServing.text = recipe.servings.toString()
                        binding.textViewTime.text = recipe.readyInMinutes.toString()
                        binding.textViewDescription.text = convertHtmlToString(recipe.summary)
                        val ingredients =  "Ingredients (${recipe.extendedIngredients.size})"
                        binding.textViewIngredients.text = ingredients

                        for (item in recipe.extendedIngredients) {
                            createIngredients(
                                image = item.image,
                                title = item.aisle,
                                unit = item.amount.toString() + " " + item.unit)
                        }
                    }
                }
                is Resource.Error-> {
                    showProgressBar(false)
                    response.message?.let { message->
                        if (message == "No Internet Connection")
                            showNetworkError(this)
                        else
                            Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading-> {
                    showProgressBar(true)
                }
            }
        }
    }

    private fun createIngredients(image: String, title: String, unit: String) {
        val view = View.inflate(this, R.layout.layout_ingredients, null)
        val imageViewIngredients: ImageView = view.findViewById(R.id.imageViewIngredients)
        val textViewAisle: TextView = view.findViewById(R.id.textViewAisle)
        val textViewUnit: TextView = view.findViewById(R.id.textViewUnit)

        Glide.with(this)
            .load(IMAGE_URL + image)
            .circleCrop()
            .into(imageViewIngredients)

        textViewAisle.text = title
        textViewUnit.text = unit
        binding.layoutIngredients.addView(view)
    }

    private fun convertHtmlToString(value: String): String {
        return HtmlCompat.fromHtml(value, HtmlCompat.FROM_HTML_MODE_COMPACT).toString()
    }

    private fun showProgressBar(boolean: Boolean) {
        if (boolean) {
            binding.layoutLoading.root.visibility = View.VISIBLE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else {
            binding.layoutLoading.root.visibility = View.GONE
            window.clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }
    }
}