package com.example.kera.data.models.teacherDailyReport

import com.google.gson.annotations.SerializedName

class TeacherReportStudentsResponseModel {
    var message: String? = null
    var status = 0

    @SerializedName("data")
    var students: List<Students>? = null

    class Students {
        @SerializedName("_id")
        var id: String? = null

        @SerializedName("username")
        var name: String? = null

        @SerializedName("profileImage")
        var image: String? = null
    }
}