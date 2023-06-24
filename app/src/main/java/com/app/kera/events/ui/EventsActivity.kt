package com.app.kera.events.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.kera.R
import com.app.kera.databinding.ActivityEventsBinding
import com.app.kera.events.adapter.PreviousListAdapter
import com.app.kera.events.adapter.UpcomingListAdapter
import com.app.kera.events.ui.eventsdetails.EventsDetailsActivity
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventsActivity : AppCompatActivity() ,UpcomingListAdapter.CallBack,PreviousListAdapter.CallBack{

    lateinit var viewDataBinding: ActivityEventsBinding
    val eventsViewModel: EventsViewModel by viewModel()
    var isScrolling: Boolean = false
    var currentItems: Int = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0
    var page = 1
    var totalNumberOfPages: Int = 1
    lateinit var manager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_events)

        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = eventsViewModel
        val window: Window =this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this,R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        viewDataBinding.adapter = UpcomingListAdapter(ArrayList(), this)
        viewDataBinding.previousAdapter = PreviousListAdapter(ArrayList(), this)
        eventsViewModel.getUpcomingEvent(page)
        eventsViewModel.getPreviousEventList(page)
          //mProgressDialog = CommonUtils.showLoadingDialog(requireActivity(), R.layout.progress_dialog)



        manager = LinearLayoutManager(this)
        viewDataBinding.recyclerView4.setLayoutManager(manager)
        viewDataBinding.recyclerView4.setAdapter(viewDataBinding.adapter) // sets your own adapter
        viewDataBinding.recyclerView4.setLayoutManager(LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)) // sets LayoutManager
        viewDataBinding.recyclerView4.getVeiledRecyclerView().layoutManager  = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        viewDataBinding.recyclerView4.addVeiledItems(15)
        viewDataBinding.recyclerView4.getVeiledRecyclerView().layoutDirection = View.LAYOUT_DIRECTION_LTR

        viewDataBinding.recyclerView4.veil()

        viewDataBinding.recyclerView3.layoutManager = manager
        viewDataBinding.recyclerView3.adapter = viewDataBinding.previousAdapter // sets your own adapter
        viewDataBinding.recyclerView3.loadSkeleton(R.layout.item_events_small_image) {
            itemCount(4)
            cornerRadius(15f)

        }

        val scale = resources.getDimension(R.dimen._20sdp)
        val padding_in_px = (scale + 0.5f).toInt()
        viewDataBinding.recyclerView4.getRecyclerView().setPadding(
            padding_in_px,
            0, padding_in_px, 0
        )
        viewDataBinding.recyclerView4 .getRecyclerView().clipToPadding = false
        viewDataBinding.recyclerView4.getVeiledRecyclerView().setPadding(
            padding_in_px,
            0, padding_in_px, 0
        )
        viewDataBinding.recyclerView4 .getVeiledRecyclerView().clipToPadding = false
        viewDataBinding.recyclerView4.getRecyclerView().addOnScrollListener(object :
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

                        eventsViewModel.getUpcomingEvent(page)
                    }
                }
            }
        })
        eventsViewModel.upcomingEventList.observe(this) {

            viewDataBinding.recyclerView4.unVeil()
            if (it.size > 0) {
                viewDataBinding.recyclerView4.visibility = View.VISIBLE
                viewDataBinding.textView62.visibility = View.VISIBLE
            } else {
                viewDataBinding.recyclerView4.visibility = View.GONE
                viewDataBinding.textView62.visibility = View.GONE
            }
            viewDataBinding.adapter!!.UpcomingEventList = it

            viewDataBinding.adapter!!.notifyDataSetChanged()


        }
        eventsViewModel.previousEventList.observe(this) {


            viewDataBinding.recyclerView3.hideSkeleton()
            viewDataBinding.previousAdapter!!.UpcomingEventList = it

            viewDataBinding.previousAdapter!!.notifyDataSetChanged()

        }
        eventsViewModel.anApiFailed.observe(this) {
            //CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.recyclerView4.unVeil()
            Log.e("apiFailed", " FF ")

        }
        viewDataBinding.constraintLayoutBack.setOnClickListener {
            finish()
        }
    }

    override fun onItemClicked(eventID: String?) {
        val myIntent = Intent(this@EventsActivity, EventsDetailsActivity::class.java)
        myIntent.putExtra("EventID", eventID) //Optional parameters
        startActivity(myIntent)
    }
}