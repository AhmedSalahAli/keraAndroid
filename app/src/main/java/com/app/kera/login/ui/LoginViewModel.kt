package com.app.kera.login.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.kera.data.models.LoginRequestModel
import com.app.kera.data.models.LoginResponseModel
import com.app.kera.data.network.AppRepo
import kotlinx.coroutines.launch

class LoginViewModel(val appRepo: AppRepo) : ViewModel() {
    val loginRequestModel = LoginRequestModel()
    val loginResponse = MutableLiveData<LoginResponseModel>()
    val loginFailed = MutableLiveData<Boolean>()
    var message = MutableLiveData<String>()

    fun login(fcmToken :String) {
        loginRequestModel.fcmToken = fcmToken
        loginRequestModel.deviceType = "Android"
        when {
            loginRequestModel.phone.isEmpty() -> {
                message.value = "please enter your phone number"
            }
            loginRequestModel.phone.length < 10 -> {
                message.value = "phone number is less than 10 digits"
            }
            else -> {
                //loginRequestModel.phone = "01113778820"
                viewModelScope.launch {
                    try {
                        loginResponse.value = appRepo.login("en", 1, loginRequestModel)
                    } catch (e: Exception) {
                        loginFailed.value = true
                        message.value = "Please enter a valid phone number"
                    }
                }
            }
        }
    }
    fun saveTokenToSharedPref(token: String) {
        appRepo.saveTokenInSharedPref(token)
    }
    fun saveUserType(type: String) {
        appRepo.saveUserTypeInSharedPref(type)
    }
    fun saveNurseryLogoToSharedPreference(logo: String?) {
        appRepo.saveNurseryLogoToSharedPreference(logo)
    }
}