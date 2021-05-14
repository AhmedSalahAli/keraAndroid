package com.example.kera.education.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.databinding.ItemEducationBinding
import com.example.kera.education.model.EducationListItemModel
import com.example.kera.meals.model.MealsItemUIModel
import com.example.kera.schoolDetails.adapter.ImagesAdapter
import com.example.kera.utils.BaseViewHolder
import com.stfalcon.frescoimageviewer.ImageViewer

class EducationListAdapter(
    var list: List<EducationListItemModel>,var callBack: EducationListAdapter.CallBack
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemEducationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    internal inner class ViewHolder(var requestListItemBinding: ItemEducationBinding) :
        BaseViewHolder(requestListItemBinding.root) {
        override fun onBind(position: Int) {
            requestListItemBinding.model = list[position]
            requestListItemBinding.roundCornerLayout6.setOnClickListener { callBack.onImageClicked(0,list[position].images) }
            requestListItemBinding.roundCornerLayout7.setOnClickListener { callBack.onImageClicked(1,list[position].images) }
            requestListItemBinding.roundCornerLayout8.setOnClickListener { callBack.onImageClicked(2,list[position].images) }


        }
    }
    interface CallBack {
        fun onImageClicked(
            position: Int,
            imagesList: ArrayList<String>
        )
    }
}