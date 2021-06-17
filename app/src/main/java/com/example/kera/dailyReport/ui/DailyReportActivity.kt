package com.example.kera.dailyReport.ui

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.AbsListView
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.R
import com.example.kera.dailyReport.adapter.DailyReportOuterAdapter
import com.example.kera.dailyReport.model.PublishReplay
import com.example.kera.data.models.DisplayDailyReportResponseModel
import com.example.kera.databinding.ActivityDailyReportBinding
import com.example.kera.profile.StudentsData
import com.example.kera.profile.adapter.ChildrenAdapter
import com.example.kera.utils.CommonUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList


class DailyReportActivity : AppCompatActivity(), ChildrenAdapter.CallBack,
    DailyReportOuterAdapter.OnReplyClicked, CustomDialogClass.CallBack,DatePickerDialog.OnDateSetListener {



    lateinit var viewDataBinding: ActivityDailyReportBinding
    val viewModel: DailyReportViewModel by viewModel()
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
     var dailyReportList: ArrayList<DisplayDailyReportResponseModel.DataBean.DocsBean> = ArrayList()
    private var mProgressDialog: ProgressDialog? = null
    private var id:String = ""
    private lateinit var dialog: CustomDialogClass

    private  var Bmonth:String = ""
    private  var Byear:String = ""
    private  var Bday:String = ""
    private  var Amonth:String = ""
    private  var Ayear:String = ""
    private  var Aday:String = ""

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

        viewDataBinding.childrenAdapter = ChildrenAdapter(
            ArrayList(),
            this,
            viewModel.getAppRepoInstance()
        )
        viewDataBinding.adapter = DailyReportOuterAdapter(ArrayList(), this)

       //mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
        //viewDataBinding.recyclerView10.veil()

        viewModel.getDailyReportData(
//            viewModel.getSelectedChildDataFromSharedPref()?.classId!!,
            viewModel.getSelectedChildDataFromSharedPref()?.studentId.toString(),
            "",
            "",
            page
        )


        val scale =   CommonUtils.pxToDp(viewDataBinding.cardTopView.layoutParams.height, this)
        val padding_in_px = (scale + 50.5f).toInt()
        manager = LinearLayoutManager(this)
        viewDataBinding.recyclerView10.setLayoutManager(manager)
        viewDataBinding.recyclerView10.setAdapter(viewDataBinding.adapter) // sets your own adapter
        viewDataBinding.recyclerView10.addVeiledItems(15)
        viewDataBinding.recyclerView10.veil()
        viewDataBinding.recyclerView10.getRecyclerView().setPadding(0, padding_in_px, 0, 0)
        viewDataBinding.recyclerView10 .getRecyclerView().clipToPadding = false
        viewDataBinding.recyclerView10.getVeiledRecyclerView().setPadding(0, padding_in_px, 0, 0)
        viewDataBinding.recyclerView10 .getVeiledRecyclerView().clipToPadding = false
        viewDataBinding.recyclerView10.getRecyclerView().addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
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

                        viewModel.getDailyReportData(
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
            dailyReportList.addAll(it.data!!.docs!!)
            totalNumberOfPages = it.data!!.pages
            viewDataBinding.adapter!!.dailyReportList = dailyReportList
            viewDataBinding.adapter!!.notifyDataSetChanged()
            if (dailyReportList.size == 0) {
                showNoData()
            } else {
                hideNoData()
            }
        })

        viewModel.publishReplayBoolean.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)
            dialog.dismiss()

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
            dailyReportList.clear()
            page = 1
            viewModel.getDailyReportData(
                viewModel.getSelectedChildDataFromSharedPref()?.studentId.toString(),
                "",
                "",
                page
            )

        })

    }
    fun getDailyData(){
        dailyReportList.clear()
        page = 1
        viewModel.getDailyReportData(
            viewModel.getSelectedChildDataFromSharedPref()?.studentId.toString(),
            "$Bmonth/$Bday/$Byear",
            "$Amonth/$Aday/$Ayear",
            page
        )

    }
    fun showDatePickerDialog(i: Int) {

        var datePickerDialog:DatePickerDialog
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
        dailyReportList.clear()
        page = 1
        isScrolling = false
        viewModel.fromDate.value = ""
        viewModel.toDate.value = ""
        setDateValues()

        viewModel.getDailyReportData(
            viewModel.getSelectedChildDataFromSharedPref()?.studentId.toString(),
            "",
            "",
            page
        )

        viewDataBinding.childrenAdapter!!.notifyDataSetChanged()
        // viewModel.fromDate.value!!
    }

    override fun onReplyClicked(id: String) {



        dialog =  CustomDialogClass.newInstance(this, id)
        dialog.show(this.supportFragmentManager, CustomDialogClass.TAG)
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
    fun stateOfChildrenFrame(state: Boolean){
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
    private fun showNoData() {
        viewDataBinding.recyclerView10.visibility = View.GONE
        viewDataBinding.noData.visibility = View.VISIBLE

    }
    private fun hideNoData() {
        viewDataBinding.recyclerView10.visibility = View.VISIBLE
        viewDataBinding.noData.visibility = View.GONE
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

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

    }
}