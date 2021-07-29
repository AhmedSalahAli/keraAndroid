package com.app.kera.schoolsList.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.kera.R
import com.app.kera.databinding.ItemSchoolsListBinding
import com.app.kera.schoolsList.SchoolListUIModel
import com.app.kera.utils.BaseViewHolder

class SchoolListAdapter(
    var schoolsList: List<SchoolListUIModel.SchoolData>,
    var callBack: CallBack,
    private var context: Context? = null


) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemSchoolsListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
        holder.itemView.setOnClickListener { callBack.onItemClicked(schoolsList[position].id) }
    }

    override fun getItemCount(): Int {
        return schoolsList.size
    }

    internal inner class ViewHolder(var item: ItemSchoolsListBinding) :
        BaseViewHolder(item.root) {
        override fun onBind(position: Int) {

//            if (context!=null){
//                item.mainContent.setAnimation(
//                    AnimationUtils.loadAnimation(
//                        context,
//                        R.anim.fade_scale
//                    )
//                )
//            }

            item.model = schoolsList[position]

            if (schoolsList[position].isLiked.get()){
                item.imageViewFav.setImageResource(R.drawable.ic_baseline_favorite_24)
            }else{
                item.imageViewFav.setImageResource(R.drawable.ic_heart)
            }
            item.imageViewFav.setOnClickListener {
                var LikesCounter : Int

                if (schoolsList[position].isLiked.get()){
                    item.imageViewFav.setImageResource(R.drawable.ic_heart)
                    schoolsList[position].isLiked.set(false)
                     LikesCounter = Integer.parseInt(item.textView9.text.toString()) -1
                    item.textView9.text = LikesCounter.toString()
                    callBack.onFavClicked(schoolsList[position].id!!, LikesCounter.toString() )

                }else{
                    item.imageViewFav.setImageResource(R.drawable.ic_baseline_favorite_24)
                    schoolsList[position].isLiked.set(true)
                   var LikesCounter : Int = Integer.parseInt(item.textView9.text.toString()) +1
                    item.textView9.text = LikesCounter.toString()
                    callBack.onFavClicked(schoolsList[position].id!!, LikesCounter.toString() )
                }

            }
            item.tagsRecycler.adapter = TagsAdapter(schoolsList[position].tags)
        }
    }

    interface CallBack {
        fun onItemClicked(
            schoolID: String?
        )

        fun onFavClicked(
            schoolID: String,
            likes: String?
        )
    }
}