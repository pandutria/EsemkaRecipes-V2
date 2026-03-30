package com.example.esemkarecipes

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.esemkarecipes.databinding.ActivityMainBinding
import com.example.esemkarecipes.ui.fragment.CategoriesFragment
import com.example.esemkarecipes.ui.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        showFragment(CategoriesFragment())
        binding.tvHeader.text = "Categories"

        binding.bottomBar.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.categoryMenu -> {
                    binding.tvHeader.text = "Categories"
                    showFragment(CategoriesFragment())
                    true
                }
                R.id.profileMenu -> {
                    binding.tvHeader.text = "My Profile"
                    showFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, fragment)
            .commit()
    }
}