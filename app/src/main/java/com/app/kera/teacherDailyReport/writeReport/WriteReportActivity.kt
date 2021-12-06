package com.app.kera.teacherDailyReport.writeReport

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.app.kera.R
import com.app.kera.databinding.ActivityWriteReportBinding
import com.app.kera.teacherDailyReport.model.PublishReportRequestModel
import com.app.kera.teacherDailyReport.model.UpdateQuestionRequestModel
import com.app.kera.teacherDailyReport.writeReport.adapter.MoodListAdapter
import com.app.kera.teacherDailyReport.writeReport.adapter.OuterAdapter
import com.app.kera.teacherDailyReport.writeReport.adapter.RadioListAdapter
import com.app.kera.teacherMedicalReport.writeMedicalReport.adapter.WriteReportStudentsAdapter
import com.app.kera.utils.CommonUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.Serializable

class WriteReportActivity : AppCompatActivity(), MoodListAdapter.MoodCallBack,
    OuterAdapter.OuterInterface,RadioListAdapter.RadioCallBack {

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

        viewDataBinding.adapter = OuterAdapter(ArrayList(), this, this,this, this,0)
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

            val dialog = AlertDialog.Builder(this, 5)
                .setCancelable(false)
                .setIcon(R.drawable.kera_box)
                .setTitle(resources.getString(R.string.publish))
                .setMessage(resources.getString(R.string.publish_report_message))
                .setNegativeButton(resources.getString(R.string.no)){
                        dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton(
                    resources.getString(R.string.yes)
                ) { dialog, which ->
                    mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
                    val requestModel = PublishReportRequestModel()
                    requestModel.reportId = reportID
                    viewModel.publishReport(requestModel)
                    val returnIntent = Intent()
                    setResult(RESULT_OK, returnIntent)
                    finish()
                }
            dialog.show()


        }

        viewModel.publishReportResultBoolean.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)
            if (it){
                showMessage(resources.getString(R.string.report_published_successfully))
            }else{
                showMessage(resources.getString(R.string.error_pulishinng))
            }

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

    override fun onRadioClick(
        id: String,
        value: String,
        questionID: String,
        questionposition: Int
    ) {
        var selectedMoods = ArrayList<String>()
        selectedMoods.add(value)

        var updateQuestionRequestModel = UpdateQuestionRequestModel()

        updateQuestionRequestModel.answer = selectedMoods
        updateQuestionRequestModel.question = questionID
        updateQuestionRequestModel.reportId = reportID
//        updateQuestionRequestModel.reportId = "5fd1c92129cffa284f214c03"
        viewModel.updateDailyReportQuestion(updateQuestionRequestModel, reportID)
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