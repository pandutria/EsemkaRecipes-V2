package com.example.esemkarecipes.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.esemkarecipes.Helper
import com.example.esemkarecipes.data.model.Category
import com.example.esemkarecipes.data.model.Recipe
import com.example.esemkarecipes.databinding.ItemCategoryBinding
import com.example.esemkarecipes.databinding.ItemLikedRecipeBinding
import com.example.esemkarecipes.databinding.ItemRecipeBinding
import com.example.esemkarecipes.ui.RecipeDetailScreen
import com.example.esemkarecipes.ui.RecipeScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeLikedAdapter(
    private val list: MutableList<Recipe> = mutableListOf()
): RecyclerView.Adapter<RecipeLikedAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemLikedRecipeBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLikedRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = list[position]
        holder.apply {
            CoroutineScope(Dispatchers.Main).launch {
                val bitmap = Helper.loadImage("recipes", recipe.image)
                binding.imgImage.setImageBitmap(bitmap)
            }

            binding.root.setOnClickListener {
                val i = Intent(binding.root.context, RecipeDetailScreen::class.java)
                i.putExtra("id", recipe.id)
                binding.root.context.startActivity(i)
            }
        }
    }
}