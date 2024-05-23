package com.raihanresa.storyapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.raihanresa.storyapp.data.ResultState
import com.raihanresa.storyapp.data.local.UserPreference
import com.raihanresa.storyapp.data.remote.response.LoginResponse
import com.raihanresa.storyapp.data.remote.response.RegisterResponse
import com.raihanresa.storyapp.data.remote.retrofit.ApiService
import retrofit2.HttpException

class UserRepository(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    fun register(name: String, email: String, password: String): LiveData<ResultState<RegisterResponse>> =
        liveData {
            emit(ResultState.Loading)
            try {
                val response = apiService.register(name, email, password)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.error == true) {
                            emit(ResultState.Error(responseBody.message ?: "Unknown error"))
                        } else {
                            emit(ResultState.Success(responseBody))
                        }
                    } else {
                        emit(ResultState.Error("Response body is null"))
                    }
                } else {
                    emit(ResultState.Error(response.message() ?: "Unknown error"))
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                emit(ResultState.Error(errorResponse.message ?: "Unknown error"))
            } catch (e: Exception) {
                emit(ResultState.Error(e.message ?: "Exception occurred"))
            }
        }

    fun login(email: String, password: String): LiveData<ResultState<LoginResponse>> =
        liveData {
            emit(ResultState.Loading)
            try {
                val response = apiService.login(email, password)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.error == true) {
                            emit(ResultState.Error(responseBody.message ?: "Unknown error"))
                        } else {
                            emit(ResultState.Success(responseBody))
                            responseBody.loginResult?.token?.let { token ->
                                userPreference.saveToken(token)
                                Log.d("Login", "Token saved: $token")
                            }
                        }
                    } else {
                        emit(ResultState.Error("Response body is null"))
                    }
                } else {
                    emit(ResultState.Error(response.message() ?: "Unknown error"))
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
                emit(ResultState.Error(errorResponse.message ?: "Unknown error"))
            } catch (e: Exception) {
                emit(ResultState.Error(e.message ?: "Exception occurred"))
            }
        }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this){
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}
