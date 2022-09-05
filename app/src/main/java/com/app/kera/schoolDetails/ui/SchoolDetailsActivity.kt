package com.app.kera.schoolDetails.ui

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.app.kera.R
import com.app.kera.data.models.schoolList.FavouriteSchoolRequestModel
import com.app.kera.databinding.SchoolDetailsFragmentBinding
import com.app.kera.registration.screen1.Registration1Activity
import com.app.kera.schoolDetails.adapter.ImagesAdapter
import com.app.kera.schoolDetails.adapter.PhonesAdapter
import com.app.kera.schoolDetails.adapter.TagsAdapter
import com.stfalcon.frescoimageviewer.ImageViewer
import org.koin.androidx.viewmodel.ext.android.viewModel


class SchoolDetailsActivity : AppCompatActivity(), PhonesAdapter.CallBack , ImagesAdapter.CallBack {

    val viewModel: SchoolDetailsViewModel by viewModel()
    lateinit var viewDataBinding: SchoolDetailsFragmentBinding
    private var mProgressDialog: ProgressDialog? = null
    var schoolID: String? = ""
    var schoolLogo: String? = ""
    var isFav :Boolean = false
    var likers :String = ""
    var lat : Double = 0.0
    var long :Double = 0.0

    @RequiresApi(Build.VERSION_CODES.N)
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

        //mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
        viewDataBinding.veilLayout.layoutDirection = View.LAYOUT_DIRECTION_LTR
        viewDataBinding.veilLayout.veil()

        viewModel.getSchoolsList(intent.getStringExtra("SchoolID")!!)

        viewDataBinding.tagsAdapter = TagsAdapter(ArrayList())
        viewDataBinding.phonesAdapter = PhonesAdapter(ArrayList(), this)
        viewDataBinding.imageViewBack.setOnClickListener {
            finish()
        }


        viewModel.schoolDetails.observe(this) {
            // CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.veilLayout.unVeil()
            viewDataBinding.tagsAdapter!!.tags = it.tags
            viewDataBinding.tagsAdapter!!.notifyDataSetChanged()

            it.images.removeIf(String::isEmpty)
            val adapter = ImagesAdapter(it.images, this, this)
            viewDataBinding.recyclerView.setSliderAdapter(adapter)



            viewDataBinding.phonesAdapter!!.phones = it.phoneNumbers
            viewDataBinding.phonesAdapter!!.notifyDataSetChanged()
            schoolID = it.id
            schoolLogo = it.logo
            isFav = it.isFavourite
            likers = it.numberOfLikes.toString()
            lat = it.lat
            long = it.long
            if (isFav) {
                viewDataBinding.imageView15.setImageResource(R.drawable.ic_baseline_favorite_24)
            } else {
                viewDataBinding.imageView15.setImageResource(R.drawable.ic_heart)

            }

        }

        viewDataBinding.textView18.setOnClickListener(View.OnClickListener {
            if (lat != 0.0 && long != 0.0) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?&daddr=" + lat + "," + long + "")
                )
                startActivity(intent)
            }

        })
       viewDataBinding.imageView15.setOnClickListener(View.OnClickListener {
           if (isFav) {
               viewDataBinding.imageView15.setImageResource(R.drawable.ic_heart)
               isFav = false
               var LikesCounter: Int = Integer.parseInt(likers) - 1
               likers = LikesCounter.toString()
               viewDataBinding.textView20.text = LikesCounter.toString()
               var favSchoolRequestModel = FavouriteSchoolRequestModel(schoolID, LikesCounter)
               viewModel.favouriteSchool(favSchoolRequestModel)

           } else {
               viewDataBinding.imageView15.setImageResource(R.drawable.ic_baseline_favorite_24)
               isFav = true
               var LikesCounter: Int = Integer.parseInt(likers) + 1
               likers = LikesCounter.toString()
               viewDataBinding.textView20.text = LikesCounter.toString()
               var favSchoolRequestModel = FavouriteSchoolRequestModel(schoolID, LikesCounter)
               viewModel.favouriteSchool(favSchoolRequestModel)
           }

       })
        viewDataBinding.textViewSubscribe.setOnClickListener {

            val myIntent = Intent(this, Registration1Activity::class.java)
            myIntent.putExtra("SchoolID", schoolID)
            myIntent.putExtra("SchoolLogo", schoolLogo) //Optional parameters
            startActivity(myIntent)
            Log.e("ID", "HI : " + schoolLogo)
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

    override fun onImageClicked(position: Int, imagesList: ArrayList<String>) {
        ImageViewer.Builder<String>(this, imagesList)
            .setStartPosition(position)
            .hideStatusBar(false)
            .allowZooming(true)
            .allowSwipeToDismiss(true)
            .show()
//        val share = Intent(Intent.ACTION_SEND)
//        share.type = "image/jpeg"
//
//        share.putExtra(
//            Intent.EXTRA_STREAM,
//            Uri.parse("file:///myPic.jpg")
//        )
//
//        startActivity(Intent.createChooser(share, "Share Image"))
    }
}