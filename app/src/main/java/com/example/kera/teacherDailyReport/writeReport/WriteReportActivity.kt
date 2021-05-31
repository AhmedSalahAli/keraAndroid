package com.example.kera.teacherDailyReport.writeReport

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.example.kera.R
import com.example.kera.databinding.ActivityWriteReportBinding
import com.example.kera.teacherDailyReport.model.PublishReportRequestModel
import com.example.kera.teacherDailyReport.model.UpdateQuestionRequestModel
import com.example.kera.teacherDailyReport.writeReport.adapter.MoodListAdapter
import com.example.kera.teacherDailyReport.writeReport.adapter.OuterAdapter
import com.example.kera.teacherMedicalReport.writeMedicalReport.adapter.WriteReportStudentsAdapter
import com.example.kera.utils.CommonUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class WriteReportActivity : AppCompatActivity(), MoodListAdapter.MoodCallBack,
    OuterAdapter.OuterInterface {

    val viewModel: WriteReportViewModel by viewModel()
    lateinit var viewDataBinding: ActivityWriteReportBinding
    lateinit var reportID: String
    private var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_write_report)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel
        messageObserver()
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        reportID = intent.getStringExtra("reportID")!!
        mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
        viewModel.getDailyReportData(reportID)
        backButtonClickListener()

        viewDataBinding.adapter = OuterAdapter(ArrayList(), this, this, this,0)
        viewDataBinding.studentsAdapter = WriteReportStudentsAdapter(ArrayList())

        viewDataBinding.textViewDate.text = CommonUtils.getCurrentDate_EEE_MM_YYY()

        viewModel.response.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.adapter!!.dailyReportList = it.data!!.answers!!
            viewDataBinding.adapter!!.status = it.data!!.status
            viewDataBinding.adapter!!.notifyDataSetChanged()

            viewDataBinding.studentsAdapter!!.studentsList = it.data!!.students!!
            viewDataBinding.studentsAdapter!!.notifyDataSetChanged()

            if (it.data!!.students!!.size > 1) {
                viewDataBinding.textViewNumberOfStudents.text =
                    "${it.data!!.students?.size} students are selected"
            } else {
                viewDataBinding.textViewNumberOfStudents.text =
                    "${it.data!!.students?.size} student is selected"
            }
            if (it.data!!.status == 1){
                viewDataBinding.imageViewPublishReport.visibility = View.VISIBLE
               // viewDataBinding.imageViewPublishReport.isClickable = true
            }else{
                viewDataBinding.imageViewPublishReport.visibility = View.INVISIBLE
                //viewDataBinding.imageViewPublishReport.isClickable = false
            }
        })

        viewModel.updateDailyReportQuestionBoolean.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)

        })



        viewDataBinding.imageViewPublishReport.setOnClickListener {
            mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
            val requestModel = PublishReportRequestModel()
            requestModel.reportId = reportID
            viewModel.publishReport(requestModel)
            finish()
        }

        viewModel.publishReportResultBoolean.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)
        })

    }


    private fun messageObserver() {
        viewModel.message.observe(this@WriteReportActivity, {
            CommonUtils.hideLoading(mProgressDialog!!)
            showMessage(it)
        })
    }

    private fun showMessage(it: String) {
        Toast.makeText(
            this, it,
            Toast.LENGTH_LONG
        ).show();
    }

    private fun backButtonClickListener() {
        viewDataBinding.imageViewBack.setOnClickListener {
            finish()
        }
    }

    override fun onItemClicked(id: String?) {
        Log.e("item id", id!!)
    }

    override fun onMarkClick(id: String, value: String, questionID: String, questionposition: Int) {
        var selectedMoods = ArrayList<String>()
        for (item in viewModel.response.value?.data?.answers?.get(questionposition)?.options!!) {
            if (item.selected) {
                selectedMoods.add(item.value!!)
            }
        }
        var updateQuestionRequestModel = UpdateQuestionRequestModel()

        updateQuestionRequestModel.answer = selectedMoods
        updateQuestionRequestModel.question = questionID
        updateQuestionRequestModel.reportId = reportID
//        updateQuestionRequestModel.reportId = "5fd1c92129cffa284f214c03"
        viewModel.updateDailyReportQuestion(updateQuestionRequestModel, reportID)

    }


    override fun onEditTextDoneClicked(text: String, questionID: String) {
        hideKeyboard(currentFocus ?: View(this))

        var updateQuestionRequestModel = UpdateQuestionRequestModel()
        val answers = ArrayList<String>()
        answers.add(text)
        updateQuestionRequestModel.answer = answers
        updateQuestionRequestModel.question = questionID
//        updateQuestionRequestModel.reportId = "5fd1c92129cffa284f214c03" // reportID
        updateQuestionRequestModel.reportId = reportID
        mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
        viewModel.updateDailyReportQuestion(updateQuestionRequestModel, reportID)

    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}