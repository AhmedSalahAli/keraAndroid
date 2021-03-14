package com.example.kera.data.models.meals

import com.google.gson.annotations.SerializedName

class DatesResponseModel {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status = 0

    @SerializedName("data")
    var data: ArrayList<String>? = null
}