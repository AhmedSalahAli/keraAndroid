package com.example.kera.events.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.databinding.ItemEventsBigImageBinding
import com.example.kera.databinding.ItemMealsBinding
import com.example.kera.events.model.UpcomingItemUIModel
import com.example.kera.meals.model.MealsItemUIModel
import com.example.kera.utils.BaseViewHolder
import com.example.kera.utils.CommonUtils

class UpcomingListAdapter(
    var UpcomingEventList: List<UpcomingItemUIModel>, var callBack: CallBack
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemEventsBigImageBinding.inflate(
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

    internal inner class ViewHolder(var requestListItemBinding: ItemEventsBigImageBinding) :
        BaseViewHolder(requestListItemBinding.root) {
        override fun onBind(position: Int) {
            requestListItemBinding.model = UpcomingEventList[position]
            Log.w("Month Day : ",
                   ""+UpcomingEventList[position].eventDate)
            requestListItemBinding.textView60.text = UpcomingEventList[position].eventDate?.let {
                CommonUtils.convertTimeStampToEe(
                    it
                )
            }
            requestListItemBinding.textView61.text = UpcomingEventList[position].eventDate?.let {
                CommonUtils.convertTimeStampToDd(
                    it
                )
            }
//            requestListItemBinding.submitButton.setOnClickListener {
//                callBack.onItemClicked(optionsListItems[position].tripID)
//            }
        }
    }

    interface CallBack {
        fun onItemClicked(
            eventID: String?
        )
    }
}