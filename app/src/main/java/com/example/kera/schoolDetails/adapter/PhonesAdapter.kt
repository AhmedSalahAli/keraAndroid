package com.example.kera.schoolDetails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.databinding.ItemPhonesBinding
import com.example.kera.utils.BaseViewHolder

class PhonesAdapter(
    var phones: ArrayList<String>?,
    var callBack: CallBack
) :
    RecyclerView.Adapter<BaseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ScheduleListHolder(
            ItemPhonesBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int = phones!!.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
        holder.itemView.setOnClickListener { callBack.onItemClicked(phones?.get(position)) }
    }

    inner class ScheduleListHolder(var binding: ItemPhonesBinding) :
        BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {
            binding.phone = phones!![position]
        }
    }

    interface CallBack {
        fun onItemClicked(
            phoneNumber: String?
        )
    }
}