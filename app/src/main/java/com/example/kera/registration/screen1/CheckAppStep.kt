package com.example.kera.registration.screen1

import com.example.kera.data.models.ProfileResponseModel
import com.google.gson.annotations.SerializedName

class CheckAppStep {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status = 0

    @SerializedName("application")
    var application: ApplicationBean ?= null

    class ApplicationBean {
        @SerializedName("_id")
        var _id: String? = null
        @SerializedName("associationId")
        var associationId: String? = null
        @SerializedName("step")
        var step: Int? = 0
        @SerializedName("student")
        var student: Student ?= null
        class Student{
            @SerializedName("name")
            var name: String? = null
            @SerializedName("profileImage")
            var profileImage: String? = null
            @SerializedName("nationality")
            var nationality: String? = null
            @SerializedName("gender")
            var gender: Int? = null
            @SerializedName("birthDate")
            var birthDate: String? = null

        }
    }
}