package com.example.kera.data.models.teacherDailyReport

import com.google.gson.annotations.SerializedName

class DailyReportResponseModel {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status = 0

    @SerializedName("data")
    var data: DataBean? = null

    class DataBean {
        @SerializedName("status")
        var status = 0

        @SerializedName("students")
        var students: ArrayList<StudentsBean>? = null

        @SerializedName("answers")
        var answers: ArrayList<AnswersBean>? = null

        class StudentsBean {
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