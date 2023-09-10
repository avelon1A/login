package com.example.presentation



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.data.AuthService
import com.example.data.data_source.LoginCredentials
import com.example.data.data_source.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthViewModel : ViewModel() {

    // Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://dummyjson.com/") // Replace with your actual API base URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Create the AuthService
    private val authService = retrofit.create(AuthService::class.java)
    private val _loginResult = MutableLiveData<String>()
    val loginResult: LiveData<String?> = _loginResult

    fun login(username: String, password: String) {
        val credentials = LoginCredentials(username, password)
        // Make the API call
        authService.login(credentials).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    val success = loginResponse?.token
                    val name = loginResponse?.id
                    if (success != null) {

                            _loginResult.value = name


                        } else {
                            _loginResult.value = "login fail"
                        }

                } else {
                    // Handle API error if needed
                    _loginResult.value =  "API Error: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Handle network or other errors
                _loginResult.value = "Network Error: ${t.message}"
            }
        })
    }



}
