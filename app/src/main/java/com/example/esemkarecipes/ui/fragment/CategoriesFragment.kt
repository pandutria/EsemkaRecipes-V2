package com.example.esemkarecipes.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.esemkarecipes.R
import com.example.esemkarecipes.data.HttpHandler
import com.example.esemkarecipes.data.model.Category
import com.example.esemkarecipes.databinding.FragmentCategoriesBinding
import com.example.esemkarecipes.ui.adapter.CategoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class CategoriesFragment : Fragment() {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)
        showData()
        return binding.root
    }

    fun showData() {
        lifecycleScope.launch {
            val list: MutableList<Category> = mutableListOf()

            val result = withContext(Dispatchers.IO) {
                HttpHandler().request(
                    "categories"
                )
            }

            if (result.code in 200..300) {
                val array = JSONArray(result.body)
                Log.d("debugData", array.toString())

                for (i in 0 until array.length()) {
                    val data = array.getJSONObject(i)

                    list.add(
                        Category(
                            data.getInt("id"),
                            data.getString("name"),
                            data.getString("icon")
                        )
                    )
                }

                binding.rv.adapter = CategoryAdapter(list)
            }
        }
    }
}