package com.example.kera.pincode

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.example.kera.R
import com.example.kera.databinding.ActivityPinCodeBinding
import com.example.kera.main.ui.MainActivity
import com.example.kera.utils.CommonUtils
import org.koin.androidx.viewmodel.ext.android.viewModel


class PinCodeActivity : AppCompatActivity() {
    lateinit var phoneNumber: String
    lateinit var token: String
    lateinit var pinCode: String
    lateinit var accessType: String
    private val pinCodeViewModel: PinCodeViewModel by viewModel()
    lateinit var viewDataBinding: ActivityPinCodeBinding
    private var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_pin_code)
        viewDataBinding.viewModel = pinCodeViewModel
        viewDataBinding.lifecycleOwner = this


        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        viewDataBinding.textViewResend.paintFlags =
            viewDataBinding.textViewResend.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        viewDataBinding.firstPinView.setAnimationEnable(true)

        phoneNumber = intent.getStringExtra("phoneNumber")!!
        token = intent.getStringExtra("token")!!
        accessType = intent.getStringExtra("accessType")!!

        val content = SpannableString(phoneNumber)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        viewDataBinding.textViewPhone.text = content

        viewDataBinding.textViewResend.setOnClickListener {
            // pinCodeViewModel.login()
        }
        messageObserver()
        pinCodeTextWatcher()
        verifyPhoneObserver()
    }
    private fun verifyPhoneObserver(){
        pinCodeViewModel.verifyPhoneObserver.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)
            pinCodeViewModel.saveTokenToSharedPref(token)
            pinCodeViewModel.saveUserLoggedIn()
            pinCodeViewModel.saveUserType(accessType)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })
    }
    private fun pinCodeTextWatcher() {
        viewDataBinding.firstPinView.addTextChangedListener {
            if (it?.length ?: 0 == 4) {
                mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
                pinCodeViewModel.verifyPhoneRequestModel.code = it.toString()
                pinCodeViewModel.verifyPhoneRequestModel.phone = phoneNumber
                pinCodeViewModel.verifyPhone(token, accessType)
            }
        }
    }

    private fun messageObserver() {
        pinCodeViewModel.message.observe(this@PinCodeActivity, {
            CommonUtils.hideLoading(mProgressDialog!!)
            showMessage(it)
        })
    }

    private fun showMessage(it: String) {
        Toast.makeText(
            this, it,
            Toast.LENGTH_LONG
        ).show();
    }
}