package com.app.kera.meals.model

import androidx.lifecycle.MutableLiveData
import com.app.kera.data.models.meals.MealDetailsResponseModel
import com.app.kera.utils.CommonUtils

class MealsDetailsUIModel(
    var images: ArrayList<String>?,
    var title: MutableLiveData<String>?,
    var date: MutableLiveData<String>?,
    var name: MutableLiveData<String>?,
    var details: MutableLiveData<String>?,
    var icon: MutableLiveData<String>?,
    var items: MutableLiveData<String>?,
) {
    companion object {
        fun convertResponseModelToUIModel(response: MealDetailsResponseModel.DataBean?): MealsDetailsUIModel {
            return MealsDetailsUIModel(
                response?.images?.map {
                    it
                } as ArrayList,
                MutableLiveData(response.title),
                MutableLiveData(CommonUtils.convertTimeStampToDate(response.date!!.toLong())),
                MutableLiveData(response.mealName),
                MutableLiveData(response.description),
                MutableLiveData(response.smallImage),
                MutableLiveData(null),
            )
        }
    }
}