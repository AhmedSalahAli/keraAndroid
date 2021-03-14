package com.example.kera.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.databinding.ItemNotificationBinding
import com.example.kera.notification.NotificationUIModel
import com.example.kera.utils.BaseViewHolder

class NotificationsListAdapter(
    var notification: List<NotificationUIModel>,
) : RecyclerView.Adapter<BaseViewHolder>() {

    lateinit var callBack: CallBack
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
//        holder.itemView.setOnClickListener { callBack.onItemClicked(optionsListItems[position].tripID) }

    }

    override fun getItemCount(): Int {
        return notification.size
    }

    internal inner class ViewHolder(var item: ItemNotificationBinding) :
        BaseViewHolder(item.root) {
        override fun onBind(position: Int) {
            item.model = notification[position]
            if (notification[position].title == "Announcement") {

            } else if (notification[position].title == "Announcement") {
                // notification[position].image =
            }

//            requestListItemBinding.submitButton.setOnClickListener {
//                callBack.onItemClicked(optionsListItems[position].tripID)
//            }
        }
    }

    interface CallBack {
        fun onItemClicked(
            tripID: String?
        )
    }
}