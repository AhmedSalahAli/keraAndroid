package com.app.kera.meals.details

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.app.kera.R
import com.app.kera.databinding.ActivityMealsDetailsBinding
import com.app.kera.profile.StudentsData
import com.app.kera.profile.adapter.ChildrenAdapter
import com.app.kera.schoolDetails.adapter.ImagesAdapter
import com.app.kera.utils.CommonUtils
import com.smarteist.autoimageslider.SliderAnimations
import com.stfalcon.frescoimageviewer.ImageViewer
import org.koin.androidx.viewmodel.ext.android.viewModel

class MealsDetailsActivity : AppCompatActivity() ,ImagesAdapter.CallBack ,ChildrenAdapter.CallBack{
    val viewModel: MealsDetailsViewModel by viewModel()
    private lateinit var viewDataBinding: ActivityMealsDetailsBinding
    private var mProgressDialog: ProgressDialog? = null
    lateinit var accessType: String

    companion object {
        fun newInstance() = MealsDetailsActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mealID = intent.getStringExtra("MealID")!!

        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_meals_details)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel

//        val window: Window = this.window
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.statusBarColor = ContextCompat.getColor(this, R.color.white);
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        accessType = viewModel.getUserType()

        viewModel.getMealDetails(mealID)
        if (accessType == "teacher") {

            viewDataBinding.imageViewExchange.visibility = View.GONE
            viewDataBinding.imageViewProfile.visibility = View.GONE

        } else {

            viewModel.getProfileData()
            viewDataBinding.imageViewExchange.visibility = View.VISIBLE
            viewDataBinding.imageViewProfile.visibility = View.VISIBLE
            Glide.with(this).load(viewModel.getSelectedChildDataFromSharedPref()!!.profileImage).into(viewDataBinding.imageViewProfile)
            viewDataBinding.childrenAdapter = ChildrenAdapter(ArrayList(), this,viewModel.getAppRepoInstance())
            viewDataBinding.imageViewExchange.setOnClickListener {

                if (viewDataBinding.childrenFrame.isVisible){
                    stateOfChildrenFrame(false)
                }else{
                    stateOfChildrenFrame(true)
                }
            }
        }


        //mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)


        viewDataBinding.veilLayout.veil()


        viewDataBinding.sendButton.setOnClickListener {
            mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)


            val comment = viewDataBinding.commentEt.text.toString()
            val studentID = viewModel.getSelectedChildDataFromSharedPref()!!.studentId
            val mealCommentPostModel = MealCommentPostModel(mealID, comment, studentID)
            viewModel.postComment(mealCommentPostModel)
        }
        viewModel.postCommentObserver.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)


            if (it) {
                Toast.makeText(this, "Comment Posted Successfully", Toast.LENGTH_SHORT).show();
                viewDataBinding.commentEt.setText("")

            } else {
                Toast.makeText(this, "Failed to submit comment", Toast.LENGTH_SHORT).show();
            }
        })
        viewModel.mealDetails.observe(this, {
            //CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.veilLayout.unVeil()

            val adapter = ImagesAdapter(it.images!!,this,this)
            viewDataBinding.recyclerView.setSliderAdapter(adapter)
            viewDataBinding.recyclerView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            viewDataBinding.recyclerView.scrollTimeInSec = 4 //set scroll delay in seconds :
            viewDataBinding.recyclerView.startAutoCycle()
        })
        viewModel.profileUIModel.observe(this, {
            //CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.childrenAdapter!!.children = it.students!!
            viewDataBinding.childrenAdapter!!.notifyDataSetChanged()
        })
        messageObserver()

        viewDataBinding.imageViewBack.setOnClickListener {
            finish()
        }
    }

    private fun messageObserver() {
        viewModel.message.observe(this@MealsDetailsActivity, {
            showMessage(it)
        })
    }

    private fun showMessage(it: String) {
        Toast.makeText(
            this, it,
            Toast.LENGTH_LONG
        ).show();
    }

    override fun onImageClicked(position: Int, imagesList: ArrayList<String>) {

        ImageViewer.Builder<String>(this, imagesList)
            .setStartPosition(position)
            .allowZooming(true)
            .allowSwipeToDismiss(true)
            .show()
    }
    fun stateOfChildrenFrame(state:Boolean){
        if (state){

            viewDataBinding.childrenFrame.setAnimation(
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.fade_in
                )
            )
            viewDataBinding.childrenFrame.visibility = View.VISIBLE

        }else{
            viewDataBinding.childrenFrame.setAnimation(
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.fade_out
                )
            )
            viewDataBinding.childrenFrame.visibility = View.GONE
        }
    }
    override fun onItemClicked(student: StudentsData) {
        stateOfChildrenFrame(false)
        viewModel.saveChildToSharedPref(student)
        viewModel.selectedUser.value = student
        viewModel.selectedUser.value!!.studentCode = "Code:" + student.studentCode
        viewModel.selectedUser.value!!.className = "Class:" + student.className
        //mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)


        viewDataBinding.childrenAdapter!!.notifyDataSetChanged()
    }

}