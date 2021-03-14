package com.example.kera.data.models

import com.google.gson.annotations.SerializedName

class MapListResponseModel {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status = 0

    @SerializedName("data")
    var data: List<DataBean>? = null

    class DataBean {
        @SerializedName("username")
        var username: String? = null

        @SerializedName("latitude")
        var latitude = 0.0

        @SerializedName("longitude")
        var longitude = 0.0

        @SerializedName("address")
        var address: String? = null

        @SerializedName("logo")
        var logo: String? = null

        @SerializedName("image")
        var image: String? = null

        @SerializedName("type")
        var type: TypeBean? = null

        @SerializedName("favorites")
        var favorites = 0

        @SerializedName("shortDescription")
        var shortDescription: String? = null

        @SerializedName("_id")
        var id: String? = null

        @SerializedName("tags")
        var tags: List<TagsBean>? = null

        class TypeBean {
            @SerializedName("_id")
            var id: String? = null

            @SerializedName("title")
            var title: String? = null
        }

        class TagsBean {
            @SerializedName("name")
            var name: String? = null

            @SerializedName("_id")
            var id: String? = null
        }
    }
}