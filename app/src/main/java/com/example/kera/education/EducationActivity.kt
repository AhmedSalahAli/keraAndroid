package com.example.kera.education

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kera.R
import com.example.kera.databinding.ActivityEducationBinding
import com.example.kera.education.adapter.DateAdapter
import com.example.kera.education.adapter.EducationListAdapter
import com.example.kera.profile.StudentsData
import com.example.kera.profile.adapter.ChildrenAdapter
import com.example.kera.utils.CommonUtils
import com.stfalcon.frescoimageviewer.ImageViewer
import org.koin.androidx.viewmodel.ext.android.viewModel

class EducationActivity : AppCompatActivity(), DateAdapter.ItemClickNavigator ,EducationListAdapter.CallBack,ChildrenAdapter.CallBack{

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

        viewDataBinding.childrenAdapter = ChildrenAdapter(ArrayList(), this,viewModel.getAppRepoInstance())


        accessType = viewModel.getUserType()

        viewDataBinding.imageViewExchange.setOnClickListener {

            if (viewDataBinding.childrenFrame.isVisible){
                stateOfChildrenFrame(false)
            }else{
                stateOfChildrenFrame(true)
            }
        }
        viewModel.profileUIModel.observe(this, {
            //CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.childrenAdapter!!.children = it.students!!
            viewDataBinding.childrenAdapter!!.notifyDataSetChanged()
        })

        //mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
        viewDataBinding.veilLayout.veil()
        viewDataBinding.recyclerEducation.setAdapter(viewDataBinding.listAdapter) // sets your own adapter
        viewDataBinding.recyclerEducation.setLayoutManager(
            LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false)
        ) // sets LayoutManager
        viewDataBinding.recyclerEducation.addVeiledItems(15)
        viewDataBinding.recyclerEducation.veil()
        if (accessType == "teacher") {
            viewModel.getDates("5fc2270ce4441941bbf5bcfd")
            viewDataBinding.imageViewExchange.visibility = View.GONE
            viewDataBinding.imageViewProfile.visibility = View.GONE
        } else {
            viewModel.getDates(viewModel.getSelectedChildDataFromSharedPref()?.classId!!)
            viewModel.getProfileData()
            viewDataBinding.imageViewExchange.visibility = View.VISIBLE
            viewDataBinding.imageViewProfile.visibility = View.VISIBLE
        }
//        viewModel.getDates(viewModel.getSelectedChildDataFromSharedPref()?.classId!!)
        viewDataBinding.datesAdapter = DateAdapter(ArrayList(), this)

        viewModel.educationList.observe(this, {
            //CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.recyclerEducation.unVeil()

            viewDataBinding.listAdapter = EducationListAdapter(it,this)
            viewDataBinding.listAdapter!!.notifyDataSetChanged()
        })

        viewModel.datesListLiveData.observe(this, {
            //CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.veilLayout.unVeil()

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

    override fun onImageClicked(position: Int, imagesList: ArrayList<String>) {
        ImageViewer.Builder<String>(this, imagesList)
            .setStartPosition(position)
            .hideStatusBar(false)
            .allowZooming(true)
            .allowSwipeToDismiss(true)
            .show()
    }
    fun stateOfChildrenFrame(state:Boolean){
        if (state){

            viewDataBinding.childrenFrame.setAnimation(
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.fade_in
                )
            )
            viewDataBinding.childrenFrame.visibility = View.VISIBLE

        }else{
            viewDataBinding.childrenFrame.setAnimation(
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.fade_out
                )
            )
            viewDataBinding.childrenFrame.visibility = View.GONE
        }
    }
    override fun onItemClicked(student: StudentsData) {
        stateOfChildrenFrame(false)
        viewModel.saveChildToSharedPref(student)
        viewModel.selectedUser.value = student
        viewModel.selectedUser.value!!.studentCode = "Code:" + student.studentCode
        viewModel.selectedUser.value!!.className = "Class:" + student.className
        //mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)


        viewDataBinding.childrenAdapter!!.notifyDataSetChanged()
    }

}