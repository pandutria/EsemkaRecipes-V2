package com.example.esemkarecipes.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.esemkarecipes.databinding.ItemIngredientAndStepBinding

class IngredientAndStepAdapter(
    private val list: MutableList<String> = mutableListOf()
) : RecyclerView.Adapter<IngredientAndStepAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemIngredientAndStepBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemIngredientAndStepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            binding.tvText.text = list[position]
        }
    }
}