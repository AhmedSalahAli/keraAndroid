package com.app.kera.dailyReport.model

import com.google.gson.annotations.SerializedName

class PublishReplay {
    @SerializedName("type")
    var type: String? = null
    @SerializedName("reportId")
    var reportId: String? = null
    @SerializedName("reply")
    var reply: String? = null

}