package com.app.kera.medical

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.kera.R
import com.app.kera.databinding.ActivityMedicalReportBinding
import com.app.kera.medical.adapter.MedicalReportsAdapter
import com.app.kera.medical.model.DisplayMedicalReportResponseModel
import com.app.kera.profile.StudentsData
import com.app.kera.profile.adapter.ChildrenAdapter
import com.app.kera.utils.CommonUtils
import com.stfalcon.imageviewer.StfalconImageViewer
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class MedicalReportActivity : AppCompatActivity() , ChildrenAdapter.CallBack,
MedicalReportsAdapter.CallBack{

    val viewModel: MedicalReportViewModel by viewModel()
    lateinit var viewDataBinding: ActivityMedicalReportBinding
    val c = Calendar.getInstance()
    var rb_year = c.get(Calendar.YEAR)
    var rb_month = c.get(Calendar.MONTH)
    var rb_day = c.get(Calendar.DAY_OF_MONTH)
    var ra_year = c.get(Calendar.YEAR)
    var ra_month = c.get(Calendar.MONTH)
    var ra_day = c.get(Calendar.DAY_OF_MONTH)
    lateinit var studentID: String
    var isScrolling: Boolean = false
    var currentItems: Int = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0
    var page = 1
    var totalNumberOfPages: Int = 1
    lateinit var manager: LinearLayoutManager
    var medicalReportList: ArrayList<DisplayMedicalReportResponseModel.DataBean.DocsBean> = ArrayList()
    private  var Bmonth:String = ""
    private  var Byear:String = ""
    private  var Bday:String = ""
    private  var Amonth:String = ""
    private  var Ayear:String = ""
    private  var Aday:String = ""
    companion object {
        fun newInstance() = MedicalReportActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_medical_report)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel
        val window: Window =this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this,R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

         viewDataBinding.childrenAdapter = ChildrenAdapter(ArrayList(), this,viewModel.getAppRepoInstance())
        viewDataBinding.adapter = MedicalReportsAdapter(ArrayList(), this,this)

       //mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
        //viewDataBinding.recyclerView10.veil()

        viewModel.getMedicalReportData(
//            viewModel.getSelectedChildDataFromSharedPref()?.classId!!,
            viewModel.getSelectedChildDataFromSharedPref()?.studentId.toString(),
            "",
            "",
            page
        )


        val scale =   CommonUtils.pxToDp(viewDataBinding.cardTopView.layoutParams.height,this)
        val padding_in_px = (scale + 50.5f).toInt()
        manager = LinearLayoutManager(this)
        viewDataBinding.recyclerView10.setLayoutManager(manager)
        viewDataBinding.recyclerView10.setAdapter(viewDataBinding.adapter) // sets your own adapter
        viewDataBinding.recyclerView10.addVeiledItems(15)
        viewDataBinding.recyclerView10.getVeiledRecyclerView().layoutDirection = View.LAYOUT_DIRECTION_LTR

        viewDataBinding.recyclerView10.veil()
        viewDataBinding.recyclerView10.getRecyclerView().setPadding(0,padding_in_px,0,0)
        viewDataBinding.recyclerView10 .getRecyclerView().clipToPadding = false
        viewDataBinding.recyclerView10.getVeiledRecyclerView().setPadding(0,padding_in_px,0,0)
        viewDataBinding.recyclerView10 .getVeiledRecyclerView().clipToPadding = false
        viewDataBinding.recyclerView10.getRecyclerView().addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItems = manager.childCount
                totalItems = manager.itemCount
                scrollOutItems = manager.findFirstVisibleItemPosition()
                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false
                    if (page < totalNumberOfPages) {
                        page += 1
//                        mProgressDialog = CommonUtils.showLoadingDialog(
//                            requireActivity(),
//                            R.layout.progress_dialog
//                        )
                        viewDataBinding.recyclerView10.veil()

                        viewModel.getMedicalReportData(
                            viewModel.getSelectedChildDataFromSharedPref()?.studentId.toString(),
                            "",
                            "",
                            page
                        )
                    }
                }
            }
        })
        viewModel.getProfileData()

        viewModel.response.observe(this, {

            viewDataBinding.recyclerView10.unVeil()

            isScrolling = false
            medicalReportList.addAll(it.data!!.docs!!)
            totalNumberOfPages = it.data!!.pages
           viewDataBinding.adapter!!.medicalReportList = medicalReportList
           viewDataBinding.adapter!!.notifyDataSetChanged()
            if (medicalReportList.size == 0) {
                showNoData()
            } else {
                hideNoData()
            }
        })


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
        viewDataBinding.imageViewBack.setOnClickListener {
            finish()
        }

        viewDataBinding.textViewFromDate.setOnClickListener {


            showDatePickerDialog(0)

        }
        viewDataBinding.textViewToDate.setOnClickListener {


            showDatePickerDialog(1)

        }
        viewDataBinding.textViewTo.setOnClickListener {
//            val pickerPopWin = DatePickerPopWin.Builder(
//                this@DailyReportActivity
//            ) { year, month, day, dateDesc ->
//                viewDataBinding.textViewToDate.text = "$day/$month"
//                viewModel.toDate.value = "$day/$month"
//            }.textConfirm("Done") //text of confirm button
//                .btnTextSize(8) // button text size
//                .viewTextSize(8) // pick view text size
//                .minYear(1990) //min year in loop
//                .maxYear(year + 1) // max year in loop
//                .build()
//            pickerPopWin.showPopWin(this@DailyReportActivity)

            showDatePickerDialog(1)

        }


        viewDataBinding.textViewFrom.setOnClickListener {
//            val pickerPopWin = DatePickerPopWin.Builder(
//                this@DailyReportActivity
//            ) { year, month, day, dateDesc ->
//                viewModel.fromDate.value = "$day/$month"
//                viewDataBinding.textViewFromDate.text = "$day/$month"
//            }.textConfirm("Done") //text of confirm button
//                .btnTextSize(8) // button text size
//                .viewTextSize(8) // pick view text size
//                .minYear(1990) //min year in loop
//                .maxYear(year + 1) // max year in loop
//                .dateChose("$(year+1)-($month+1)-$day") // date chose when init popwindow
//                .build()
//            pickerPopWin.showPopWin(this@DailyReportActivity)
            showDatePickerDialog(0)
        }


        viewModel.profileUIModel.observe(this, {
            //CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.childrenAdapter!!.children = it.students!!
            viewDataBinding.childrenAdapter!!.notifyDataSetChanged()
        })
        setDateValues()
        viewDataBinding.imageView65.setOnClickListener(View.OnClickListener {
            viewModel.fromDate.value = ""
            viewModel.toDate.value = ""
            rb_year = c.get(Calendar.YEAR)
            rb_month = c.get(Calendar.MONTH)
            rb_day = c.get(Calendar.DAY_OF_MONTH)
            ra_year = c.get(Calendar.YEAR)
            ra_month = c.get(Calendar.MONTH)
            ra_day = c.get(Calendar.DAY_OF_MONTH)
            setDateValues()
            medicalReportList.clear()
            page = 1
            viewModel.getMedicalReportData(
                viewModel.getSelectedChildDataFromSharedPref()?.studentId.toString(),
                "",
                "",
                page
            )

        })
    }
    fun getDailyData(){
        medicalReportList.clear()
        page = 1
        viewModel.getMedicalReportData(
            viewModel.getSelectedChildDataFromSharedPref()?.studentId.toString(),
            "$Bmonth/$Bday/$Byear",
            "$Amonth/$Aday/$Ayear",
            page
        )

    }
    fun showDatePickerDialog(i: Int) {

        var datePickerDialog: DatePickerDialog
        if (i==0){
            datePickerDialog = DatePickerDialog(
                this, R.style.MyTimePickerDialogTheme,
                { view, year, monthOfYear, dayOfMonth ->
                    //DO SOMETHING
                }, rb_year,
                rb_month,
                rb_day
            )

            datePickerDialog.show()

        }else{
            datePickerDialog = DatePickerDialog(
                this, R.style.MyTimePickerDialogTheme,
                { view, year, monthOfYear, dayOfMonth ->
                    //DO SOMETHING
                }, rb_year,
                rb_month,
                rb_day
            )
            datePickerDialog.show()
        }


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            datePickerDialog.setOnDateSetListener(DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                if (i == 0) {
                    Bmonth = "" + (month + 1)
                    Bday = "" + dayOfMonth
                    Byear = "" + year
                    rb_day = dayOfMonth
                    rb_month = month
                    rb_year = year
                    viewDataBinding.textViewFromDate.text = "$Bday/$Bmonth"
                } else {
                    Amonth = "" + (month + 1)
                    Aday = "" + dayOfMonth
                    Ayear = "" + year
                    ra_day = dayOfMonth
                    ra_month = month
                    ra_year = year
                    viewDataBinding.textViewToDate.text = "$Aday/$Amonth"
                }
                getDailyData()

            })
        }
    }
    fun setDateValues(){
        if (viewModel.fromDate.value.isNullOrEmpty()){
            viewDataBinding.textViewFromDate.text = resources.getString(R.string.start)
        }else{
            viewDataBinding.textViewFromDate.text = viewModel.fromDate.value
        }



        if (viewModel.toDate.value.isNullOrEmpty()){
            viewDataBinding.textViewToDate.text = resources.getString(R.string.end)
        }else{
            viewDataBinding.textViewToDate.text = viewModel.toDate.value
        }

    }
    override fun onItemClicked(student: StudentsData) {
        stateOfChildrenFrame(false)
        viewModel.saveChildToSharedPref(student)
        viewModel.selectedUser.value = student
        viewModel.selectedUser.value!!.studentCode = "Code:" + student.studentCode
        viewModel.selectedUser.value!!.className = "Class:" + student.className
        //mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
        viewDataBinding.recyclerView10.veil()
        medicalReportList.clear()
        page = 1
        isScrolling = false
        viewModel.fromDate.value = ""
        viewModel.toDate.value = ""
        setDateValues()
        viewModel.getMedicalReportData(
            viewModel.getSelectedChildDataFromSharedPref()?.studentId!!,
            viewModel.fromDate.value!!,
            viewModel.toDate.value!!,
            page
        )

        viewDataBinding.childrenAdapter!!.notifyDataSetChanged()
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

    override fun onImageClicked(position: Int, imagesList: ArrayList<String>) {
        StfalconImageViewer.Builder<String>(this, imagesList) { imageView, imageUrl ->
            CommonUtils.loadImage(imageView, imageUrl) // Replace with your preferred image loading method
        }
            .withStartPosition(position)
            .withBackgroundColor(ContextCompat.getColor(this, R.color.black)) // Set background color
            .allowZooming(true) // Enable zoom
            .allowSwipeToDismiss(true) // Allow swipe-to-dismiss functionality
            .withHiddenStatusBar(true) // Hide the status bar for immersive view
            .show()
    }

    private fun showNoData() {
        viewDataBinding.recyclerView10.visibility = View.GONE
        viewDataBinding.noData.visibility = View.VISIBLE

    }
    private fun hideNoData() {
        viewDataBinding.recyclerView10.visibility = View.VISIBLE
        viewDataBinding.noData.visibility = View.GONE
    }
}