package com.app.kera.data.models

import com.google.gson.annotations.SerializedName

class NewsResponseModel {
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
            @SerializedName("content")
            var content: String? = ""

            @SerializedName("associationId")
            var associationId: AssociationIdBean? = null

            @SerializedName("date")
            var date: String? = null

            @SerializedName("_id")
            var id: String? = null
        }

        class AssociationIdBean {
            @SerializedName("_id")
            var id: String? = null

            @SerializedName("image")
            var image: String? = null
        }
    }
}