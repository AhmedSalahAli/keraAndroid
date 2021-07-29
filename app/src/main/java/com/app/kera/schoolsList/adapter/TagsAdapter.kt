package com.app.kera.schoolsList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.kera.databinding.ItemTagsBinding
import com.app.kera.schoolsList.SchoolListUIModel
import com.app.kera.utils.BaseViewHolder

class TagsAdapter(
    var tags: List<SchoolListUIModel.Tags>
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemTagsBinding.inflate(
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
        return tags.size
    }

    internal inner class ViewHolder(var item: ItemTagsBinding) :
        BaseViewHolder(item.root) {
        override fun onBind(position: Int) {
            item.item = tags[position]

        }
    }
}