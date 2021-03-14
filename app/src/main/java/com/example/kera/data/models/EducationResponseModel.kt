package com.example.kera.data.models

import com.google.gson.annotations.SerializedName

class EducationResponseModel {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status = 0

    @SerializedName("data")
    var data: DataBean? = null

    class DataBean {
        @SerializedName("educations")
        var educations: List<EducationsBean>? = null

        class EducationsBean {
            @SerializedName("title")
            var title: String? = null

            @SerializedName("shortDescription")
            var shortDescription: String? = null

            @SerializedName("date")
            var date: String? = null

            @SerializedName("_id")
            var id: String? = null

            @SerializedName("images")
            var images: List<String>? = null
        }
    }
}