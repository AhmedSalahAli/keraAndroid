package com.app.kera.dailyReport.ui

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.kera.dailyReport.model.PublishReplay
import com.app.kera.data.models.DisplayDailyReportResponseModel
import com.app.kera.data.network.AppRepo
import com.app.kera.profile.ProfileUIModel
import com.app.kera.profile.StudentsData
import kotlinx.coroutines.launch

class ReportDetailsViewModel(var appRepo: AppRepo) : ViewModel() {

    var response = MutableLiveData< DisplayDailyReportResponseModel.DataBean.DocsBean>()
    var message = MutableLiveData<String>()
    var profileUIModel = MutableLiveData<ProfileUIModel>()
    var fromDate = MutableLiveData<String>("")
    var toDate = MutableLiveData<String>("")
    var selectedUser = MutableLiveData<StudentsData>()
    var publishReplayBoolean = MutableLiveData<Boolean>()

    init {
        getSelectedChildDataFromSharedPref()
    }


    fun getProfileData() {
        viewModelScope.launch {
            try {
                val response = appRepo.getProfileData("en", 1, "user")
                profileUIModel.value = ProfileUIModel.mapResponseModelToUIModel(response.data)
            } catch (e: Exception) {
              
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
              
            }
        }
    }
    fun saveChildToSharedPref(studentData: StudentsData) {
        appRepo.saveSelectedChildData(studentData)
    }
    fun getAppRepoInstance() :AppRepo{
        return appRepo
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun getSelectedChildDataFromSharedPref(): StudentsData? {
        var response = appRepo.getSelectedChildData()
        selectedUser.value = response
        return appRepo.getSelectedChildData()
    }
}