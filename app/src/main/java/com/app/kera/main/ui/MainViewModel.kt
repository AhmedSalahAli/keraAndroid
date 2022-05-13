package com.app.kera.main.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.kera.data.network.AppRepo
import com.app.kera.home.model.ImagesUIModel
import kotlinx.coroutines.launch

class MainViewModel(val appRepo: AppRepo) : ViewModel() {
    var bottomBarClickPosition = MutableLiveData<Int>()
    var homeNurseryData = MutableLiveData<ImagesUIModel>()
    var message = MutableLiveData<String>()

    var isSetUp = MutableLiveData<Boolean>()
    fun bottomBarClicks(position: Int) {
        Log.e("view model",position.toString())
        bottomBarClickPosition.value = position
    }

    fun getUserType() : String{
        return appRepo.getUserTypeFromSharedPref()
    }
    fun getSetUp(setUp: Boolean) {
        viewModelScope.launch {
            isSetUp.value = setUp
        }
    }

    fun getNurseryData() {
        viewModelScope.launch {
            try {
                val response = appRepo.getHomeNurseryData()
                homeNurseryData.value = ImagesUIModel.convertResponseModelToUIModel(response.data!!)
                saveNurseryLogoToSharedPreference(response.data!!.logo)
            } catch (e: Exception) {
              
            }
        }
    }
    private fun saveNurseryLogoToSharedPreference(logo: String?) {
        appRepo.saveNurseryLogoToSharedPreference(logo)
    }
}