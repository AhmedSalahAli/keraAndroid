package com.example.kera.events.model

import com.example.kera.data.models.meals.ClassMealsResponseModel
import com.example.kera.utils.CommonUtils
import com.google.gson.annotations.SerializedName

class ClassUpcomingResponseModel {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status = 0
    @SerializedName("data")
    var data: DataBean? = null

    class DataBean {
        @SerializedName("total")
        var total = 0

        @SerializedName("limit")
        var limit = 0

        @SerializedName("page")
        var page: String? = null

        @SerializedName("pages")
        var pages = 0

        @SerializedName("docs")
        var docs: ArrayList<DocsBean>? = null

        class DocsBean {
            @SerializedName("title")
            var title: String? = ""
            @SerializedName("shortDescription")
            var shortDescription: String? = ""
            @SerializedName("mainCover")
            var mainCover: String? = ""
            @SerializedName("latitude")
            var latitude: Double? = 0.0
            @SerializedName("longitude")
            var longitude: Double? = 0.0
            @SerializedName("address")
            var address: String? = ""
            @SerializedName("eventDate")
            var eventDate: String? = ""
            @SerializedName("_id")
            var _id: String? = ""

        }
    }
}