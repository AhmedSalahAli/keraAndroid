package com.app.kera.notification.model

import com.app.kera.utils.CommonUtils
import java.io.Serializable

class NotificationItemUIModel (
    var pages: Int,
    var limit: Int,
    var totalPages: Int,
    var notifications: List<NotificationModel>
) {

    class NotificationModel(
        var _id: String? = "",
        var date: String? = "",
        var title: String? = "",
        var body: String? = "",
        var relatedId: String? = "",
        var userType: String? = "",
        var notificationIcon: String? = "",
        var notificationType: String? = ""
    ):Serializable

    companion object {
        fun convertResponseModelTOUIModel(responseModel: NotificationsResponseModel.DataBean): NotificationItemUIModel {
            return NotificationItemUIModel(
                responseModel.pages,
                responseModel.limit,
                responseModel.total,
                responseModel.docs?.map {
                    NotificationModel(
                        it.Id,
                        CommonUtils.getTimeDateFromTimeStamp(it.NotificationDate!!.toLong()),
                        it.title,
                        it.body,
                        it.relatedId,
                        it.userType,
                        it.notificationIcon,
                        it.notificationType
                    )
                } as ArrayList
            )
        }
    }
}