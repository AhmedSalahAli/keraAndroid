package com.app.kera.navigation

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.kera.R
import com.app.kera.databinding.NavigationFragmentBinding
import com.app.kera.navigation.adapter.TabLayoutAdapter
import com.app.kera.navigation.ui.MapFragment
import com.app.kera.navigation.ui.NavigationMapViewModel
import com.app.kera.preference.SharedPrefKeys.Companion.LOCATION
import com.app.kera.utils.GPSTracker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.MessageFormat


class NavigationFragment() : Fragment() {
    private val viewModel: NavigationViewModel by viewModel()
    var listener : SharedPreferences.OnSharedPreferenceChangeListener? = null

    lateinit var viewDataBinding: NavigationFragmentBinding
    var lFrag: MapFragment? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = DataBindingUtil.inflate(
            inflater, R.layout.navigation_fragment, container, false
        )

        initFragments()


        return viewDataBinding.root
    }

    public fun  checkPermissionStatus(permission: String) : Boolean{

        return ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            permission)
    }

    override fun onResume() {
        super.onResume()
       // initFragments()
    }
    public fun newInstance(): NavigationFragment? {
        return NavigationFragment()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        viewModel.appRepo.sharedPreference?.mPrefs.registerOnSharedPreferenceChangeListener(listener)

        val window: Window =requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        lFrag = MapFragment().newInstance();


        viewDataBinding.viewPager.currentItem = 1
        viewDataBinding.viewPager.addOnPageChangeListener(
            TabLayoutOnPageChangeListener(
                viewDataBinding.tabLayout
            )
        )
        viewDataBinding.tabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewDataBinding.viewPager.currentItem = tab!!.position;
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            // Implementation
            if (key!=null){
                return@OnSharedPreferenceChangeListener
            }
            // Implementation
            Log.w("PrefsListener", "Activation for : $key")
            if (key.equals(LOCATION)) {
                Log.e("LOCATION", "LOCATION")
            }
        }
//instantiating the LocationCallBack

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MapFragment().LOCATION_SETTING_REQUEST) {


            lFrag!!.onActivityResult(requestCode, resultCode, data)

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        viewModel.appRepo.sharedPreference?.mPrefs?.unregisterOnSharedPreferenceChangeListener(listener)

        super.onDestroy()
    }
    private fun initFragments() {
        val tabTitles = arrayOf(getString(R.string.map), getString(R.string.list))

        if (viewModel.getUserLocation()==null){


//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//            && ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
            viewDataBinding.viewPager.adapter = TabLayoutAdapter(
                childFragmentManager,
                tabTitles,
                false
            )
            viewDataBinding.tabLayout.visibility = View.GONE

        }else{
            Log.e("GPS2","lat : "+ (viewModel.getUserLocation()?.latitude ?: ""))

            viewDataBinding.tabLayout.visibility = View.VISIBLE

            viewDataBinding.viewPager.adapter = TabLayoutAdapter(
                childFragmentManager,
                tabTitles
                ,true
            )
        }
        viewDataBinding.tabLayout.setupWithViewPager(viewDataBinding.viewPager)
    }


    interface CallBack {
        fun onBottomBarActionClicked(position: Int)
    }
}