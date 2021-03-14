package com.example.kera.data.models.meals

import com.google.gson.annotations.SerializedName

class MealDetailsResponseModel {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status = 0

    @SerializedName("data")
    var data: DataBean? = null

    class DataBean {
        @SerializedName("title")
        var title: String? = null

        @SerializedName("description")
        var description: String? = null

        @SerializedName("date")
        var date: String? = null

        @SerializedName("_id")
        var id: String? = null

        @SerializedName("images")
        var images: List<String>? = null

        @SerializedName("smallImage")
        var smallImage: String? = null

        @SerializedName("mealName")
        var mealName: String? = null
    }
}