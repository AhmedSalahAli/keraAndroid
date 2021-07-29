package com.app.kera.attendanceHistory.model

import com.app.kera.utils.CommonUtils

class AttendanceListItemModel (
    var date: String? = "",
    var _id: String? = "",
    var arrivalTime: String? = "",
    var departureTime: String? = "",
    var arrivalAA: String? = "",
    var departureAA: String? = "",

) {
    companion object {
        fun convertResponseModelToUIModel(response: AttendanceResponseModel.DataBean?): ArrayList<AttendanceListItemModel> {
            return response?.docs?.map{
                AttendanceListItemModel(
                    CommonUtils.convertTimeStampToDate_EEEE_MMM_MM(it.date!!),
                    it._id,
                    it.arrivalTime?.let { it1 -> CommonUtils.convertTimeStampToTime_Am_Pm(it1) },
                    it.departureTime?.let { it1 -> CommonUtils.convertTimeStampToTime_Am_Pm(it1) },
                    it.arrivalTime?.let { it1 -> CommonUtils.convertTimeStampTo_Am_Pm(it1) },
                    it.departureTime?.let { it1 -> CommonUtils.convertTimeStampTo_Am_Pm(it1) }
                )
            } as ArrayList
        }
    }
}