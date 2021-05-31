package com.example.kera.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.databinding.ItemNotificationBinding
import com.example.kera.notification.NotificationUIModel
import com.example.kera.notification.model.NotificationItemUIModel
import com.example.kera.schoolsList.adapter.SchoolListAdapter
import com.example.kera.utils.BaseViewHolder

class NotificationsListAdapter(
    var notifications: List<NotificationItemUIModel.NotificationModel>, var callBack: NotificationsListAdapter.CallBack
) : RecyclerView.Adapter<BaseViewHolder>() {


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
        holder.itemView.setOnClickListener { callBack.onItemClicked(notifications[position].notificationType) }

    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    internal inner class ViewHolder(var item: ItemNotificationBinding) :
        BaseViewHolder(item.root) {
        override fun onBind(position: Int) {
            item.model = notifications[position]

        }
    }

    interface CallBack {
        fun onItemClicked(
            notificationType: String?
        )
    }
}