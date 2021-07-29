package com.app.kera.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.kera.data.network.AppRepo
import com.app.kera.databinding.ItemStudentBinding
import com.app.kera.profile.StudentsData
import com.app.kera.utils.BaseViewHolder

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