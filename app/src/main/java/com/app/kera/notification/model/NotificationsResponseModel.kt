package com.app.kera.notification.model

import com.google.gson.annotations.SerializedName

class NotificationsResponseModel {
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
        var docs: ArrayList<DocsBean>? = null

        class DocsBean {
            @SerializedName("_id")
            var _id: String? = null

            @SerializedName("title")
            var title: String? = null

            @SerializedName("body")
            var body: String? = null

            @SerializedName("notificationIcon")
            var notificationIcon: String? = null

            @SerializedName("notificationType")
            var notificationType: String? = null

            @SerializedName("NotificationDate")
            var NotificationDate: String? = null
        }
    }
}