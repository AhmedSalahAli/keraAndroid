package com.example.kera.main.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kera.data.network.AppRepo

class MainViewModel(val appRepo: AppRepo) : ViewModel() {
    var bottomBarClickPosition = MutableLiveData<Int>()

    fun bottomBarClicks(position: Int) {
        Log.e("view model",position.toString())
        bottomBarClickPosition.value = position
    }

    fun getUserType() : String{
        return appRepo.getUserTypeFromSharedPref()
    }
}