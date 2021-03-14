package com.example.kera.meals.details

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.kera.R
import com.example.kera.databinding.ActivityMealsDetailsBinding
import com.example.kera.schoolDetails.adapter.ImagesAdapter
import com.example.kera.utils.CommonUtils
import com.smarteist.autoimageslider.SliderAnimations
import org.koin.androidx.viewmodel.ext.android.viewModel

class MealsDetailsActivity : AppCompatActivity() {
    val viewModel: MealsDetailsViewModel by viewModel()
    private lateinit var viewDataBinding: ActivityMealsDetailsBinding
    private var mProgressDialog: ProgressDialog? = null

    companion object {
        fun newInstance() = MealsDetailsActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mealID = intent.getStringExtra("MealID")!!

        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_meals_details)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel

        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
        viewModel.getMealDetails(mealID)
        viewDataBinding.sendButton.setOnClickListener {
            mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
            val comment = viewDataBinding.commentEt.text.toString()
            val studentID = "5fc22767e4441941bbf5bcff"
            val mealCommentPostModel = MealCommentPostModel(mealID, comment, studentID)
            viewModel.postComment(mealCommentPostModel)
        }
        viewModel.postCommentObserver.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)
            if (it) {
                Toast.makeText(this, "Comment Posted Successfully", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Failed to submit comment", Toast.LENGTH_SHORT).show();
            }
        })
        viewModel.mealDetails.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)
            val adapter = ImagesAdapter(it.images!!)
            viewDataBinding.recyclerView.setSliderAdapter(adapter)
            viewDataBinding.recyclerView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            viewDataBinding.recyclerView.scrollTimeInSec = 4 //set scroll delay in seconds :
            viewDataBinding.recyclerView.startAutoCycle()
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

}