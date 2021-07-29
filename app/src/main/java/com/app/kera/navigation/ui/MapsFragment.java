package com.app.kera.navigation.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.parkearn.services.LocationUpdatesService;
import com.app.kera.R;
import com.app.kera.databinding.FragmentMapsBinding;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

public class MapsFragment extends Fragment {
    private NavigationMapViewModel navigationMapViewModel;
    private FragmentMapsBinding fragmentMapsBinding;
    private GoogleApiClient googleApiClient;
    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private static final int REQUEST_LOCATION = 123;
    private static final int LOCATION_UPDATE_MIN_DISTANCE = 1;
    private static final int LOCATION_UPDATE_MIN_TIME = 1;

    private static double latitide, longitude;
    private LatLng latLng;


    private MyReceiver myReceiver;

    private LocationUpdatesService mService;




    private boolean mBound = false;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            MapStyle();

            ActivityCompat.requestPermissions(getActivity(), new String []{Manifest.permission.ACCESS_FINE_LOCATION},44 );

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                ActivityCompat.requestPermissions(getActivity(), new String []{Manifest.permission.ACCESS_FINE_LOCATION},44 );


                return;
            }else {
                mLocationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
                setcurrentLocation();
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.getUiSettings().setZoomControlsEnabled(false);
                mMap.getUiSettings().setMapToolbarEnabled(false);
                fragmentMapsBinding.BmyLocationCard.setOnClickListener(v -> {
                    if(latLng != null){
                        getZoomLevel(latLng,16);
                    }
                });
            }

            }
    };
    private void MapStyle() {
        mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        getContext(), R.raw.googlemap_style));
    }
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            myReceiver = new MyReceiver();
            mService.requestLocationUpdates();


            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;
            mBound = false;

        }
    };
    class MyReceiver extends BroadcastReceiver {
        public void onReceive(
                Context context,
                Intent intent
        ) {
            Location location =
                    (Location) intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            if (location != null) {

                Log.w("New Location", location.toString());

                // the user location updates
            }
        }

    }
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
                myReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST)
        );
        requireActivity().bindService(
                new Intent(requireActivity(), LocationUpdatesService.class),
                mServiceConnection,
                Context.BIND_AUTO_CREATE
        );
    }

    public void onPause() {
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(myReceiver);
        super.onPause();
    }

    public void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            requireActivity().unbindService(mServiceConnection);
            mBound = false;
        }
        super.onStop();
    }


    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            mService.stopSelf();
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        navigationMapViewModel = new ViewModelProvider(this).get(NavigationMapViewModel.class);
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        fragmentMapsBinding = DataBindingUtil.bind(view);
        if (fragmentMapsBinding != null) {

            fragmentMapsBinding.setViewModel(navigationMapViewModel);

        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
    private void setcurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;
        if (!(isGPSEnabled || isNetworkEnabled)) {
            Toast.makeText(getActivity(), "Location not available at this time", Toast.LENGTH_SHORT).show();
        } else {
            if (isNetworkEnabled) {
                if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if (location != null){
                getZoomLevel(new LatLng(location.getLatitude(),location.getLongitude()),16);

            }
        }
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||

                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||

                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),

                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,

                            Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);

        } else {
            mMap.setMyLocationEnabled(true);
        }
    }
    boolean isLocationDetected = false;
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                if (mMap != null) {



                    latitide = location.getLatitude();
                    longitude = location.getLongitude();
                    latLng = new LatLng(latitide, longitude);
                    if (mMap.isMyLocationEnabled()){
                        if (!isLocationDetected){
                            getZoomLevel(new LatLng(location.getLatitude(),location.getLongitude()),16);
                        }
                    }













                }
            } else {
                Toast.makeText(getActivity(), "Location not available", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void getZoomLevel(LatLng latLng, int ZoomLevel) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(ZoomLevel);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom, 1000, null);
        isLocationDetected = true;

    }

}