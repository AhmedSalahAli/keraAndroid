package com.app.kera.navigation.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.kera.data.network.AppRepo
import com.app.kera.navigation.model.MapLatLongList
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class NavigationMapViewModel(val appRepo: AppRepo) : ViewModel() {

    var schoolsLocations = MutableLiveData<ArrayList<MapLatLongList>>()

    fun getSchoolsLocationList(lat:String?,lon:String?) {
        viewModelScope.launch {
            try {
                val response = appRepo.getMapData(lat,lon)
                schoolsLocations.value = MapLatLongList.mapResponseToUI(response)
            } catch (e: Exception) {

            }
        }
    }
    fun getUserLocation(): LatLng? {
        return appRepo.getUserLocation()
    }


}