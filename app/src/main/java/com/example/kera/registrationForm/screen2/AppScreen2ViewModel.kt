package com.example.kera.registrationForm.screen2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kera.data.network.AppRepo
import com.example.kera.registrationForm.screen1.model.PublishAppStep1Model
import com.example.kera.registrationForm.screen1.model.PublishAppStep2Model
import kotlinx.coroutines.launch

class AppScreen2ViewModel(val appRepo: AppRepo) : ViewModel() {
    var publishApp1Boolean = MutableLiveData<Boolean>()
    var message = MutableLiveData<String>()
    var applicationId = MutableLiveData<String>()
    fun publishScreen2(model: PublishAppStep2Model) {
        viewModelScope.launch {
            try {
                val data = appRepo.publishApp2(model)
                if (data.status == 200) {
                    //message.value = "Form published successfully"
                    publishApp1Boolean.value = true
                    applicationId.value = data.applicationId
                }
            } catch (e: Exception) {
                message.value = e.toString()
            }
        }
    }
}