package com.example.kera.login.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.kera.R
import com.example.kera.databinding.ActivityLoginBinding
import com.example.kera.pincode.PinCodeActivity
import com.example.kera.utils.CommonUtils
import com.example.kera.visitor.VisitorMain
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginActivity : AppCompatActivity() {

    lateinit var viewDataBinding: ActivityLoginBinding
    val viewModel: LoginViewModel by viewModel()
    private var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel

        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        viewDataBinding.textViewGo.setOnClickListener {
            mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
            viewModel.login()
        }
        setupUI(viewDataBinding.loginLay)
        viewDataBinding.textViewSkip.setOnClickListener {
            viewModel.saveTokenToSharedPref("kera-app")
            val myIntent = Intent(this, VisitorMain::class.java)
            startActivity(myIntent)
        }

        viewModel.loginFailed.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)
        })
        viewModel.loginResponse.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)
            if (it.status == 200) {
                val myIntent = Intent(this, PinCodeActivity::class.java)
                myIntent.putExtra("phoneNumber", viewModel.loginRequestModel.phone)
                myIntent.putExtra("accessType", it.type)
                myIntent.putExtra("token", it.token)
                startActivity(myIntent)
                finish()
            } else {
                showMessage(it.message!!)
            }
        })
        messageObserver()
    }

    private fun messageObserver() {
        viewModel.message.observe(this@LoginActivity, {
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
    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager: InputMethodManager = activity.getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken,
                0
            )
        }
    }
    fun setupUI(view: View) {

        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                hideSoftKeyboard(this)
                false
            }
        }


    }
    fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.cancel()
        }
    }
}