package com.app.kera.attendanceHistory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AbsListView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.kera.R
import com.app.kera.attendanceHistory.adapter.AttendanceListAdapter
import com.app.kera.attendanceHistory.model.AttendanceListItemModel
import com.app.kera.databinding.ActivityAttendanceHistoryBinding
import com.app.kera.education.adapter.DateAdapter
import com.app.kera.utils.CommonUtils
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import org.koin.androidx.viewmodel.ext.android.viewModel

class AttendanceHistory : AppCompatActivity(), AttendanceListAdapter.CallBack , DateAdapter.ItemClickNavigator{
    private val viewModel: AttendanceViewModel by viewModel()
    lateinit var viewDataBinding: ActivityAttendanceHistoryBinding
    var isScrolling: Boolean = false
    var currentItems: Int = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0
    var page = 1
    var totalNumberOfPages: Int = 1
    var attendanceList = ArrayList<AttendanceListItemModel>()
    lateinit var manager: LinearLayoutManager
    var actualeDate:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.transparent);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_attendance_history)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel



        viewDataBinding.datesAdapter = DateAdapter(ArrayList(), this,this)


        viewDataBinding.listAdapter = AttendanceListAdapter(ArrayList(), this)

        manager = LinearLayoutManager(this)
        viewDataBinding.recyclerAttendance.layoutManager = manager
        viewDataBinding.recyclerAttendance.adapter = viewDataBinding.listAdapter // sets your own adapter
        viewDataBinding.recyclerAttendance.loadSkeleton(R.layout.attendance_item) {
            itemCount(4)
            cornerRadius(15f)

        }


        viewDataBinding.recyclerAttendance.addOnScrollListener(object :
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
                        //  viewDataBinding.recyclerView.veil()

                        viewModel.getAttendanceList(page,"")
                    }
                }
            }
        })
        viewDataBinding.imageViewBack.setOnClickListener {
            finish()
        }
        viewModel.attendanceList.observe(this) {
            // CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.recyclerAttendance.hideSkeleton()

            isScrolling = false

            totalNumberOfPages = 1

            viewDataBinding.listAdapter = AttendanceListAdapter(it, this)
            viewDataBinding.listAdapter!!.notifyDataSetChanged()
        }
        viewModel.getDates()
        viewModel.datesListLiveData.observe(this) {


            //viewDataBinding.datesRecycler.hideSkeleton()

          //  viewDataBinding.datesAdapter!!.datesList = it
            if (it.size > 0) {
                viewDataBinding.datesAdapter!!.datesList = it
                run breaker@{
                    it.forEach { it1 ->
                        if (CommonUtils.isTodayTimeStamp(it1.dateTimestamp)) {
                            viewDataBinding.datesAdapter!!.selectedItem = it.indexOf(it1)
                            actualeDate = it1.actualDate
                            viewDataBinding.datesRecycler.smoothScrollToPosition(it.indexOf(it1))
                            return@breaker
                        }

                    }
                }
                if (actualeDate.isNullOrEmpty()) {
                    val index = it.size - 1
                    actualeDate = it[index].actualDate
                    viewDataBinding.datesAdapter!!.selectedItem = index
                    viewDataBinding.datesRecycler.smoothScrollToPosition(index)

                }
                viewModel.getAttendanceList(page,actualeDate)
                viewDataBinding.datesAdapter!!.notifyDataSetChanged()

            }
        }

    }

    override fun onDateClick(position: Int,date: String) {
        viewDataBinding.recyclerAttendance.loadSkeleton(R.layout.attendance_item) {
            itemCount(4)
            cornerRadius(15f)

        }
        actualeDate = date
        viewModel.getAttendanceList(page,date)

    }

    override fun onImageClicked(position: Int, imagesList: ArrayList<String>) {

    }
}