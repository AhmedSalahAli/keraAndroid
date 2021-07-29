package com.app.kera.teacherDailyReport.model

import com.google.gson.annotations.SerializedName

class UpdateQuestionRequestModel {
    @SerializedName("question")
    var question: String? = ""

    @SerializedName("reportId")
    var reportId: String? = ""

    @SerializedName("answer")
    var answer: ArrayList<String>? = null
}