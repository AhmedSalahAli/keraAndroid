package com.example.kera.teacherMedicalReport.writeMedicalReport

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kera.data.models.teacherMedicalReport.MedicalReportResponseModel
import com.example.kera.data.network.AppRepo
import com.example.kera.teacherDailyReport.model.UpdateQuestionRequestModel
import kotlinx.coroutines.launch

class WriteMedicalReportViewModel(val appRepo: AppRepo) : ViewModel() {
    var response = MutableLiveData<MedicalReportResponseModel>()
    var message = MutableLiveData<String>()
    var updateDailyReportQuestionBoolean = MutableLiveData<Boolean>()
    var publishReportResultBoolean = MutableLiveData<Boolean>()
    fun getDailyMedicalData(reportID: String) {
        viewModelScope.launch {
            try {
                Log.e("getting report data", reportID)
                val data = appRepo.getTeacherMedicalReportData(reportID)
                response.value = data
            } catch (e: Exception) {
                message.value = e.toString()
            }
        }
    }
    fun updateDailyReportQuestion(requestModel: UpdateQuestionRequestModel, reportID: String) {
        viewModelScope.launch {
            try {
                val data = appRepo.updateDailyReportQuestion(requestModel)
                if (data.status == 200) {
                    getDailyMedicalData(reportID)
                    updateDailyReportQuestionBoolean.value = true
                }
            } catch (e: Exception) {
                message.value = e.toString()
            }
        }
    }

}