package com.app.kera.dailyReport.ui

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.app.kera.R
import com.app.kera.dailyReport.adapter.DailyReportAdapter
import com.app.kera.dailyReport.model.PublishReplay
import com.app.kera.data.models.DisplayDailyReportResponseModel


import com.app.kera.databinding.ActivityReportDetailsBinding
import com.app.kera.utils.CommonUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReportDetails : AppCompatActivity() , CustomDialogClass.CallBack{
    private lateinit var dialog: CustomDialogClass
    lateinit var viewDataBinding: ActivityReportDetailsBinding
    private var mProgressDialog: ProgressDialog? = null
    val viewModel: ReportDetailsViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_report_details)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        if (intent.getSerializableExtra("report") !=null){
            viewModel.response.value = intent.getSerializableExtra("report") as DisplayDailyReportResponseModel.DataBean.DocsBean

        }else{
            finish()
        }
        viewModel.response.observe(this, {
            it.dateConverted =
                CommonUtils.convertTimeStampToDate_EEE_MMM_MM_yyyyTT(it.date!!)
            viewDataBinding.textViewReply.setOnClickListener {

            }
            var reportID  = it.id
            viewDataBinding.textViewDate.text =  CommonUtils.convertTimeStampToDate_EEE_MMM_MM_yyyy(it.date!!)
            viewDataBinding.textViewTime.text =  CommonUtils.convertTimeStampToDate_TT(it.date!!)
            viewDataBinding.recycler.adapter = DailyReportAdapter(it.answers)
            viewDataBinding.textViewReply.setOnClickListener(View.OnClickListener {
                dialog =  CustomDialogClass.newInstance(this@ReportDetails, reportID.toString())
                dialog.show(this.supportFragmentManager, CustomDialogClass.TAG)
            })


        })
        viewDataBinding.imageViewBack.setOnClickListener {
            finish()
        }
        viewModel.publishReplayBoolean.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)
            dialog.dismiss()

        })

    }

    override fun onSendReplyClicked(comment: String, id: String) {
        if (comment.isNullOrEmpty()){
            showMessage(resources.getString(R.string.please_write_your_comment_here))

        }  else{
            mProgressDialog = CommonUtils.showLoadingDialog(
                this,
                R.layout.progress_dialog
            )
            val requestModel = PublishReplay()
            requestModel.reply = comment
            requestModel.reportId = id
            requestModel.type = "daily"
            viewModel.sendReplay(requestModel)
            messageObserver()
        }
    }
    private fun messageObserver() {
        viewModel.message.observe(this, {
            showMessage(it)
            CommonUtils.hideLoading(mProgressDialog!!)
        })

    }
    private fun showMessage(it: String) {
        CommonUtils.hideLoading(mProgressDialog!!)
        Toast.makeText(
            this, it,
            Toast.LENGTH_LONG
        ).show();
    }

}