package com.bangkit.instadicoding.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.bangkit.instadicoding.R
import com.bangkit.instadicoding.databinding.ActivityLoginBinding
import com.bangkit.instadicoding.ui.main.MainActivity
import com.bangkit.instadicoding.ui.main.MainViewModel
import com.bangkit.instadicoding.ui.signup.SignUpViewModel
import com.bangkit.instadicoding.ui.welcome.OnBoardActivity
import com.bangkit.instadicoding.utils.State
import com.bangkit.instadicoding.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBtnLoginClicked()
        observeLoginResult()
    }

    private fun onBtnLoginClicked(){
        binding.btnLoginWelcome.setOnClickListener {
            viewModel.login(
                email = binding.edLoginEmail.text.toString(),
                password = binding.edLoginPassword.text.toString(),
            )
        }
    }

    private fun observeLoginResult() {
        viewModel.loginResult.observe(this) {
            when(it) {
                is State.Loading -> {
                    binding.progressIndicator.visibility = android.view.View.VISIBLE
                }
                is State.Success -> {
                    binding.progressIndicator.visibility = android.view.View.GONE
                    viewModel.saveSession(it.data)
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is State.Error -> {
                    binding.progressIndicator.visibility = android.view.View.GONE
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

//    @Deprecated("Deprecated in Java")
//    override fun onBackPressed() {
//        super.onBackPressed()
//        val intent = Intent(this, OnBoardActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
}