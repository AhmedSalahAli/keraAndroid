package com.app.kera.attendanceHistory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.kera.attendanceHistory.model.AttendanceListItemModel
import com.app.kera.data.network.AppRepo
import com.app.kera.meals.model.ClassMealsDates
import kotlinx.coroutines.launch

class AttendanceViewModel (val appRepo: AppRepo) : ViewModel() {
    var datesListLiveData = MutableLiveData<ArrayList<ClassMealsDates>>()
    var attendanceList = MutableLiveData<ArrayList<AttendanceListItemModel>>()
    var message = MutableLiveData<String>()


    fun getDates() {
        viewModelScope.launch {
            try {
                val response = appRepo.getAttendanceDates()
                datesListLiveData.value = ClassMealsDates.convertDate(response.data!!)
            } catch (e: Exception) {
              
            }
        }
    }
    fun getAttendanceList(page: Int, date: String) {
        viewModelScope.launch {
            val response = appRepo.getAttendanceList(page, date)
            attendanceList.value =
                AttendanceListItemModel.convertResponseModelToUIModel(response.data)
        }
    }
}