package com.raihanresa.storyapp.ui.signup

import androidx.lifecycle.ViewModel
import com.raihanresa.storyapp.repository.UserRepository

class SignupViewModel(private val repository: UserRepository) : ViewModel() {

    fun register(username: String, email: String, password: String) = repository.register(username, email, password)
}
