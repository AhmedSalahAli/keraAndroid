package com.example.kera.attendanceHistory

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
import com.example.kera.R
import com.example.kera.attendanceHistory.adapter.AttendanceListAdapter
import com.example.kera.attendanceHistory.model.AttendanceListItemModel
import com.example.kera.databinding.ActivityAttendanceHistoryBinding
import com.example.kera.databinding.ActivityQRBinding
import com.example.kera.education.adapter.DateAdapter
import com.example.kera.education.adapter.EducationListAdapter
import com.example.kera.qrCode.QRViewModel
import com.example.kera.schoolsList.SchoolListUIModel
import com.example.kera.schoolsList.adapter.SchoolListAdapter
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
        viewDataBinding.veilLayout.veil()
        viewDataBinding.datesAdapter = DateAdapter(ArrayList(), this)

        viewModel.getAttendanceList(page,"")
        viewDataBinding.listAdapter = AttendanceListAdapter(ArrayList(), this)

        manager = LinearLayoutManager(this)
        viewDataBinding.recyclerAttendance.setLayoutManager(manager)
        viewDataBinding.recyclerAttendance.setAdapter(viewDataBinding.listAdapter) // sets your own adapter
        viewDataBinding.recyclerAttendance.addVeiledItems(15)
        viewDataBinding.recyclerAttendance.veil()

        viewDataBinding.recyclerAttendance.getRecyclerView().addOnScrollListener(object :
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
        viewModel.attendanceList.observe(this, {
            // CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.recyclerAttendance.unVeil()

            isScrolling = false

            totalNumberOfPages = 1

            viewDataBinding.listAdapter = AttendanceListAdapter(it,this)
            viewDataBinding.listAdapter!!.notifyDataSetChanged()
        })
        viewModel.getDates()
        viewModel.datesListLiveData.observe(this, {
            viewDataBinding.veilLayout.unVeil()

            viewDataBinding.datesAdapter!!.datesList = it

            viewDataBinding.datesAdapter!!.notifyDataSetChanged()

        })

    }

    override fun onDateClick(date: String) {
        viewDataBinding.recyclerAttendance.veil()
        viewModel.getAttendanceList(page,date)
    }

    override fun onImageClicked(position: Int, imagesList: ArrayList<String>) {

    }
}