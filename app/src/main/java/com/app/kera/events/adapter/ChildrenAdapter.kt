package com.app.kera.events.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.app.kera.R
import com.app.kera.databinding.ItemChildrenEventBinding
import com.app.kera.events.model.EventDetailsResponseModel
import com.app.kera.utils.BaseViewHolder

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