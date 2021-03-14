package com.example.kera.dailyReport.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.data.models.DisplayDailyReportResponseModel
import com.example.kera.databinding.ItemActivityBinding
import com.example.kera.utils.BaseViewHolder

class ActivityAdapter(
    var moodList: DisplayDailyReportResponseModel.DataBean.DocsBean.AnswersBean,
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemActivityBinding.inflate(
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
        return moodList.options!!.size
    }

    internal inner class ViewHolder(var item: ItemActivityBinding) :
        BaseViewHolder(item.root) {
        override fun onBind(position: Int) {
            item.model = moodList.options!![position]
        }
    }
}