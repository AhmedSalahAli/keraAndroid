package com.app.kera.teacherMedicalReport.writeMedicalReport

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.kera.data.models.teacherMedicalReport.MedicalReportResponseModel
import com.app.kera.data.models.teacherMedicalReport.UpdateMedicalRequestModel
import com.app.kera.data.network.AppRepo
import com.app.kera.teacherDailyReport.model.PublishReportRequestModel
import com.app.kera.teacherMedicalReport.model.ImageRequest
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
              
            }
        }
    }
    fun updateMedicalReportQuestion(requestModel: UpdateMedicalRequestModel, reportID: String) {
        viewModelScope.launch {
            try {
                val data = appRepo.updateMedicalReportQuestion(requestModel)
                if (data.status == 200) {
                    getDailyMedicalData(reportID)
                    updateDailyReportQuestionBoolean.value = true
                }
            } catch (e: Exception) {
              
            }
        }
    }
    fun updateMedicalImage(requestModel: ImageRequest) {
        viewModelScope.launch {
            try {
                val response = appRepo.uploadImage(requestModel)

            } catch (e: Exception) {

            }
        }
    }
    fun publishReport(model: PublishReportRequestModel) {
        viewModelScope.launch {
            try {
                val data = appRepo.publishMedicalReport(model)
                if (data.status == 200) {
                    message.value = "Report published successfully"
                    publishReportResultBoolean.value = true
                }
            } catch (e: Exception) {
              
            }
        }
    }

}