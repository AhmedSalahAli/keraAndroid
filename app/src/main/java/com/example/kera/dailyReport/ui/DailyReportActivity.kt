package com.example.kera.dailyReport.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bruce.pickerview.popwindow.DatePickerPopWin
import com.example.kera.R
import com.example.kera.dailyReport.adapter.DailyReportOuterAdapter
import com.example.kera.databinding.ActivityDailyReportBinding
import com.example.kera.profile.StudentsData
import com.example.kera.profile.adapter.ChildrenAdapter
import com.example.kera.utils.CommonUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList


class DailyReportActivity : AppCompatActivity(), ChildrenAdapter.CallBack,
    DailyReportOuterAdapter.OnReplyClicked, CustomDialogClass.CallBack {


    lateinit var viewDataBinding: ActivityDailyReportBinding
    val viewModel: DailyReportViewModel by viewModel()
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    lateinit var studentID: String
    private var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_daily_report)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        viewDataBinding.childrenAdapter = ChildrenAdapter(ArrayList(), this)
        viewDataBinding.adapter = DailyReportOuterAdapter(ArrayList(), this)

        mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)

        viewModel.getDailyReportData(
//            viewModel.getSelectedChildDataFromSharedPref()?.classId!!,
            "5fc22767e4441941bbf5bcff",
            "",
            ""
        )
        viewModel.getProfileData()

        viewModel.response.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.adapter!!.dailyReportList = it.data!!.docs!!
            viewDataBinding.adapter!!.notifyDataSetChanged()
        })


        viewDataBinding.imageViewExchange.setOnClickListener {
            // should open the students recycler
            viewDataBinding.recyclerStudents.visibility = View.VISIBLE
        }

        viewDataBinding.imageViewBack.setOnClickListener {
            finish()
        }


        viewDataBinding.textViewTo.setOnClickListener {
            val pickerPopWin = DatePickerPopWin.Builder(
                this@DailyReportActivity
            ) { year, month, day, dateDesc ->
                viewDataBinding.textViewToDate.text = "$day/$month"
                viewModel.toDate.value = "$day/$month"
            }.textConfirm("Done") //text of confirm button
                .btnTextSize(8) // button text size
                .viewTextSize(8) // pick view text size
                .minYear(1990) //min year in loop
                .maxYear(year + 1) // max year in loop
                .build()
            pickerPopWin.showPopWin(this@DailyReportActivity)
        }


        viewDataBinding.textViewFrom.setOnClickListener {
            val pickerPopWin = DatePickerPopWin.Builder(
                this@DailyReportActivity
            ) { year, month, day, dateDesc ->
                viewModel.fromDate.value = "$day/$month"
                viewDataBinding.textViewFromDate.text = "$day/$month"
            }.textConfirm("Done") //text of confirm button
                .btnTextSize(8) // button text size
                .viewTextSize(8) // pick view text size
                .minYear(1990) //min year in loop
                .maxYear(year + 1) // max year in loop
                .dateChose("$(year+1)-($month+1)-$day") // date chose when init popwindow
                .build()
            pickerPopWin.showPopWin(this@DailyReportActivity)
        }
        viewModel.profileUIModel.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.childrenAdapter!!.children = it.students!!
            viewDataBinding.childrenAdapter!!.notifyDataSetChanged()
        })
    }

    override fun onItemClicked(student: StudentsData) {
        viewDataBinding.recyclerStudents.visibility = View.GONE
        viewModel.saveChildToSharedPref(student)
        viewModel.selectedUser.value = student
        viewModel.selectedUser.value!!.studentCode = "Code:" + student.studentCode
        viewModel.selectedUser.value!!.className = "Class:" + student.className
        mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
        viewModel.getDailyReportData(
            viewModel.getSelectedChildDataFromSharedPref()?.classId!!,
            viewModel.fromDate.value!!,
            ""
        )
    }

    override fun onReplyClicked(id: String) {
        CustomDialogClass.newInstance(this)
            .show(this.supportFragmentManager, CustomDialogClass.TAG)
    }

    override fun onSendReplyClicked(comment: String) {
        // should call send reply api
    }
}