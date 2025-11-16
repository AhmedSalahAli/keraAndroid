package com.app.kera.meals.details

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.app.kera.R
import com.app.kera.databinding.ActivityMealsDetailsBinding
import com.app.kera.imageViewer.ImageViewerActivity
import com.app.kera.profile.StudentsData
import com.app.kera.profile.adapter.ChildrenAdapter
import com.app.kera.schoolDetails.adapter.ImagesAdapter
import com.app.kera.utils.CommonUtils
import com.google.android.material.tabs.TabLayoutMediator
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import org.koin.androidx.viewmodel.ext.android.viewModel

class MealsDetailsActivity : AppCompatActivity(), ImagesAdapter.CallBack, ChildrenAdapter.CallBack {

    val viewModel: MealsDetailsViewModel by viewModel()
    private lateinit var viewDataBinding: ActivityMealsDetailsBinding
    private var mProgressDialog: ProgressDialog? = null
    lateinit var accessType: String

    companion object {
        fun newInstance() = MealsDetailsActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mealID = intent.getStringExtra("MealID")
        val mealDate = intent.getStringExtra("MealDate")

        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_meals_details)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        viewDataBinding.textView100.text = mealDate?.let { CommonUtils.convertIsoToDate(it) }

        accessType = viewModel.getUserType()

        mealID?.let { viewModel.getMealDetails(it) }

        if (accessType == "teacher") {
            viewDataBinding.imageViewExchange.visibility = View.GONE
            viewDataBinding.imageViewProfile.visibility = View.GONE
        } else {
            viewModel.getProfileData()
            viewDataBinding.imageViewExchange.visibility = View.VISIBLE
            viewDataBinding.imageViewProfile.visibility = View.VISIBLE
            CommonUtils.loadImage(
                viewDataBinding.imageViewProfile,
                viewModel.getSelectedChildDataFromSharedPref()?.profileImage
            )
            viewDataBinding.childrenAdapter =
                ChildrenAdapter(ArrayList(), this, viewModel.getAppRepoInstance())
            viewDataBinding.imageViewExchange.setOnClickListener {
                if (viewDataBinding.childrenFrame.isVisible) {
                    stateOfChildrenFrame(false)
                } else {
                    stateOfChildrenFrame(true)
                }
            }
        }

        viewDataBinding.veilLayout.loadSkeleton()

        viewDataBinding.sendButton.setOnClickListener {
            mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
            val comment = viewDataBinding.commentEt.text.toString()
            val studentID = viewModel.getSelectedChildDataFromSharedPref()?.studentId
            val mealCommentPostModel = MealCommentPostModel(mealID, comment, studentID)
            viewModel.postComment(mealCommentPostModel)
        }

        viewModel.postCommentObserver.observe(this) {
            CommonUtils.hideLoading(mProgressDialog!!)
            if (it) {
                Toast.makeText(this, "Comment Posted Successfully", Toast.LENGTH_SHORT).show()
                viewDataBinding.commentEt.setText("")
            } else {
                Toast.makeText(this, "Failed to submit comment", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.mealDetails.observe(this) {
            viewDataBinding.veilLayout.hideSkeleton()
            val adapter = ImagesAdapter(it.images ?: ArrayList(), this, this)
            viewDataBinding.viewPager.adapter = adapter
            viewDataBinding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            TabLayoutMediator(viewDataBinding.tabLayout, viewDataBinding.viewPager) { _, _ -> }.attach()

        }

        viewModel.profileUIModel.observe(this) {
            viewDataBinding.childrenAdapter!!.children = it.students ?: ArrayList()
            viewDataBinding.childrenAdapter!!.notifyDataSetChanged()
        }

        messageObserver()

        viewDataBinding.imageViewBack.setOnClickListener {
            finish()
        }
    }

    private fun messageObserver() {
        viewModel.message.observe(this) {
            showMessage(it)
        }
    }

    private fun showMessage(it: String) {
        Toast.makeText(this, it, Toast.LENGTH_LONG).show()
    }

    override fun onImageClicked(position: Int, imagesList: ArrayList<String>) {
        val intent = Intent(this, ImageViewerActivity::class.java)
        intent.putExtra("location", imagesList)
        intent.putExtra("Position", position)
        startActivity(intent)
    }

    private fun stateOfChildrenFrame(state: Boolean) {
        if (state) {
            viewDataBinding.childrenFrame.startAnimation(
                AnimationUtils.loadAnimation(this, R.anim.fade_in)
            )
            viewDataBinding.childrenFrame.visibility = View.VISIBLE
        } else {
            viewDataBinding.childrenFrame.startAnimation(
                AnimationUtils.loadAnimation(this, R.anim.fade_out)
            )
            viewDataBinding.childrenFrame.visibility = View.GONE
        }
    }

    override fun onItemClicked(student: StudentsData) {
        stateOfChildrenFrame(false)
        viewModel.saveChildToSharedPref(student)
        viewModel.selectedUser.value = student
        viewModel.selectedUser.value?.apply {
            studentCode = "Code: $studentCode"
            className = "Class: $className"
        }
        viewDataBinding.childrenAdapter?.notifyDataSetChanged()
    }
}
