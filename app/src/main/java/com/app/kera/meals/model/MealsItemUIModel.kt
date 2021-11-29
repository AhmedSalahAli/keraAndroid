package com.app.kera.meals.model

import android.net.Uri
import com.app.kera.R
import com.app.kera.data.models.meals.ClassMealsResponseModel
import com.app.kera.utils.CommonUtils

class MealsItemUIModel(
    var id: String? = "",
    var date: String? = "",
    var title: String?,
    var images: String?,
    var mealName: String? = "",
    var details: String? = "",
) {
    companion object {
        fun convertResponseModelToUIModel(response: List<ClassMealsResponseModel.DataBean.MealsBean>?): ArrayList<MealsItemUIModel> {
           return response?.map {
                MealsItemUIModel(
                    it.id,
                    it.date,
                    it.title,
                    imageChecker(it.images),
                    it.mealName,
                    it.shortDescription
                )
            } as ArrayList
        }

        private fun imageChecker(images: List<String>?): String? {
            return if (images != null) {
                if (images.isNotEmpty()){

                    images[0]

                }else{
                    val uri: Uri = Uri.parse("R.drawable.ic_meals_m")
                    uri.toString()
                }

            }else{
                val uri: Uri = Uri.parse("R.drawable.ic_meals_m")
                uri.toString()
            }
        }


    }
}