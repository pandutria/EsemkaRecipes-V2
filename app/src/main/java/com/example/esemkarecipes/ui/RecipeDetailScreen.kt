package com.example.esemkarecipes.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.esemkarecipes.Helper
import com.example.esemkarecipes.R
import com.example.esemkarecipes.data.HttpHandler
import com.example.esemkarecipes.data.model.Category
import com.example.esemkarecipes.data.model.Recipe
import com.example.esemkarecipes.databinding.ActivityRecipeDetailScreenBinding
import com.example.esemkarecipes.databinding.ActivityRecipeScreenBinding
import com.example.esemkarecipes.ui.adapter.IngredientAndStepAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class RecipeDetailScreen : AppCompatActivity() {
    private var _binding: ActivityRecipeDetailScreenBinding? = null
    private val binding get() = _binding!!
    var recipe: Recipe? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityRecipeDetailScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.back.setOnClickListener {
            finish()
        }
        showData(intent.getIntExtra("id", 0))
        isLike(intent.getIntExtra("id", 0))
    }

    fun showData(id: Int) {
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                HttpHandler().request(
                    "recipes/detail/${id}"
                )

            }

            if (result.code in 200..300) {
                val data = JSONObject(result.body)
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

                recipe = Recipe(
                    id = data.getInt("id"),
                    image = data.getString("image"),
                    isLike = false,
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

                binding.tvName.text = recipe?.title
                binding.tvHeader.text = recipe?.title
                binding.tvCategory.text = recipe?.category?.name
                binding.tvCoockingEstimate.text = "Coocking Time Estimate: ${recipe?.cookingTimeEstimate} Minutes"
                binding.tvPriceEstimate.text = "Price Estimate: $${recipe?.priceEstimate}"

                val bitmap = Helper.loadImage("recipes", recipe?.image!!)
                binding.imgImage.setImageBitmap(bitmap)

                binding.rvSteps.adapter = IngredientAndStepAdapter(recipe?.steps!!)
                binding.rvIngredients.adapter = IngredientAndStepAdapter(recipe?.ingredients!!)
            }
        }
    }

    fun isLike(id: Int) {
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                HttpHandler().request(
                    "recipes/like-recipe?recipeId=${id}"
                )
            }

            if (result.code in 200..300) {
                if (result.body == "true") {
                    Log.d("debugDatas", result.body)
                    binding.imgLike.setImageResource(R.drawable.like)
                } else {
                    binding.imgLike.setImageResource(R.drawable.unlike)
                }
            }
        }
    }
}