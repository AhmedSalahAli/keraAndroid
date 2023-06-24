package com.app.kera.education.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.app.kera.databinding.ItemEducationBinding
import com.app.kera.education.model.EducationListItemModel
import com.app.kera.utils.BaseViewHolder
import com.app.kera.utils.CommonUtils

class EducationListAdapter(
    var list: List<EducationListItemModel>,var callBack: EducationListAdapter.CallBack,var context: Context
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
            Log.i("Image Count",""+list[position].images.size)

            if (list[position].images.size > 0) {
                if ( list[position].images[0] != null){

                    CommonUtils.loadImage(requestListItemBinding.imgEdu1,list[position].images[0])
                }

            }
            if (list[position].images.size > 1 ) {
                if ( list[position].images[1] != null){

                    CommonUtils.loadImage(requestListItemBinding.imgEdu2,list[position].images[1])
                }

            }
            if (list[position].images.size > 2 ) {
                if ( list[position].images[2] != null){

                    CommonUtils.loadImage(requestListItemBinding.imgEdu3,list[position].images[2])
                }

            }

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