package com.raihanresa.storyapp.ui.login

import androidx.lifecycle.ViewModel
import com.raihanresa.storyapp.repository.UserRepository

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    fun login(email: String, password: String) = repository.login(email, password)
}