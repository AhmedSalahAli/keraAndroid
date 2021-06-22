package com.example.kera.navigation.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.kera.R
import com.example.kera.databinding.MapCustomMarkerBinding
import com.example.kera.databinding.NavigationMapFragmentBinding
import com.example.kera.navigation.model.MapLatLongList
import com.example.kera.schoolDetails.ui.SchoolDetailsActivity
import com.fonfon.geohash.GeoHash
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException


class NavigationMapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener ,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    LocationListener {


    val viewModel: NavigationMapViewModel by viewModel()
    lateinit var viewDataBinding: NavigationMapFragmentBinding
    lateinit var supportMapFragment: SupportMapFragment
    lateinit var currentLocation: Location

    private lateinit var map: GoogleMap
    lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    private lateinit var locationCallback: LocationCallback
    private var schoolsLocations = ArrayList<LatLng>()
    private var result: PendingResult<LocationSettingsResult>? = null
    val REQUEST_LOCATION = 199
    private var mCustomMarkerView: View? = null
    private lateinit var googleApiClient: GoogleApiClient
    val LAYOUT_INFLATER_SERVICE = "layout_inflater"

    // 2
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = DataBindingUtil.inflate(
            inflater, R.layout.navigation_map_fragment, container, false
        )
        return viewDataBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val window: Window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        mCustomMarkerView =
            (requireContext()!!.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.view_custom_marker,
                null
            )

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        fetchLastLocation()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
//                placeMarkerOnMap(LatLng(lastLocation.latitude, lastLocation.longitude))
            }
        }


    }

    private fun schoolListObserver() {
        viewModel.schoolsLocations.observe(viewLifecycleOwner, {
            for (school in it) {
                val latLng = LatLng(school.lat, school.long)

                addCustomMarkerFromURL(school)
                schoolsLocations.add(latLng)
                Log.e("logos", "l : " + school.image);
            }
        })
    }
    private fun addCustomMarkerFromURL(school: MapLatLongList) {
        if (map == null) {
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
                    val marker: Marker = map.addMarker(
                        MarkerOptions().title(school.name)
                            .position(LatLng(school.lat, school.lat))

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

    private fun fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 101
            )
            return
        }
        val task: Task<Location> = fusedLocationClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                supportMapFragment =
                    childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment
                supportMapFragment.getMapAsync(this)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLastLocation()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val style = MapStyleOptions.loadRawResourceStyle(this.context, R.raw.googlemap_style)
        map.setMapStyle(style)


        Handler().post { map.setPadding(20, 200, 20, 200) }


        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 101
            )
            return
        }
        map.isMyLocationEnabled = true
        map.uiSettings.isZoomControlsEnabled = false
        map.uiSettings.isZoomGesturesEnabled = true
        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = false
        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16F))
        map.setOnMarkerClickListener(this)
        viewModel.getSchoolsLocationList()
        schoolListObserver()
        buildGoogleApiClient()
        viewDataBinding.BmyLocationCard.setOnClickListener(View.OnClickListener {
            if (latLng != null) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16F))
            }
        })
        map.setOnMarkerClickListener(OnMarkerClickListener { marker ->


            Handler(Looper.getMainLooper()).postDelayed({
                val myIntent = Intent(requireContext(), SchoolDetailsActivity::class.java)
                myIntent.putExtra("SchoolID", marker.tag.toString()) //Optional parameters
                startActivity(myIntent)
            }, 500)
            false
        })
        setUpMap()
    }
    @Synchronized
    protected fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(requireContext())
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        googleApiClient.connect()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        map.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                //placeMarkerOnMap(currentLatLng, null)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
            }
        }
    }


    private fun startLocationUpdates() {
        //1
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        //2
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }

    private fun createLocationRequest() {
        // 1
        locationRequest = LocationRequest()
        // 2
        locationRequest.interval = 10000
        // 3
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        // 4
        val client = LocationServices.getSettingsClient(requireActivity())
        val task = client.checkLocationSettings(builder.build())

        // 5
        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            // 6
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(
                        requireActivity(),
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
        result =
            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())

        (result as PendingResult<LocationSettingsResult>).setResultCallback(ResultCallback { result ->
            val status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                    // Location settings are not satisfied. But could be fixed by showing the user
                    // a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(
                            requireActivity(),
                            REQUEST_LOCATION
                        )
                    } catch (e: SendIntentException) {
                        // Ignore the error.
                    }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                }
            }
        })

    }

    // 1
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }
    }

    // 2
    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // 3
    public override fun onResume() {
        super.onResume()
        if (!locationUpdateState  ) {
            fetchLastLocation()
        }
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
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2

    }

    private fun getGeoHash(location: Location): String {
//        val location = Location("geohash")
        location.latitude
        location.longitude

        val hash: GeoHash = GeoHash.fromLocation(location, 9)
        hash.toString() //"v12n8trdj"
        return hash.toString()
    }

    private fun getAddress(latLng: LatLng): String {
        // 1
        val geocoder = Geocoder(requireContext())
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""

        try {
            // 2
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            // 3
            if (null != addresses && !addresses.isEmpty()) {
                address = addresses[0]
                for (i in 0 until address.maxAddressLineIndex) {
                    addressText += if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(
                        i
                    )
                }
            }
        } catch (e: IOException) {
            Log.e("MapsActivity", e.localizedMessage)
        }

        return addressText
    }

    private fun getBitmapFromView(marker: Marker?, image: String?) {
        val customMarkerView: MapCustomMarkerBinding =
            MapCustomMarkerBinding.inflate(layoutInflater, null, false)

        Glide.with(requireActivity()).asBitmap()
            .load(image).listener(object : RequestListener<Bitmap> {

//                override fun onResourceReady(
//                    resource: Drawable?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    dataSource: DataSource?,
//                    isFirstResource: Boolean
//                ): Boolean {

//                    marker!!.setIcon(BitmapDescriptorFactory.fromBitmap(returnedBitmap))
////                    setMapMarker(
////                        location,
////                        createDrawableFromView(requireContext(), customMarkerView.root)
////                    )
//                    viewDataBinding.layout.addView(customMarkerView.root)
//                    return false
//                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
//                    if (resource != null) {
//                        val bitmap:(BitmapDrawable) = customMarkerView.imageView91.drawable as BitmapDrawable
//
//                        val icon = BitmapDescriptorFactory.fromBitmap(resource)
//                        marker!!.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap.bitmap))
//                    }
                    return false
                }
            })
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(customMarkerView.imageView91)

    }

    private fun setMapMarker(location: LatLng): Marker? {
        val markerOptions = MarkerOptions().position(location)
        return map.addMarker(markerOptions)
    }

    override fun onMarkerClick(p0: Marker?) = false
    override fun onConnected(p0: Bundle?) {
        createLocationRequest()
       // Toast.makeText(requireContext(), "coneected", Toast.LENGTH_LONG).show()
    }

    override fun onConnectionSuspended(p0: Int) {
        //Toast.makeText(requireContext(), "ss", Toast.LENGTH_LONG).show()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

      //Toast.makeText(requireContext(), "faild", Toast.LENGTH_LONG).show()
    }

    override fun onLocationChanged(p0: Location) {
       // Toast.makeText(requireContext(), "detected", Toast.LENGTH_LONG).show()
    }

}