package com.example.kera.navigation.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kera.app.MyApp.Companion.application
import com.example.kera.data.network.AppRepo
import com.example.kera.navigation.model.MapLatLongList
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