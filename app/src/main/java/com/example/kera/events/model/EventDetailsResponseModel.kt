package com.example.kera.events.model

import com.google.gson.annotations.SerializedName

class EventDetailsResponseModel {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status = 0

    @SerializedName("data")
    var data: DataBean? = null

    class DataBean {
        @SerializedName("title")
        var title: String? = null

        @SerializedName("description")
        var description: String? = null

        @SerializedName("images")
        var images: List<String>? = null

        @SerializedName("latitude")
        var latitude: Double? = 0.0

        @SerializedName("longitude")
        var longitude: Double? = null

        @SerializedName("address")
        var address: String? = null

        @SerializedName("eventPrice")
        var eventPrice: Float? = null

        @SerializedName("eventDate")
        var eventDate: String? = null

        @SerializedName("fromTime")
        var fromTime: String? = null

        @SerializedName("toTime")
        var toTime: String? = null

        @SerializedName("_id")
        var _id: String? = null

        @SerializedName("students")
        var students: List<StudentData>? = null

        class StudentData {
            @SerializedName("accept")
            var accept: Boolean? = null
            @SerializedName("_id")
            var _id: String? = null
            @SerializedName("student")
            var student: Student? = null
            class Student {
                @SerializedName("_id")
                var _id: String? = null
                @SerializedName("profileImage")
                var profileImage: String? = null
            }
        }
    }
}