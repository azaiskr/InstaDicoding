package com.bangkit.instadicoding.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.instadicoding.R
import com.bangkit.instadicoding.databinding.ActivitySignUpBinding
import com.bangkit.instadicoding.ui.welcome.OnBoardActivity
import com.bangkit.instadicoding.utils.State
import com.bangkit.instadicoding.utils.ViewModelFactory

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            register(
                name = binding.edRegisterName.text.toString(),
                email = binding.edRegisterEmail.text.toString(),
                password = binding.edRegisterPassword.text.toString(),
            )
        }

        playAnimation()
    }

    private fun playAnimation(){
            val appLogo = ObjectAnimator.ofFloat(binding.appLogo,View.TRANSLATION_Y,-700f,0f).setDuration(200)
            val ivLogin = ObjectAnimator.ofFloat(binding.ivLogin,View.ALPHA,0f,1f).setDuration(500)
            val loginLabel = ObjectAnimator.ofFloat(binding.tvLoginLabel,View.ALPHA,0f,1f).setDuration(500)
            val username = ObjectAnimator.ofFloat(binding.inputUsername,View.ALPHA,0f,1f).setDuration(500)
            val emailInput = ObjectAnimator.ofFloat(binding.inputEmail,View.ALPHA,0f,1f).setDuration(500)
            val passwordInput = ObjectAnimator.ofFloat(binding.inputPaddword,View.ALPHA,0f,1f).setDuration(500)
            val btnLogin = ObjectAnimator.ofFloat(binding.btnRegister,View.ALPHA,0f,1f).setDuration(500)

            AnimatorSet().apply {
                playSequentially(appLogo,ivLogin,loginLabel,username,emailInput,passwordInput,btnLogin)
                start()

        }
    }

    private fun register(name: String, email: String, password: String) {
        viewModel.register(name, email, password).observe(this) {
            when (it) {
                is State.Loading -> {
                    showLoading(true)
                }

                is State.Success -> {
                    showLoading(false)
                    showToast(getString(R.string.register_success))
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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, OnBoardActivity::class.java)
        startActivity(intent)
        finish()
    }
}