package com.example.esemkarecipes.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.esemkarecipes.Helper
import com.example.esemkarecipes.MainActivity
import com.example.esemkarecipes.R
import com.example.esemkarecipes.data.HttpHandler
import com.example.esemkarecipes.data.local.SessionManager
import com.example.esemkarecipes.data.model.User
import com.example.esemkarecipes.databinding.ActivityLoginScreenBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDateTime

class LoginScreen : AppCompatActivity() {
    private var _binding: ActivityLoginScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvSign.setOnClickListener {
            startActivity(Intent(this, RegisterScreen::class.java))
        }

        binding.etUsername.setText("string")
        binding.etPssword.setText("string")

        binding.btn.setOnClickListener {
            if (binding.etUsername.text.toString().isEmpty() || binding.etPssword.text.toString().isEmpty()) {
                Helper.toast(this, "All fields must be filled")
                return@setOnClickListener
            }
            login()
        }
    }

    fun login() {
        lifecycleScope.launch {
            val rBody = JSONObject().apply {
                put("username", binding.etUsername.text.toString())
                put("password", binding.etPssword.text.toString())
            }
            val result = withContext(Dispatchers.IO) {
                HttpHandler().request(
                    "sign-in",
                    "POST",
                    rBody.toString()
                )
            }

            if (result.code in 200..300) {
                val data = JSONObject(result.body)
                SessionManager(this@LoginScreen).user = User(
                    data.getInt("id"),
                    data.getString("username"),
                    data.getString("fullName"),
                    data.getString("password"),
                    data.getString("dateOfBirth"),
                    data.getString("image")
                )
                startActivity(Intent(this@LoginScreen, MainActivity::class.java))
            } else {
                Helper.toast(this@LoginScreen, "Your data is not valid")
            }
        }
    }
}