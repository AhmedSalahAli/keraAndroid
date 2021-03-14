package com.example.kera.data.models.teacherDailyReport

class DailyReportClassesResponseModel {
    var message: String? = null
    var status = 0
    var data: DataBean? = null

    class DataBean {
        var classes: List<ClassesBean>? = null

        class ClassesBean {
            var _id: String? = null
            var name: String? = null
        }
    }
}