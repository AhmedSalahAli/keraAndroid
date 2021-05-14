package com.example.kera.registrationForm.screen4

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.kera.R
import com.example.kera.databinding.AppScreen3FragmentBinding
import com.example.kera.databinding.AppScreen4FragmentBinding
import com.example.kera.registration.screen1.Registration1Activity
import com.example.kera.registrationForm.screen1.model.PublishAppStep2Model
import com.example.kera.registrationForm.screen3.AppScreen3
import com.example.kera.registrationForm.screen3.AppScreen3ViewModel
import com.example.kera.registrationForm.screen4.model.SumbitFinalForm
import com.example.kera.utils.CommonUtils
import com.example.kera.utils.CommonUtilsJava
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException

class AppScreen4 : Fragment() {
    val viewModel: AppScreen4ViewModel by viewModel()
    lateinit var viewDataBinding: AppScreen4FragmentBinding
    val PERMISSION_CODE_READ =1000
    val PERMISSION_CODE_WRITE = 1001
    val ID_REQUEST_CODE = 100
    val BIRTH_REQUEST_CODE = 200
    var applicationId = ""
    var base64 = ""
    var idBase64 = ""
    var birthBase64 = ""
    private var mProgressDialog: ProgressDialog? = null
    companion object {
        fun newInstance() = AppScreen4()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = DataBindingUtil.inflate(
            inflater, R.layout.app_screen4_fragment, container, false
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
        viewModel.getAssociationTerms(requireActivity().intent.getStringExtra("SchoolID")!!)
        viewModel.response.observe(requireActivity(), {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
              viewDataBinding.textView57.setText(Html.fromHtml(it.data!!, Html.FROM_HTML_MODE_LEGACY));
            } else
                viewDataBinding.textView57.setText(Html.fromHtml(it.data!!));
        })

        nextButtonClickListener()
        imageClickListener()
        backButtonClickListener()

    }
    private fun backButtonClickListener() {
        viewDataBinding.backButton.setOnClickListener {
            requireActivity().finish()
        }
    }
    private fun checkPermissionForImage(case: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED)
                && (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED)
            ) {
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                val permissionCoarse = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)


                requestPermissions(permission, PERMISSION_CODE_READ) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_READ LIKE 1001

                requestPermissions(permissionCoarse, PERMISSION_CODE_WRITE) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_WRITE LIKE 1002
            } else {
                openGalleryForImage(case);

            }
        }
    }
    private fun imageClickListener() {

        viewDataBinding.frameImgIdCopy.setOnClickListener(View.OnClickListener {
            checkPermissionForImage(ID_REQUEST_CODE)

        })
        viewDataBinding.frameImgBirth.setOnClickListener(View.OnClickListener {
            checkPermissionForImage(BIRTH_REQUEST_CODE)

        })



    }

    private fun openGalleryForImage(REQUEST_CODE :Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK ){
            if (requestCode == ID_REQUEST_CODE){
                try {
                    val imageUri: Uri? = data!!.data
                    var  selectedImage= MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        imageUri
                    )
                    idBase64 = CommonUtilsJava().encodeImage(selectedImage);
                    Glide.with(this).load(data?.data).centerCrop().into(viewDataBinding.imgIdCopy)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }else if (requestCode == BIRTH_REQUEST_CODE){
                try {
                    val imageUri: Uri? = data!!.data
                    var  selectedImage= MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        imageUri
                    )
                    birthBase64 = CommonUtilsJava().encodeImage(selectedImage);
                    Glide.with(this).load(data?.data).centerCrop().into(viewDataBinding.imgBirth)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }




        }
    }
    private fun nextButtonClickListener() {
        viewDataBinding.textView58.setOnClickListener {
            // startActivity(Intent(this, Registration2Activity::class.java))
            mProgressDialog = CommonUtils.showLoadingDialog(
                requireActivity(),
                R.layout.progress_dialog
            )
            val requestModel = SumbitFinalForm()
            requestModel.applicationId = applicationId

            if (!idBase64.isEmpty()){
                requestModel.idImage = "data:image/jpeg;base64,"+idBase64
            }
            if (!birthBase64.isEmpty()){
                requestModel.birthImage = "data:image/jpeg;base64,"+birthBase64
            }



            viewModel.publishScreen4(requestModel)

            messageObserver();
        }
        viewModel.publishApp1Boolean.observe(viewLifecycleOwner, {
            CommonUtils.hideLoading(mProgressDialog!!)
            requireActivity().finish()
        })
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