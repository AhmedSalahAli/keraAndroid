package com.app.kera.teacherDailyReport.writeReport.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.kera.R
import com.app.kera.data.models.teacherDailyReport.DailyReportResponseModel
import com.app.kera.databinding.ItemMoodBinding
import com.app.kera.databinding.ItemQuestionRadioBinding
import com.app.kera.utils.BaseViewHolder

class MoodListAdapter(
    var moodList: DailyReportResponseModel.DataBean.AnswersBean,
    var callBack: MoodCallBack,
    var questionposition: Int,
    var context: Context,
    var status: Int
) : RecyclerView.Adapter<BaseViewHolder>() {
    private val mood_view_type = 1
    private val second_view_type = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == mood_view_type){
            return ViewHolder(
                ItemMoodBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }else{
            return RadioViewHolder(
                ItemQuestionRadioBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
        //holder.itemView.setOnClickListener { callBack.onItemClicked(moodList.options!![position].id) }
    }

    override fun getItemCount(): Int {
        return moodList.options!!.size
    }
    override fun getItemViewType(position: Int): Int {
        if (moodList.questionType == 2){
            return mood_view_type
        }else{
            return second_view_type
        }

    }
    private var lastCheckedPosition = -1
    internal inner class RadioViewHolder(var item: ItemQuestionRadioBinding) :
        BaseViewHolder(item.root) {
        override fun onBind(position: Int) {
            item.model = moodList.options!![position]

            if (moodList.options!![position].selected){
                lastCheckedPosition = position
            }
            item.checkBox2.setChecked(position == lastCheckedPosition);
//            item.checkBox2.setOnClickListener(View.OnClickListener {
//                lastCheckedPosition = getAdapterPosition();
//                //because of this blinking problem occurs so
//                //i have a suggestion to add notifyDataSetChanged();
//                 notifyItemRangeChanged(0, moodList.options!!.size);//blink list problem
////                notifyDataSetChanged();
//            })
            if (status == 1){
                item.constraintMood.isEnabled = true
                item.constraintMood.isClickable = true
            }else{
                item.constraintMood.isEnabled = false
                item.constraintMood.isClickable = false
            }
            if (position == lastCheckedPosition){

                item.constraintMood.background =
                    context.resources.getDrawable(R.drawable.rounded_white_fcfcfc_stroke_fed93b)
                item.textView25.setTextColor(context.resources.getColor(R.color.blue))
            }else{
                item.constraintMood.background =
                    context.resources.getDrawable(R.drawable.rounded_white_fcfcfc_stroke_d2dce8)
                item.textView25.setTextColor(context.resources.getColor(R.color.gray_D2DCE8))

            }

            item.constraintMood.setOnClickListener {
                lastCheckedPosition = getAdapterPosition();
                //because of this blinking problem occurs so
                //i have a suggestion to add notifyDataSetChanged();
                notifyItemRangeChanged(0, moodList.options!!.size);//blink list problem
//                if (moodList.options!![position].selected) {
//                    moodList.options!![position].selected = false
//                    item.constraintMood.background =
//                        context.resources.getDrawable(R.drawable.rounded_white_fcfcfc_stroke_d2dce8)
//                    //item. checkBox2.isChecked = false
//                    item.textView25.setTextColor(context.resources.getColor(R.color.gray_D2DCE8))
//                } else {
//                    item.constraintMood.background =
//                        context.resources.getDrawable(R.drawable.rounded_white_fcfcfc_stroke_fed93b)
//                    moodList.options!![position].selected = true
//                  //  item. checkBox2.isChecked = true
//                    item.textView25.setTextColor(context.resources.getColor(R.color.blue))
//
//                }

                callBack.onRadioClick(
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
    internal inner class ViewHolder(var item: ItemMoodBinding) :
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

    interface MoodCallBack {
        fun onRadioClick(id: String, value: String, questionID: String, questionposition: Int)
        fun onItemClicked(id: String?)
        fun onMarkClick(id: String, value: String, questionID: String, questionposition: Int)
    }
}