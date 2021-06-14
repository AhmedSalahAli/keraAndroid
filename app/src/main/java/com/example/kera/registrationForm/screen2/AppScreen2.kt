package com.example.kera.registrationForm.screen2

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.kera.R
import com.example.kera.databinding.AppScreen2FragmentBinding
import com.example.kera.registration.screen1.Registration1Activity
import com.example.kera.registrationForm.screen1.model.PublishAppStep2Model
import com.example.kera.registrationForm.screen3.AppScreen3
import com.example.kera.utils.CommonUtils
import com.example.kera.utils.CommonUtilsJava
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException


class AppScreen2 : Fragment() {
    val viewModel: AppScreen2ViewModel by viewModel()
    lateinit var viewDataBinding: AppScreen2FragmentBinding
    val PERMISSION_CODE_READ =1000
    val PERMISSION_CODE_WRITE = 1001
    val FATHER_REQUEST_CODE = 100
    val MOTHER_REQUEST_CODE = 200
    val RELATIVE_REQUEST_CODE = 300
    var fatherBase64 = ""
    var motherBase64 = ""
    var relativeBase64 = ""
    var applicationId = ""
    var base64 = ""
    var ImageUrl = ""
    private var mProgressDialog: ProgressDialog? = null
    companion object {
        fun newInstance() = AppScreen2()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = DataBindingUtil.inflate(
            inflater, R.layout.app_screen2_fragment, container, false
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
        ImageUrl= bundle!!.getString("profileUrl", "")
        Glide.with(this).load(CommonUtilsJava.convert(base64)).error(R.drawable.ic_person).into(
            viewDataBinding.imageView18
        )
        if (!ImageUrl.isNullOrEmpty()){
            Glide.with(this).load(ImageUrl).error(R.drawable.ic_person).into(
                viewDataBinding.imageView18
            )
        }

        imageClickListener()
        backButtonClickListener()
        nextButtonClickListener()
        addPhoneListener()

    }

    private fun addPhoneListener() {
        viewDataBinding.imgAddPhone.setOnClickListener {

            viewDataBinding.textView462.setAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.slide_in
                )
            )
            viewDataBinding.textView462.visibility = View.VISIBLE
        }
        viewDataBinding.imgAddPhone2.setOnClickListener {
            viewDataBinding.textView4662.setAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.slide_in
                )
            )
            viewDataBinding.textView4662.visibility = View.VISIBLE
        }
        viewDataBinding.imgAddPhone3.setOnClickListener {
            viewDataBinding.textView46662.setAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.slide_in
                )
            )
            viewDataBinding.textView46662.visibility = View.VISIBLE
        }
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

        viewDataBinding.frameFatherImage.setOnClickListener(View.OnClickListener {
            checkPermissionForImage(FATHER_REQUEST_CODE)

        })
        viewDataBinding.frameMotherImage.setOnClickListener(View.OnClickListener {
            checkPermissionForImage(MOTHER_REQUEST_CODE)

        })
        viewDataBinding.frameRelativeImage.setOnClickListener(View.OnClickListener {
            checkPermissionForImage(RELATIVE_REQUEST_CODE)

        })


    }

    private fun openGalleryForImage(REQUEST_CODE: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK ){
           if (requestCode == FATHER_REQUEST_CODE){
               try {
                   val imageUri: Uri? = data!!.data
                   var  selectedImage= MediaStore.Images.Media.getBitmap(
                       requireActivity().contentResolver,
                       imageUri
                   )
                   fatherBase64 = CommonUtilsJava().encodeImage(selectedImage);
                   Glide.with(this).load(data?.data).centerCrop().into(viewDataBinding.imageView35)
               } catch (e: IOException) {
                   e.printStackTrace()
               }

           }else if (requestCode == MOTHER_REQUEST_CODE){
               try {
                   val imageUri: Uri? = data!!.data
                   var  selectedImage= MediaStore.Images.Media.getBitmap(
                       requireActivity().contentResolver,
                       imageUri
                   )
                   motherBase64 = CommonUtilsJava().encodeImage(selectedImage);
                   Glide.with(this).load(data?.data).centerCrop().into(viewDataBinding.imageView355)
               } catch (e: IOException) {
                   e.printStackTrace()
               }

           }else if (requestCode == RELATIVE_REQUEST_CODE){

               try {
                   val imageUri: Uri? = data!!.data
                   var  selectedImage= MediaStore.Images.Media.getBitmap(
                       requireActivity().contentResolver,
                       imageUri
                   )
                   relativeBase64 = CommonUtilsJava().encodeImage(selectedImage);
                   Glide.with(this).load(data?.data).centerCrop(). into(viewDataBinding.imageView3555)
               } catch (e: IOException) {
                   e.printStackTrace()
               }

           }




        }
    }
    private fun nextButtonClickListener() {
        viewDataBinding.textView48.setOnClickListener {
            // startActivity(Intent(this, Registration2Activity::class.java))
            mProgressDialog = CommonUtils.showLoadingDialog(
                requireActivity(),
                R.layout.progress_dialog
            )
            val requestModel = PublishAppStep2Model()
            requestModel.applicationId = applicationId
            requestModel.fastherName = viewDataBinding.textView43.text.toString()
            requestModel.fatherJob = viewDataBinding.textView45.text.toString()
            var fatherphoneList: MutableList<String> = mutableListOf<String>()
            fatherphoneList.add(viewDataBinding.textView46.text.toString())
            if (!viewDataBinding.textView462.text.isEmpty()){
                fatherphoneList.add(viewDataBinding.textView462.text.toString())
            }
            requestModel.fatherPhones = fatherphoneList
            if (!fatherBase64.isEmpty()){
                requestModel.fatherProfileImage = "data:image/jpeg;base64,"+fatherBase64
            }



            requestModel.motherName = viewDataBinding.textView433.text.toString()
            requestModel.motherJob = viewDataBinding.textView455.text.toString()
            var motherphoneList: MutableList<String> = mutableListOf<String>()
            motherphoneList.add(viewDataBinding.textView466.text.toString())
            if (!viewDataBinding.textView4662.text.isEmpty()){
                motherphoneList.add(viewDataBinding.textView4662.text.toString())
            }
            requestModel.motherPhone = motherphoneList
            if (!motherBase64.isEmpty()){
                requestModel.motherProfileImage = "data:image/jpeg;base64,"+motherBase64
            }

            requestModel.relatedName = viewDataBinding.editText.text.toString()
            requestModel.relatedrelation = viewDataBinding.editText2.text.toString()
            var relatedphoneList: MutableList<String> = mutableListOf<String>()
            relatedphoneList.add(viewDataBinding.textView4666.text.toString())
            if (!viewDataBinding.textView46662.text.isEmpty()){
                relatedphoneList.add(viewDataBinding.textView4662.text.toString())
            }
            requestModel.relatedPhones = relatedphoneList
            if (!relativeBase64.isEmpty()){
                requestModel.relatedProfileImage = "data:image/jpeg;base64,"+relativeBase64
            }

            viewModel.publishScreen2(requestModel)

            messageObserver();
        }
        viewModel.publishApp1Boolean.observe(viewLifecycleOwner, {
            CommonUtils.hideLoading(mProgressDialog!!)
            val fragment = AppScreen3()
            val bundle = Bundle()
            bundle.putString("applicationId", applicationId)
            bundle.putString("profileImage", base64)
            bundle.putString("profileUrl", ImageUrl)
            fragment.arguments = bundle
            (activity as Registration1Activity?)?.switchFragment(fragment)
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