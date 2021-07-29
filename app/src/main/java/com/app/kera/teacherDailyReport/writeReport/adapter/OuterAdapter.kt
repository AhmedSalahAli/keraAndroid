package com.app.kera.teacherDailyReport.writeReport.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.kera.R
import com.app.kera.data.models.teacherDailyReport.DailyReportResponseModel
import com.app.kera.databinding.ItemMoodRecyclerBinding
import com.app.kera.databinding.ItemTeacherCommentBinding
import com.app.kera.utils.BaseViewHolder


class OuterAdapter(
    var dailyReportList: ArrayList<DailyReportResponseModel.DataBean.AnswersBean>,
    var moodCallBack: MoodListAdapter.MoodCallBack,
    var radioCallBack: RadioListAdapter.RadioCallBack,
    var editTextAction: OuterInterface,
    var context: Context,var status :Int
) : RecyclerView.Adapter<BaseViewHolder>() {

    private val mood_view_type = 1
    private val second_view_type = 2
    private val Radio_view_type = 3
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == mood_view_type) {
            return MoodViewHolder(
                ItemMoodRecyclerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }else if   (viewType == Radio_view_type){

            return RadioViewHolder(
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
                context,
                status
            )
            binding.textViewTitle.text = dailyReportList[position].question!!.value
        }
    }
    inner class RadioViewHolder(var binding: ItemMoodRecyclerBinding) :
        BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {
            binding.moodRecycler.adapter = RadioListAdapter(
                dailyReportList[position],
                radioCallBack,
                position,
                context,
                status
            )
            binding.textViewTitle.text = dailyReportList[position].question!!.value
        }
    }
    inner class TeacherCommentViewHolder(var binding: ItemTeacherCommentBinding) :
        BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {

//            binding.textViewComment.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//
//                    editTextAction.onEditTextDoneClicked(
//                        binding.textViewComment.text.toString(),
//                        dailyReportList[position].question?.id!!
//                    )
//                    return@OnEditorActionListener true
//                }
//                false
//            })

            if (status == 1){
                binding.textViewComment.isEnabled = true
                binding.saveComment.isEnabled = true
                binding.saveComment.isClickable = true
            }else{
                binding.textViewComment.isEnabled = false
                binding.saveComment.isEnabled = false
                binding.saveComment.isClickable = false
            }

            binding.textViewComment.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                    // TODO Auto-generated method stub
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                    // TODO Auto-generated method stub
                }

                override fun afterTextChanged(s: Editable) {

                    if (!binding.textViewComment.text.toString().equals(dailyReportList[position].answer.toString())){
                        binding.saveComment.setText("*"+ context.getString(R.string.Save))
                    }

                }
            })
            binding.saveComment.setOnClickListener(View.OnClickListener {
                editTextAction.onEditTextDoneClicked(
                    binding.textViewComment.text.toString(),
                    dailyReportList[position].question?.id!!

                )
                binding.saveComment.setText(context.getString(R.string.Save))
            })

            binding.textViewTitle.text = dailyReportList[position].question!!.value
            binding.textViewComment.setText(dailyReportList[position].answer.toString())
        }
    }

    interface OuterInterface {
        fun onEditTextDoneClicked(text: String, questionID: String)
    }
}