package com.app.kera.notification

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.app.kera.R

import com.app.kera.dailyReport.ui.DailyReportActivity
import com.app.kera.databinding.NotificationFragmentBinding
import com.app.kera.medical.MedicalReportActivity
import com.app.kera.notification.adapter.PaginationListener
import com.app.kera.notification.adapter.PaginationListener.PAGE_START
import com.app.kera.notification.adapter.PostRecyclerAdapter
import com.app.kera.notification.model.NotificationItemUIModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class NotificationFragment : Fragment(), PostRecyclerAdapter.CallBack , SwipeRefreshLayout.OnRefreshListener{

    lateinit var viewDataBinding: NotificationFragmentBinding
    val viewModel: NotificationViewModel by viewModel()

    var page = 1

    lateinit var manager: LinearLayoutManager
    var notificationList = ArrayList<NotificationItemUIModel.NotificationModel>()
    private var adapter: PostRecyclerAdapter? = null
    private var currentPage: Int = PAGE_START
    private var isLastPage = false
    private var totalPage = 10
    private var isLoading = false
    var itemCount = 0
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

        viewModel.getNotifications(currentPage)
       viewDataBinding. swipeRefresh.setOnRefreshListener(this)
        val layoutManager = LinearLayoutManager(requireContext())

        adapter = PostRecyclerAdapter(ArrayList(), requireContext(),this)
        viewDataBinding.adapter =  PostRecyclerAdapter(ArrayList(), requireContext(),this)

        viewDataBinding.recyclerNotifications.setAdapter(adapter) // sets your own adapter
        viewDataBinding.recyclerNotifications.setLayoutManager(
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL, false
            )
        )
        viewDataBinding.recyclerNotifications.getRecyclerView().setHasFixedSize(true)

        viewDataBinding.recyclerNotifications.addVeiledItems(15)
        viewDataBinding.recyclerNotifications.getRecyclerView().setPadding(0, 100, 0, 200)
        viewDataBinding.recyclerNotifications .getRecyclerView().clipToPadding = false
        viewDataBinding.recyclerNotifications.getVeiledRecyclerView().setPadding(0, 100, 0, 200)
        viewDataBinding.recyclerNotifications .getVeiledRecyclerView().clipToPadding = false
        viewDataBinding.recyclerNotifications.veil()
        manager = LinearLayoutManager(requireContext())
        viewDataBinding.recyclerNotifications.setLayoutManager(layoutManager)


        viewDataBinding.recyclerNotifications.getRecyclerView().addOnScrollListener(object :
            PaginationListener(
                layoutManager
            ) {
            override fun loadMoreItems() {

                this@NotificationFragment.isLoading = true;
                currentPage++;
                viewModel.getNotifications(currentPage)
            }

            override fun isLastPage(): Boolean {
                return this@NotificationFragment.isLastPage;
            }

            override fun isLoading(): Boolean {
                return this@NotificationFragment.isLoading;
            }


        })

//        viewDataBinding.recyclerNotifications.getRecyclerView().addOnScrollListener(object :
//            RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    isScrolling = true
//                }
//            }
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                currentItems = manager.childCount
//                totalItems = manager.itemCount
//                scrollOutItems = manager.findFirstVisibleItemPosition()
//                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
//                    isScrolling = false
//                    if (page < totalNumberOfPages) {
//                        page += 1
//
//
//
//
//                        viewModel.getNotifications(page)
//
//
//                    }
//                }
//            }
//        })
        viewModel.notificationsList.observe(viewLifecycleOwner, {
            viewDataBinding.recyclerNotifications.unVeil()

            var items: ArrayList<NotificationItemUIModel.NotificationModel> = ArrayList()
            totalPage = it.pages

            for (tt in it.notifications) {
                itemCount++

                items.add(tt)
            }
            /**
             * manage progress view
             */
            /**
             * manage progress view
             */
            /**
             * manage progress view
             */
            /**
             * manage progress view
             */

            if (currentPage !== PAGE_START) adapter!!.removeLoading()
            adapter!!.addItems(items)

            viewDataBinding.swipeRefresh.isRefreshing = false
            // check weather is last page or not
            if (currentPage < totalPage) {
                adapter!!.addLoading()
            } else {
                isLastPage = true
            }
            isLoading = false
            Log.e("items Count L ",""+ adapter!!.items.size)
            if (adapter!!.items.size == 0) {
                showNoData()
            } else {
                hideNoData()
            }


//            viewDataBinding.adapter!!.notifyItemInserted(notificationList.size - 1);
//            totalNumberOfPages = it.pages
//
//            for (notification in it.notifications) {
//                notificationList.add(notification)
//                if (it.notifications.size != notificationList.size) {
//                    viewDataBinding.adapter!!.notifyDataSetChanged()
//                }
//
//            }
//
//            // viewDataBinding.adapter!!.notifyDataSetChanged()
//            if (notificationList.size == 0) {
//                showNoData()
//            } else {
//                hideNoData()
//            }
        })
        messageObserver()
        viewModel.logo.observe(viewLifecycleOwner, Observer {
            if (it == "visitor") {
                viewDataBinding.imageView26.setImageResource(R.drawable.kera_box)
            } else {
                Glide.with(this).load(it).into(viewDataBinding.imageView26)

            }
        })

    }

    override fun onItemClicked(notificationType: String?) {

        if (notificationType!!.equals("1")){

            startActivity(Intent(requireContext(), MedicalReportActivity::class.java))


        }else if (notificationType!!.equals("2")){

            startActivity(Intent(requireContext(), DailyReportActivity::class.java))
        }
    }
    private fun messageObserver() {
        viewModel.message.observe(viewLifecycleOwner, {
            viewDataBinding.recyclerNotifications.unVeil()
            showNoData()
        })
    }
    private fun showNoData() {
        viewDataBinding.recyclerNotifications.visibility = View.GONE
        viewDataBinding.relNoNotifications.visibility = View.VISIBLE

    }
    private fun hideNoData() {
        viewDataBinding.recyclerNotifications.visibility = View.VISIBLE
        viewDataBinding.relNoNotifications.visibility = View.GONE
    }


    override fun onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        adapter!!.clear();
        viewDataBinding.recyclerNotifications.veil()
        viewModel.getNotifications(currentPage)
    }
}