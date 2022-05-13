package com.app.kera.events.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.kera.data.network.AppRepo
import com.app.kera.events.model.UpcomingItemUIModel
import kotlinx.coroutines.launch

class EventsViewModel(val appRepo: AppRepo) : ViewModel() {
    var upcomingEventList = MutableLiveData<ArrayList<UpcomingItemUIModel>>()
    var previousEventList = MutableLiveData<ArrayList<UpcomingItemUIModel>>()

    var message = MutableLiveData<String>()
    var anApiFailed = MutableLiveData<Boolean>()
    var logo = MutableLiveData<String>()

    init {

        logo.value = getNurseryLogo()

    }
    fun getUpcomingEvent(page:Int) {
        viewModelScope.launch {
            try {
                val response = appRepo.getUpcomingEventList(page)
                upcomingEventList.value = UpcomingItemUIModel.convertResponseModelToUIModel(response.data?.docs!!)
            } catch (e: Exception) {
              
                anApiFailed.value = true
            }
        }
    }
    fun getPreviousEventList(page:Int) {
        viewModelScope.launch {
            try {
                val response = appRepo.getPreviousEventList(page)
                previousEventList.value = UpcomingItemUIModel.convertResponseModelToUIModel(response.data?.docs!!)
            } catch (e: Exception) {
              
                anApiFailed.value = true
            }
        }
    }
    fun getNurseryLogo(): String {
        return appRepo.getNurseryLogoFromSharedPreference()
    }
}