package com.app.kera.data.models.teacherMedicalReport

import com.app.kera.data.models.teacherDailyReport.DailyReportResponseModel
import com.google.gson.annotations.SerializedName

class MedicalReportResponseModel {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status = 0

    @SerializedName("data")
    var data: DataBean? = null

    class DataBean {
        @SerializedName("date")
        var date: String? = ""

        var dateConverted: String? = ""

        @SerializedName("_id")
        var id: String? = null
        @SerializedName("status")
        var status: Int? = 0
        @SerializedName("students")
        var students: ArrayList<DailyReportResponseModel.DataBean.StudentsBean>? = null

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