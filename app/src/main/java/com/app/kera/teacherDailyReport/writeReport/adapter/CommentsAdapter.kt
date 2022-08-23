package com.app.kera.teacherDailyReport.writeReport.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.kera.data.models.DisplayDailyReportResponseModel
import com.app.kera.data.models.teacherDailyReport.DailyReportResponseModel
import com.app.kera.databinding.ItemActivityBinding
import com.app.kera.databinding.ItemCommentBinding
import com.app.kera.databinding.ItemDailyReportTeacherCommentBinding
import com.app.kera.utils.BaseViewHolder

class CommentsAdapter(
    var commentList: List<DailyReportResponseModel.DataBean.Replies>?,
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemCommentBinding.inflate(
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
        return commentList?.size!!
    }

    internal inner class ViewHolder(var item: ItemCommentBinding) :
        BaseViewHolder(item.root) {
        override fun onBind(position: Int) {

                item.model = commentList!![position]




        }
    }
}