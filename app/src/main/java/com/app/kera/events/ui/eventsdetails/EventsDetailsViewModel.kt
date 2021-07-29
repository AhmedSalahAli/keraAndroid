package com.app.kera.events.ui.eventsdetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.kera.data.network.AppRepo
import com.app.kera.events.model.EventDetailsUIModel
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