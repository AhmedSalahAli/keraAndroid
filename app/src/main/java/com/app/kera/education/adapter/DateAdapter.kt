package com.app.kera.education.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.kera.R
import com.app.kera.databinding.CalendarDateItemBinding
import com.app.kera.meals.model.ClassMealsDates
import com.app.kera.utils.BaseViewHolder

class DateAdapter(
    var datesList: ArrayList<ClassMealsDates>, var itemClickNavigator: ItemClickNavigator,
    var context: Context
) :
    RecyclerView.Adapter<BaseViewHolder>() {
    var selectedItem = 0
    var isDateAvailable =false
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
            itemClickNavigator.onDateClick(selectedItem,datesList[position].actualDate)
            notifyItemChanged(selectedItem)
            selectedItem = position
            notifyItemChanged(selectedItem)
        }

    }

    inner class ScheduleListHolder(var binding: CalendarDateItemBinding) :
        BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {
            binding.viewModel = datesList[position]




            if (selectedItem == position) {
                binding.viewDateIndicator.setBackgroundResource(R.drawable.rounded_white_10)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    binding.tvDatePickerDayWeek.typeface = context.resources.getFont(R.font.sf_ui_display_black)
                }
            } else {
                binding.viewDateIndicator.setBackgroundResource(R.drawable.rounded_blue_d2dce8_stroke_97b4d8)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    binding.tvDatePickerDayWeek.typeface = context.resources.getFont(R.font.sf_ui_display_regular)
                }
            }


        }
    }

    interface ItemClickNavigator {
        fun onDateClick(position :Int ,date: String)
    }
}