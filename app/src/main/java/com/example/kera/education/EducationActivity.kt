package com.example.kera.education

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.kera.R
import com.example.kera.databinding.ActivityEducationBinding
import com.example.kera.education.adapter.DateAdapter
import com.example.kera.education.adapter.EducationListAdapter
import com.example.kera.utils.CommonUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class EducationActivity : AppCompatActivity(), DateAdapter.ItemClickNavigator {

    private lateinit var viewDataBinding: ActivityEducationBinding
    val viewModel: EducationViewModel by viewModel()
    private lateinit var dayOfCurrentSelectedDate: String
    private var isSelectedDateIsTodayFlag = true
    private var mProgressDialog: ProgressDialog? = null
    lateinit var accessType: String

    companion object {
        fun newInstance() = EducationActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        messageObserver()
        viewDataBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_education)

        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        accessType = viewModel.getUserType()

        mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)

        if (accessType == "teacher") {
            viewModel.getDates("5fc2270ce4441941bbf5bcfd")
        } else {
            viewModel.getDates(viewModel.getSelectedChildDataFromSharedPref()?.classId!!)
        }
//        viewModel.getDates(viewModel.getSelectedChildDataFromSharedPref()?.classId!!)
        viewDataBinding.datesAdapter = DateAdapter(ArrayList(), this)

        viewModel.educationList.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.listAdapter = EducationListAdapter(it)
            viewDataBinding.listAdapter!!.notifyDataSetChanged()
        })

        viewModel.datesListLiveData.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.datesAdapter!!.datesList = it
            if (accessType == "teacher") {
                viewModel.getEducationList("5fc2270ce4441941bbf5bcfd", it[0].actualDate)
            } else {
                viewModel.getEducationList(
                    viewModel.getSelectedChildDataFromSharedPref()!!.classId!!,
                    it[0].actualDate
                )
            }
//            viewModel.getEducationList(
//                viewModel.getSelectedChildDataFromSharedPref()!!.classId!!,
//                it[0].actualDate
//            )
            viewDataBinding.datesAdapter!!.notifyDataSetChanged()

        })

        viewDataBinding.imageViewBack.setOnClickListener {
            finish()
        }

    }

    private fun messageObserver() {
        viewModel.message.observe(this@EducationActivity, {
            showMessage(it)
        })
    }

    private fun showMessage(it: String) {
        Toast.makeText(
            this, it,
            Toast.LENGTH_LONG
        ).show();
    }

    override fun onDateClick(date: String) {
        viewModel.getEducationList(viewModel.getSelectedChildDataFromSharedPref()?.classId!!, date)
        if (accessType == "teacher") {
            viewModel.getEducationList("5fc2270ce4441941bbf5bcfd", date)
        } else {
            viewModel.getEducationList(
                viewModel.getSelectedChildDataFromSharedPref()?.classId!!,
                date
            )
        }
    }
}