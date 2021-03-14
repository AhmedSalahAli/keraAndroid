package com.example.kera.teacherDailyReport.writeReport.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.R
import com.example.kera.data.models.teacherDailyReport.DailyReportResponseModel
import com.example.kera.databinding.ItemMoodBinding
import com.example.kera.utils.BaseViewHolder

class MoodListAdapter(
    var moodList: DailyReportResponseModel.DataBean.AnswersBean,
    var callBack: MoodCallBack,
    var questionposition: Int,
    var context: Context
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemMoodBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
        holder.itemView.setOnClickListener { callBack.onItemClicked(moodList.options!![position].id) }
    }

    override fun getItemCount(): Int {
        return moodList.options!!.size
    }

    internal inner class ViewHolder(var item: ItemMoodBinding) :
        BaseViewHolder(item.root) {
        override fun onBind(position: Int) {
            item.model = moodList.options!![position]

            item.checkBox2.setOnClickListener {
                if (moodList.options!![position].selected) {
                    moodList.options!![position].selected = false
                    item.constraintMood.background =
                        context.resources.getDrawable(R.drawable.rounded_white_fcfcfc_stroke_d2dce8)
                } else {
                    item.constraintMood.background =
                        context.resources.getDrawable(R.drawable.rounded_white_fcfcfc_stroke_fed93b)
                    moodList.options!![position].selected = true
                }
                callBack.onMarkClick(
                    moodList.options!![position].id!!,
                    moodList.options!![position].value!!,
                    moodList.question?.id!!,
                    questionposition
                )
            }
//            item.imageViewFav.setOnClickListener {
//                callBack.onFavClicked(schoolsList[position].id!!,schoolsList[position].likes)
//            }
        }
    }

    interface MoodCallBack {
        fun onItemClicked(id: String?)
        fun onMarkClick(id: String, value: String, questionID: String, questionposition: Int)
    }
}