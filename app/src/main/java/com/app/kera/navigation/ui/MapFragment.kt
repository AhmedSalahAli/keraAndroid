package com.app.kera.navigation.ui

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.*
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.parkearn.services.LocationUpdatesService
import com.app.parkearn.services.LocationUpdatesService.LocalBinder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.app.kera.R
import com.app.kera.databinding.FragmentMapsBinding
import com.app.kera.navigation.model.MapLatLongList
import com.app.kera.schoolDetails.ui.SchoolDetailsActivity
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MapFragment : Fragment(), LocationListener {

    private val viewModel: NavigationMapViewModel by viewModel()
    private lateinit var fragmentMapsBinding: FragmentMapsBinding
    private var googleApiClient: GoogleApiClient? = null
    private var mMap: GoogleMap? = null
    private var mLocationManager: LocationManager? = null
    private val REQUEST_LOCATION = 123
     val LOCATION_SETTING_REQUEST = 321
    private val LOCATION_UPDATE_MIN_DISTANCE = 1
    private val LOCATION_UPDATE_MIN_TIME = 1

    private var latitide:Double = 0.0
    private  var longitude:Double = 0.0
    private var latLng: LatLng? = null


    private  var myReceiver: MyReceiver? = null

    private var mService: LocationUpdatesService? = null
    private var schoolsLocations = ArrayList<LatLng>()

    private var mBound = false
    private var mCustomMarkerView: View? = null

    public fun newInstance(): MapFragment? {
        return MapFragment()
    }
    private val callback =
        OnMapReadyCallback { googleMap ->
            mMap = googleMap
            MapStyle()

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                44
            )
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    44
                )
                return@OnMapReadyCallback
            } else {
                mLocationManager =
                    requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
                setcurrentLocation()
                viewModel.getSchoolsLocationList()
                schoolListObserver()
                mMap!!.uiSettings.isMyLocationButtonEnabled = false
                mMap!!.uiSettings.isZoomControlsEnabled = false
                mMap!!.uiSettings.isMapToolbarEnabled = false
                fragmentMapsBinding.BmyLocationCard.setOnClickListener { v ->
                    if (latLng != null) {
                        getZoomLevel(latLng!!, 16)
                    }
                }
                mMap!!.setOnMarkerClickListener(GoogleMap.OnMarkerClickListener { marker ->


                    Handler(Looper.getMainLooper()).postDelayed({
                        val myIntent = Intent(requireContext(), SchoolDetailsActivity::class.java)
                        myIntent.putExtra("SchoolID", marker.tag.toString()) //Optional parameters
                        startActivity(myIntent)
                    }, 500)
                    false
                })
            }
        }

    private fun schoolListObserver() {
        viewModel.schoolsLocations.observe(viewLifecycleOwner, {
            for (school in it) {
                val latLng = LatLng(school.lat, school.long)

                addCustomMarkerFromURL(school)
                schoolsLocations.add(latLng)
                Log.e("logos", "l : " + school.image);
                Log.e("locations ", "l : " + latLng.latitude+" "+latLng.longitude);
            }
        })
    }
    private fun addCustomMarkerFromURL(school: MapLatLongList) {
        if (mMap == null) {
            return
        }

        // adding a marker with image from URL using glide image loading library
        Glide.with(requireContext())
            .asBitmap()
            .load(school.image).fitCenter()
            .into(object : SimpleTarget<Bitmap?>() {


                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    val marker: Marker = mMap!!.addMarker(
                        MarkerOptions().title(school.name)
                            .position(LatLng(school.lat, school.long))

                            .icon(
                                BitmapDescriptorFactory.fromBitmap(
                                    getMarkerBitmapFromView(
                                        mCustomMarkerView!!,
                                        resource
                                    )
                                )
                            )
                    )
                    marker.tag = school._id
                }
            })
    }
    private fun getMarkerBitmapFromView(view: View, bitmap: Bitmap): Bitmap? {
        val mMarkerImageView: ImageView =
            mCustomMarkerView!!.findViewById<ImageView>(R.id.marker_image)
        mMarkerImageView.setImageBitmap(bitmap)
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.buildDrawingCache()
        val returnedBitmap = Bitmap.createBitmap(
            view.measuredWidth, view.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        val drawable = view.background
        drawable?.draw(canvas)
        view.draw(canvas)
        return returnedBitmap
    }
    private fun MapStyle() {
        val style = MapStyleOptions.loadRawResourceStyle(
            this.context,
            R.raw.googlemap_style
        )
        mMap!!.setMapStyle(style)
    }

//    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
//        override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
//            val binder = service as LocalBinder
//            mService = binder.service
//            myReceiver = MyReceiver()
//            mService!!.requestLocationUpdates()
//            mBound = true
//        }
//
//        override fun onServiceDisconnected(componentName: ComponentName) {
//            mService = null
//            mBound = false
//        }
//    }

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, iBinder: IBinder) {
            val binder = iBinder as LocalBinder
            mService = binder.service
            myReceiver = MyReceiver()
            mService!!.requestLocationUpdates()
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mService = null
            mBound = false
        }
    }

     class MyReceiver : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            val location =
                intent.getParcelableExtra<Parcelable>(LocationUpdatesService.EXTRA_LOCATION) as Location?
            if (location != null) {
                Log.w("New Location", location.toString())

                // the user location updates
            }
        }
    }
    override fun onResume() {
        super.onResume()
        myReceiver?.let {
            LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
                it,
                IntentFilter(LocationUpdatesService.ACTION_BROADCAST)
            )
        }
        requireActivity().bindService(
            Intent(requireActivity(), LocationUpdatesService::class.java),
            mServiceConnection,
            Context.BIND_AUTO_CREATE
        )

    }

    override fun onPause() {
        myReceiver?.let { LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(it) }
        super.onPause()
    }

    override fun onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            requireActivity().unbindService(mServiceConnection)
            mBound = false
        }
        super.onStop()
    }


    override fun onDestroy() {
        super.onDestroy()
        if (mService != null) {
            mService!!.stopSelf()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentMapsBinding = DataBindingUtil.inflate(
            inflater, com.app.kera.R.layout.fragment_maps, container, false
        )
        return fragmentMapsBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val window: Window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        mCustomMarkerView =
            (requireContext()!!.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.view_custom_marker,
                null
            )
    }

     fun showEnableLocationSetting() {
        activity?.let {
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)

            val task = LocationServices.getSettingsClient(it)
                .checkLocationSettings(builder.build())

            task.addOnSuccessListener { response ->
                val states = response.locationSettingsStates
                if (states.isLocationPresent) {
                    setcurrentLocation()
                }
            }
            task.addOnFailureListener { e ->
                if (e is ResolvableApiException) {
                    try {
                        // Handle result in onActivityResult()
                        e.startResolutionForResult(
                            it,
                            LOCATION_SETTING_REQUEST
                        )
                    } catch (sendEx: IntentSender.SendIntentException) { }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOCATION_SETTING_REQUEST -> when (resultCode) {
                Activity.RESULT_OK -> {

                    // All required changes were successfully made
                    Toast.makeText(activity, "Location enabled by user!", Toast.LENGTH_LONG).show()
                    setcurrentLocation()
                }
                Activity.RESULT_CANCELED -> {

                    // The user was asked to change settings, but chose not to
                    Toast.makeText(
                        activity,
                        "Location not enabled, user cancelled.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                }
            }
        }
    }

    private fun setcurrentLocation() {
        mMap!!.isMyLocationEnabled = true
        val isGPSEnabled = mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled =
            mLocationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        var location: Location? = null
        if (!(isGPSEnabled || isNetworkEnabled)) {
            Toast.makeText(activity, "Location not available at this time", Toast.LENGTH_SHORT)
                .show()
            showEnableLocationSetting()
        } else {
            if (isNetworkEnabled) {
                if (ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                mLocationManager!!.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    LOCATION_UPDATE_MIN_TIME.toLong(),
                    LOCATION_UPDATE_MIN_DISTANCE.toFloat(),
                    mLocationListener
                )
                location = mLocationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }
            if (isGPSEnabled) {
                mLocationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_UPDATE_MIN_TIME.toLong(),
                    LOCATION_UPDATE_MIN_DISTANCE.toFloat(),
                    mLocationListener
                )
                location = mLocationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }
            if (location != null) {
                getZoomLevel(LatLng(location.latitude, location.longitude), 16)
            }
        }

        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_ADMIN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), REQUEST_LOCATION
            )
        } else {
            mMap!!.isMyLocationEnabled = true
        }
    }

    var isLocationDetected = false
    private val mLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            if (location != null) {
                if (mMap != null) {
                    latitide = location.latitude
                    longitude = location.longitude
                    latLng = LatLng(latitide, longitude)
                    if (mMap!!.isMyLocationEnabled) {
                        if (!isLocationDetected) {
                            getZoomLevel(LatLng(location.latitude, location.longitude), 16)
                        }
                    }
                }
            } else {
                Toast.makeText(activity, "Location not available", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
        override fun onProviderEnabled(s: String) {}
        override fun onProviderDisabled(s: String) {}
    }

    private fun getZoomLevel(latLng: LatLng, ZoomLevel: Int) {

        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16F))
        isLocationDetected = true
    }

    override fun onLocationChanged(location: Location) {
        if (location != null) {
            if (mMap != null) {
                latitide = location.latitude
                longitude = location.longitude
                latLng = LatLng(latitide, longitude)
                if (mMap!!.isMyLocationEnabled) {
                    if (!isLocationDetected) {
                        getZoomLevel(LatLng(location.latitude, location.longitude), 16)
                    }
                }
            }
        } else {
            Toast.makeText(activity, "Location not available", Toast.LENGTH_SHORT).show()
        }
    }

}