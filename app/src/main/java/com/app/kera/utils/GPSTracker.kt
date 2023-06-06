package com.app.kera.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class GPSTracker(private val mContext: Context, var callBack: CallBack) :
    LocationListener {
    // Declaring a Location Manager
    protected var locationManager: LocationManager? = null
    // flag for GPS Status
    var isGPSEnabled = false
    // flag for network status
    var isNetworkEnabled = false
    var canGetLocation = false
    var location: Location? = null
    var latitude = 0.0
    var longitude = 0.0
     fun getLocationPlace(): Location? {
        try {
            if (ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    (mContext as Activity), arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    REQUEST_LOCATION_PERMISION
                )
            } else {
                locationManager = mContext
                    .getSystemService(Context.LOCATION_SERVICE) as LocationManager
                // getting GPS status
                isGPSEnabled = locationManager!!
                    .isProviderEnabled(LocationManager.GPS_PROVIDER)
                // getting network status
                isNetworkEnabled = locationManager!!
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                if (!isGPSEnabled && !isNetworkEnabled) { // location service disabled
                    callBack.onGpsAndNetworkNotEnabled()
                } else {
                    canGetLocation = true
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        locationManager!!.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
                        )
                        Log.d("GPS Enabled", "GPS Enabled")
                        if (locationManager != null) {
                            location = locationManager!!
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            //updateGPSCoordinates();
                        }
                    }
                    // First get location from Network Provider
                    if (isNetworkEnabled) {
                        if (location == null) {
                            locationManager!!.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
                            )
                            Log.d("Network", "Network")
                            if (locationManager != null) {
                                location = locationManager!!
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                                //updateGPSCoordinates();
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) { // e.printStackTrace();
            Log.e(
                "Error : Location",
                "Impossible to connect to LocationManager", e
            )
        }
        return location
    }

    fun updateGPSCoordinates(location: Location?) {
        if (location != null) {
            latitude = location.latitude
            longitude = location.longitude
        }
    }

    fun canGetLocation(): Boolean {
        return canGetLocation
    }

    fun cancleGetLocation() {
        locationManager!!.removeUpdates(this)
    }

    override fun onLocationChanged(location: Location) {
        updateGPSCoordinates(location)
    }

    override fun onStatusChanged(
        provider: String,
        status: Int,
        extras: Bundle
    ) {
    }

    override fun onProviderDisabled(provider: String) {}
    override fun onProviderEnabled(provider: String) {}
    interface CallBack {
        fun onGpsAndNetworkNotEnabled()
    }

    companion object {
        // The minimum distance to change updates in metters
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 0
        // The minimum time beetwen updates in milliseconds
        private const val MIN_TIME_BW_UPDATES: Long = 0
        const val REQUEST_LOCATION_PERMISION = 1
    }

    init {
        getLocationPlace()
    }
}