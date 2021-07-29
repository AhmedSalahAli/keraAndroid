package com.app.kera.visitor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.app.kera.R
import com.app.kera.databinding.ActivityVisitorMainBinding
import com.app.kera.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel

class SideViewMain : AppCompatActivity() {

    val viewModel: VisitorMainViewModel by viewModel()
    lateinit var viewDataBinding: ActivityVisitorMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_visitor_main)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue_fcfdfd);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        var url = this.intent.getStringExtra("SideUrl")
        viewDataBinding.webview.loadUrl(url!!)
        when(url){
            Constants.termsLinkApp -> viewDataBinding.textView88.text = resources.getString(R.string.terms_amp_conditions)
            Constants.aboutLinkApp -> viewDataBinding.textView88.text = resources.getString(R.string.about_kera)
            Constants.privacyPolicyLinkApp -> viewDataBinding.textView88.text = resources.getString(R.string.privacy_policy)
            Constants.moreImages -> viewDataBinding.textView88.text = resources.getString(R.string.event_images)
        }
        viewDataBinding.constraintLayoutBack.setOnClickListener {
            finish()
        }
    }

}