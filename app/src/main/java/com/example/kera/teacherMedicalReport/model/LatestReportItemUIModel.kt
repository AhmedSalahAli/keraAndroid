package com.example.kera.teacherMedicalReport.model

import com.example.kera.data.models.teacherDailyReport.LatestReportsResponseModel
import com.example.kera.utils.CommonUtils

class LatestReportItemUIModel(
    var pages: Int,
    var limit: Int,
    var totalPages: Int,
    var reports: List<ReportModel>
) {

    class ReportModel(
        var id: String? = "",
        var date: String? = "",
        var details: String? = "",
        var image: String? = "",
        var sent: String? = ""
    )

    companion object {
        fun convertResponseModelTOUIModel(responseModel: LatestReportsResponseModel.ReportsBean): LatestReportItemUIModel {
            return LatestReportItemUIModel(
                responseModel.pages,
                responseModel.limit,
                responseModel.total,
                responseModel.docs?.map {
                    ReportModel(
                        it.id,
                        CommonUtils.getTimeDateFromTimeStamp(it.date!!.toLong()),
                        it.text,
                        it.profileImage,
                        "Sent to ${it.sent}"
                    )
                } as ArrayList
            )
        }
    }
}
