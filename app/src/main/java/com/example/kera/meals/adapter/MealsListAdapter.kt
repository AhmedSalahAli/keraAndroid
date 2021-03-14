package com.example.kera.meals.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.databinding.ItemMealsBinding
import com.example.kera.meals.model.MealsItemUIModel
import com.example.kera.utils.BaseViewHolder

class MealsListAdapter(
    var mealsList: List<MealsItemUIModel>, var callBack: CallBack
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemMealsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
        holder.itemView.setOnClickListener { callBack.onItemClicked(mealsList[position].id) }

    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    internal inner class ViewHolder(var requestListItemBinding: ItemMealsBinding) :
        BaseViewHolder(requestListItemBinding.root) {
        override fun onBind(position: Int) {
            requestListItemBinding.model = mealsList[position]

//            requestListItemBinding.submitButton.setOnClickListener {
//                callBack.onItemClicked(optionsListItems[position].tripID)
//            }
        }
    }

    interface CallBack {
        fun onItemClicked(
            mealID: String?
        )
    }
}