package com.raihanresa.storyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.raihanresa.storyapp.R
import com.raihanresa.storyapp.data.local.UserPreference
import com.raihanresa.storyapp.ui.main.MainActivity
import com.raihanresa.storyapp.ui.welcome.WelcomeActivity
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val userPreference = UserPreference.getInstance(this)
        lifecycleScope.launch {
            val token = userPreference.tokenFlow.firstOrNull()
            if (token != null) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
            }
            finish()
        }
    }
}