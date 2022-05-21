package com.lester.recipe.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.lester.recipe.adapter.RecipeAdapter
import com.lester.recipe.databinding.ActivityMainBinding
import com.lester.recipe.utils.Common.showNetworkError
import com.lester.recipe.utils.Constants.RECIPE_ID
import com.lester.recipe.utils.Resource
import com.lester.recipe.viewmodel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recipeAdapter: RecipeAdapter
    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupListener()
        getRecipes()
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter()
        binding.recyclerViewRecipe.apply {
            adapter = recipeAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            itemAnimator = DefaultItemAnimator()
            setHasFixedSize(true)
        }
    }

    private fun setupListener() {
        recipeAdapter.setOnItemClickListener { recipes ->
            val intent = Intent(this, InformationActivity::class.java)
            intent.putExtra(RECIPE_ID, recipes.id.toString())
            startActivity(intent)
        }
    }

    private fun getRecipes() {
        viewModel.recipes.observe(this) { response ->
            when (response) {
                is Resource.Success-> {
                    showProgressBar(false)
                    response.data?.let { recipe ->
                        recipeAdapter.differ.submitList(recipe.recipes)
                    }
                }
                is Resource.Error-> {
                    showProgressBar(false)
                    response.message?.let { message ->
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