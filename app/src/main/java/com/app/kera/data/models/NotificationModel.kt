package com.app.kera.data.models

import java.io.Serializable

class NotificationModel :Serializable{
    var from: String? = ""
    var reportId: String? = ""
    var userType: String?= ""
    var reportType: String?= ""
    var type: String?= ""
    var title: String?= ""
    var body: String?= ""
}
