package com.app.kera.data.models

import com.google.gson.annotations.SerializedName

class DisplayDailyReportResponseModel {
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
            @SerializedName("date")
            var date: String? = ""

            var dateConverted: String? = ""
            @SerializedName("_id")
            var id: String? = null

            @SerializedName("teacher")
            var teacher: TeacherBean? = null

            @SerializedName("answers")
            var answers: ArrayList<AnswersBean>? = null

            class TeacherBean {
                @SerializedName("_id")
                var id: String? = null

                @SerializedName("username")
                var username: String? = null

                @SerializedName("profileImage")
                var profileImage: String? = null
            }

            class AnswersBean {
                @SerializedName("question")
                var question: QuestionBean? = null

                @SerializedName("_id")
                var id: String? = null

                @SerializedName("answer")
                var answer: String? = null

                @SerializedName("options")
                var options: List<OptionsBean>? = null

                class QuestionBean {
                    @SerializedName("_id")
                    var id: String? = null

                    @SerializedName("value")
                    var value: String? = null
                }

                class OptionsBean {
                    @SerializedName("selected")
                    var selected = false

                    @SerializedName("_id")
                    var id: String? = null

                    @SerializedName("value")
                    var value: String? = null

                    @SerializedName("icon")
                    var icon: String? = null
                }
            }
        }
    }
}