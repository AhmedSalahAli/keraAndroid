package com.example.kera.data.models.teacherMedicalReport

import com.google.gson.annotations.SerializedName

class UpdateMedicalRequestModel {
    @SerializedName("question1")
    var question1: String? = ""

    @SerializedName("question2")
    var question2: String? = ""
    @SerializedName("reportId")
    var reportId: String? = ""

}