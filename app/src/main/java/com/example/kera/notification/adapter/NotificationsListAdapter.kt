package com.example.kera.notification.adapter

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.databinding.ItemNotificationBinding
import com.example.kera.notification.model.NotificationItemUIModel
import com.example.kera.utils.BaseViewHolder


class NotificationsListAdapter(
    var notifications: List<NotificationItemUIModel.NotificationModel>,
    var callBack: NotificationsListAdapter.CallBack,var context: Context
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

//        val animation: Animation =
//            AnimationUtils.loadAnimation(context, R.anim.slide_in_left)
//        animation.startOffset = (position * 100).toLong()
//        holder.itemView.startAnimation(animation)
//                    if (context!=null){
//                        holder.itemView.setAnimation(
//                    AnimationUtils.loadAnimation(
//                        context,
//                        R.anim.slide_in_left
//                    )
//                )
//            }
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