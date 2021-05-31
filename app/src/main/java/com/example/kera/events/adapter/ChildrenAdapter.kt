package com.example.kera.events.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.data.network.AppRepo
import com.example.kera.databinding.ItemStudentBinding
import com.example.kera.profile.StudentsData
import com.example.kera.utils.BaseViewHolder

class ChildrenAdapter(
    var children: ArrayList<StudentsData>,
    var callBack: CallBack,var appRepo: AppRepo
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
            if (children[position].studentId.equals(appRepo.getSelectedChildData()?.studentId)){
                item.circleImageView.borderWidth  = 4

            }else{
                item.circleImageView.borderWidth  = 0
            }

        }
    }

    interface CallBack {
        fun onItemClicked(
            student: StudentsData,
        )
    }
}