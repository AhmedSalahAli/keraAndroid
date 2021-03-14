package com.example.kera.medical

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.kera.R
import com.example.kera.databinding.ActivityMedicalReportBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MedicalReportActivity : AppCompatActivity() {

    val viewModel: MedicalReportViewModel by viewModel()
    lateinit var viewDataBinding: ActivityMedicalReportBinding


    companion object {
        fun newInstance() = MedicalReportActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_medical_report)

        val window: Window =this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this,R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        viewDataBinding.imageViewBack.setOnClickListener {
            finish()
        }
    }
}