package com.app.kera.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.app.kera.databinding.HomeDetailsListItemBinding
import com.app.kera.databinding.ItemHomeImagesBinding
import com.app.kera.databinding.SchoolDetailsListItemBinding
import com.app.kera.schoolDetails.adapter.ImagesAdapter
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter

class ImagesListAdapter(
    var imagesList: ArrayList<String>, var context:Context,    var callBack: ImagesListAdapter.CallBack
) : SliderViewAdapter<ImagesListAdapter.SliderAdapterVH>() {

    override fun onCreateViewHolder(parent: ViewGroup?): ImagesListAdapter.SliderAdapterVH {
        return SliderAdapterVH(
            HomeDetailsListItemBinding.inflate(
                LayoutInflater.from(parent?.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        viewHolder.onBind(position)
        viewHolder.itemView.setOnClickListener { callBack.onImageClicked(position, imagesList) }

    }

    override fun getCount(): Int {
        return imagesList.size
    }

    inner class SliderAdapterVH(var sliderItemBinding: HomeDetailsListItemBinding) :
        SliderViewAdapter.ViewHolder(sliderItemBinding.root) {
        fun onBind(position: Int) {
            sliderItemBinding.model = imagesList[position]
            if (!imagesList[position].isEmpty() && imagesList[position] != null) {
                Glide.with(context).load(imagesList[position]).into(sliderItemBinding.imageSlider)
            }


        }
    }

    interface CallBack {
        fun onImageClicked(
            position: Int,
            imagesList: ArrayList<String>
        )
    }
}