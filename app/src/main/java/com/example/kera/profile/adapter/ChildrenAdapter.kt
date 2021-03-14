package com.example.kera.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.databinding.ItemStudentBinding
import com.example.kera.profile.StudentsData
import com.example.kera.utils.BaseViewHolder

class ChildrenAdapter(
    var children: ArrayList<StudentsData>,
    var callBack: CallBack
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemStudentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
        holder.itemView.setOnClickListener { callBack.onItemClicked(children[position]) }
    }

    override fun getItemCount(): Int {
        return children.size
    }

    internal inner class ViewHolder(var item: ItemStudentBinding) :
        BaseViewHolder(item.root) {
        override fun onBind(position: Int) {
            item.model = children[position]
        }
    }

    interface CallBack {
        fun onItemClicked(
            student: StudentsData,
        )
    }
}