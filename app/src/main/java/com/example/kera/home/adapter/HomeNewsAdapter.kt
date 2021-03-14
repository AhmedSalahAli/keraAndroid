package com.example.kera.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.databinding.ItemHomeNewsListBinding
import com.example.kera.home.model.HomeNewsUIModel
import com.example.kera.utils.BaseViewHolder

class HomeNewsAdapter(
    var news: ArrayList<HomeNewsUIModel.NewsList>,
) : RecyclerView.Adapter<BaseViewHolder>() {

    lateinit var callBack: CallBack
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemHomeNewsListBinding.inflate(
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
        return news.size
    }

    internal inner class ViewHolder(var item: ItemHomeNewsListBinding) :
        BaseViewHolder(item.root) {
        override fun onBind(position: Int) {
            item.model = news[position]
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