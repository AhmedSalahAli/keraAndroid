package com.app.kera.data.models

import com.google.gson.annotations.SerializedName

class ProfileResponseModel {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status = 0

    @SerializedName("data")
    var data: DataBean? = null

    class DataBean {
        @SerializedName("username")
        var username: String? = null

        @SerializedName("email")
        var email: String? = null

        @SerializedName("phoneVerified")
        var phoneVerified = false

        @SerializedName("phoneNumber")
        var phoneNumber: String? = null

        @SerializedName("profileImage")
        var profileImage: String? = null

        @SerializedName("associationId")
        var associationId: String? = null

        @SerializedName("associationLogo")
        var associationLogo: String? = null

        @SerializedName("blocked")
        var blocked = false

        @SerializedName("location")
        var location: LocationBean? = null

        @SerializedName("type")
        var type: String? = null

        @SerializedName("students")
        var students: List<StudentsBean>? = null

        class LocationBean {
            @SerializedName("latitude")
            var latitude = 0.0

            @SerializedName("longitude")
            var longitude = 0.0

            @SerializedName("address")
            var address: String? = null
        }

        class StudentsBean {
            @SerializedName("username")
            var username: String? = null

            @SerializedName("studentCode")
            var studentCode: String? = null

            @SerializedName("_id")
            var id: String? = null

            @SerializedName("profileImage")
            var profileImage: String? = null

            @SerializedName("classId")
            var classId: ClassIdBean? = null

            @SerializedName("class")
            var classX: String? = null

            class ClassIdBean {
                @SerializedName("_id")
                var id: String? = null

                @SerializedName("name")
                var name: String? = null
            }
        }
    }
}