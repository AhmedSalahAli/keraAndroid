package com.app.kera.teacherMedicalReport.writeMedicalReport

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.app.kera.R
import com.app.kera.data.models.teacherMedicalReport.UpdateMedicalRequestModel
import com.app.kera.databinding.WriteMedicalReportFragmentBinding
import com.app.kera.teacherDailyReport.model.PublishReportRequestModel
import com.app.kera.teacherMedicalReport.model.ImageRequest
import com.app.kera.teacherMedicalReport.writeMedicalReport.adapter.WriteReportStudentsAdapter
import com.app.kera.utils.CommonUtils
import com.app.kera.utils.CommonUtilsJava
import com.app.kera.imageViewer.ImageViewerActivity
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException

class WriteMedicalReportActivity : AppCompatActivity() {

    private lateinit var viewDataBinding: WriteMedicalReportFragmentBinding
    val viewModel: WriteMedicalReportViewModel by viewModel()
    var isYesCheckBoxChecked: Boolean = false
    var isNoCheckBoxChecked: Boolean = false
     var reportID: String =""
    private var mProgressDialog: ProgressDialog? = null
    private var ReportStatus:Int = 0
    val REQUEST_CODE_ONE = 100
    val REQUEST_CODE_TWO= 200
    val REQUEST_CODE_THREE = 300
    val images = ArrayList<String>()
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
        viewModel.response.observe(this) {
            CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.textView138.setText(it.data!!.question2!!.answer)
            if (it.data!!.question1!!.isYes) {
                isYesCheckBoxChecked = true
                isNoCheckBoxChecked = false
                viewDataBinding.checkBoxYes.isChecked = true
                recommendTourFields()

            } else if (it.data!!.question1!!.isNo) {
                isNoCheckBoxChecked = true
                isYesCheckBoxChecked = false
                viewDataBinding.checkBoxNo.isChecked = true
                recommendTourFields()

            } else {

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
            if (it.data!!.status == 1) {
                viewDataBinding.imageView80.visibility = View.VISIBLE
                viewDataBinding.constraintlayoutNo.isClickable = true
                viewDataBinding.constraintLayoutYes.isClickable = true

                viewDataBinding.textView138.isEnabled = true



            } else {
                viewDataBinding.imageView80.visibility = View.INVISIBLE
                viewDataBinding.constraintLayoutYes.isClickable = false
                viewDataBinding.constraintlayoutNo.isClickable = false

                viewDataBinding.textView138.isEnabled = false

            }
//            if (it.data!!.images!![0].isEmpty()) {
//                Glide.with(this).load(resources.getDrawable(R.drawable.upload_picc))
//                    .error(R.drawable.upload_picc).into(viewDataBinding.imageView2)
//
//            }
//            if (it.data!!.images!![1].isEmpty()) {
//                Glide.with(this).load(resources.getDrawable(R.drawable.upload_picc))
//                    .error(R.drawable.upload_picc).into(viewDataBinding.imageView1)
//            }
//            if (it.data!!.images!![2].isEmpty()) {
//                Glide.with(this).load(resources.getDrawable(R.drawable.upload_picc))
//                    .error(R.drawable.upload_picc).into(viewDataBinding.imageView83)
//            }
            Glide.with(this).load(it.data!!.images!![0]).error(R.drawable.upload_picc)
                .into(viewDataBinding.imageView2)
            Glide.with(this).load(it.data!!.images!![1]).error(R.drawable.upload_picc)
                .into(viewDataBinding.imageView1)
            Glide.with(this).load(it.data!!.images!![2]).error(R.drawable.upload_picc)
                .into(viewDataBinding.imageView83)
        }
        viewDataBinding.imageView80.setOnClickListener {

            mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
            val requestModel = PublishReportRequestModel()
            requestModel.reportId = reportID
            viewModel.publishReport(requestModel)
            finish()
        }
        checkBoxNoClickListener()
        checkBoxYesClickListener()
        imagesListener()
        recommendTourFields()
        viewModel.updateDailyReportQuestionBoolean.observe(this) {
            CommonUtils.hideLoading(mProgressDialog!!)
            finish()
        }

    }

    private fun imagesListener() {

        viewDataBinding.constraintLayout30.setOnClickListener(View.OnClickListener {
            if (ReportStatus == 1){
                openGalleryForImageOne()
            }else{
                showImages(0)
            }

        })
        viewDataBinding.constraintLayout3.setOnClickListener(View.OnClickListener {

            if (ReportStatus == 1){
                openGalleryForImageTwo()
            }else{
                showImages(1)
            }
        })
        viewDataBinding.constraintLayout32.setOnClickListener(View.OnClickListener {

            if (ReportStatus == 1){
                openGalleryForImageThree()
            }else{
                showImages(2)
            }

        })
    }

    private fun showImages(position: Int) {
        if (images.isNotEmpty()) {
            val intent = Intent(this, ImageViewerActivity::class.java)
            intent.putStringArrayListExtra("location", images)
            intent.putExtra("Position", position)
            startActivity(intent)
        }
    }


    private fun openGalleryForImageOne() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_ONE)
    }
    private fun openGalleryForImageTwo() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_TWO)
    }
    private fun openGalleryForImageThree() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_THREE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK ){

            if (data!=null){
                try {
                    val imageUri: Uri? = data!!.data
                    var  selectedImage= MediaStore.Images.Media.getBitmap(
                        contentResolver,
                        imageUri
                    )

                    when(requestCode){
                        REQUEST_CODE_ONE->{

                                viewModel.updateMedicalImage(ImageRequest(reportID,"data:image/jpeg;base64,"+CommonUtilsJava().encodeImage(selectedImage)))
                                Glide.with(this).load(data?.data).error(R.drawable.upload_picc).into(viewDataBinding.imageView83)

                        }
                        REQUEST_CODE_TWO->{

                                viewModel.updateMedicalImage(ImageRequest(reportID,"data:image/jpeg;base64,"+CommonUtilsJava().encodeImage(selectedImage)))
                                Glide.with(this).load(data?.data).error(R.drawable.upload_picc).into(viewDataBinding.imageView1)

                        }
                        REQUEST_CODE_THREE->{

                                viewModel.updateMedicalImage(ImageRequest(reportID,"data:image/jpeg;base64,"+CommonUtilsJava().encodeImage(selectedImage)))
                                Glide.with(this).load(data?.data).error(R.drawable.upload_picc).into(viewDataBinding.imageView2)
                            }
                        }




                } catch (e: IOException) {
                    e.printStackTrace()
                }



            }





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