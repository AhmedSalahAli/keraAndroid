package com.example.kera.data.models.schoolList

import com.google.gson.annotations.SerializedName

class SchoolDetailsResponseModel {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status = 0

    @SerializedName("data")
    var data: DataBean? = null

    class DataBean {
        @SerializedName("username")
        var username: String? = null

        @SerializedName("slogan")
        var slogan: String? = null

        @SerializedName("type")
        var type: TypeBean? = null

        @SerializedName("video")
        var video: String? = null

        @SerializedName("latitude")
        var latitude = 0.0

        @SerializedName("longitude")
        var longitude = 0.0

        @SerializedName("address")
        var address: String? = null

        @SerializedName("logo")
        var logo: String? = null

        @SerializedName("favorites")
        var favorites = 0

        @SerializedName("description")
        var description: String? = null

        @SerializedName("_id")
        var id: String? = null

        @SerializedName("phones")
        var phones: List<String>? = null

        @SerializedName("tags")
        var tags: List<TagsBean>? = null


        @SerializedName("images")
        var images: List<Images>? = null

        class TypeBean {
            @SerializedName("_id")
            var id: String? = null

            @SerializedName("title")
            var title: String? = null
        }

        class TagsBean {
            @SerializedName("name")
            var name: String? = ""

            @SerializedName("_id")
            var id: String? = null
        }

        class Images {
            @SerializedName("link")
            var videoUrl: String? = null

            @SerializedName("image")
            var imageLink: String? = null
        }

    }
}