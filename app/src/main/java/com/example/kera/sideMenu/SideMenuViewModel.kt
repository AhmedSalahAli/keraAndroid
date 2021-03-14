package com.example.kera.sideMenu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kera.data.network.AppRepo

class SideMenuViewModel(val appRepo: AppRepo) : ViewModel() {
    // TODO: Implement the ViewModel
    var loggedOut = MutableLiveData<Boolean>()

    fun logout() {
        appRepo.logOut()
        loggedOut.value = true
    }
}