package com.example.esemkarecipes.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.esemkarecipes.R
import com.example.esemkarecipes.data.HttpHandler
import com.example.esemkarecipes.data.model.Category
import com.example.esemkarecipes.data.model.Recipe
import com.example.esemkarecipes.databinding.ActivityRecipeScreenBinding
import com.example.esemkarecipes.ui.adapter.RecipeAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class RecipeScreen : AppCompatActivity() {
    private var _binding: ActivityRecipeScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityRecipeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.back.setOnClickListener {
            finish()
        }

        showData(intent.getIntExtra("categoryId", 0), binding.etSearch.text.toString())

        binding.etSearch.addTextChangedListener {
            showData(intent.getIntExtra("categoryId", 0), binding.etSearch.text.toString())
        }
    }

    fun showData(categoryId: Int? = null, search: String) {
        lifecycleScope.launch {
            val list: MutableList<Recipe> = mutableListOf()
            list.clear()

            val result = withContext(Dispatchers.IO) {
                if (categoryId != null && search.isNotEmpty()) {
                    HttpHandler().request(
                        "recipes?categoryId=$categoryId&search=$search"
                    )
                } else if (categoryId != null) {
                    HttpHandler().request(
                        "recipes?categoryId=$categoryId"
                    )
                } else if (search.isNotEmpty()) {
                    HttpHandler().request(
                        "recipes?search=$search"
                    )
                } else {
                    HttpHandler().request(
                        "recipes"
                    )
                }
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

                binding.rv.adapter = RecipeAdapter(list)
                binding.etSearch.setHint("Search ${list.count()} recipes")

                if (list.isEmpty()) {
                    binding.layoutContent.visibility = View.GONE
                    binding.imgNoRecipe.visibility = View.VISIBLE
                } else {
                    binding.tvHeader.text = list[0].category.name
                    binding.layoutContent.visibility = View.VISIBLE
                    binding.imgNoRecipe.visibility = View.GONE
                }
            }
        }
    }
}