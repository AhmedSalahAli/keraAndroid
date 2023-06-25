package com.app.kera.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.app.kera.preference.SharedPrefKeys
import com.app.kera.preference.SharedPrefKeys.Companion.APP_LANG
import com.app.kera.utils.CommonUtils

import com.app.kera.utils.Configurations
import com.app.kera.utils.Configurations.Companion.API_PATH
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import io.alterac.blurkit.BlurKit
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MyApp : Application(),ForceUpdateChecker.onCheckConfigParamsListner {
    private val TAG: String = MyApp::class.java.simpleName
    var sharedPreferences: SharedPreferences? = null

    companion object {
        lateinit var application: MyApp
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        application = this
        ForceUpdateChecker.with(this).onCheckConfigParams(this).config()
        lannguage(this)
        BlurKit.init(this);
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApp)
            val modules = ArrayList(allFeatures)
            modules.addAll(listOf(viewModelModule))
            modules(
                modules
            )
        }

        initFirebaseRemoteConfig()

    }
    private fun initFirebaseRemoteConfig() {
        FirebaseApp.initializeApp(this)
//        FirebaseRemoteConfig.getInstance().apply {
//            //set this during development
//            val configSettings = FirebaseRemoteConfigSettings.Builder()
//                .setMinimumFetchIntervalInSeconds(7200)
//                .build()
//            setConfigSettingsAsync(configSettings)
//            //set this during development
//
//
//            // set in-app defaults
//            val remoteConfigDefaults: MutableMap<String?, Any?> = HashMap<String?, Any?>()
//            remoteConfigDefaults[ForceUpdateChecker.KEY_UPDATE_REQUIRED] = false
//            remoteConfigDefaults[ForceUpdateChecker.KEY_NORMAL_UPDATE_REQUIRED] = false
//            remoteConfigDefaults[ForceUpdateChecker.KEY_CURRENT_VERSION] = "1.0.0"
//            remoteConfigDefaults[ForceUpdateChecker.KEY_UPDATE_URL] =
//                "https://drive.google.com/file/d/1yhBp9RIbK8yh5ivtoJWGtLrDCfo2sXkR/view?usp=sharing"
//            remoteConfigDefaults[ForceUpdateChecker.prod_base_url] =
//                "https://kera-app.herokuapp.com/"
//            setDefaultsAsync(remoteConfigDefaults)
//            fetchAndActivate().addOnCompleteListener { task ->
//                val updated = task.result
//                if (task.isSuccessful) {
//                    Log.d(TAG, "Config params updated: $updated")
//                } else {
//                    Log.d(TAG, "Config params updated not success: $updated")
//                }
//            }
//        }
        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        // set in-app defaults

        val remoteConfigDefaults: MutableMap<String?, Any?> = HashMap<String?, Any?>()
        remoteConfigDefaults[ForceUpdateChecker.KEY_UPDATE_REQUIRED] = false
        remoteConfigDefaults[ForceUpdateChecker.KEY_CURRENT_VERSION] = "1.0.0"
        remoteConfigDefaults[ForceUpdateChecker.KEY_UPDATE_URL] =
            "https://play.google.com/store/apps/details?id=com.sembozdemir.renstagram"




        firebaseRemoteConfig.setDefaultsAsync(remoteConfigDefaults);
        firebaseRemoteConfig.fetch(60).addOnCompleteListener(OnCompleteListener {
            if (it.isSuccessful) {
                Log.d(TAG, "remote config is fetched.")
                firebaseRemoteConfig.activate()
            }
        }) // fetch every minutes
    }
    fun lannguage(newBase: Context?) {
        sharedPreferences = newBase!!.getSharedPreferences("keraPreference", Context.MODE_PRIVATE)

        CommonUtils.setLocale(this,  sharedPreferences!!.getString(
            SharedPrefKeys.APP_LANG,
            "en"
        )!!)

    }
    override fun onCheckConfigParams(BaseUrl: String?) {
//        if (BaseUrl != null) {
//         // Configurations.BASE_URL = BaseUrl+API_PATH
//            Configurations.BASE_URL = "https://kera-test-app.herokuapp.com/api/"
//            Log.i("BaseUrl","reach baseUrl : "+BaseUrl)
//        }
//        Log.i("BaseUrl","reach listner")
    }
}