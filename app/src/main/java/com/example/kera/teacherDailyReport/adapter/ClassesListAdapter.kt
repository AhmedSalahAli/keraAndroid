package com.example.kera.teacherDailyReport.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.R
import com.example.kera.databinding.ItemTeacherReportClassesBinding
import com.example.kera.teacherMedicalReport.model.TeacherReportClassesUIModel
import com.example.kera.utils.BaseViewHolder

class ClassesListAdapter(
    var classesList: ArrayList<TeacherReportClassesUIModel>,
    var callBack: CallBack,
    var context : Context
) : RecyclerView.Adapter<BaseViewHolder>() {
    var selectedItem = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemTeacherReportClassesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
        holder.itemView.setOnClickListener {
            callBack.onItemClicked(classesList[position].id)
            notifyItemChanged(selectedItem)
            selectedItem = position
            notifyItemChanged(selectedItem)
        }
    }

    override fun getItemCount(): Int {
        return classesList.size
    }

    internal inner class ViewHolder(var item: ItemTeacherReportClassesBinding) :
        BaseViewHolder(item.root) {
        override fun onBind(position: Int) {
            if (selectedItem == position){
                item.constraintClass.setBackgroundResource(R.drawable.rounded_30_shadow_classes_yello)
                item.textView119.setTextColor(context.resources.getColor(R.color.dark_blue_152D4A))
            }
            else{
                item.constraintClass.setBackgroundResource(R.drawable.rounded_light_yellow_f4eee7)
                item.textView119.setTextColor(context.resources.getColor(R.color.light_blue))
            }
            item.model = classesList[position]
        }
    }

    interface CallBack {
        fun onItemClicked(
            classID: String?
        )
    }
}