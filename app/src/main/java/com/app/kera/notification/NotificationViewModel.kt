package com.app.kera.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.kera.data.network.AppRepo
import com.app.kera.notification.model.NotificationItemUIModel
import kotlinx.coroutines.launch

class NotificationViewModel(val appRepo: AppRepo) : ViewModel() {
    var notificationsList = MutableLiveData<NotificationItemUIModel>()
    var message = MutableLiveData<String>()
    var logo = MutableLiveData<String>()

    init {
       // logo.value = getNurseryLogo()
    }

    fun getNurseryLogo(): String {
        return if (!appRepo.getNurseryLogoFromSharedPreference().isNullOrEmpty()){
            appRepo.getNurseryLogoFromSharedPreference()
        }else{
            ""
        }

    }
    fun getNotifications(page : Int) {
        viewModelScope.launch {
            try {
                val response = appRepo.getNotifications(page)

                notificationsList.value =
                    NotificationItemUIModel.convertResponseModelTOUIModel(response.data!!)
            } catch (e: Exception) {
              
            }
        }
    }

}

