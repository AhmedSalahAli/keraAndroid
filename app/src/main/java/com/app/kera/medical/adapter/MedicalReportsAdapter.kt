package com.app.kera.medical.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.app.kera.R

import com.app.kera.databinding.ItemMedicalReportBinding
import com.app.kera.medical.model.DisplayMedicalReportResponseModel
import com.app.kera.utils.BaseViewHolder
import com.app.kera.utils.CommonUtils

class MedicalReportsAdapter(
    var medicalReportList: ArrayList<DisplayMedicalReportResponseModel.DataBean.DocsBean>,
    var callBack: CallBack,
    var context: Context
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemMedicalReportBinding.inflate(
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
        return medicalReportList.size
    }

    inner class ViewHolder(var binding: ItemMedicalReportBinding) :
        BaseViewHolder(binding.root) {

        override fun onBind(position: Int) {
            binding.model = medicalReportList[position]
            binding.beforeTextViewDate.text =  CommonUtils.convertTimeStampToDate_EEE_MMM_yyyy(medicalReportList[position].date!!)
            binding.textView116.text =  CommonUtils.convertTimeStampToDate_EEE_MMM_yyyy(medicalReportList[position].date!!)

            if (medicalReportList[position].question1!!.isYes){
                binding.textView114.text = "YES"
            }else{
                binding.textView114.text = "NO"


            }

            binding.roundCornerLayout6.setOnClickListener { callBack.onImageClicked(0,
                medicalReportList[position].images!!
            ) }
            binding.roundCornerLayout7.setOnClickListener { callBack.onImageClicked(1,
                medicalReportList[position].images!!
            ) }
            binding.roundCornerLayout8.setOnClickListener { callBack.onImageClicked(2,
                medicalReportList[position].images!!
            ) }

            binding.iconExpand.setOnClickListener {
                if (binding.beforeExpand.isVisible) {

                    binding.beforeExpand.animation = AnimationUtils.loadAnimation(
                        context,
                        R.anim.fade_out
                    )
                    binding.afterExpand.animation = AnimationUtils.loadAnimation(
                        context,
                        R.anim.fade_in
                    )
                    binding.beforeExpand.visibility = View.GONE
                    binding.afterExpand.visibility = View.VISIBLE

                    // CommonUtilsJava.expand(binding.outerReportItem, binding.afterExpand, binding.beforeExpand)


                } else {
                    binding.beforeExpand.visibility = View.VISIBLE
                    binding.afterExpand.visibility = View.GONE
                }
            }
            binding.iconColaps.setOnClickListener {
                if (binding.beforeExpand.isVisible) {

                    binding.beforeExpand.visibility = View.GONE
                    binding.afterExpand.visibility = View.VISIBLE
                } else {
                    binding.beforeExpand.animation = AnimationUtils.loadAnimation(
                        context,
                        R.anim.fade_in
                    )
                    binding.afterExpand.animation = AnimationUtils.loadAnimation(
                        context,
                        R.anim.fade_out
                    )
                    binding.beforeExpand.visibility = View.VISIBLE
                    binding.afterExpand.visibility = View.GONE

                }
            }
        }
    }

    interface CallBack {
        fun onImageClicked(
            position: Int,
            imagesList: ArrayList<String>
        )
    }
}