package com.example.kera.visitor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kera.data.network.AppRepo

class NeedToLoginViewModel(val appRepo: AppRepo) : ViewModel() {
    var loggedOut = MutableLiveData<Boolean>()

    fun logout() {
        appRepo.logOut()
        loggedOut.value = true
    }
}