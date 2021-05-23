package com.example.kera.teacherMedicalReport

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kera.data.network.AppRepo
import com.example.kera.teacherDailyReport.model.CreateReportRequestModel
import com.example.kera.teacherMedicalReport.model.LatestReportItemUIModel
import com.example.kera.teacherMedicalReport.model.StudentItemUIModel
import com.example.kera.teacherMedicalReport.model.TeacherReportClassesUIModel
import kotlinx.coroutines.launch

class TeacherMedicalReportViewModel(val appRepo: AppRepo) : ViewModel() {
    var classesList = MutableLiveData<ArrayList<TeacherReportClassesUIModel>>()
    var studentsList = MutableLiveData<ArrayList<StudentItemUIModel>>()
    var message = MutableLiveData<String>()
    var createdReportResponseID = MutableLiveData<String>()
    var latestReportsList = MutableLiveData<LatestReportItemUIModel>()

    fun getClasses() {
        viewModelScope.launch {
            try {
                val response = appRepo.getClasses("en", 1)
                classesList.value =
                    TeacherReportClassesUIModel.convertResponseModelToUIModel(response.data!!)
                getStudentsByClass("0")
            } catch (e: Exception) {
                message.value = "Please enter a valid phone number"
            }
        }
    }

    fun getStudentsByClass(classId: String) {
        viewModelScope.launch {
            try {
                val response = appRepo.getTeacherStudentsByClassID("en", 1, classId)
                studentsList.value = StudentItemUIModel.convertResponseModelToUIModel(response)
            } catch (e: Exception) {
                message.value = e.toString()
            }
        }
    }


    fun createMedicalReport(requestModel: CreateReportRequestModel) {
        viewModelScope.launch {
            try {
                val response = appRepo.createMedicalReport(requestModel)
                createdReportResponseID.value = response.data?.id
            } catch (e: Exception) {
                message.value = e.toString()
            }
        }
    }

    fun getLatestReports(page : Int) {
        viewModelScope.launch {
            try {
                val response = appRepo.getLatestMedicalReports(page)
                Log.e("size of response", response.reports?.docs!!.size.toString())
                latestReportsList.value =
                    LatestReportItemUIModel.convertResponseModelTOUIModel(response.reports!!)
            } catch (e: Exception) {
                message.value = e.toString()
            }
        }
    }}