package com.example.kera.schoolDetails.ui

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.kera.R
import com.example.kera.databinding.SchoolDetailsFragmentBinding
import com.example.kera.registration.screen1.Registration1Activity
import com.example.kera.schoolDetails.adapter.ImagesAdapter
import com.example.kera.schoolDetails.adapter.PhonesAdapter
import com.example.kera.schoolDetails.adapter.TagsAdapter
import com.example.kera.utils.CommonUtils
import com.smarteist.autoimageslider.SliderAnimations
import org.koin.androidx.viewmodel.ext.android.viewModel


class SchoolDetailsActivity : AppCompatActivity(), PhonesAdapter.CallBack {

    val viewModel: SchoolDetailsViewModel by viewModel()
    lateinit var viewDataBinding: SchoolDetailsFragmentBinding
    private var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.school_details_fragment)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel

        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
        viewModel.getSchoolsList(intent.getStringExtra("SchoolID")!!)

        viewDataBinding.tagsAdapter = TagsAdapter(ArrayList())
        viewDataBinding.phonesAdapter = PhonesAdapter(ArrayList(), this)
        viewDataBinding.imageViewBack.setOnClickListener {
            finish()
        }
        viewModel.schoolDetails.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.tagsAdapter!!.tags = it.tags
            viewDataBinding.tagsAdapter!!.notifyDataSetChanged()

            val adapter = ImagesAdapter(it.images)
            viewDataBinding.recyclerView.setSliderAdapter(adapter)
            viewDataBinding.recyclerView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            viewDataBinding.recyclerView.scrollTimeInSec = 4 //set scroll delay in seconds :
            viewDataBinding.recyclerView.startAutoCycle()

            viewDataBinding.phonesAdapter!!.phones = it.phoneNumbers
            viewDataBinding.phonesAdapter!!.notifyDataSetChanged()
        })

        viewDataBinding.textViewSubscribe.setOnClickListener {
            startActivity(Intent(this, Registration1Activity::class.java))
        }
    }

    companion object {
        fun newInstance() = SchoolDetailsActivity()
    }

    override fun onItemClicked(phoneNumber: String?) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }
}