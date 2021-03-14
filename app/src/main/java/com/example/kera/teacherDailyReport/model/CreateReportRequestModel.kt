package com.example.kera.teacherDailyReport.model

import com.google.gson.annotations.SerializedName

class CreateReportRequestModel {
    @SerializedName("students")
    var students: ArrayList<String>? = null
}