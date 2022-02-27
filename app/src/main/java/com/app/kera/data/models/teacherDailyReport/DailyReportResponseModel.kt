package com.app.kera.data.models.teacherDailyReport

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DailyReportResponseModel : Serializable{
    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status = 0

    @SerializedName("data")
    var data: DataBean? = null

    class DataBean : Serializable {
        @SerializedName("status")
        var status = 0

        @SerializedName("students")
        var students: ArrayList<StudentsBean>? = null

        @SerializedName("answers")
        var answers: ArrayList<AnswersBean>? = null

        class StudentsBean : Serializable{
            @SerializedName("_id")
            var id: String? = null

            @SerializedName("username")
            var username: String? = null

            @SerializedName("profileImage")
            var profileImage: String? = null
        }

        class AnswersBean : Serializable {
            @SerializedName("question")
            var question: QuestionBean? = null

            @SerializedName("_id")
            var id: String? = null

            @SerializedName("answer")
            var answer: String? = null

            @SerializedName("questionType")
            var questionType: Int? = 0

            @SerializedName("options")
            var options: List<OptionsBean>? = null

            class QuestionBean {
                @SerializedName("_id")
                var id: String? = null

                @SerializedName("value")
                var value: String? = null
            }

        public    class OptionsBean : Serializable{
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