package com.app.kera.dailyReport.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.app.kera.data.models.DisplayDailyReportResponseModel
import com.app.kera.databinding.ItemDailyReportOuterItemBinding
import com.app.kera.utils.BaseViewHolder
import com.app.kera.utils.CommonUtils


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
                CommonUtils.convertTimeStampToDate_EEE_MMM_MM_yyyyTT(dailyReportList[position].date!!)
            binding.recycler.adapter = DailyReportAdapter(dailyReportList[position].answers)
            binding.textViewReply.setOnClickListener {
                callBack.onReplyClicked(dailyReportList[position].id!!)
            }
            binding.beforeTextViewDate.text =  CommonUtils.convertTimeStampToDate_EEE_MMM_MM_yyyyTT(dailyReportList[position].date!!)
            binding.textViewDate.text =  CommonUtils.convertTimeStampToDate_EEE_MMM_MM_yyyy(dailyReportList[position].date!!)
            binding.textViewTime.text =  CommonUtils.convertTimeStampToDate_TT(dailyReportList[position].date!!)
            for (it in dailyReportList[position].answers!!){
                if (it.answer !=null && !it.answer.isNullOrEmpty()){
                    binding.txtDesc.text = it.answer
                }
            }
            binding.iconExpand.setOnClickListener({
                if (binding.beforeExpand.isVisible) {

                    binding.beforeExpand.visibility = View.GONE
                    binding.afterExpand.visibility = View.VISIBLE

                   // CommonUtilsJava.expand(binding.outerReportItem, binding.afterExpand, binding.beforeExpand)


                } else {
                    binding.beforeExpand.visibility = View.VISIBLE
                    binding.afterExpand.visibility = View.GONE
                }
            })
            binding.iconExpand2.setOnClickListener({
                if (binding.beforeExpand.isVisible) {
                    binding.beforeExpand.visibility = View.GONE
                    binding.afterExpand.visibility = View.VISIBLE
                } else {
                    binding.beforeExpand.visibility = View.VISIBLE
                    binding.afterExpand.visibility = View.GONE

                }
            })
        }
    }

    interface OnReplyClicked {
        fun onReplyClicked(id: String)
    }

}