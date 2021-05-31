package com.example.kera.events.ui.eventsdetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kera.data.network.AppRepo
import com.example.kera.events.model.EventDetailsUIModel
import com.example.kera.meals.model.MealsDetailsUIModel
import kotlinx.coroutines.launch

class EventsDetailsViewModel(val appRepo: AppRepo) : ViewModel() {
    var eventDetails = MutableLiveData<EventDetailsUIModel>()

    fun getEventDetails(classID: String) {
        viewModelScope.launch {
            val response = appRepo.getEventDetails(classID)
            eventDetails.value = EventDetailsUIModel.convertResponseModelToUIModel(response.data)
        }
    }
}