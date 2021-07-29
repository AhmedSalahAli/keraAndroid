package com.app.kera.data.models

import com.google.gson.annotations.SerializedName

class HomeImagesResponseModel {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status = 0

    @SerializedName("data")
    var data: DataBean? = null

    class DataBean {
        @SerializedName("portalName")
        var portalName: String? = null

        @SerializedName("slogan")
        var slogan: String? = null

        @SerializedName("logo")
        var logo: String? = null

        @SerializedName("type")
        var type: TypeBean? = null

        @SerializedName("images")
        var images: ArrayList<Images>? = null

        @SerializedName("students")
        var students: List<StudentsBean>? = null

        class TypeBean {
            @SerializedName("_id")
            var id: String? = null

            @SerializedName("title")
            var title: String? = null
        }

        class Images {
            @SerializedName("link")
            var link: String? = null

            @SerializedName("image")
            var image: String? = null
        }

        class StudentsBean {
            @SerializedName("_id")
            var id: String? = null

            @SerializedName("username")
            var username: String? = null

            @SerializedName("classId")
            var classId: String? = null

            @SerializedName("profileImage")
            var profileImage: String? = null
        }
    }
}