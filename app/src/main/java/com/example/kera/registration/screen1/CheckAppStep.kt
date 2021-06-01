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
        @SerializedName("associationId")
        var associationId: String? = null
        @SerializedName("step")
        var step: Int? = 0
    }
}