package com.app.kera.attendanceHistory.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.kera.attendanceHistory.model.AttendanceListItemModel
import com.app.kera.databinding.AttendanceItemBinding
import com.app.kera.utils.BaseViewHolder


class AttendanceListAdapter(
    var list: List<AttendanceListItemModel>,var callBack: AttendanceListAdapter.CallBack
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            AttendanceItemBinding.inflate(
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
        return list.size
    }

    internal inner class ViewHolder(var requestListItemBinding: AttendanceItemBinding) :
        BaseViewHolder(requestListItemBinding.root) {
        override fun onBind(position: Int) {
            requestListItemBinding.model = list[position]
            if (list[position].arrivalTime.isNullOrEmpty()){
                requestListItemBinding.constraintLayout39.visibility = View.GONE
            }else if (list[position].departureTime.isNullOrEmpty()){
                requestListItemBinding.constraintLayout41.visibility = View.GONE
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