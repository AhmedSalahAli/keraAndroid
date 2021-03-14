package com.example.kera.teacherDailyReport.writeReport

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kera.data.models.teacherDailyReport.DailyReportResponseModel
import com.example.kera.data.network.AppRepo
import com.example.kera.teacherDailyReport.model.PublishReportRequestModel
import com.example.kera.teacherDailyReport.model.UpdateQuestionRequestModel
import kotlinx.coroutines.launch

class WriteReportViewModel(val appRepo: AppRepo) : ViewModel() {

    var response = MutableLiveData<DailyReportResponseModel>()
    var message = MutableLiveData<String>()
    var updateDailyReportQuestionBoolean = MutableLiveData<Boolean>()
    var publishReportResultBoolean = MutableLiveData<Boolean>()

    fun getDailyReportData(reportID: String) {
        viewModelScope.launch {
            try {
                Log.e("getting report data", reportID)
                val data = appRepo.getTeacherDailyReportData(reportID)
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
                    getDailyReportData(reportID)
                    updateDailyReportQuestionBoolean.value = true
                }
            } catch (e: Exception) {
                message.value = e.toString()
            }
        }
    }

    fun publishReport(model: PublishReportRequestModel) {
        viewModelScope.launch {
            try {
                val data = appRepo.publishReport(model)
                if (data.status == 200) {
                    message.value = "Report published successfully"
                    publishReportResultBoolean.value = true
                }
            } catch (e: Exception) {
                message.value = e.toString()
            }
        }
    }
}