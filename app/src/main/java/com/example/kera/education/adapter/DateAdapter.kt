package com.example.kera.education.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.R
import com.example.kera.databinding.CalendarDateItemBinding
import com.example.kera.meals.model.ClassMealsDates
import com.example.kera.utils.BaseViewHolder

class DateAdapter(
    var datesList: ArrayList<ClassMealsDates>, var itemClickNavigator: ItemClickNavigator
) :
    RecyclerView.Adapter<BaseViewHolder>() {
    var selectedItem = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ScheduleListHolder(
            CalendarDateItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int = datesList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
        holder.itemView.setOnClickListener {
            itemClickNavigator.onDateClick(datesList[position].actualDate)
            notifyItemChanged(selectedItem)
            selectedItem = position
            notifyItemChanged(selectedItem)
        }
    }

    inner class ScheduleListHolder(var binding: CalendarDateItemBinding) :
        BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {
            if (selectedItem == position) {
                binding.viewDateIndicator.setBackgroundResource(R.drawable.rounded_white_10)
            } else {
                binding.viewDateIndicator.setBackgroundResource(R.drawable.rounded_blue_d2dce8_stroke_97b4d8)
            }
            binding.date = datesList[position].displayDate

        }
    }

    interface ItemClickNavigator {
        fun onDateClick(date: String)
    }
}