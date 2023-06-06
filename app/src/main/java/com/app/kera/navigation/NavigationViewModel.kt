package com.app.kera.navigation

import android.location.Location
import androidx.lifecycle.ViewModel
import com.app.kera.data.network.AppRepo
import com.google.android.gms.maps.model.LatLng

class NavigationViewModel(val appRepo: AppRepo) : ViewModel() {
    fun getUserLocation(): LatLng? {
        return appRepo.getUserLocation()
    }
}