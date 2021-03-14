package com.example.kera.teacherDailyReport.model

import com.google.gson.annotations.SerializedName

class SelectedStudentsRequestModel(
    @SerializedName("students")
    var students: ArrayList<String>
)