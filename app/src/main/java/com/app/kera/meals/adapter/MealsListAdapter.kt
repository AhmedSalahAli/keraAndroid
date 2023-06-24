package com.app.kera.meals.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.kera.databinding.ItemMealsBinding
import com.app.kera.meals.model.MealsItemUIModel
import com.app.kera.utils.BaseViewHolder

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


    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    internal inner class ViewHolder(var requestListItemBinding: ItemMealsBinding) :
        BaseViewHolder(requestListItemBinding.root) {
        override fun onBind(position: Int) {
            requestListItemBinding.model = mealsList[position]
            requestListItemBinding.view.setOnClickListener { callBack.onItemClicked(mealsList[position]) }
//            requestListItemBinding.submitButton.setOnClickListener {
//                callBack.onItemClicked(optionsListItems[position].tripID)
//            }
        }
    }

    interface CallBack {
        fun onItemClicked(
            meal: MealsItemUIModel
        )
    }
}