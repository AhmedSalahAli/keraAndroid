package com.example.kera.splash.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.kera.R
import com.example.kera.databinding.ActivitySplashBinding
import com.example.kera.login.ui.LoginActivity
import com.example.kera.main.ui.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val splashViewModel: SplashViewModel by viewModel()
    lateinit var viewDataBinding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        val window: Window =this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this,R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = splashViewModel
        splashViewModel.getIsUserLoggedIn()
        splashViewModel.timerFinished.observe(this, {
            appSettingObservation()
        })
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