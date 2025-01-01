package com.app.kera.events.ui.eventsdetails

import android.content.Intent
import android.net.Uri
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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.app.kera.R
import com.app.kera.databinding.ActivityEventsDetailsBinding
import com.app.kera.events.adapter.ChildrenAdapter
import com.app.kera.events.model.EventDetailsResponseModel
import com.app.kera.schoolDetails.adapter.ImagesAdapter
import com.app.kera.utils.Constants
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventsDetailsActivity : AppCompatActivity() ,ImagesAdapter.CallBack , ChildrenAdapter.CallBack {
    lateinit var viewDataBinding: ActivityEventsDetailsBinding
    val eventsDetailsViewModel: EventsDetailsViewModel by viewModel()
    var lat : Double = 0.0
    var long :Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val EventID = intent.getStringExtra("EventID")!!
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_events_details)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = eventsDetailsViewModel

        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        viewDataBinding.adapter = ChildrenAdapter(ArrayList(), this, this)
        viewDataBinding.veilLayout.loadSkeleton()
        eventsDetailsViewModel.getEventDetails(EventID)

        // Set up ViewPager2 and TabLayout
        val viewPager: ViewPager2 = viewDataBinding.viewPager
        val tabLayout: TabLayout = viewDataBinding.tabLayout

        eventsDetailsViewModel.eventDetails.observe(this) { eventDetails ->
            val adapter = ImagesAdapter(eventDetails.images!!, this, this)
            viewPager.adapter = adapter

            // Set up TabLayout with ViewPager2
            TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

            val price: Float? = eventDetails.eventPrice
            if (price == 0.0f || price == null) {
                viewDataBinding.textView80.text = "Free"
            } else {
                viewDataBinding.textView80.text = "${price} L.E"
            }

            viewDataBinding.recyclerView7.adapter = viewDataBinding.adapter
            viewDataBinding.recyclerView7.layoutManager = StaggeredGridLayoutManager(
                1,
                StaggeredGridLayoutManager.HORIZONTAL
            )
            viewDataBinding.adapter!!.children = eventDetails.students!!
            viewDataBinding.adapter!!.notifyDataSetChanged()

            lat = eventDetails.latitude?.value!!
            long = eventDetails.longitude?.value!!
            Constants.moreImages = eventDetails.imagesLink?.value.toString()
            viewDataBinding.veilLayout.hideSkeleton()
        }

        setupListeners()
    }
    private fun setupListeners() {
        viewDataBinding.btnAccept.setOnClickListener {
            if (viewDataBinding.recyclerView7.isVisible) {
                viewDataBinding.recyclerView7.animation = AnimationUtils.loadAnimation(
                    this,
                    R.anim.slide_out
                )
                viewDataBinding.recyclerView7.visibility = View.GONE
            } else {
                viewDataBinding.recyclerView7.animation = AnimationUtils.loadAnimation(
                    this,
                    R.anim.slide_in
                )
                viewDataBinding.recyclerView7.visibility = View.VISIBLE
            }
        }

        viewDataBinding.textView78.setOnClickListener {
            if (lat != 0.0 && long != 0.0) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?&daddr=$lat,$long")
                )
                startActivity(intent)
            }
        }

        viewDataBinding.imageView47.setOnClickListener {
            finish()
        }

        viewDataBinding.btnMorePhoto.setOnClickListener {
            if (!Constants.moreImages.isNullOrEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.moreImages))
                startActivity(intent)
            } else {
                Toast.makeText(this, getString(R.string.no_more_images), Toast.LENGTH_LONG).show()
            }
        }

        if (!Constants.moreImages.isNullOrEmpty()) {
            viewDataBinding.btnMorePhoto.visibility = View.VISIBLE
        } else {
            viewDataBinding.btnMorePhoto.visibility = View.GONE
        }
    }


    override fun onImageClicked(position: Int, imagesList: ArrayList<String>) {
//        ImageViewer.Builder<String>(this, imagesList)
//            .setStartPosition(position)
//            .allowZooming(true)
//            .allowSwipeToDismiss(true)
//            .show()

    }

    override fun onItemClicked(student: EventDetailsResponseModel.DataBean.StudentData) {

    }
}