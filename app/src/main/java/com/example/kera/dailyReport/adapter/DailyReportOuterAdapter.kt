package com.example.kera.dailyReport.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.data.models.DisplayDailyReportResponseModel
import com.example.kera.databinding.ItemDailyReportOuterItemBinding
import com.example.kera.utils.BaseViewHolder
import com.example.kera.utils.CommonUtils


class DailyReportOuterAdapter(
    var dailyReportList: ArrayList<DisplayDailyReportResponseModel.DataBean.DocsBean>,
    var callBack: OnReplyClicked
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemDailyReportOuterItemBinding.inflate(
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
        return dailyReportList.size
    }

    inner class ViewHolder(var binding: ItemDailyReportOuterItemBinding) :
        BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {
            binding.model = dailyReportList[position]
            dailyReportList[position].dateConverted =
                CommonUtils.convertTimeStampToDate_EEE_MMM_MM_yyyy(dailyReportList[position].date!!)
            binding.recycler.adapter = DailyReportAdapter(dailyReportList[position].answers)
            binding.textViewReply.setOnClickListener {
                callBack.onReplyClicked("123")
            }
        }
    }

    interface OnReplyClicked {
        fun onReplyClicked(id: String)
    }

}