package com.example.kera.teacherMedicalReport.writeMedicalReport

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.kera.R
import com.example.kera.data.models.teacherMedicalReport.UpdateMedicalRequestModel
import com.example.kera.databinding.WriteMedicalReportFragmentBinding
import com.example.kera.teacherDailyReport.model.PublishReportRequestModel
import com.example.kera.teacherDailyReport.model.UpdateQuestionRequestModel
import com.example.kera.teacherMedicalReport.TeacherMedicalReportViewModel
import com.example.kera.teacherMedicalReport.writeMedicalReport.adapter.WriteReportStudentsAdapter
import com.example.kera.utils.CommonUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class WriteMedicalReportActivity : AppCompatActivity() {

    private lateinit var viewDataBinding: WriteMedicalReportFragmentBinding
    val viewModel: WriteMedicalReportViewModel by viewModel()
    var isYesCheckBoxChecked: Boolean = false
    var isNoCheckBoxChecked: Boolean = false
    lateinit var reportID: String
    private var mProgressDialog: ProgressDialog? = null
    private var ReportStatus:Int = 0

    companion object {
        fun newInstance() = WriteMedicalReportActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding =
            DataBindingUtil.setContentView(this, R.layout.write_medical_report_fragment)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel

        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        reportID = intent.getStringExtra("reportID")!!
        mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
        viewModel.getDailyMedicalData(reportID)
        backButtonClickListener()
        viewDataBinding.studentsAdapter = WriteReportStudentsAdapter(ArrayList())

        viewDataBinding.textView132.text = CommonUtils.getCurrentDate_EEE_MM_YYY()
        viewModel.response.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.textView138.setText(it.data!!.question2!!.answer)
            if (it.data!!.question1!!.isYes){
                isYesCheckBoxChecked = true
                isNoCheckBoxChecked = false
                viewDataBinding.checkBoxYes.isChecked = true
                recommendTourFields()

            }else if (it.data!!.question1!!.isNo){
                isNoCheckBoxChecked = true
                isYesCheckBoxChecked = false
                viewDataBinding.checkBoxNo.isChecked = true
                recommendTourFields()

            }else{

            }
            viewDataBinding.studentsAdapter!!.studentsList = it.data!!.students!!
            viewDataBinding.studentsAdapter!!.notifyDataSetChanged()

            if (it.data!!.students!!.size > 1) {
                viewDataBinding.textView131.text =
                    "${it.data!!.students?.size} students are selected"
            } else {
                viewDataBinding.textView131.text =
                    "${it.data!!.students?.size} student is selected"
            }
            ReportStatus = it.data!!.status!!
            if (it.data!!.status == 1){
                viewDataBinding.imageView80.visibility = View.VISIBLE
                viewDataBinding.constraintlayoutNo.isClickable = true
                viewDataBinding.constraintLayoutYes.isClickable = true

                viewDataBinding.textView138.isEnabled = true


                if (it.data!!.images!![0].isEmpty()){
                    Glide.with(this).load(resources.getDrawable(R.drawable.upload_picc)).error(R.drawable.upload_picc).into(viewDataBinding.imageView2)

                }
                if (it.data!!.images!![1].isEmpty()){
                    Glide.with(this).load(resources.getDrawable(R.drawable.upload_picc)).error(R.drawable.upload_picc).into(viewDataBinding.imageView1)
                }
                if (it.data!!.images!![2].isEmpty()){
                    Glide.with(this).load(resources.getDrawable(R.drawable.upload_picc)).error(R.drawable.upload_picc).into(viewDataBinding.imageView83)
                }
            }else{
                viewDataBinding.imageView80.visibility = View.INVISIBLE
                viewDataBinding.constraintLayoutYes.isClickable = false
                viewDataBinding.constraintlayoutNo.isClickable = false

                viewDataBinding.textView138.isEnabled = false

            }
            Glide.with(this).load(it.data!!.images!![0]).error(R.drawable.upload_picc).into(viewDataBinding.imageView2)
            Glide.with(this).load(it.data!!.images!![1]).error(R.drawable.upload_picc).into(viewDataBinding.imageView1)
            Glide.with(this).load(it.data!!.images!![2]).error(R.drawable.upload_picc).into(viewDataBinding.imageView83)
        })
        viewDataBinding.imageView80.setOnClickListener {

            mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
            val requestModel = PublishReportRequestModel()
            requestModel.reportId = reportID
            viewModel.publishReport(requestModel)
            finish()
        }
        checkBoxNoClickListener()
        checkBoxYesClickListener()
        recommendTourFields()
        viewModel.updateDailyReportQuestionBoolean.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)
            finish()
        })

    }

    override fun onBackPressed() {

        if (ReportStatus == 1){
            UpdateReport()
        }else{
            super.onBackPressed()
        }
    }
    private fun backButtonClickListener() {
        viewDataBinding.imageView55.setOnClickListener {
            if (ReportStatus == 1){
                UpdateReport()
            }else{
                finish()
            }


        }
    }
    private fun UpdateReport(){
        mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)

        var updateQuestionRequestModel = UpdateMedicalRequestModel()
        if (isYesCheckBoxChecked){
            updateQuestionRequestModel.question1 = "isYes"

        }else if (isNoCheckBoxChecked){
            updateQuestionRequestModel.question1 = "isNo"
        }
        updateQuestionRequestModel.question2 = viewDataBinding.textView138.text.toString()
        updateQuestionRequestModel.reportId = reportID
        viewModel.updateMedicalReportQuestion(updateQuestionRequestModel, reportID)
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


    override fun onStop() {
        super.onStop()

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
            viewDataBinding.textView135.setTextColor(resources.getColor(R.color.blue_yes))
            viewDataBinding.textView136.setTextColor(resources.getColor(R.color.gray_nothing))

            viewDataBinding.constraintlayoutNo.background =
                resources.getDrawable(R.drawable.rounded_white_fcfcfc_stroke_cfd6de)

        } else if (isNoCheckBoxChecked) {
            viewDataBinding.constraintlayoutNo.background =
                resources.getDrawable(R.drawable.rounded_white_fcfcfc_stroke_f2818a)
            viewDataBinding.textView136.setTextColor(resources.getColor(R.color.red_no))
            viewDataBinding.textView135.setTextColor(resources.getColor(R.color.gray_nothing))

            viewDataBinding.constraintLayoutYes.background =
                resources.getDrawable(R.drawable.rounded_white_fcfcfc_stroke_cfd6de)

        } else if (!isNoCheckBoxChecked && !isYesCheckBoxChecked) {
            viewDataBinding.textView136.setTextColor(resources.getColor(R.color.gray_nothing))
            viewDataBinding.textView135.setTextColor(resources.getColor(R.color.gray_nothing))
            viewDataBinding.constraintLayoutYes.background =
                resources.getDrawable(R.drawable.rounded_white_fcfcfc_stroke_cfd6de)
            viewDataBinding.constraintlayoutNo.background =
                resources.getDrawable(R.drawable.rounded_white_fcfcfc_stroke_cfd6de)
        }
    }
}