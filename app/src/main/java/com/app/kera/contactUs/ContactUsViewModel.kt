package com.app.kera.contactUs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.kera.contactUs.model.ContactUsUIModel
import com.app.kera.data.network.AppRepo

class ContactUsViewModel(val appRepo: AppRepo) : ViewModel() {
    var contactUsUIModel = ContactUsUIModel()
    var logo = MutableLiveData<String>()

    init {

        logo.value = getNurseryLogo()

    }
    fun getNurseryLogo(): String {
        return appRepo.getNurseryLogoFromSharedPreference()
    }
}