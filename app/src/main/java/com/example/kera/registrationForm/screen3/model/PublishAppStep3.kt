package com.example.kera.registrationForm.screen3.model

import com.google.gson.annotations.SerializedName

class PublishAppStep3 {
    @SerializedName("applicationId")
    var applicationId: String? = null
    @SerializedName("address")
    var address: String? = null
    @SerializedName("latitude")
    var latitude: Float? = null
    @SerializedName("longitude")
    var longitude: Float? = null
    @SerializedName("medicalHistory")
    var medicalHistory: String? = null
}