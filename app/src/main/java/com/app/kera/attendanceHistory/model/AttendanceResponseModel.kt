package com.app.kera.attendanceHistory.model

import com.google.gson.annotations.SerializedName

class AttendanceResponseModel {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status = 0

    @SerializedName("data")
    var data: DataBean? = null

    class DataBean {
        @SerializedName("total")
        var total = 0

        @SerializedName("limit")
        var limit = 0

        @SerializedName("page")
        var page: String? = null

        @SerializedName("pages")
        var pages = 0

        @SerializedName("docs")
        var docs: List<DocsBean>? = null

        class DocsBean {
            @SerializedName("date")
            var date: String? = null

            @SerializedName("_id")
            var _id: String? = null

            @SerializedName("arrivalTime")
            var arrivalTime: String? = null

            @SerializedName("departureTime")
            var departureTime: String? = null
        }
        }


}