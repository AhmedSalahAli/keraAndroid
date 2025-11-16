package com.app.kera.education

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.kera.R
import com.app.kera.databinding.ActivityEducationBinding
import com.app.kera.education.adapter.DateAdapter
import com.app.kera.education.adapter.EducationListAdapter
import com.app.kera.imageViewer.ImageViewerActivity
import com.app.kera.profile.StudentsData
import com.app.kera.profile.adapter.ChildrenAdapter
import com.app.kera.utils.CommonUtils
import com.bumptech.glide.Glide

import com.squareup.picasso.Picasso
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import org.koin.androidx.viewmodel.ext.android.viewModel

class EducationActivity : AppCompatActivity(), DateAdapter.ItemClickNavigator ,EducationListAdapter.CallBack,ChildrenAdapter.CallBack{

    private lateinit var viewDataBinding: ActivityEducationBinding
    val viewModel: EducationViewModel by viewModel()
    private lateinit var dayOfCurrentSelectedDate: String
    private var isSelectedDateIsTodayFlag = true
    private var mProgressDialog: ProgressDialog? = null
    lateinit var accessType: String
    var actualeDate:String = ""
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
        viewModel.profileUIModel.observe(this) {
            //CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.childrenAdapter!!.children = it.students!!
            viewDataBinding.childrenAdapter!!.notifyDataSetChanged()
            if (it.students!!.size == 1) {
                viewDataBinding.imageViewExchange.visibility = View.GONE
            } else {
                viewDataBinding.imageViewExchange.visibility = View.VISIBLE

            }
        }

        //mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)

        viewDataBinding.recyclerEducation.adapter = viewDataBinding.listAdapter // sets your own adapter
        viewDataBinding.recyclerEducation.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false) // sets LayoutManager
        viewDataBinding.recyclerEducation.loadSkeleton(R.layout.item_education) {
            itemCount(4)
            cornerRadius(15f)

        }
        if (accessType == "teacher") {
            viewModel.getDates(viewModel.getTeacheerProfile().classNumber!!)
            viewDataBinding.imageViewExchange.visibility = View.GONE
            viewDataBinding.imageViewProfile.visibility = View.GONE
        } else {
            viewModel.getSelectedChildDataFromSharedPref()?.classId?.let { viewModel.getDates(it) }
            viewModel.getProfileData()
            viewDataBinding.imageViewExchange.visibility = View.VISIBLE
            viewDataBinding.imageViewProfile.visibility = View.VISIBLE
        }
//        viewModel.getDates(viewModel.getSelectedChildDataFromSharedPref()?.classId!!)
        viewDataBinding.datesAdapter = DateAdapter(ArrayList(), this,this)

        viewModel.educationList.observe(this) {
            //CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.recyclerEducation.hideSkeleton()

            viewDataBinding.listAdapter = EducationListAdapter(it, this, this)
            viewDataBinding.listAdapter!!.notifyDataSetChanged()
            if (it.size == 0) {
                showNoData()

            } else {
                hideNoData()
                Log.i("ed123", "More than 0 ")
            }
        }
        viewModel.apiError.observe(this) {
            viewDataBinding.recyclerEducation.hideSkeleton()
            showNoData()
            viewDataBinding.datesAdapter!!.datesList.clear()
            viewDataBinding.datesAdapter!!.notifyDataSetChanged()
        }
        viewModel.apiErrorDates.observe(this) {

            viewDataBinding.datesAdapter!!.datesList.clear()
            viewDataBinding.datesAdapter!!.notifyDataSetChanged()
        }


        viewModel.datesListLiveData.observe(this) {
            //CommonUtils.hideLoading(mProgressDialog!!)

            if (it.size > 0) {

                    viewDataBinding.datesAdapter!!.datesList = it
                    run breaker@{
                        it.forEach { it1 ->
                            if (CommonUtils.isTodayTimeStamp(it1.dateTimestamp)) {
                                viewDataBinding.datesAdapter!!.selectedItem=it.indexOf(it1)
                                actualeDate = it1.actualDate
                                viewDataBinding.datesRecycler.smoothScrollToPosition(it.indexOf(it1))
                                return@breaker
                            }

                        }
                    }
                if (actualeDate.isNullOrEmpty()){
                    val index =  it.size-1
                    actualeDate = it[index].actualDate
                    viewDataBinding.datesAdapter!!.selectedItem=index
                    viewDataBinding.datesRecycler.smoothScrollToPosition(index)

                }

                if (accessType == "teacher") {
                    viewModel.getEducationList(
                        viewModel.getTeacheerProfile().classNumber!!,
                        actualeDate
                    )
                } else {
                    viewModel.getEducationList(
                        viewModel.getSelectedChildDataFromSharedPref()!!.classId!!,
                        actualeDate
                    )
                }
               //actualeDate = it[0].actualDate
//            viewModel.getEducationList(
//                viewModel.getSelectedChildDataFromSharedPref()!!.classId!!,
//                it[0].actualDate
//            )
            } else {
                showNoData()
                viewDataBinding.datesAdapter!!.datesList.clear()

            }

            viewDataBinding.datesAdapter!!.notifyDataSetChanged()

        }
        viewDataBinding.imageViewProfile.setOnClickListener {

            if (viewDataBinding.childrenFrame.isVisible){
                stateOfChildrenFrame(false)
            }else{
                stateOfChildrenFrame(true)
            }
        }
        viewDataBinding.imageViewBack.setOnClickListener {
            finish()
        }
        setupUI(viewDataBinding.educationLay)
    }

    private fun messageObserver() {
        viewModel.message.observe(this@EducationActivity) {
            showMessage(it)
        }
    }

    private fun showMessage(it: String) {
        Toast.makeText(
            this, it,
            Toast.LENGTH_LONG
        ).show();
    }
    fun setupUI(view: View) {

        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                viewDataBinding.childrenFrame.visibility = View.GONE
                false
            }
        }
    }
    override fun onDateClick(position: Int,date: String) {

        if (accessType == "teacher") {
            viewModel.getEducationList(viewModel.getTeacheerProfile().classNumber!!, date)
        } else {
            viewModel.getEducationList(
                viewModel.getSelectedChildDataFromSharedPref()?.classId!!,
                date
            )
        }
        actualeDate = date
    }

    override fun onImageClicked(position: Int, imagesList: ArrayList<String>) {
//        ImageViewer.Builder<String>(this, imagesList)
//            .setStartPosition(position)
//            .hideStatusBar(false)
//            .allowZooming(true)
//            .allowSwipeToDismiss(true)
//            .show()

//        val intent = Intent(this, ImageViewerActivity::class.java)
//        intent.putExtra("location", imagesList)
//        intent.putExtra("selectedPosition",position)
//
//        startActivity(intent)



        val intent = Intent(this, ImageViewerActivity::class.java)
        intent.putExtra("location", imagesList)
        intent.putExtra("Position", position)
        startActivity(intent)
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
//        if (accessType == "teacher") {
//
//            viewModel.getDates(viewModel.getTeacheerProfile().classNumber!!)
//            viewDataBinding.imageViewExchange.visibility = View.GONE
//            viewDataBinding.imageViewProfile.visibility = View.GONE
//           // viewModel.getEducationList(viewModel.selectedUser.value!!.studentCode!!, "")
//        } else {
//
//            viewModel.getDates(viewModel.selectedUser.value!!.className!!)
//
//            viewDataBinding.imageViewExchange.visibility = View.VISIBLE
//            viewDataBinding.imageViewProfile.visibility = View.VISIBLE
//            //viewModel.getEducationList(viewModel.getSelectedChildDataFromSharedPref()?.classId!!, "")
//        }

        if (accessType == "teacher") {
            viewModel.getDates(viewModel.getTeacheerProfile().classNumber!!)
            viewDataBinding.imageViewExchange.visibility = View.GONE
            viewDataBinding.imageViewProfile.visibility = View.GONE
        } else {
            viewModel.getDates(viewModel.getSelectedChildDataFromSharedPref()?.classId!!)
            viewModel.getProfileData()
            viewDataBinding.imageViewExchange.visibility = View.VISIBLE
            viewDataBinding.imageViewProfile.visibility = View.VISIBLE
        }
    }
    private fun showNoData() {
        viewDataBinding.recyclerEducation.visibility = View.GONE
        viewDataBinding.relNoPlanned.visibility = View.VISIBLE

    }
    private fun hideNoData() {
        viewDataBinding.recyclerEducation.visibility = View.VISIBLE
        viewDataBinding.relNoPlanned.visibility = View.GONE
    }
}