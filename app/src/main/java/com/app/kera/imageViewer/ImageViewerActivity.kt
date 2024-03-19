package com.app.kera.imageViewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.app.kera.R
import com.app.kera.databinding.ActivityImageViewerBinding
import com.app.kera.databinding.ActivityMainBinding

class ImageViewerActivity : AppCompatActivity() {

    private var slider: ImageViewerSlider? = null
    lateinit var pager: ViewPager
    val location: ArrayList<String>? = null
    var lastPage = -1
    lateinit var viewDataBinding: ActivityImageViewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_image_viewer)
        viewDataBinding.lifecycleOwner = this

        viewDataBinding.closeImg?.setOnClickListener { v: View? -> super.onBackPressed() }
        val location =
            intent.getSerializableExtra("location") as ArrayList<String>
        val selectedPosition = intent.getIntExtra("Position", 0)
        slider = ImageViewerSlider()


        slider!!.addSlider(
            this,
            window.decorView,
            ImageViewerSlider.getSlider(location)
        )
        pager = viewDataBinding.sliderPager
        lastPage = selectedPosition
        pager.setCurrentItem(selectedPosition, true)
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {


            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {


            }

            override fun onPageSelected(position: Int) {


                // slider?.adapter?.playAt(position)
            }
        })
        //        Glide.with(this).load(getIntent().getStringExtra("location")).into(imageViewer);
    }
}