package com.app.kera.schoolDetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.app.kera.databinding.SchoolDetailsListItemBinding
import com.app.kera.utils.CommonUtils

class ImagesAdapter(
    private val imagesList: ArrayList<String>,
    private val context: Context,
    private val callBack: CallBack
) : RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = SchoolDetailsListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = imagesList.size

    inner class ImageViewHolder(private val binding: SchoolDetailsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val imageUrl = imagesList[position]
            binding.model = imageUrl

            if (imageUrl.isNotEmpty()) {
                CommonUtils.loadImage(binding.imageSlider, imageUrl)
                // Alternative: Glide.with(context).load(imageUrl).into(binding.imageSlider)
            }

            binding.root.setOnClickListener {
                callBack.onImageClicked(position, imagesList)
            }
        }
    }

    interface CallBack {
        fun onImageClicked(position: Int, imagesList: ArrayList<String>)
    }
}
