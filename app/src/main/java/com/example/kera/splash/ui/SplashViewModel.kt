package com.example.kera.splash.ui

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kera.data.network.AppRepo

class SplashViewModel(val appRepo: AppRepo) : ViewModel() {

    var isAuthorized = MutableLiveData(false)

    init {
        countDownTimer()
    }

    val timerFinished = MutableLiveData<Boolean>()
    private fun countDownTimer() {
        val timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                timerFinished.value = true
            }
        }
        timer.start()
    }

     fun getIsUserLoggedIn() {
        appRepo.getIsUserLoggedIn().let {
            isAuthorized.value = it
        }
    }

}