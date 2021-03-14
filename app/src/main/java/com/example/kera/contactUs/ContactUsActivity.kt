package com.example.kera.contactUs

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.kera.R
import com.example.kera.databinding.ContactUFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class ContactUsActivity : AppCompatActivity() {

    val viewModel: ContactUsViewModel by viewModel()
    private lateinit var viewDataBinding: ContactUFragmentBinding

    companion object {
        fun newInstance() = ContactUsActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.contact_u_fragment)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel
        viewDataBinding.messageEt.movementMethod = ScrollingMovementMethod()

        viewDataBinding.sendButton.setOnClickListener {

        }
    }
}