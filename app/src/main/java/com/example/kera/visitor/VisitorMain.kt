package com.example.kera.visitor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.kera.R
import com.example.kera.databinding.ActivityRegistration1Binding
import com.example.kera.databinding.ActivityVisitorMainBinding
import com.example.kera.navigation.NavigationFragment
import com.example.kera.registration.screen1.Registration1ViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class VisitorMain : AppCompatActivity() {

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
        switchFragment(NavigationFragment())
    }
    fun switchFragment(fragment: Fragment) {


        val fm: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fm.beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out)

        transaction.replace(viewDataBinding.contentFragment.id,fragment)
        transaction.commit()
    }
}