package com.lester.recipe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lester.recipe.R
import com.lester.recipe.data.model.RecipeInformation

class RecipeAdapter: RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private var onItemClickListener: ((RecipeInformation) -> Unit)? = null

    private val differCallBack = object : DiffUtil.ItemCallback<RecipeInformation>() {
        override fun areItemsTheSame(oldItem: RecipeInformation, newItem: RecipeInformation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RecipeInformation, newItem: RecipeInformation): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = differ.currentList[position]

        holder.itemView.apply{
            Glide
                .with(context)
                .load(recipe.image)
                .into(holder.imageViewRecipe)

            holder.textViewTitle.text = recipe.title
            holder.textViewLikes.text = recipe.aggregateLikes.toString()
            holder.textViewPrice.text = recipe.pricePerServing.toString()

            val separator = " • "
            val dishType = recipe.dishTypes.joinToString(separator)
            holder.textViewDishType.text = dishType

            setOnClickListener {
                onItemClickListener?.let { listener ->
                    listener(recipe)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setOnItemClickListener(listener: (RecipeInformation) -> Unit) {
        onItemClickListener = listener
    }

    inner class RecipeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var imageViewRecipe: ImageView = itemView.findViewById(R.id.imageViewRecipe)
        var textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        var textViewLikes: TextView = itemView.findViewById(R.id.textViewLikes)
        var textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
        var textViewDishType: TextView = itemView.findViewById(R.id.textViewDishType)
    }
}