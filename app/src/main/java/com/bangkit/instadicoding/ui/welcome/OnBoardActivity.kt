package com.bangkit.instadicoding.ui.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        }

        binding.materialButtonSignUp.setOnClickListener {
            val intentSignUp = Intent(this, SignUpActivity::class.java)
            startActivity(intentSignUp)

        }
    }


}