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
import com.app.kera.R
import com.app.kera.databinding.ActivityEventsDetailsBinding
import com.app.kera.events.adapter.ChildrenAdapter
import com.app.kera.events.model.EventDetailsResponseModel
import com.app.kera.schoolDetails.adapter.ImagesAdapter
import com.app.kera.utils.Constants
import com.smarteist.autoimageslider.SliderAnimations
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
        window.statusBarColor = ContextCompat.getColor(this, R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        viewDataBinding.adapter = ChildrenAdapter(ArrayList(), this, this)
        viewDataBinding.veilLayout.veil()
        eventsDetailsViewModel.getEventDetails(EventID)
        eventsDetailsViewModel.eventDetails.observe(this, {
            val adapter = ImagesAdapter(it.images!!, this, this)
            viewDataBinding.recyclerView.setSliderAdapter(adapter)
            viewDataBinding.recyclerView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            viewDataBinding.recyclerView.scrollTimeInSec = 4 //set scroll delay in seconds :
            viewDataBinding.recyclerView.startAutoCycle()
            val price: Float? = it.eventPrice
            if (price == 0.0f || it.eventPrice == null) {
                viewDataBinding.textView80.text = "Free"
            } else {
                viewDataBinding.textView80.setText("${it.eventPrice} L.E")
            }

            viewDataBinding.recyclerView7.setAdapter(viewDataBinding.adapter) // sets your own adapter
            viewDataBinding.recyclerView7.setLayoutManager(
                StaggeredGridLayoutManager(
                    1,
                    StaggeredGridLayoutManager.HORIZONTAL
                )
            )
            viewDataBinding.adapter!!.children = it.students!!
            viewDataBinding.adapter!!.notifyDataSetChanged()
            lat = it.latitude?.value!!
            long = it.longitude?.value!!
            Constants.moreImages = it.imagesLink?.value!!
            viewDataBinding.veilLayout.unVeil()
        }

        )


        viewDataBinding.btnAccept.setOnClickListener {

            if (viewDataBinding.recyclerView7.isVisible){
                viewDataBinding.recyclerView7.setAnimation(
                    AnimationUtils.loadAnimation(
                        this,
                        R.anim.slide_out
                    )
                )
                viewDataBinding.recyclerView7.visibility = View.GONE
            }else{
                viewDataBinding.recyclerView7.setAnimation(
                    AnimationUtils.loadAnimation(
                        this,
                        R.anim.slide_in
                    )
                )
                viewDataBinding.recyclerView7.visibility = View.VISIBLE
            }

        }
        viewDataBinding.textView78.setOnClickListener(View.OnClickListener {
            if (lat != 0.0 && long != 0.0) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?&daddr=" + lat + "," + long + "")
                )
                startActivity(intent)
            }

        })
        viewDataBinding.imageView47.setOnClickListener {
            finish()
        }

        viewDataBinding.btnMorePhoto.setOnClickListener(View.OnClickListener {
            if (!Constants.moreImages.isNullOrEmpty() && Constants.moreImages != "") {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(Constants.moreImages)
                startActivity(i)
            } else {
                Toast.makeText(
                    this, resources.getString(R.string.no_more_images),
                    Toast.LENGTH_LONG
                ).show();
            }

        })
        if (!Constants.moreImages.isNullOrEmpty() && Constants.moreImages != "") {
            viewDataBinding.btnMorePhoto.visibility =View.VISIBLE
        } else {
            viewDataBinding.btnMorePhoto.visibility =View.GONE
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