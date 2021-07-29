package com.app.kera.teacherDailyReport.writeReport.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.kera.R
import com.app.kera.data.models.teacherDailyReport.DailyReportResponseModel
import com.app.kera.databinding.ItemMoodBinding
import com.app.kera.databinding.ItemQuestionRadioBinding
import com.app.kera.utils.BaseViewHolder

class RadioListAdapter(
    var moodList: DailyReportResponseModel.DataBean.AnswersBean,
    var callBack: RadioCallBack,
    var questionposition: Int,
    var context: Context,
    var status :Int
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemQuestionRadioBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
        //holder.itemView.setOnClickListener { callBack.onItemClicked(moodList.options!![position].id) }
    }

    override fun getItemCount(): Int {
        return moodList.options!!.size
    }

    internal inner class ViewHolder(var item: ItemQuestionRadioBinding) :
        BaseViewHolder(item.root) {
        override fun onBind(position: Int) {
            item.model = moodList.options!![position]

              if (status == 1){
                  item.constraintMood.isEnabled = true
                  item.constraintMood.isClickable = true
            }else{
                  item.constraintMood.isEnabled = false
                  item.constraintMood.isClickable = false
            }
            if (moodList.options!![position].selected){

                item.constraintMood.background =
                    context.resources.getDrawable(R.drawable.rounded_white_fcfcfc_stroke_fed93b)
                item.textView25.setTextColor(context.resources.getColor(R.color.blue))
            }else{
                item.constraintMood.background =
                    context.resources.getDrawable(R.drawable.rounded_white_fcfcfc_stroke_d2dce8)
                item.textView25.setTextColor(context.resources.getColor(R.color.gray_D2DCE8))

            }

            item.constraintMood.setOnClickListener {
                if (moodList.options!![position].selected) {
                    moodList.options!![position].selected = false
                    item.constraintMood.background =
                        context.resources.getDrawable(R.drawable.rounded_white_fcfcfc_stroke_d2dce8)
                    item. checkBox2.isChecked = false
                    item.textView25.setTextColor(context.resources.getColor(R.color.gray_D2DCE8))
                } else {
                    item.constraintMood.background =
                        context.resources.getDrawable(R.drawable.rounded_white_fcfcfc_stroke_fed93b)
                    moodList.options!![position].selected = true
                    item. checkBox2.isChecked = true
                    item.textView25.setTextColor(context.resources.getColor(R.color.blue))

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

    interface RadioCallBack {
        fun onItemClicked(id: String?)
        fun onMarkClick(id: String, value: String, questionID: String, questionposition: Int)
    }
}