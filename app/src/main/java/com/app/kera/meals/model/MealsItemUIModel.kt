package com.app.kera.meals.model

import com.app.kera.data.models.meals.ClassMealsResponseModel

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
                    it.images?.get(0),
                    it.mealName,
                    it.shortDescription
                )
            } as ArrayList
        }
    }
}