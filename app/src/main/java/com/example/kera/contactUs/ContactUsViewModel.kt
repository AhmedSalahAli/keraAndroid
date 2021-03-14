package com.example.kera.contactUs

import androidx.lifecycle.ViewModel
import com.example.kera.contactUs.model.ContactUsUIModel
import com.example.kera.data.network.AppRepo

class ContactUsViewModel(val appRepo: AppRepo) : ViewModel() {
    var contactUsUIModel = ContactUsUIModel()
}