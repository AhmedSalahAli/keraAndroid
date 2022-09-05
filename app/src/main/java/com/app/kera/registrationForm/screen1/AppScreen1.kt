package com.app.kera.registrationForm.screen1

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings.Secure
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.app.kera.R
import com.app.kera.databinding.AppScreen1FragmentBinding
import com.app.kera.registration.screen1.Registration1Activity
import com.app.kera.registrationForm.screen1.model.PublishAppStep1Model
import com.app.kera.registrationForm.screen2.AppScreen2
import com.app.kera.utils.CommonUtils
import com.app.kera.utils.CommonUtilsJava
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException
import java.util.*


class AppScreen1 : Fragment() {
    val viewModel: AppScreen1ViewModel by viewModel()
    lateinit var viewDataBinding: AppScreen1FragmentBinding
    val PERMISSION_CODE_READ =1000
    val PERMISSION_CODE_WRITE = 1001
    var isMaleGenderSelected: Boolean = false
    var isFemaleGenderSelected: Boolean = false
    val REQUEST_CODE = 100
    lateinit var dateListener: DatePickerDialog.OnDateSetListener
    var encodedImage: String=""
    private var mProgressDialog: ProgressDialog? = null
    companion object {
        fun newInstance() = AppScreen1()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = DataBindingUtil.inflate(
            inflater, R.layout.app_screen1_fragment, container, false
        )
        return viewDataBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewDataBinding.viewModel = viewModel
        viewDataBinding.lifecycleOwner = viewLifecycleOwner

        Glide.with(this).load(requireActivity().intent.getStringExtra("SchoolLogo")!!).fitCenter().into(viewDataBinding.imageView18)

        viewDataBinding.textView30.textView_selectedCountry.text = ""
        nextButtonClickListener();
        backButtonClickListener()
        imageClickListener()
        datePicker()
        checkBoxNoClickListener()
        checkBoxYesClickListener()
    }
    private fun checkPermissionForImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                && (checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED)
            ) {
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                val permissionCoarse = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)


                requestPermissions(permission, PERMISSION_CODE_READ) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_READ LIKE 1001

                requestPermissions(permissionCoarse, PERMISSION_CODE_WRITE) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_WRITE LIKE 1002
            } else {
                openGalleryForImage()
            }
        }
    }
    private fun backButtonClickListener() {
        viewDataBinding.backButton.setOnClickListener {
           requireActivity().finish()
        }
    }
    private fun imageClickListener() {

        viewDataBinding.imgFrame.setOnClickListener(View.OnClickListener {
            checkPermissionForImage()

        })

    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }
    private fun datePicker() {
        dateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            viewDataBinding.datePicker.text =
                dayOfMonth.toString() + "/" + (month + 1).toString() + "/" + year.toString()
        }

        viewDataBinding.constraintLayout4.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateListener,
                year, month, day
            )
            datePickerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            datePickerDialog.show()
        }
    }

    private fun checkBoxNoClickListener() {
        viewDataBinding.imageViewFemale.setOnClickListener {

            if (isFemaleGenderSelected) {
                isFemaleGenderSelected = false

            } else if (!isFemaleGenderSelected && isMaleGenderSelected) {
                isFemaleGenderSelected = true
                isMaleGenderSelected = false
            } else if (!isFemaleGenderSelected && !isMaleGenderSelected) {
                isFemaleGenderSelected = true
                isMaleGenderSelected = false
            }
            recommendTourFields()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){



             try {
                 val imageUri: Uri? = data!!.data
               var  selectedImage= MediaStore.Images.Media.getBitmap(
                   requireActivity().contentResolver,
                   imageUri
               )
                 encodedImage = CommonUtilsJava().encodeImage(selectedImage);

                 Glide.with(this).load(data?.data).error(R.drawable.ic_person).into(viewDataBinding.profileImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }


        }
    }
    private fun checkBoxYesClickListener() {
        viewDataBinding.imageViewMale.setOnClickListener {
            if (isMaleGenderSelected) {
                isMaleGenderSelected = false
            } else if (!isMaleGenderSelected && isFemaleGenderSelected) {
                isFemaleGenderSelected = false
                isMaleGenderSelected = true
            } else if (!isMaleGenderSelected && !isFemaleGenderSelected) {
                isMaleGenderSelected = true
            }
            recommendTourFields()
        }
    }

    private fun recommendTourFields() {
        if (isMaleGenderSelected) {
            viewDataBinding.imageViewMale.background =
                resources.getDrawable(R.drawable.rounded_light_blue_stroke)
            viewDataBinding.imageViewFemale.background = null
        } else if (isFemaleGenderSelected) {
            viewDataBinding.imageViewFemale.background =
                resources.getDrawable(R.drawable.rounded_light_pink_stroke)
            viewDataBinding.imageViewMale.background = null
        } else if (!isFemaleGenderSelected && !isMaleGenderSelected) {
            viewDataBinding.imageViewMale.background = null
            viewDataBinding.imageViewFemale.background = null
        }
    }

        private fun nextButtonClickListener() {
        viewDataBinding.textView33.setOnClickListener {
           // startActivity(Intent(this, Registration2Activity::class.java))
            mProgressDialog = CommonUtils.showLoadingDialog(
                requireActivity(),
                R.layout.progress_dialog
            )
            val requestModel = PublishAppStep1Model()
            requestModel.name = viewDataBinding.textView28.text.toString()
            if (isFemaleGenderSelected){
                requestModel.gender = 2
            }else if (isMaleGenderSelected){
                requestModel.gender = 1

            }
            requestModel.associationId = requireActivity().intent.getStringExtra("SchoolID")!!
            requestModel.birthDate ="953823068000"
            if (!encodedImage!!.isEmpty()){
                requestModel.profileImage = "data:image/jpeg;base64,"+encodedImage
            }

            requestModel.udId = Secure.getString(
                requireContext().getContentResolver(),
                Secure.ANDROID_ID
            );
            viewModel.publishScreen1(requestModel)

            messageObserver();
        }
            viewModel.publishApp1Boolean.observe(viewLifecycleOwner) {
                CommonUtils.hideLoading(mProgressDialog!!)

                viewModel.applicationId.observe(viewLifecycleOwner, {

                    val fragment = AppScreen2()
                    val bundle = Bundle()
                    bundle.putString("applicationId", it)
                    bundle.putString("profileImage", encodedImage)
                    fragment.arguments = bundle
                    (activity as Registration1Activity?)?.switchFragment(fragment)
                })
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