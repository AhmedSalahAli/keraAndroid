package com.app.kera.teacherMedicalReport.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.kera.R
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
            if (reportsList[position].isPublished!!){
               item.txtPublished.setImageResource(R.drawable.ic_baseline_check_circle_24_success)
                item.textView141.setText(R.string.published)
                item.txtIsPublished.setText(R.string.published)
                item.txtIsPublished.setTextColor(item.root.resources.getColor(R.color.teal_700))
                item.textView141.setTextColor(item.root.resources.getColor(R.color.teal_700))
            }else{
                item.txtPublished.setImageResource(R.drawable.ic_baseline_check_circle_24)

                item.txtIsPublished.setText(R.string.draft)
                item.txtIsPublished.setTextColor(item.root.resources.getColor(R.color.red_no))
                item.textView141.text = reportsList[position].details

                item.textView141.setTextColor(item.root.resources.getColor(R.color.light_grey_8c99a7))
            }
        }
    }
    interface CallBack {
        fun onReportItemClicked(reportID: String?)
    }

}