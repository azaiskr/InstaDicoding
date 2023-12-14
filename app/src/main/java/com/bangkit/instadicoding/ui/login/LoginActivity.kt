package com.bangkit.instadicoding.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.instadicoding.databinding.ActivityLoginBinding
import com.bangkit.instadicoding.ui.main.MainActivity
import com.bangkit.instadicoding.ui.welcome.OnBoardActivity
import com.bangkit.instadicoding.utils.State
import com.bangkit.instadicoding.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLoginWelcome.setOnClickListener {
            login(
                binding.edLoginEmail.text.toString(),
                binding.edLoginPassword.text.toString()
            )
        }

        playAnimation()
    }

    private fun playAnimation(){
        val appLogo = ObjectAnimator.ofFloat(binding.appLogo,View.TRANSLATION_Y,-700f,0f).setDuration(200)
        val ivLogin = ObjectAnimator.ofFloat(binding.ivLogin,View.ALPHA,0f,1f).setDuration(500)
        val loginLabel = ObjectAnimator.ofFloat(binding.tvLoginLabel,View.ALPHA,0f,1f).setDuration(500)
        val emailInput = ObjectAnimator.ofFloat(binding.emailInput,View.ALPHA,0f,1f).setDuration(500)
        val passwordInput = ObjectAnimator.ofFloat(binding.passwordInput,View.ALPHA,0f,1f).setDuration(500)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLoginWelcome,View.ALPHA,0f,1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(appLogo,ivLogin,loginLabel,emailInput,passwordInput,btnLogin)
            start()
        }

    }

    private fun login(email: String, password: String) {
        viewModel.login(email, password).observe(this) {
            when (it) {
                is State.Loading -> {
                    showLoading(true)
                }

                is State.Success -> {
                    showLoading(false)
                    showToast("Welcome ${it.data.name} ") //recheck
                    viewModel.saveSession(it.data)

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                is State.Error -> {
                    showLoading(false)
                    showToast(it.error)
                }
            }
        }
    }

    private fun showToast(mssg: String?) {
        Toast.makeText(this, mssg, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, OnBoardActivity::class.java)
        startActivity(intent)
        finish()
    }
}