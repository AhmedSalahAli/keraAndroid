package com.example.kera.events.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kera.R
import com.example.kera.data.network.AppRepo
import com.example.kera.databinding.ItemChildrenEventBinding
import com.example.kera.databinding.ItemStudentBinding
import com.example.kera.events.model.EventDetailsResponseModel
import com.example.kera.profile.StudentsData
import com.example.kera.utils.BaseViewHolder

class ChildrenAdapter(
    var children: ArrayList<EventDetailsResponseModel.DataBean.StudentData>,
    var callBack: CallBack,var context: Context
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemChildrenEventBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
        holder.itemView.setOnClickListener {
            callBack.onItemClicked(children[position])

        }
    }

    override fun getItemCount(): Int {
        return children.size
    }

    internal inner class ViewHolder(var item: ItemChildrenEventBinding) :
        BaseViewHolder(item.root) {
        override fun onBind(position: Int) {
            item.model = children[position]
            if (children[position].accept!!){
                Glide.with(context).load(context.resources.getDrawable(R.drawable.ic_student_cheked)).into(item.imgAccept)
            }else{
                Glide.with(context).load(context.resources.getDrawable(R.drawable.ic_student_uncheked)).into(item.imgAccept)
            }


        }
    }

    interface CallBack {
        fun onItemClicked(
            student: EventDetailsResponseModel.DataBean.StudentData,
        )
    }
}