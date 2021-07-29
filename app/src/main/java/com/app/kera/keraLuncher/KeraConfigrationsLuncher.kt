package com.app.kera.keraLuncher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.app.kera.R
import com.app.kera.databinding.ActivityKeraConfigrationsLuncherBinding
import com.app.kera.main.ui.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class KeraConfigrationsLuncher : AppCompatActivity() {
    val ViewModel: KeraLuncherViewModel by viewModel()
    lateinit var viewDataBinding: ActivityKeraConfigrationsLuncherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_kera_configrations_luncher)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = ViewModel
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        ViewModel.getNurseryData()
        userProfileDataObservation()



    }
    private fun userProfileDataObservation() {
        ViewModel.profileUIModel.observe(this, {
            //CommonUtils.hideLoading(mProgressDialog!!)


            ViewModel.saveProfileResponseToSharedPref(it)
            if (ViewModel.getSelectedChildDataFromSharedPref() == null) {
                ViewModel.saveChildDataToSharedPref(it.students!![0])
            }

        })
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}