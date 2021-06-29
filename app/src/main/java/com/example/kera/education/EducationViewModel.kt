package com.example.kera.education

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kera.data.network.AppRepo
import com.example.kera.education.model.EducationListItemModel
import com.example.kera.meals.model.ClassMealsDates
import com.example.kera.profile.ProfileUIModel
import com.example.kera.profile.StudentsData
import com.example.kera.teacherProfile.TeacherProfileUIModel
import kotlinx.coroutines.launch

class EducationViewModel(val appRepo: AppRepo) : ViewModel() {
    var datesListLiveData = MutableLiveData<ArrayList<ClassMealsDates>>()
    var educationList = MutableLiveData<ArrayList<EducationListItemModel>>()
    var monthAndYearTitleLiveData = MutableLiveData<String>()
    private var calendarDate: String? = null
    var message = MutableLiveData<String>()
    var apiError = MutableLiveData<Boolean>()
    var apiErrorDates = MutableLiveData<Boolean>()
    var selectedUser = MutableLiveData<StudentsData>()
    var profileUIModel = MutableLiveData<ProfileUIModel>()
    val language = "en"
    val version = 1
    fun getEducationList(classID: String, date: String) {
        viewModelScope.launch {

            try {
                val response = appRepo.getEducationList(classID, date, language, version)
                educationList.value =
                    EducationListItemModel.convertResponseModelToUIModel(response.data)
            } catch (e: Exception) {
                message.value = e.toString()
                apiError.value = true
            }
        }
    }

    fun getDates(classID: String) {
        viewModelScope.launch {
            try {
                val response = appRepo.getEducationDates(classID)
                datesListLiveData.value = ClassMealsDates.convertDate(response.data!!)

            } catch (e: Exception) {
                message.value = e.toString()
                apiError.value = true
                apiErrorDates.value = true
            }
        }
    }


    fun getUserType() : String{
        return appRepo.getUserTypeFromSharedPref()
    }
    fun getSelectedChildDataFromSharedPref(): StudentsData? {
        var response = appRepo.getSelectedChildData()
        selectedUser.value = response!!
        return appRepo.getSelectedChildData()
    }
    fun getTeacheerProfile(): TeacherProfileUIModel {
        return appRepo.getTeacherData()
    }
    fun getAppRepoInstance() :AppRepo{
        return appRepo
    }
    fun saveChildToSharedPref(studentData: StudentsData) {
        appRepo.saveSelectedChildData(studentData)
    }
    fun getProfileData() {
        viewModelScope.launch {
            try {
                val response = appRepo.getProfileData("en", 1, "user")
                profileUIModel.value = ProfileUIModel.mapResponseModelToUIModel(response.data)
            } catch (e: Exception) {
                apiError.value = true
                message.value = e.toString()
            }
        }
    }
}