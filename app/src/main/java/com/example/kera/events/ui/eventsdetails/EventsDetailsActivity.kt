package com.example.kera.events.ui.eventsdetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.kera.R
import com.example.kera.databinding.ActivityEventsDetailsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventsDetailsActivity : AppCompatActivity() {
    lateinit var viewDataBinding: ActivityEventsDetailsBinding
    val eventsDetailsViewModel: EventsDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_events_details)

    }
}