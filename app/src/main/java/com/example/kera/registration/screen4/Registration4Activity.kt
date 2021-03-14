package com.example.kera.registration.screen4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.kera.R
import com.example.kera.databinding.ActivityRegistration4Binding
import org.koin.androidx.viewmodel.ext.android.viewModel

class Registration4Activity : AppCompatActivity() {

    val viewModel: Registration4ViewModel by viewModel()
    lateinit var viewDataBinding: ActivityRegistration4Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_registration4)

        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel

        backButtonClickListener()
    }

    private fun backButtonClickListener() {
        viewDataBinding.backButton.setOnClickListener {
            finish()
        }
    }
}