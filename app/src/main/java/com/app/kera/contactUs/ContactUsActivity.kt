package com.app.kera.contactUs

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.app.kera.R
import com.app.kera.databinding.ContactUFragmentBinding
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
        viewDataBinding.textView130.movementMethod = ScrollingMovementMethod()

        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        viewDataBinding.sendButton.setOnClickListener {

        }
        backClickListener()
    }
    private fun backClickListener() {
        viewDataBinding.imageView77.setOnClickListener {
            finish()
        }
    }
}