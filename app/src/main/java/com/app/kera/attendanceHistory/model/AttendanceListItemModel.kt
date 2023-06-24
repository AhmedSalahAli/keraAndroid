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
                    CommonUtils.convertIsoToDate(it.date!!),
                    it._id,
                    it.arrivalTime?.let { it1 -> CommonUtils.convertTimeStampToDate(it1,"hh:mm") },
                    it.departureTime?.let { it1 -> CommonUtils.convertTimeStampToDate(it1,"hh:mm") },
                    it.arrivalTime?.let { it1 -> CommonUtils.convertTimeStampToDate(it1,"aa") },
                    it.departureTime?.let { it1 -> CommonUtils.convertTimeStampToDate(it1,"aa") }
                )
            } as ArrayList
        }
    }
}