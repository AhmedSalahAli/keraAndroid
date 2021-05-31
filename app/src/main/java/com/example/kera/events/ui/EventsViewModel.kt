package com.example.kera.events.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kera.data.network.AppRepo
import com.example.kera.events.model.UpcomingItemUIModel
import com.example.kera.meals.model.MealsItemUIModel
import com.example.kera.schoolsList.SchoolListUIModel
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
                message.value = e.toString()
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
                message.value = e.toString()
                anApiFailed.value = true
            }
        }
    }
    fun getNurseryLogo(): String {
        return appRepo.getNurseryLogoFromSharedPreference()
    }
}