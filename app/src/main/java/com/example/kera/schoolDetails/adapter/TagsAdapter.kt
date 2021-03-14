package com.example.kera.schoolDetails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.databinding.TagListItemBinding
import com.example.kera.schoolDetails.model.SchoolDetailsUIModel
import com.example.kera.utils.BaseViewHolder

class TagsAdapter(
    var tags: ArrayList<String>?,
) :
    RecyclerView.Adapter<BaseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ScheduleListHolder(
            TagListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int = tags!!.size
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    inner class ScheduleListHolder(var binding: TagListItemBinding) :
        BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {
            binding.model = tags!![position]
        }
    }
}