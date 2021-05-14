package com.example.kera.splash.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.example.kera.R
import com.example.kera.databinding.ActivitySplashBinding
import com.example.kera.login.ui.LoginActivity
import com.example.kera.main.ui.MainActivity
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val splashViewModel: SplashViewModel by viewModel()
    lateinit var viewDataBinding: ActivitySplashBinding
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val config: ImagePipelineConfig = ImagePipelineConfig.newBuilder(this)
            .setProgressiveJpegConfig(SimpleProgressiveJpegConfig())
            .setResizeAndRotateEnabledForNetwork(true)
            .setDownsampleEnabled(true)
            .build()
        Fresco.initialize(this, config)

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
                    splashViewModel.timerFinished.observe(this@SplashActivity, {
                        appSettingObservation()
                    })
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .check()
    }
    private fun appSettingObservation() {
        splashViewModel.isAuthorized.observe(this, {
            if (it) {
                startActivity(Intent(this, MainActivity::class.java).apply {})
                finish()
//                navigateToUriWithClearStack(R.string.home_user)
            } else {
                startActivity(Intent(this, LoginActivity::class.java).apply {})
                finish()
//                navigateToUriWithClearStack(R.string.on_boarding)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        viewDataBinding.blurLayout.startBlur()
    }

    override fun onStop() {
        viewDataBinding.blurLayout.pauseBlur()
        super.onStop()
    }
}