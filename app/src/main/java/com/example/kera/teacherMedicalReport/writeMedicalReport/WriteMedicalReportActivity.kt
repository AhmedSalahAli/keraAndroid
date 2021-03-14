package com.example.kera.teacherMedicalReport.writeMedicalReport

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.kera.R
import com.example.kera.databinding.WriteMedicalReportFragmentBinding

class WriteMedicalReportActivity : AppCompatActivity() {

    private lateinit var viewModel: WriteMedicalReportViewModel
    private lateinit var viewDataBinding: WriteMedicalReportFragmentBinding
    var isYesCheckBoxChecked: Boolean = false
    var isNoCheckBoxChecked: Boolean = false

    companion object {
        fun newInstance() = WriteMedicalReportActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding =
            DataBindingUtil.setContentView(this, R.layout.write_medical_report_fragment)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel
        checkBoxNoClickListener()
        checkBoxYesClickListener()
    }

    private fun checkBoxNoClickListener() {
        viewDataBinding.constraintlayoutNo.setOnClickListener {

            if (isNoCheckBoxChecked) {
                isNoCheckBoxChecked = false
                viewDataBinding.checkBoxNo.isChecked = false

            } else if (!isNoCheckBoxChecked && isYesCheckBoxChecked) {
                isNoCheckBoxChecked = true
                isYesCheckBoxChecked = false
                viewDataBinding.checkBoxNo.isChecked = true
                viewDataBinding.checkBoxYes.isChecked = false
            } else if (!isNoCheckBoxChecked && !isYesCheckBoxChecked) {
                isNoCheckBoxChecked = true
                isYesCheckBoxChecked = false
                viewDataBinding.checkBoxNo.isChecked = true
                viewDataBinding.checkBoxYes.isChecked = false
            }
            recommendTourFields()
        }
    }

    private fun checkBoxYesClickListener() {
        viewDataBinding.constraintLayoutYes.setOnClickListener {

            if (isYesCheckBoxChecked) {
                viewDataBinding.checkBoxYes.isChecked = false
                isYesCheckBoxChecked = false

            } else if (!isYesCheckBoxChecked && isNoCheckBoxChecked) {
                isNoCheckBoxChecked = false
                isYesCheckBoxChecked = true
                viewDataBinding.checkBoxYes.isChecked = true
                viewDataBinding.checkBoxNo.isChecked = false
            } else if (!isYesCheckBoxChecked && !isNoCheckBoxChecked) {
                isYesCheckBoxChecked = true
                viewDataBinding.checkBoxYes.isChecked = true
            }
            recommendTourFields()
        }
    }

    private fun recommendTourFields() {
        if (isYesCheckBoxChecked) {
            viewDataBinding.constraintLayoutYes.background =
                resources.getDrawable(R.drawable.rounded_white_fcfcfc_stroke_2db7be)

            viewDataBinding.constraintlayoutNo.background =
                resources.getDrawable(R.drawable.rounded_white_fcfcfc_stroke_cfd6de)

        } else if (isNoCheckBoxChecked) {
            viewDataBinding.constraintlayoutNo.background =
                resources.getDrawable(R.drawable.rounded_white_fcfcfc_stroke_f2818a)

            viewDataBinding.constraintLayoutYes.background =
                resources.getDrawable(R.drawable.rounded_white_fcfcfc_stroke_cfd6de)

        } else if (!isNoCheckBoxChecked && !isYesCheckBoxChecked) {
            viewDataBinding.constraintLayoutYes.background =
                resources.getDrawable(R.drawable.rounded_white_fcfcfc_stroke_cfd6de)
            viewDataBinding.constraintlayoutNo.background =
                resources.getDrawable(R.drawable.rounded_white_fcfcfc_stroke_cfd6de)
        }
    }
}