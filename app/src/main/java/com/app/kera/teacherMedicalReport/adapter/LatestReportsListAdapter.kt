package com.app.kera.teacherMedicalReport.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.kera.databinding.ItemTeacherMedicalReportReportBinding
import com.app.kera.teacherMedicalReport.model.LatestReportItemUIModel
import com.app.kera.utils.BaseViewHolder

class LatestReportsListAdapter(
    var reportsList: ArrayList<LatestReportItemUIModel.ReportModel>,
    var callBack: CallBack
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemTeacherMedicalReportReportBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
        holder.itemView.setOnClickListener { callBack.onReportItemClicked(reportsList[position].id) }
    }

    override fun getItemCount(): Int {
        return reportsList.size
    }

    internal inner class ViewHolder(var item: ItemTeacherMedicalReportReportBinding) :
        BaseViewHolder(item.root) {
        override fun onBind(position: Int) {
            item.model = reportsList[position]
        }
    }
    interface CallBack {
        fun onReportItemClicked(reportID: String?)
    }

}