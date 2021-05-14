package com.example.kera.registration.screen1

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.kera.registrationForm.screen1.AppScreen1
import com.example.kera.R
import com.example.kera.databinding.ActivityRegistration1Binding
import com.example.kera.registrationForm.screen2.AppScreen2
import com.example.kera.registrationForm.screen4.AppScreen4
import org.koin.androidx.viewmodel.ext.android.viewModel


class Registration1Activity : AppCompatActivity() {


    val viewModel: Registration1ViewModel by viewModel()
    lateinit var viewDataBinding: ActivityRegistration1Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_registration1)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue_fcfdfd);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;



       // nextButtonClickListener()


        switchFragment(AppScreen1())
    }


    fun switchFragment(fragment: Fragment) {


        val fm: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fm.beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out)

        transaction.replace(viewDataBinding.contentFragment.id,fragment)
        transaction.commit()
    }
//    private fun nextButtonClickListener() {
//        viewDataBinding.textView33.setOnClickListener {
//           // startActivity(Intent(this, Registration2Activity::class.java))
//            switchFragment(AppScreen2())
//        }
//    }


}