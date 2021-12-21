package com.app.kera.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.app.kera.databinding.ItemHomeImagesBinding
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlin.coroutines.coroutineContext

class ImagesListAdapter(var images: ArrayList<String?>) :
    SliderViewAdapter<ImagesListAdapter.SliderAdapterVH>() {

    override fun onCreateViewHolder(parent: ViewGroup?): ImagesListAdapter.SliderAdapterVH {
        return SliderAdapterVH(
            ItemHomeImagesBinding.inflate(
                LayoutInflater.from(parent?.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        viewHolder.onBind(position)
    }

    override fun getCount(): Int {
        return images.size
    }

    inner class SliderAdapterVH(var sliderItemBinding: ItemHomeImagesBinding) :
        SliderViewAdapter.ViewHolder(sliderItemBinding.root) {
        fun onBind(position: Int) {
            if (!images[position].isNullOrEmpty()){
                sliderItemBinding.path = images[position]
            }

        }
    }
}