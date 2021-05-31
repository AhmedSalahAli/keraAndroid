package com.example.kera.notification

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AbsListView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.R
import com.example.kera.databinding.NotificationFragmentBinding
import com.example.kera.notification.adapter.NotificationsListAdapter
import com.example.kera.notification.model.NotificationItemUIModel
import com.example.kera.teacherMedicalReport.adapter.LatestReportsListAdapter
import com.example.kera.teacherMedicalReport.model.LatestReportItemUIModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationFragment : Fragment(), NotificationsListAdapter.CallBack {

    lateinit var viewDataBinding: NotificationFragmentBinding
    val viewModel: NotificationViewModel by viewModel()
    var isScrolling: Boolean = false
    var currentItems: Int = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0
    var page = 1
    var totalNumberOfPages: Int = 1
    lateinit var manager: LinearLayoutManager
    var notificationList = ArrayList<NotificationItemUIModel.NotificationModel>()

    companion object {
        fun newInstance() = NotificationFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = DataBindingUtil.inflate(
            inflater, R.layout.notification_fragment, container, false
        )
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.lifecycleOwner = viewLifecycleOwner
        viewDataBinding.viewModel = viewModel
        val window: Window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        viewModel.getNotifications(page)

        viewDataBinding.adapter = NotificationsListAdapter(ArrayList(), this)

        viewDataBinding.recyclerNotifications.setAdapter(viewDataBinding.adapter) // sets your own adapter
        viewDataBinding.recyclerNotifications.setLayoutManager(
            LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,false)
        ) // sets LayoutManager
        viewDataBinding.recyclerNotifications.addVeiledItems(15)
        viewDataBinding.recyclerNotifications.getRecyclerView().setPadding(0,100,0,200)
        viewDataBinding.recyclerNotifications .getRecyclerView().clipToPadding = false
        viewDataBinding.recyclerNotifications.getVeiledRecyclerView().setPadding(0,100,0,200)
        viewDataBinding.recyclerNotifications .getVeiledRecyclerView().clipToPadding = false
        viewDataBinding.recyclerNotifications.veil()
        manager = LinearLayoutManager(requireContext())
        viewDataBinding.recyclerNotifications.setLayoutManager(manager)
        viewDataBinding.recyclerNotifications.getRecyclerView().addOnScrollListener(object :
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
//                        mProgressDialog =
//                            CommonUtils.showLoadingDialog(
//                                this@TeacherMedicalReportActivity,
//                                R.layout.progress_dialog
//                            )
                        Log.e("number of pages", totalNumberOfPages.toString())
                        Log.e("page = ", page.toString())
                        viewModel.getNotifications(page)
                    }
                }
            }
        })
        viewModel.notificationsList.observe(viewLifecycleOwner, {
            viewDataBinding.recyclerNotifications.unVeil()

            totalNumberOfPages = it.pages
            notificationList.addAll(it.notifications)
            viewDataBinding.adapter!!.notifications = notificationList
            viewDataBinding.adapter!!.notifyDataSetChanged()
        })
    }

    override fun onItemClicked(notificationType: String?) {

        if (notificationType!!.equals("1")){
            startActivity(Intent(),)


        }else if (notificationType!!.equals("2")){

        }
    }
}