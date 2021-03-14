package com.example.kera.registration.screen2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.kera.R
import com.example.kera.databinding.ActivityRegistration2Binding
import com.example.kera.registration.screen3.Registration3Activity
import org.koin.androidx.viewmodel.ext.android.viewModel

class Registration2Activity : AppCompatActivity() {
    val viewModel: Registration2ViewModel by viewModel()
    lateinit var viewDataBinding: ActivityRegistration2Binding

    companion object {
        fun newInstance() = Registration2Activity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_registration2)

        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel
        backButtonClickListener()
        nextButtonClickListener()
    }

    private fun backButtonClickListener() {
        viewDataBinding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun nextButtonClickListener() {
        viewDataBinding.textView48.setOnClickListener {
            startActivity(Intent(this, Registration3Activity::class.java))
        }
    }
}