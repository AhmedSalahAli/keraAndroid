package com.example.kera.dailyReport.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.data.models.DisplayDailyReportResponseModel
import com.example.kera.databinding.ItemDailyReportActivitiesBinding
import com.example.kera.databinding.ItemDailyReportTeacherCommentBinding
import com.example.kera.utils.BaseViewHolder


class DailyReportAdapter(
    var dailyReportList: ArrayList<DisplayDailyReportResponseModel.DataBean.DocsBean.AnswersBean>?,
) : RecyclerView.Adapter<BaseViewHolder>() {

    private val type_1 = 1
    private val commentType = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == type_1) {
            return ActivityViewHolder(
                ItemDailyReportActivitiesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return TeacherCommentViewHolder(
                ItemDailyReportTeacherCommentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val viewHolder: BaseViewHolder

        if (getItemViewType(position) == type_1) {
            viewHolder = holder as ActivityViewHolder

        } else {
            viewHolder = holder as TeacherCommentViewHolder

        }
        viewHolder.onBind(position)
    }

    override fun getItemViewType(position: Int): Int {
        if (dailyReportList?.get(position)?.options?.isNotEmpty() == true) {
            return type_1
        } else {
            return commentType
        }
    }

    override fun getItemCount(): Int {
        return dailyReportList?.size!!
    }

    inner class ActivityViewHolder(var binding: ItemDailyReportActivitiesBinding) :
        BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {
            binding.recyclerViewActivities.adapter = ActivityAdapter(
                dailyReportList?.get(position)!!,
            )
            binding.textViewTitle.text = dailyReportList!![position].question!!.value
        }
    }

    inner class TeacherCommentViewHolder(var binding: ItemDailyReportTeacherCommentBinding) :
        BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {
            binding.textViewTitle.text = dailyReportList?.get(position)?.question!!.value
            binding.textViewComment.setText(dailyReportList?.get(position)?.answer.toString())
        }
    }
}