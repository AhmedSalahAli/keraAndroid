package com.app.kera.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.kera.databinding.HomeDetailsListItemBinding
import com.app.kera.utils.CommonUtils

class ImagesListAdapter(
private val images: List<String>,
private val context: Context,
private val callBack: CallBack
) : RecyclerView.Adapter<ImagesListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: HomeDetailsListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String) {
            CommonUtils.loadImage(binding.imageSlider, imageUrl)
            binding.imageSlider.setOnClickListener {
                callBack.onImageClicked(adapterPosition, ArrayList(images))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeDetailsListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    interface CallBack {
        fun onImageClicked(position: Int, imagesList: ArrayList<String>)
    }
}

