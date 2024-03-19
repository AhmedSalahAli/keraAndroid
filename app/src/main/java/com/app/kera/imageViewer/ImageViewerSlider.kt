package com.app.kera.imageViewer

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.app.kera.R

class ImageViewerSlider {
    var adapter:SliderAdapter? =null
    fun addSlider(
        context: Context,
        view: View,
        mList: List<String>
    ) {
        val pager: ViewPager = view.findViewById(R.id.sliderPager)
        val layout = view.findViewById<LinearLayout>(R.id.layoutDots)




        adapter =  SliderAdapter(context, getList(mList))
        pager.adapter = adapter
        val dotsImages =
            arrayOfNulls<ImageView>(mList.size)
        val params = LinearLayout.LayoutParams(30, 30)
        params.setMargins(8, 8, 8, 8)
        for (i in mList.indices) {
            dotsImages[i] = ImageView(context)
            dotsImages[i]!!.setImageResource(R.drawable.dots_white)
            dotsImages[i]!!.layoutParams = params
            layout.addView(dotsImages[i])
        }
        dotsImages[0]!!.setImageResource(R.drawable.dots_gold)
        pager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                for (i in mList.indices) {
                    dotsImages[i]!!.setImageResource(R.drawable.dots_white)
                }
                dotsImages[position]!!.setImageResource(R.drawable.dots_gold)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

    }

    private fun getList(mList: List<String>): List<String> {
        val list:ArrayList<String> = ArrayList()
        for (t in mList){
            list.add(t)
        }
        return list
    }

    companion object {
        fun getSlider(mainImageResponses: List<String>): List<String> {
            return mainImageResponses
        }

        fun refreshUi(activity: Activity) {
            activity.finish()
            activity.overridePendingTransition(0, 0)
            activity.startActivity(activity.intent)
            activity.overridePendingTransition(0, 0)
        }
    }
}