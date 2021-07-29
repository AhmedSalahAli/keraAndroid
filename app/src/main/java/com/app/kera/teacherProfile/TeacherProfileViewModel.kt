package com.app.kera.teacherProfile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.kera.data.network.AppRepo
import kotlinx.coroutines.launch

class TeacherProfileViewModel(val appRepo: AppRepo) : ViewModel() {
    //    var profileUIModel = TeacherProfileUIModel()
    var profileUIModel = MutableLiveData<TeacherProfileUIModel>()
    var message = MutableLiveData<String>()
    var logo = MutableLiveData<String>()

    init {
        logo.value = getNurseryLogo()
    }

    fun getProfileData() {
        viewModelScope.launch {
            try {
                val response = appRepo.getTeacherProfileData("en", 1)
                profileUIModel.value =
                    TeacherProfileUIModel.mapResponseModelToUIModel(response.data!!)
            } catch (e: Exception) {
                message.value = e.toString()
            }
        }
    }


    fun getNurseryLogo(): String {
        return appRepo.getNurseryLogoFromSharedPreference()
    }
}