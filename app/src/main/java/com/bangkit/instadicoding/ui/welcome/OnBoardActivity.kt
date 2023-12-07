package com.bangkit.instadicoding.ui.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.instadicoding.databinding.ActivityOnBoardBinding
import com.bangkit.instadicoding.ui.login.LoginActivity
import com.bangkit.instadicoding.ui.signup.SignUpActivity

class OnBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityOnBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLoginWelcome.setOnClickListener {
            val intentLogin = Intent(this, LoginActivity::class.java)
            startActivity(intentLogin)
            finish()
        }

        binding.materialButtonSignUp.setOnClickListener {
            val intentSignUp = Intent(this, SignUpActivity::class.java)
            startActivity(intentSignUp)
            finish()
        }

        playAnimation()
    }

    @SuppressLint("Recycle")
    private fun playAnimation(){
        val welcomeTitle = ObjectAnimator.ofFloat(binding.welcomeTitle , View.ALPHA, 1f).setDuration(500)
        val welcomeSubTitle = ObjectAnimator.ofFloat(binding.welcomeSubtitle , View.ALPHA, 1f).setDuration(500)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLoginWelcome , View.ALPHA, 1f).setDuration(500)
        val dontaHaveaAccount = ObjectAnimator.ofFloat(binding.dontHaveAccount , View.ALPHA, 1f).setDuration(500)
        val btnSignUp = ObjectAnimator.ofFloat(binding.materialButtonSignUp , View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply{
            playSequentially(welcomeTitle,welcomeSubTitle,btnLogin,dontaHaveaAccount,btnSignUp)
            start()
        }

    }
}