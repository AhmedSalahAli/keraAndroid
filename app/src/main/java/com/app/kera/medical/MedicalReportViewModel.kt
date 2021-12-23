package com.app.kera.medical

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.kera.data.network.AppRepo
import com.app.kera.medical.model.DisplayMedicalReportResponseModel
import com.app.kera.profile.ProfileUIModel
import com.app.kera.profile.StudentsData
import kotlinx.coroutines.launch

class MedicalReportViewModel(val appRepo: AppRepo) : ViewModel() {
    var response = MutableLiveData<DisplayMedicalReportResponseModel>()
    var message = MutableLiveData<String>()
    var profileUIModel = MutableLiveData<ProfileUIModel>()
    var fromDate = MutableLiveData<String>("")
    var toDate = MutableLiveData<String>("")
    var selectedUser = MutableLiveData<StudentsData>()

    fun getMedicalReportData(studentID: String, fromDate: String, toDate: String,page:Int) {
        viewModelScope.launch {
            try {
//                Log.e("getting report data", studentID)
                val data = appRepo.getMedicalReportData(studentID, fromDate, toDate,page)
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