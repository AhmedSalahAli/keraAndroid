package com.app.kera.pincode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.kera.BuildConfig
import com.app.kera.data.models.FcmModelRequest
import com.app.kera.data.models.LoginRequestModel
import com.app.kera.data.models.LoginResponseModel
import com.app.kera.data.models.VerifyPhoneRequestModel
import com.app.kera.data.network.AppRepo
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PinCodeViewModel(val appRepo: AppRepo) : ViewModel() {
    val verifyPhoneRequestModel = VerifyPhoneRequestModel()
    val loginRequestModel = LoginRequestModel()
    val loginResponse = MutableLiveData<LoginResponseModel>()
    var message = MutableLiveData<String>()
    var verifyPhoneObserver = MutableLiveData<Boolean>()
    val fcmModelRequest: FcmModelRequest = FcmModelRequest()
    fun verifyPhone(token: String, accessType: String) {
        viewModelScope.launch {
            try {
                val response = appRepo.verifyPhoneNumber("en", 1, verifyPhoneRequestModel, token)
                if (response.status == 200) {
                    verifyPhoneObserver.value = true
                   // appRepo.saveTokenInSharedPref(loginResponse.value!!.token!!)
                }
            } catch (e: Exception) {
                message.value = "Invalid pinCode"
//                if (BuildConfig.DEBUG){
//                    verifyPhoneObserver.value = true
//                }
//                verifyPhoneObserver.value = true
            }
        }
    }

    fun login() {
        loginRequestModel.fcmToken = "test"
        if (loginRequestModel.phone.isEmpty()) {
            message.value = "please enter your phone number"
        } else if (loginRequestModel.phone.length < 10) {
            message.value = "phone number is less than 10 digits"
        } else {
            //loginRequestModel.phone = "01113778820"
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    loginResponse.value = appRepo.login("en", 1, loginRequestModel)
                } catch (e: Exception) {
                    message.value = "Please enter a valid phone number"
                }
            }
        }
    }

    fun saveUserType(type: String) {
        appRepo.saveUserTypeInSharedPref(type)
    }

    fun saveTokenToSharedPref(token: String) {
        appRepo.saveTokenInSharedPref(token)
    }

    fun saveUserLoggedIn() {
        appRepo.saveUserLoggedIN()
    }

//    private fun sendTokenToBackend() {
//        viewModelScope.launch {
//            loginRepo.sendTokenToBackend(fcmModelRequest).collect {
//                setIsLoading(false)
//                loginFinished.value = true
//            }
//        }
//    }

    private fun requestFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            fcmModelRequest.reg = it
            fcmModelRequest.deviceType = "android"

            // token should be added to the login request model
//            loginRepo.saveFcmData(fcmModelRequest)
//            login()
        }
    }
}