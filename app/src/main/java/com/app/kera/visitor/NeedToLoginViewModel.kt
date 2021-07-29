package com.app.kera.visitor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.kera.data.network.AppRepo

class NeedToLoginViewModel(val appRepo: AppRepo) : ViewModel() {
    var loggedOut = MutableLiveData<Boolean>()

    fun logout() {
        appRepo.logOut()
        loggedOut.value = true
    }
}