package com.raihanresa.storyapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.raihanresa.storyapp.di.Injection
import com.raihanresa.storyapp.repository.StoryRepository
import com.raihanresa.storyapp.repository.UserRepository
import com.raihanresa.storyapp.ui.add.AddViewModel
import com.raihanresa.storyapp.ui.login.LoginViewModel
import com.raihanresa.storyapp.ui.main.MainViewModel
import com.raihanresa.storyapp.ui.signup.SignupViewModel

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(AddViewModel::class.java) -> {
                AddViewModel(storyRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            val userRepository = Injection.provideUserRepository(context)
            val storyRepository = Injection.provideStoryRepository(context) // Tambahkan ini di Injection
            return INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(userRepository, storyRepository)
                    .also { INSTANCE = it }
            }
        }
    }
}
