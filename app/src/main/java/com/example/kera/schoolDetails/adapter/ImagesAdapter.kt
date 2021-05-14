package com.example.kera.schoolDetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.kera.databinding.SchoolDetailsListItemBinding
import com.smarteist.autoimageslider.SliderViewAdapter

class ImagesAdapter(
    var imagesList: ArrayList<String>, var context:Context,    var callBack: ImagesAdapter.CallBack
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
        viewHolder.itemView.setOnClickListener { callBack.onImageClicked(position,imagesList) }

    }

    override fun getCount(): Int {
        return imagesList.size
    }

    inner class SliderAdapterVH(var sliderItemBinding: SchoolDetailsListItemBinding) :
        SliderViewAdapter.ViewHolder(sliderItemBinding.root) {
        fun onBind(position: Int) {
            sliderItemBinding.model = imagesList[position]
            if (!imagesList[position].isEmpty()&&imagesList[position]!=null){
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