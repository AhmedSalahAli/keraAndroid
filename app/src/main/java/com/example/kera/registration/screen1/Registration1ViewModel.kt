package com.example.kera.registration.screen1

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
    var message = MutableLiveData<String>()
    fun getAppStep(appStepModel: AppStepModel) {
        viewModelScope.launch {
            try {
                val response = appRepo.getAppStep("en", 1, appStepModel)
                step.value = response.application
            } catch (e: Exception) {
                message.value = e.toString()
            }
        }
    }
}