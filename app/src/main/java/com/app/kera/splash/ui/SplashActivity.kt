package com.app.kera.splash.ui

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.PermissionRequest
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.TaskStackBuilder

import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.app.kera.R
import com.app.kera.app.ForceUpdateChecker
import com.app.kera.dailyReport.ui.DailyReportActivity
import com.app.kera.dailyReport.ui.ReportDetails
import com.app.kera.data.models.NotificationModel
import com.app.kera.databinding.ActivitySplashBinding
import com.app.kera.login.ui.LoginActivity
import com.app.kera.main.ui.MainActivity
import com.app.kera.medical.MedicalReportActivity
import com.app.kera.preference.SharedPrefKeys
import com.app.kera.splash.ui.SplashActivity.Companion.getStartIntent
import com.app.kera.teacherDailyReport.ui.TeacherDailyReportActivity
import com.app.kera.teacherDailyReport.writeReport.WriteReportActivity
import com.app.kera.teacherMedicalReport.TeacherMedicalReportActivity
import com.app.kera.teacherMedicalReport.writeMedicalReport.WriteMedicalReportActivity
import com.app.kera.utils.CommonUtils
import com.app.kera.utils.Configurations
import com.app.kera.utils.Configurations.Companion.API_PATH

import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken

import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class SplashActivity : AppCompatActivity(),ForceUpdateChecker.onCheckConfigParamsListner {

    private val splashViewModel: SplashViewModel by viewModel()
    lateinit var viewDataBinding: ActivitySplashBinding
    var sharedPreferences: SharedPreferences? = null

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ForceUpdateChecker.with(this).onCheckConfigParams(this).config()
        lannguage(this)
//        val config: ImagePipelineConfig = ImagePipelineConfig.newBuilder(this)
//            .setProgressiveJpegConfig(SimpleProgressiveJpegConfig())
//            .setResizeAndRotateEnabledForNetwork(true)
//            .setDownsampleEnabled(true)
//            .build()
//        Fresco.initialize(this, config)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        val window: Window =this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = splashViewModel
        splashViewModel.getIsUserLoggedIn()
        requestLocationPermission()


    }
    fun lannguage(newBase: Context?) {
        sharedPreferences = newBase!!.getSharedPreferences("keraPreference", Context.MODE_PRIVATE)

        CommonUtils.setLocale(this,  sharedPreferences!!.getString(
            SharedPrefKeys.APP_LANG,
            "en"
        )!!)

    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    fun requestLocationPermission() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    /*  if (!report.areAllPermissionsGranted()) {
                            finish();
                            return;
                        }*/
                    splashViewModel.timerFinished.observe(this@SplashActivity) {
                        appSettingObservation()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }


            })
            .check()
    }
    private fun appSettingObservation() {
        splashViewModel.isAuthorized.observe(this) {
            if (it) {

                navUser()
//                navigateToUriWithClearStack(R.string.home_user)
            } else {
                startActivity(Intent(this, LoginActivity::class.java).apply {})
                finish()
//                navigateToUriWithClearStack(R.string.on_boarding)
            }
        }
    }

    private fun navUser() {
        if (intent.hasExtra("type")) {

            val intent = intent
            val notificationBundle = intent.extras

            val notificationModel = NotificationModel()
            notificationModel.reportId = notificationBundle?.get("reportId").toString()
            notificationModel.userType = notificationBundle?.get("userType").toString()
            notificationModel.reportType = notificationBundle?.get("reportType").toString()
            notificationModel.type = notificationBundle?.get("type").toString()


            //notificationModel.bundle = notificationBundle

            if (notificationModel.userType.equals("user")) {
                if (notificationModel.type.equals("daily")) {

                    val intent: Intent = Intent(applicationContext, MainActivity::class.java)
                    val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(applicationContext)

                    val intentReport: Intent = Intent(
                        applicationContext,
                        DailyReportActivity::class.java
                    )
                    stackBuilder.addParentStack(MainActivity::class.java)
                    stackBuilder.addNextIntent(intent)
                    stackBuilder.addNextIntent(intentReport).startActivities()
                    finish()


                } else if (notificationModel.type.equals("medical")) {


                    val intent: Intent = Intent(applicationContext, MainActivity::class.java)
                    val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(applicationContext)

                    val intentReport: Intent = Intent(
                        applicationContext,
                        MedicalReportActivity::class.java
                    )
                    stackBuilder.addParentStack(MainActivity::class.java)
                    stackBuilder.addNextIntent(intent)
                    stackBuilder.addNextIntent(intentReport).startActivities()
                    finish()

                } else {

                    val intent: Intent = Intent(applicationContext, MainActivity::class.java)
                    val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(applicationContext)

                    val intentReport: Intent = Intent(
                        applicationContext,
                        MainActivity::class.java
                    )
                    stackBuilder.addParentStack(MainActivity::class.java)
                    stackBuilder.addNextIntent(intent)
                    stackBuilder.addNextIntent(intentReport).startActivities()
                    finish()
                }
            } else if (notificationModel.userType.equals("teacher")) {
                if (notificationModel.type.equals("daily")) {

                    val intent: Intent = Intent(applicationContext, MainActivity::class.java)
                    val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(applicationContext)


                    val intentReports: Intent = Intent(
                        applicationContext,
                        TeacherDailyReportActivity::class.java
                    )
                    val intentReportDetails: Intent = Intent(
                        applicationContext,
                        WriteReportActivity::class.java
                    )
                    intentReportDetails.putExtra("reportID", notificationModel.reportId)

                    stackBuilder.addParentStack(MainActivity::class.java)
                    stackBuilder.addNextIntent(intent)
                    stackBuilder.addNextIntent(intentReports)
                    stackBuilder.addNextIntent(intentReportDetails).startActivities()
                    finish()
                } else if (notificationModel.type.equals("medical")) {


                    val intent: Intent = Intent(applicationContext, MainActivity::class.java)
                    val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(applicationContext)


                    val intentReports: Intent = Intent(
                        applicationContext,
                        TeacherMedicalReportActivity::class.java
                    )
                    val intentReportDetails: Intent = Intent(
                        applicationContext,
                        WriteMedicalReportActivity::class.java
                    )
                    stackBuilder.addParentStack(MainActivity::class.java)
                    stackBuilder.addNextIntent(intent)
                    stackBuilder.addNextIntent(intentReports)
                    stackBuilder.addNextIntent(intentReportDetails).startActivities()
                    finish()
                } else {

                    startActivity(Intent(this, MainActivity::class.java).apply {})
                    finish()

                }
            } else {
                startActivity(Intent(this, MainActivity::class.java).apply {})
                finish()

            }
        }else{
            startActivity(Intent(this, MainActivity::class.java).apply {})
            finish()

        }

    }

    override fun onStart() {
        super.onStart()

    }

    override fun onStop() {

        super.onStop()
    }
    companion object {
        fun getStartIntent(context: Context, bundle: Bundle?): Intent {
            val intent = Intent(
                context, SplashActivity::class.java
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            return intent
        }
    }
    override fun onCheckConfigParams(BaseUrl: String?) {
        if (BaseUrl != null) {
//            Configurations.BASE_URL = BaseUrl+API_PATH
        Configurations.BASE_URL = "https://kera-test-app.herokuapp.com/api/"
            Log.i("BaseUrl","reach baseUrl : "+BaseUrl)
        }
        Log.i("BaseUrl","reach listner")
    }
}