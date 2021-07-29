package com.app.kera.navigation.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.kera.data.network.AppRepo
import com.app.kera.navigation.model.MapLatLongList
import kotlinx.coroutines.launch

class NavigationMapViewModel(val appRepo: AppRepo) : ViewModel() {

    var schoolsLocations = MutableLiveData<ArrayList<MapLatLongList>>()

    fun getSchoolsLocationList() {
        viewModelScope.launch {
            try {
                val response = appRepo.getMapData()
                schoolsLocations.value = MapLatLongList.mapResponseToUI(response)
            } catch (e: Exception) {

            }
        }
    }

}