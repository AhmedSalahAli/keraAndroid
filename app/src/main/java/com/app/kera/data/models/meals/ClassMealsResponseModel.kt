package com.app.kera.data.models.meals

import com.google.gson.annotations.SerializedName

class ClassMealsResponseModel {

    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status = 0

    @SerializedName("data")
    var data: DataBean? = null

    class DataBean {
        @SerializedName("Meals")
        var Meals: List<MealsBean>? = null

        class MealsBean {
            @SerializedName("title")
            var title: String? = null

            @SerializedName("shortDescription")
            var shortDescription: String? = null

            @SerializedName("mealName")
            var mealName: String? = null

            @SerializedName("mainCover")
            var mainCover: String? = null
//
//            @SerializedName("date")
//            var date: String? = null

            @SerializedName("_id")
            var id: String? = null

            @SerializedName("images")
            var images: List<String>? = null
        }
    }
}