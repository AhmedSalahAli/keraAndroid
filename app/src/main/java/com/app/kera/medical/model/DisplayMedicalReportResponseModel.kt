package com.app.kera.medical.model

import com.google.gson.annotations.SerializedName

class DisplayMedicalReportResponseModel {
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
            @SerializedName("createdAt")
            var date: String? = ""

            var dateConverted: String? = ""

            @SerializedName("_id")
            var id: String? = null

            @SerializedName("teacher")
            var teacher: TeacherBean? = null

            @SerializedName("question1")
            var question1: AnswersBean1? = null

            @SerializedName("question2")
            var question2: AnswersBean2? = null

            @SerializedName("images")
            var images: ArrayList<String>? = null

            class TeacherBean {
                @SerializedName("_id")
                var id: String? = null

                @SerializedName("username")
                var username: String? = null

                @SerializedName("profileImage")
                var profileImage: String? = null
            }

            class AnswersBean1 {


                @SerializedName("isYes")
                var isYes: Boolean = false

                @SerializedName("isNo")
                var isNo: Boolean = false

                @SerializedName("question")
                var question: String? = null


            }
            class AnswersBean2 {


                @SerializedName("question")
                var question: String? = null

                @SerializedName("answer")
                var answer: String? = null


            }
        }
    }
}