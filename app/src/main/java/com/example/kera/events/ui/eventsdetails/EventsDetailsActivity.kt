package com.example.kera.events.ui.eventsdetails

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.kera.R
import com.example.kera.databinding.ActivityEventsDetailsBinding
import com.example.kera.schoolDetails.adapter.ImagesAdapter
import com.smarteist.autoimageslider.SliderAnimations
import com.stfalcon.frescoimageviewer.ImageViewer
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventsDetailsActivity : AppCompatActivity() ,ImagesAdapter.CallBack  {
    lateinit var viewDataBinding: ActivityEventsDetailsBinding
    val eventsDetailsViewModel: EventsDetailsViewModel by viewModel()

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


        eventsDetailsViewModel.getEventDetails(EventID)
        eventsDetailsViewModel.eventDetails.observe(this, {
            val adapter = ImagesAdapter(it.images!!,this,this)
            viewDataBinding.recyclerView.setSliderAdapter(adapter)
            viewDataBinding.recyclerView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            viewDataBinding.recyclerView.scrollTimeInSec = 4 //set scroll delay in seconds :
            viewDataBinding.recyclerView.startAutoCycle()
            val price : Float? = it.eventPrice
            if (price == 0.0f  ||it.eventPrice == null){
                viewDataBinding.textView80.text = "Free"
            }else{
                viewDataBinding.textView80.setText("${it.eventPrice} L.E")
            }

        }

        )


    }
    override fun onImageClicked(position: Int, imagesList: ArrayList<String>) {
        ImageViewer.Builder<String>(this, imagesList)
            .setStartPosition(position)
            .allowZooming(true)
            .allowSwipeToDismiss(true)
            .show()
    }
}