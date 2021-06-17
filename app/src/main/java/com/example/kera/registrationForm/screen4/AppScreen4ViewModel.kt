package com.example.kera.registrationForm.screen4

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kera.data.network.AppRepo
import com.example.kera.registrationForm.screen3.model.PublishAppStep3
import com.example.kera.registrationForm.screen4.model.AccocialtionTermsModel
import com.example.kera.registrationForm.screen4.model.SumbitFinalForm
import kotlinx.coroutines.launch

class AppScreen4ViewModel(val appRepo: AppRepo) : ViewModel() {
    var response = MutableLiveData<AccocialtionTermsModel>()
    var message = MutableLiveData<String>()
    var publishApp1Boolean = MutableLiveData<Boolean>()
    fun getAssociationTerms(associationId :String) {
        viewModelScope.launch {
            try {
//                Log.e("getting report data", studentID)
                val data = appRepo.getAssociationTerms(associationId)
                response.value = data
            } catch (e: Exception) {
                message.value = e.toString()
            }
        }
    }
    fun publishScreen4(model: SumbitFinalForm) {
        viewModelScope.launch {
            try {
                val data = appRepo.publishApp4(model)
                if (data.status == 200) {
                   // message.value = "Form published successfully"
                    publishApp1Boolean.value = true

                }
            } catch (e: Exception) {
                message.value = e.toString()
            }
        }
    }
}