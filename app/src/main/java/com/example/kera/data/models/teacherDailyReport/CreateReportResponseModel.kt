package com.example.kera.data.models.teacherDailyReport

import com.google.gson.annotations.SerializedName

class CreateReportResponseModel {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status = 0

    @SerializedName("data")
    var data: DataBean? = null

    class DataBean {
        @SerializedName("_id")
        var id: String? = null

        @SerializedName("date")
        var date: String? = null

        @SerializedName("selectedStudents")
        var selectedStudents: List<SelectedStudentsBean>? = null

        class SelectedStudentsBean {
            @SerializedName("_id")
            var id: String? = null

            @SerializedName("username")
            var username: String? = null

            @SerializedName("profileImage")
            var profileImage: String? = null
        }
    }
}