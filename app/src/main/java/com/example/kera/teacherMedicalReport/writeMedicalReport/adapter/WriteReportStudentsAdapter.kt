package com.example.kera.teacherMedicalReport.writeMedicalReport.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.data.models.teacherDailyReport.DailyReportResponseModel
import com.example.kera.databinding.ItemStudentNameImageSmallBinding
import com.example.kera.utils.BaseViewHolder

class WriteReportStudentsAdapter(
    var studentsList: ArrayList<DailyReportResponseModel.DataBean.StudentsBean>
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemStudentNameImageSmallBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return studentsList.size
    }

    internal inner class ViewHolder(var item: ItemStudentNameImageSmallBinding) :
        BaseViewHolder(item.root) {
        override fun onBind(position: Int) {
            item.model = studentsList[position]
        }
    }
}