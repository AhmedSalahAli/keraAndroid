package com.example.kera.data.models

import com.google.gson.annotations.SerializedName

class TeacherProfileResponseModel {
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

        @SerializedName("specialization")
        var specialization: String? = null

        @SerializedName("location")
        var location: LocationBean? = null

        @SerializedName("type")
        var type: String? = null

        @SerializedName("classes")
        var classes: List<ClassesBean>? = null

        class LocationBean {
            @SerializedName("latitude")
            var latitude = 0.0

            @SerializedName("longitude")
            var longitude = 0.0

            @SerializedName("address")
            var address: String? = null
        }
        class ClassesBean {
            @SerializedName("_id")
            var id: String? = null

            @SerializedName("name")
            var name: String? = null
        }
    }
}