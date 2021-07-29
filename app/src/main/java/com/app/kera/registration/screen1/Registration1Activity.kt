package com.app.kera.registration.screen1

import android.app.ProgressDialog
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.app.kera.registrationForm.screen1.AppScreen1
import com.app.kera.R
import com.app.kera.databinding.ActivityRegistration1Binding
import com.app.kera.registrationForm.screen2.AppScreen2
import com.app.kera.registrationForm.screen3.AppScreen3
import com.app.kera.registrationForm.screen4.AppScreen4
import com.app.kera.utils.CommonUtils
import org.koin.androidx.viewmodel.ext.android.viewModel


class Registration1Activity : AppCompatActivity() {


    val viewModel: Registration1ViewModel by viewModel()
    lateinit var viewDataBinding: ActivityRegistration1Binding
    private var mProgressDialog: ProgressDialog? = null

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

        mProgressDialog = CommonUtils.showLoadingDialog(
            this,
            R.layout.progress_dialog
        )
        val stepModel = AppStepModel()
        stepModel.associationId = this.intent.getStringExtra("SchoolID")
        stepModel.message = "test"
        viewModel.getAppStep(
            Settings.Secure.getString(
            this.getContentResolver(),
            Settings.Secure.ANDROID_ID
        ))
       // nextButtonClickListener()
        viewModel.res.observe(this, Observer {
            switchFragment(AppScreen1())
        })
        viewModel.step.observe(this, Observer {
            CommonUtils.hideLoading(mProgressDialog!!)

            var fragment = Fragment()
            val bundle = Bundle()
            bundle.putString("applicationId", it?._id)
            bundle.putString("profileImage", "")
            bundle.putString("profileUrl", it.student?.profileImage)
                when (it.step) {
                    0 ->{
                        fragment = AppScreen1()
                        fragment.arguments = bundle
                        switchFragment(fragment)
                    }
                    1 ->{
                        fragment = AppScreen2()
                        fragment.arguments = bundle
                        switchFragment(fragment)
                    }
                    2 ->{
                        fragment = AppScreen3()
                        fragment.arguments = bundle
                        switchFragment(fragment)
                    }
                    3 ->{
                        fragment = AppScreen4()
                        fragment.arguments = bundle
                        switchFragment(fragment)
                    }

                    else -> { // Note the block
                        finish()
                    }
                }



        })

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