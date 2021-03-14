package com.example.kera.teacherDailyReport.writeReport.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.data.models.teacherDailyReport.DailyReportResponseModel
import com.example.kera.databinding.ItemMoodRecyclerBinding
import com.example.kera.databinding.ItemTeacherCommentBinding
import com.example.kera.utils.BaseViewHolder


class OuterAdapter(
    var dailyReportList: ArrayList<DailyReportResponseModel.DataBean.AnswersBean>,
    var moodCallBack: MoodListAdapter.MoodCallBack,
    var editTextAction: OuterInterface,
    var context: Context
) : RecyclerView.Adapter<BaseViewHolder>() {

    private val mood_view_type = 1
    private val second_view_type = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == mood_view_type) {
            return MoodViewHolder(
                ItemMoodRecyclerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return TeacherCommentViewHolder(
                ItemTeacherCommentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val viewHolder: BaseViewHolder

        if (getItemViewType(position) == mood_view_type) {
            viewHolder = holder as MoodViewHolder

        } else {
            viewHolder = holder as TeacherCommentViewHolder

        }
        viewHolder.onBind(position)
    }

    override fun getItemViewType(position: Int): Int {
        if (dailyReportList[position].options?.isNotEmpty() == true) {
            return mood_view_type
        } else {
            return second_view_type
        }
    }

    override fun getItemCount(): Int {
        return dailyReportList.size
    }

    inner class MoodViewHolder(var binding: ItemMoodRecyclerBinding) :
        BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {
            binding.moodRecycler.adapter = MoodListAdapter(
                dailyReportList[position],
                moodCallBack,
                position,
                context
            )
            binding.textViewTitle.text = dailyReportList[position].question!!.value
        }
    }

    inner class TeacherCommentViewHolder(var binding: ItemTeacherCommentBinding) :
        BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {

            binding.textViewComment.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    editTextAction.onEditTextDoneClicked(
                        binding.textViewComment.text.toString(),
                        dailyReportList[position].question?.id!!
                    )
                    return@OnEditorActionListener true
                }
                false
            })

            binding.textViewTitle.text = dailyReportList[position].question!!.value
            binding.textViewComment.setText(dailyReportList[position].answer.toString())
        }
    }

    interface OuterInterface {
        fun onEditTextDoneClicked(text: String, questionID: String)
    }
}