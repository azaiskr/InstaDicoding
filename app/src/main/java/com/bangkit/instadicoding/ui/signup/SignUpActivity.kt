package com.bangkit.instadicoding.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bangkit.instadicoding.databinding.ActivitySignUpBinding
import com.bangkit.instadicoding.ui.main.MainViewModel
import com.bangkit.instadicoding.ui.welcome.OnBoardActivity
import com.bangkit.instadicoding.utils.State
import com.bangkit.instadicoding.utils.ViewModelFactory

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val viewModel by viewModels<SignUpViewModel>{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val viewModelFactory: ViewModelFactory = ViewModelFactory.getInstance(this)
////        val viewModel = ViewModelProvider(this, viewModelFactory)[SignUpViewModel::class.java]

        onBtnRegisterClicked()
        observeState()
    }

    private fun onBtnRegisterClicked() {
        binding.btnRegister.apply {
            setOnClickListener {
                viewModel.register(
                    name = binding.edRegisterName.text.toString(),
                    email = binding.edRegisterEmail.text.toString(),
                    password = binding.edRegisterPassword.text.toString(),
                )
            }
        }
    }

    private fun observeState(){
        viewModel.registerState.observe(this){
            when(it){
                is State.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                }
                is State.Success -> {
                    binding.progressIndicator.visibility = View.GONE
                    Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, OnBoardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is State.Error -> {
                    binding.progressIndicator.visibility = View.GONE
                    Toast.makeText(
                        this,
                        "Registration failed: ${it.error.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("RegistrationError", "Error during registration", it.error)
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, OnBoardActivity::class.java)
        startActivity(intent)
        finish()
    }
}