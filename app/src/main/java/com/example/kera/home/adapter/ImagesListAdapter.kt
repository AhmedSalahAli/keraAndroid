package com.example.kera.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.kera.databinding.ItemHomeImagesBinding
import com.smarteist.autoimageslider.SliderViewAdapter

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
            sliderItemBinding.path = images[position]
        }
    }
}