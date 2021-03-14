package com.example.kera.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kera.data.network.AppRepo

class NotificationViewModel(val appRepo: AppRepo) : ViewModel() {
    var logo = MutableLiveData<String>()

    init {
        logo.value = getNurseryLogo()
    }

    fun getNurseryLogo(): String {
        return appRepo.getNurseryLogoFromSharedPreference()
    }
}
