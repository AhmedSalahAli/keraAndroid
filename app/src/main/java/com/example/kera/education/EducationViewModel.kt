package com.example.kera.education

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kera.data.network.AppRepo
import com.example.kera.education.model.EducationListItemModel
import com.example.kera.meals.model.ClassMealsDates
import com.example.kera.profile.StudentsData
import kotlinx.coroutines.launch

class EducationViewModel(val appRepo: AppRepo) : ViewModel() {
    var datesListLiveData = MutableLiveData<ArrayList<ClassMealsDates>>()
    var educationList = MutableLiveData<ArrayList<EducationListItemModel>>()
    var monthAndYearTitleLiveData = MutableLiveData<String>()
    private var calendarDate: String? = null
    var message = MutableLiveData<String>()

    val language = "en"
    val version = 1
    fun getEducationList(classID: String, date: String) {
        viewModelScope.launch {
            val response = appRepo.getEducationList(classID, date, language, version)
            educationList.value =
                EducationListItemModel.convertResponseModelToUIModel(response.data)
        }
    }

    fun getDates(classID: String) {
        viewModelScope.launch {
            try {
                val response = appRepo.getEducationDates(classID)
                datesListLiveData.value = ClassMealsDates.convertDate(response.data!!)
            } catch (e: Exception) {
                message.value = e.toString()
            }
        }
    }

    fun getSelectedChildDataFromSharedPref(): StudentsData? {
        return appRepo.getSelectedChildData()
    }
    fun getUserType() : String{
        return appRepo.getUserTypeFromSharedPref()
    }
}