package com.example.kera.qrCode

import android.app.ProgressDialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.kera.R
import com.example.kera.databinding.ActivityMainBinding
import com.example.kera.databinding.ActivityQRBinding
import com.example.kera.main.ui.MainViewModel
import com.example.kera.registration.screen1.Registration1Activity
import com.example.kera.registrationForm.screen1.model.PublishAppStep1Model
import com.example.kera.registrationForm.screen2.AppScreen2
import com.example.kera.utils.CommonUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
private const val CAMERA_REQUEST_CODE = 101
class QRActivity : AppCompatActivity() {
    private val viewModel: QRViewModel by viewModel()
    lateinit var viewDataBinding: ActivityQRBinding
    private lateinit var codeScanner: CodeScanner
    private var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    //        val window: Window = this.window
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.statusBarColor = ContextCompat.getColor(this, R.color.transparent);
//
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_q_r)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel
        setUpPermissions()
        codeScanner = CodeScanner(this, viewDataBinding.scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {

            runOnUiThread {
                mProgressDialog = CommonUtils.showLoadingDialog(
                    this,
                    R.layout.progress_dialog
                )
                val requestModel = QrCodeModel()
                requestModel.code = it.text
                viewModel.publishAttendanceQrCode(requestModel)
                messageObserver()
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        viewDataBinding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
        viewDataBinding.constraintLayoutBack.setOnClickListener {
            finish()
        }
        viewModel.publishApp1Boolean.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)
            finish()

        })

    }
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
    private  fun setUpPermissions(){
        val permission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }

    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "You need to allow camera Permission !",
                        Toast.LENGTH_LONG).show()
                    finish()
                }
            }


        }
    }
    private fun messageObserver() {
        viewModel.message.observe(this, {
            showMessage(it)
        })

    }
    private fun showMessage(it: String) {
        CommonUtils.hideLoading(mProgressDialog!!)
        Toast.makeText(
            this, it,
            Toast.LENGTH_LONG
        ).show();
    }
}