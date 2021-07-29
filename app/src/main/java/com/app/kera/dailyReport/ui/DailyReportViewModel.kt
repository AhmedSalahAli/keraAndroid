package com.app.kera.dailyReport.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.kera.dailyReport.model.PublishReplay
import com.app.kera.data.models.DisplayDailyReportResponseModel
import com.app.kera.data.network.AppRepo
import com.app.kera.profile.ProfileUIModel
import com.app.kera.profile.StudentsData
import kotlinx.coroutines.launch

class DailyReportViewModel(var appRepo: AppRepo) : ViewModel() {

    var response = MutableLiveData<DisplayDailyReportResponseModel>()
    var message = MutableLiveData<String>()
    var profileUIModel = MutableLiveData<ProfileUIModel>()
    var fromDate = MutableLiveData<String>("")
    var toDate = MutableLiveData<String>("")
    var selectedUser = MutableLiveData<StudentsData>()
    var publishReplayBoolean = MutableLiveData<Boolean>()

    init {
        getSelectedChildDataFromSharedPref()
    }

    fun getDailyReportData(studentID: String, fromDate: String, toDate: String,page:Int) {
        viewModelScope.launch {
            try {
//                Log.e("getting report data", studentID)
                val data = appRepo.getDailyReportData(studentID, fromDate, toDate,page)
                response.value = data
            } catch (e: Exception) {
                message.value = e.toString()
            }
        }
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
    fun sendReplay(model: PublishReplay) {
        viewModelScope.launch {
            try {
                val data = appRepo.publishReplay(model)
                if (data.status == 200) {
                    // message.value = "Form published successfully"
                    publishReplayBoolean.value = true

                }
            } catch (e: Exception) {
                message.value = e.toString()
            }
        }
    }
    fun saveChildToSharedPref(studentData: StudentsData) {
        appRepo.saveSelectedChildData(studentData)
    }
    fun getAppRepoInstance() :AppRepo{
        return appRepo
    }

    fun getSelectedChildDataFromSharedPref(): StudentsData? {
        var response = appRepo.getSelectedChildData()
        selectedUser.value = response!!
        return appRepo.getSelectedChildData()
    }
}