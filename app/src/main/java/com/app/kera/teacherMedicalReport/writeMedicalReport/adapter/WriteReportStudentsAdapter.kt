package com.app.kera.teacherMedicalReport.writeMedicalReport.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.kera.data.models.teacherDailyReport.DailyReportResponseModel
import com.app.kera.databinding.ItemStudentNameImageSmallBinding
import com.app.kera.utils.BaseViewHolder

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