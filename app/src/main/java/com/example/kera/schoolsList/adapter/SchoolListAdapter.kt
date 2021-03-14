package com.example.kera.schoolsList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.databinding.ItemSchoolsListBinding
import com.example.kera.schoolsList.SchoolListUIModel
import com.example.kera.utils.BaseViewHolder

class SchoolListAdapter(
    var schoolsList: List<SchoolListUIModel.SchoolData>,
    var callBack: CallBack
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemSchoolsListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
        holder.itemView.setOnClickListener { callBack.onItemClicked(schoolsList[position].id) }
    }

    override fun getItemCount(): Int {
        return schoolsList.size
    }

    internal inner class ViewHolder(var item: ItemSchoolsListBinding) :
        BaseViewHolder(item.root) {
        override fun onBind(position: Int) {
            item.model = schoolsList[position]
            item.imageViewFav.setOnClickListener {
                callBack.onFavClicked(schoolsList[position].id!!, schoolsList[position].likes)
            }
            item.tagsRecycler.adapter = TagsAdapter(schoolsList[position].tags)
        }
    }

    interface CallBack {
        fun onItemClicked(
            schoolID: String?
        )

        fun onFavClicked(
            schoolID: String,
            likes: String?
        )
    }
}