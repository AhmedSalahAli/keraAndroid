package com.example.kera.registration.screen1

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kera.data.network.AppRepo
import com.example.kera.meals.model.ClassMealsDates
import com.example.kera.profile.ProfileUIModel
import com.example.kera.profile.StudentsData
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
                message.value = e.toString()
                res.value = true

            }
        }
    }
}