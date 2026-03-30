package com.example.esemkarecipes.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.esemkarecipes.Helper
import com.example.esemkarecipes.data.model.Category
import com.example.esemkarecipes.databinding.ItemCategoryBinding
import com.example.esemkarecipes.ui.RecipeScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryAdapter(
    private val list: MutableList<Category> = mutableListOf()
): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = list[position]
        holder.apply {
            binding.tvName.text = category.name

            CoroutineScope(Dispatchers.Main).launch {
                val bitmap = Helper.loadImage("categories", category.icon)
                binding.imgImage.setImageBitmap(bitmap)
            }

            binding.root.setOnClickListener {
                val i = Intent(binding.root.context, RecipeScreen::class.java)
                i.putExtra("categoryId", category.id)
                binding.root.context.startActivity(i)
            }
        }
    }
}