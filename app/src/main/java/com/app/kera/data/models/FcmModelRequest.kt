package com.app.kera.data.models

import com.google.gson.annotations.SerializedName

class FcmModelRequest {
    @SerializedName("deviceType")
    var deviceType: String? = null
    @SerializedName("registerToken")
    var reg: String? = null
}
