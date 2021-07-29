package com.app.kera.data.models

import com.google.gson.annotations.SerializedName

class LoginResponseModel {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("token")
    var token: String? = null

    @SerializedName("exist")
    var exist = false

    @SerializedName("type")
    var type: String? = null

    @SerializedName("status")
    var status = 0

    @SerializedName("data")
    var data: DataBean? = null

    class DataBean {
        @SerializedName("_id")
        var id: String? = null

        @SerializedName("email")
        var email: String? = null

        @SerializedName("phoneVerified")
        var phoneVerified = false

        @SerializedName("phoneNumber")
        var phoneNumber: String? = null

        @SerializedName("username")
        var username: String? = null

        @SerializedName("fcmToken")
        var fcmToken: String? = null

        @SerializedName("profileImage")
        var profileImage: String? = null

        @SerializedName("portal")
        var portal: String? = null

        @SerializedName("blocked")
        var blocked = false

        @SerializedName("type")
        var type: String? = null

        @SerializedName("classes")
        var classes: List<ClassesBean>? = null

        class ClassesBean {
            @SerializedName("_id")
            var id: String? = null

            @SerializedName("name")
            var name: String? = null
        }
    }
}