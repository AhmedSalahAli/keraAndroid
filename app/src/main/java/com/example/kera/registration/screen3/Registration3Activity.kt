package com.example.kera.registration.screen3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.kera.R
import com.example.kera.databinding.Registration3ActivityBinding
import com.example.kera.registration.screen4.Registration4Activity
import org.koin.androidx.viewmodel.ext.android.viewModel

class Registration3Activity : AppCompatActivity() {
    val viewModel: Registration3ViewModel by viewModel()
    lateinit var viewDataBinding: Registration3ActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.registration3_activity)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel

        viewDataBinding.textViewNext.setOnClickListener {
            startActivity(Intent(this, Registration4Activity::class.java))
        }
    }
}