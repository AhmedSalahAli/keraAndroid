package com.example.kera.notification

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kera.data.network.AppRepo
import com.example.kera.notification.model.NotificationItemUIModel
import com.example.kera.teacherMedicalReport.model.LatestReportItemUIModel
import kotlinx.coroutines.launch

class NotificationViewModel(val appRepo: AppRepo) : ViewModel() {
    var notificationsList = MutableLiveData<NotificationItemUIModel>()
    var message = MutableLiveData<String>()
    var logo = MutableLiveData<String>()

    init {
        logo.value = getNurseryLogo()
    }

    fun getNurseryLogo(): String {

        return appRepo.getNurseryLogoFromSharedPreference()
    }
    fun getNotifications(page : Int) {
        viewModelScope.launch {
            try {
                val response = appRepo.getNotifications(page)

                notificationsList.value =
                    NotificationItemUIModel.convertResponseModelTOUIModel(response.data!!)
            } catch (e: Exception) {
                message.value = e.toString()
            }
        }
    }

}

