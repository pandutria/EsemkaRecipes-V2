package com.example.esemkarecipes.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.esemkarecipes.Helper
import com.example.esemkarecipes.R
import com.example.esemkarecipes.data.HttpHandler
import com.example.esemkarecipes.data.local.SessionManager
import com.example.esemkarecipes.data.model.User
import com.example.esemkarecipes.databinding.ActivityRegisterScreenBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class RegisterScreen : AppCompatActivity() {
    private var _binding: ActivityRegisterScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityRegisterScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvSign.setOnClickListener {
            startActivity(Intent(this, LoginScreen::class.java))
        }

        binding.btn.setOnClickListener {
            if (binding.etUsername.text.toString().isEmpty() || binding.etPassword.text.toString().isEmpty()
                || binding.etFullname.text.toString().isEmpty()) {
                Helper.toast(this, "All fields must be filled")
                return@setOnClickListener
            }

            if (binding.etConnPassword.text.toString() != binding.etPassword.text.toString()) {
                Helper.toast(this, "Password and confirm password must be same")
                return@setOnClickListener
            }

            try {
                val date = OffsetDateTime.parse(binding.etDate.text.toString())
                date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            } catch (e: Exception) {
                Helper.toast(this, "Date birth must be format yyyy-MM-dd")
                return@setOnClickListener
            }

            register()
        }
    }

    fun register() {
        lifecycleScope.launch {
            val rBody = JSONObject().apply {
                put("username", binding.etUsername.text.toString())
                put("fullName", binding.etFullname.text.toString())
                put("password", binding.etPassword.text.toString())
                put("dateOfBirth", binding.etDate.text.toString())
            }
            val result = withContext(Dispatchers.IO) {
                HttpHandler().request(
                    "sign-in",
                    "POST",
                    rBody.toString()
                )
            }

            if (result.code in 200..300) {
                startActivity(Intent(this@RegisterScreen, LoginScreen::class.java))
            } else {
                Helper.toast(this@RegisterScreen, "Your data is not valid")
            }
        }
    }
}