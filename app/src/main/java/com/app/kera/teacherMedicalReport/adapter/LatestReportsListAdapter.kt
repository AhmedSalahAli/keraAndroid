package com.app.kera.teacherMedicalReport.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SimpleOnItemTouchListener
import com.app.kera.R
import com.app.kera.databinding.ItemTeacherMedicalReportReportBinding
import com.app.kera.teacherMedicalReport.model.LatestReportItemUIModel
import com.app.kera.teacherMedicalReport.writeMedicalReport.adapter.WriteReportStudentsAdapter
import com.app.kera.utils.BaseViewHolder



class LatestReportsListAdapter(
    var reportsList: ArrayList<LatestReportItemUIModel.ReportModel>,
    var callBack: CallBack,
    var context:Context
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

    }

    override fun getItemCount(): Int {
        return reportsList.size
    }

    internal inner class ViewHolder(var item: ItemTeacherMedicalReportReportBinding) :
        BaseViewHolder(item.root) {
        override fun onBind(position: Int) {
            item.model = reportsList[position]

            item.itemLatest.setOnClickListener { callBack.onReportItemClicked(reportsList[position].id) }
            item.recyclerView6.isClickable = false
            item.recyclerView6.layoutManager = GridLayoutManager(
                context,
                1,GridLayoutManager.HORIZONTAL, false
            )
            item.studentsAdapter = WriteReportStudentsAdapter(ArrayList())
            item.studentsAdapter!!.studentsList = reportsList[position]!!.students
            item.studentsAdapter!!.notifyDataSetChanged()

            if (reportsList[position].isPublished!!){
               item.txtPublished.setImageResource(R.drawable.ic_baseline_check_circle_24_success)

                item.txtIsPublished.setText(R.string.published)
                item.txtIsPublished.setTextColor(item.root.resources.getColor(R.color.teal_700))
                if (reportsList[position].details.equals("Draft")){
                    item.textView141.setText(R.string.published)
                    item.textView141.setTextColor(item.root.resources.getColor(R.color.teal_700))
                }else{
                    item.textView141.text = reportsList[position].details
                    item.textView141.setTextColor(item.root.resources.getColor(R.color.light_grey_8c99a7))
                }


            }else{
                item.txtPublished.setImageResource(R.drawable.ic_baseline_check_circle_24)

                item.txtIsPublished.setText(R.string.draft)
                item.txtIsPublished.setTextColor(item.root.resources.getColor(R.color.red_no))
                if (reportsList[position].details.equals("Draft")){

                    item.textView141.setText(R.string.draft)
                    item.textView141.setTextColor(item.root.resources.getColor(R.color.red_no))
                }else{
                    item.textView141.text = reportsList[position].details
                    item.textView141.setTextColor(item.root.resources.getColor(R.color.light_grey_8c99a7))
                }


            }
        }
    }
    interface CallBack {
        fun onReportItemClicked(reportID: String?)
    }

}