package com.app.kera.keraLuncher

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.kera.data.models.ProfileResponseModel
import com.app.kera.data.network.AppRepo
import com.app.kera.home.model.HomeNewsUIModel
import com.app.kera.home.model.ImagesUIModel
import com.app.kera.profile.ProfileUIModel
import com.app.kera.profile.StudentsData
import com.app.kera.teacherProfile.TeacherProfileUIModel
import kotlinx.coroutines.launch

class KeraLuncherViewModel(val appRepo: AppRepo) : ViewModel() {
    var message = MutableLiveData<String>()
    var newsList = MutableLiveData<HomeNewsUIModel>()
    var homeNurseryData = MutableLiveData<ImagesUIModel>()
    val profileResponseModel = MutableLiveData<ProfileResponseModel>()
    var profileUIModel = MutableLiveData<ProfileUIModel>()
    var teacherProfileUIModel = MutableLiveData<TeacherProfileUIModel>()
    fun getNewsList(id: String, page: Int) {
        viewModelScope.launch {
            try {
                val response = appRepo.getHomeNews(id, page)
                newsList.value = HomeNewsUIModel.convertResponseModelToUIModel(response.data!!)
            } catch (e: Exception) {
                message.value = e.toString()
            }
        }
    }

    fun getNurseryData() {
        viewModelScope.launch {
            try {
                val response = appRepo.getHomeNurseryData()
                homeNurseryData.value = ImagesUIModel.convertResponseModelToUIModel(response.data!!)
                saveNurseryLogoToSharedPreference(response.data!!.logo)
            } catch (e: Exception) {
                message.value = e.toString()
            }
        }
    }

    private fun saveNurseryLogoToSharedPreference(logo: String?) {
        appRepo.saveNurseryLogoToSharedPreference(logo)
    }

    fun getUserTypeFromSharedPref(): String {
        return appRepo.getUserTypeFromSharedPref()
    }

    fun getProfileDataFromSharedPref(): ProfileUIModel {
        return appRepo.getProfileData()
    }


    fun getProfileData(accessType: String) {
        viewModelScope.launch {
            try {
                val response = appRepo.getProfileData("en", 1, accessType)
                profileUIModel.value = ProfileUIModel.mapResponseModelToUIModel(response.data)
            } catch (e: Exception) {
                message.value = e.message
            }
        }
    }

    fun getTeacherProfileData() {
        viewModelScope.launch {
            try {
                val response = appRepo.getTeacherProfileData("en", 1)
                teacherProfileUIModel.value =
                    TeacherProfileUIModel.mapResponseModelToUIModel(response.data!!)
            } catch (e: Exception) {
                message.value = e.toString()
            }
        }
    }

    fun saveProfileResponseToSharedPref(responseModel: ProfileUIModel) {
        appRepo.saveProfileResponse(responseModel)
    }

    fun getUserType(): String {
        return appRepo.getUserTypeFromSharedPref()
    }

    fun getSelectedChildDataFromSharedPref(): StudentsData? {
        return appRepo.getSelectedChildData()
    }

    fun saveChildDataToSharedPref(studentData: StudentsData) {
        appRepo.saveSelectedChildData(studentData)
    }
}