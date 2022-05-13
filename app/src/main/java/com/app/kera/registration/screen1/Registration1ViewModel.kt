package com.app.kera.registration.screen1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.kera.data.network.AppRepo
import kotlinx.coroutines.launch

class Registration1ViewModel(val appRepo: AppRepo) : ViewModel() {
    var step = MutableLiveData<CheckAppStep.ApplicationBean>()
    var res = MutableLiveData<Boolean>()
    var message = MutableLiveData<String>()
    fun getAppStep(udid: String) {
        viewModelScope.launch {
            try {
                val response = appRepo.getAppStep("en", 1, udid)
                step.value = response.application

            } catch (e: Exception) {
              
                res.value = true

            }
        }
    }
}