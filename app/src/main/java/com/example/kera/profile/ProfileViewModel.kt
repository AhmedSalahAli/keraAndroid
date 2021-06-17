package com.example.kera.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kera.data.network.AppRepo
import kotlinx.coroutines.launch

class ProfileViewModel(val appRepo: AppRepo) : ViewModel() {
    var profileUIModel = MutableLiveData<ProfileUIModel>()
    var message = MutableLiveData<String>()
    var logo = MutableLiveData<String>()
    var selectedUser = MutableLiveData<StudentsData>()

    init {

        logo.value = getNurseryLogo()
        getSelectedChildDataFromSharedPref()
    }

    fun getProfileData() {
        viewModelScope.launch {
            try {
                val response = appRepo.getProfileData("en", 1, "user")
                profileUIModel.value = ProfileUIModel.mapResponseModelToUIModel(response.data)
            } catch (e: Exception) {
                message.value = e.toString()
            }
        }
    }

    fun saveChildDataToSharedPref(studentData: StudentsData) {
        appRepo.saveSelectedChildData(studentData)
    }

    fun getSelectedChildDataFromSharedPref(): StudentsData? {
        val response = appRepo.getSelectedChildData()
        selectedUser.value = response!!
        selectedUser.value!!.studentCode = "Code:" + response.studentCode
        selectedUser.value!!.className = "Class:" + response.className
        return response
    }
    fun getAppRepoInstance() :AppRepo{
        return appRepo
    }
    fun getNurseryLogo(): String {
        return appRepo.getNurseryLogoFromSharedPreference()
    }
}