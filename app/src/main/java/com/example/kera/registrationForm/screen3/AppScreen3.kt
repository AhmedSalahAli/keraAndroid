package com.example.kera.registrationForm.screen3

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.kera.R
import com.example.kera.addLocation.AddLocation
import com.example.kera.databinding.AppScreen3FragmentBinding
import com.example.kera.registration.screen1.Registration1Activity
import com.example.kera.registrationForm.screen1.model.PublishAppStep2Model
import com.example.kera.registrationForm.screen3.model.PublishAppStep3
import com.example.kera.registrationForm.screen4.AppScreen4
import com.example.kera.utils.CommonUtils
import com.example.kera.utils.CommonUtilsJava
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class AppScreen3 : Fragment() {
    val viewModel: AppScreen3ViewModel by viewModel()
    lateinit var viewDataBinding: AppScreen3FragmentBinding
    var applicationId = ""
    var base64 = ""
    var MAP_RESULT = 100;
    var lat :Float = 0.0F
    var long :Float = 0.0F
    private var mProgressDialog: ProgressDialog? = null
    companion object {
        fun newInstance() = AppScreen3()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = DataBindingUtil.inflate(
            inflater, R.layout.app_screen3_fragment, container, false
        )
        return viewDataBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewDataBinding.viewModel = viewModel
        viewDataBinding.lifecycleOwner = viewLifecycleOwner
        val bundle = this.arguments
        applicationId = bundle!!.getString("applicationId", "")
        base64 = bundle!!.getString("profileImage", "")

        Glide.with(this).load(CommonUtilsJava.convert(base64)).error(R.drawable.ic_person).into(
            viewDataBinding.imageView18
        )

        nextButtonClickListener()
        addLocationListner()
        backButtonClickListener()
    }
    private fun backButtonClickListener() {
        viewDataBinding.backButton.setOnClickListener {
            requireActivity().finish()
        }
    }
    private fun addLocationListner() {
        viewDataBinding.textView51.setOnClickListener(View.OnClickListener {
            val i = Intent(requireContext(), AddLocation::class.java)
            startActivityForResult(i, MAP_RESULT)
        })
    }

    private fun nextButtonClickListener() {
        viewDataBinding.textViewNext.setOnClickListener {
            // startActivity(Intent(this, Registration2Activity::class.java))
            mProgressDialog = CommonUtils.showLoadingDialog(
                requireActivity(),
                R.layout.progress_dialog
            )
            val requestModel = PublishAppStep3()
            requestModel.applicationId = applicationId
            requestModel.address = viewDataBinding.textView50.text.toString()
            requestModel.medicalHistory = viewDataBinding.textView54.text.toString()
            requestModel.latitude = lat
            requestModel.longitude = long

            viewModel.publishScreen3(requestModel)

            messageObserver();
        }
        viewModel.publishApp1Boolean.observe(viewLifecycleOwner, {
            CommonUtils.hideLoading(mProgressDialog!!)
            val fragment = AppScreen4()
            val bundle = Bundle()
            bundle.putString("applicationId", applicationId)
            bundle.putString("profileImage",base64)
            fragment.arguments = bundle
            (activity as Registration1Activity?)?.switchFragment(fragment)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MAP_RESULT) {

                    lat = data!!.getFloatExtra("lat",0.0f)!!
                    long = data!!.getFloatExtra("long",0.0f)!!

                viewDataBinding.textView52.text = "("+lat+","+long+")"

                }

            }
        }
    private fun messageObserver() {
        viewModel.message.observe(viewLifecycleOwner, {
            showMessage(it)
        })

    }
    private fun showMessage(it: String) {
        CommonUtils.hideLoading(mProgressDialog!!)
        Toast.makeText(
            requireContext(), it,
            Toast.LENGTH_LONG
        ).show();
    }
    }

