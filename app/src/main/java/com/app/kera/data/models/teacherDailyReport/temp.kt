package com.app.kera.data.models.teacherDailyReport

import com.google.gson.annotations.SerializedName

class temp {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status = 0

    @SerializedName("reports")
    var reports: ReportsBean? = null

    class ReportsBean {
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
            @SerializedName("profileImage")
            var profileImage: String? = null

            @SerializedName("createdAt")
            var date: String? = null

            @SerializedName("sent")
            var sent: String? = null

            @SerializedName("text")
            var text: String? = null
        }
    }
}