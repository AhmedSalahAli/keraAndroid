package com.example.kera.schoolDetails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.kera.databinding.SchoolDetailsListItemBinding
import com.smarteist.autoimageslider.SliderViewAdapter

class ImagesAdapter(
    var imagesList: ArrayList<String>,
) : SliderViewAdapter<ImagesAdapter.SliderAdapterVH>() {

    override fun onCreateViewHolder(parent: ViewGroup?): ImagesAdapter.SliderAdapterVH {
        return SliderAdapterVH(
            SchoolDetailsListItemBinding.inflate(
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
        return imagesList.size
    }

    inner class SliderAdapterVH(var sliderItemBinding: SchoolDetailsListItemBinding) :
        SliderViewAdapter.ViewHolder(sliderItemBinding.root) {
        fun onBind(position: Int) {
            sliderItemBinding.model = imagesList[position]
        }
    }
}