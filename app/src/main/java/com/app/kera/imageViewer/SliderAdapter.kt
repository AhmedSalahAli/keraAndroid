package com.app.kera.imageViewer

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.app.kera.R
import com.app.kera.utils.CommonUtils
import com.app.kera.utils.ZoomableImageView
import com.bumptech.glide.Glide


class SliderAdapter(
    var context: Context,
    var list: List<String>
) : PagerAdapter() {
    override fun getCount(): Int {
        return list.size
    }



    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return list == obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = list[position]

        val inflater = LayoutInflater.from(context)
        val layoutId = R.layout.image_viewer_zoom
        val itemView = inflater.inflate(layoutId, container, false)

        val imageView = itemView.findViewById<ZoomableImageView>(R.id.imageViewer)

        CommonUtils.loadImage(imageView,item)

        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)

    }


}