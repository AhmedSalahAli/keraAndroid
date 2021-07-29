package com.app.kera.teacherMedicalReport.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.kera.R
import com.app.kera.databinding.ItemTeacherReportClassesBinding
import com.app.kera.teacherMedicalReport.model.TeacherReportClassesUIModel
import com.app.kera.utils.BaseViewHolder

class MedicalReportClassesListAdapter(
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
            callBack.onClassClicked(classesList[position].id)
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
                item.constraintClass.setBackgroundResource(R.drawable.rounded_orange_f2818a)
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
        fun onClassClicked(
            classID: String?
        )
    }
}