package com.example.kera.qrCode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kera.data.network.AppRepo
import com.example.kera.registrationForm.screen1.model.PublishAppStep1Model
import kotlinx.coroutines.launch

class QRViewModel(val appRepo: AppRepo) : ViewModel()  {

    var publishApp1Boolean = MutableLiveData<Boolean>()
    var message = MutableLiveData<String>()
    var logo = MutableLiveData<String>()

    init {

        logo.value = getNurseryLogo()

    }
    fun getNurseryLogo(): String {
        return appRepo.getNurseryLogoFromSharedPreference()
    }
    fun publishAttendanceQrCode(Code: QrCodeModel) {
        viewModelScope.launch {
            try {

                val data = appRepo.publishAttendanceQrCode(Code)
                if (data.status == 200) {
                    message.value = "Attendance scanned successfully"
                    publishApp1Boolean.value = true

                }
            } catch (e: Exception) {
                message.value = e.toString()
            }
        }
    }
}