package com.app.kera.teacherMedicalReport.model

import com.app.kera.data.models.teacherDailyReport.LatestReportsResponseModel
import com.app.kera.utils.CommonUtils

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
                        CommonUtils.convertTimeStampToDate_EEE_MMM_MM_yyyyTT(it.date!!),
                        it.text,
                        it.profileImage,
                        "Sent to ${it.sent}"
                    )
                } as ArrayList
            )
        }
    }
}
