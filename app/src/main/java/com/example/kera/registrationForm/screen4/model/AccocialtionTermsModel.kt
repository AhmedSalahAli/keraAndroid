package com.example.kera.registrationForm.screen4.model

import com.example.kera.data.models.DisplayDailyReportResponseModel
import com.google.gson.annotations.SerializedName

class AccocialtionTermsModel {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status = 0

    @SerializedName("data")
    var data: String? = null
}