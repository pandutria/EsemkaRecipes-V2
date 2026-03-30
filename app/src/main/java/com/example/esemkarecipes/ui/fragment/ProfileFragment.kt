package com.example.esemkarecipes.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.esemkarecipes.Helper
import com.example.esemkarecipes.data.HttpHandler
import com.example.esemkarecipes.data.model.Category
import com.example.esemkarecipes.data.model.Recipe
import com.example.esemkarecipes.databinding.FragmentProfileBinding
import com.example.esemkarecipes.ui.adapter.RecipeAdapter
import com.example.esemkarecipes.ui.adapter.RecipeLikedAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        showDataMe()
        showData()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        showDataMe()
        showData()
    }

    fun showDataMe() {
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                HttpHandler().request(
                    "me"
                )
            }

            if (result.code in 200..300) {
                val data = JSONObject(result.body)
                binding.tvName.text = "Hello, ${data.getString("fullName")}"

                val bitmap = Helper.loadImage("profiles", data.getString("image"))
                binding.imgImage.setImageBitmap(bitmap)
            }
        }
    }

    fun showData() {
        lifecycleScope.launch {
            val list: MutableList<Recipe> = mutableListOf()
            list.clear()

            val result = withContext(Dispatchers.IO) {
                HttpHandler().request(
                    "me/liked-recipes"
                )

            }

            if (result.code in 200..300) {
                val array = JSONArray(result.body)

                for (i in 0 until array.length()) {
                    val data = array.getJSONObject(i)
                    val category = data.getJSONObject("category")
                    val ingredientData = data.getJSONArray("ingredients")
                    val stepData = data.getJSONArray("steps")

                    val ingredientList: MutableList<String> = mutableListOf()

                    for (ii in 0 until ingredientData.length()) {
                        val ingredient = ingredientData.getString(ii)
                        ingredientList.add(
                            ingredient
                        )
                    }

                    val stepList: MutableList<String> = mutableListOf()

                    for (ii in 0 until stepData.length()) {
                        val step = stepData.getString(ii)
                        stepList.add(
                            step
                        )
                    }

                    list.add(
                        Recipe(
                            id = data.getInt("id"),
                            image = data.getString("image"),
                            isLike = data.getBoolean("isLike"),
                            priceEstimate = data.getInt("priceEstimate"),
                            title = data.getString("title"),
                            cookingTimeEstimate = data.getInt("cookingTimeEstimate"),
                            description = data.getString("description"),
                            ingredients = ingredientList,
                            steps = stepList,
                            category = Category(
                                category.getInt("id"),
                                category.getString("name"),
                                category.getString("icon")
                            )
                        )
                    )
                }

                binding.rv.adapter = RecipeLikedAdapter(list)

                if (list.isEmpty()) {
                    binding.rv.visibility = View.GONE
                    binding.layoutEmpty.visibility = View.VISIBLE
                } else {
                    binding.rv.visibility = View.VISIBLE
                    binding.layoutEmpty.visibility = View.GONE
                }
            }
        }
    }
}