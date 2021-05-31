package com.example.kera.events.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.databinding.ItemEventsBigImageBinding
import com.example.kera.databinding.ItemEventsSmallImageBinding
import com.example.kera.databinding.ItemMealsBinding
import com.example.kera.events.model.UpcomingItemUIModel
import com.example.kera.meals.model.MealsItemUIModel
import com.example.kera.utils.BaseViewHolder
import com.example.kera.utils.CommonUtils

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