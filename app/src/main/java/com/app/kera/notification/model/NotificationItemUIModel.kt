package com.app.kera.notification.model

import com.app.kera.utils.CommonUtils

class NotificationItemUIModel (
    var pages: Int,
    var limit: Int,
    var totalPages: Int,
    var notifications: List<NotificationModel>
) {

    class NotificationModel(
        var id: String? = "",
        var date: String? = "",
        var title: String? = "",
        var body: String? = "",
        var notificationIcon: String? = "",
        var notificationType: String? = ""
    )

    companion object {
        fun convertResponseModelTOUIModel(responseModel: NotificationsResponseModel.DataBean): NotificationItemUIModel {
            return NotificationItemUIModel(
                responseModel.pages,
                responseModel.limit,
                responseModel.total,
                responseModel.docs?.map {
                    NotificationModel(
                        it._id,
                        CommonUtils.getTimeDateFromTimeStamp(it.NotificationDate!!.toLong()),
                        it.title,
                        it.body,
                        it.notificationIcon,
                        it.notificationType
                    )
                } as ArrayList
            )
        }
    }
}