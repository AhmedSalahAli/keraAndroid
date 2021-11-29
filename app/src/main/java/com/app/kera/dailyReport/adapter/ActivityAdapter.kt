package com.app.kera.dailyReport.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.kera.data.models.DisplayDailyReportResponseModel
import com.app.kera.databinding.ItemActivityBinding
import com.app.kera.utils.BaseViewHolder

class ActivityAdapter(
    var moodList: List<DisplayDailyReportResponseModel.DataBean.DocsBean.AnswersBean.OptionsBean>?,
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
        return moodList?.size!!
    }

    internal inner class ViewHolder(var item: ItemActivityBinding) :
        BaseViewHolder(item.root) {
        override fun onBind(position: Int) {

                item.model = moodList!![position]




        }
    }
}