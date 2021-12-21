package com.app.kera.events.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.kera.databinding.ItemEventsSmallImageBinding
import com.app.kera.events.model.UpcomingItemUIModel
import com.app.kera.utils.BaseViewHolder
import com.app.kera.utils.CommonUtils

class PreviousListAdapter(
    var UpcomingEventList: List<UpcomingItemUIModel>, var callBack: CallBack
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemEventsSmallImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
        holder.itemView.setOnClickListener { callBack.onItemClicked(UpcomingEventList[position].id) }

    }

    override fun getItemCount(): Int {
        return UpcomingEventList.size
    }

    internal inner class ViewHolder(var requestListItemBinding: ItemEventsSmallImageBinding) :
        BaseViewHolder(requestListItemBinding.root) {
        override fun onBind(position: Int) {
            requestListItemBinding.model = UpcomingEventList[position]
            if (UpcomingEventList[position].address.isNullOrEmpty()){
                requestListItemBinding.textView66.visibility = View.GONE
            }else{
                requestListItemBinding.textView66.visibility = View.VISIBLE
            }
            requestListItemBinding.textView65.text = UpcomingEventList[position].eventDate?.let {
                CommonUtils.convertIsoToDate(
                    it
                )
            }
        }
    }

    interface CallBack {
        fun onItemClicked(
            eventID: String?
        )
    }
}