package com.app.kera.teacherProfile

import com.app.kera.data.models.TeacherProfileResponseModel

class TeacherProfileUIModel(
    var name: String? = "",
    var specialization: String? = "",
    var classNumber: String? = "",
    var className: String? = "",
    var address: String? = "",
    var email: String? = "",
    var phone: String? = "",
    var image: String? = "",
    var classID: String? = "",
    var associationId: String? = ""

) {
    companion object {
        fun mapResponseModelToUIModel(response: TeacherProfileResponseModel.DataBean): TeacherProfileUIModel {
            return TeacherProfileUIModel(
                response.username,
                response.specialization,
                response.classes!!.get(0).id,
                response.classes!!.get(0).name,
                response.location?.address,
                response.email,
                response.phoneNumber,
                response.profileImage,
                response.classes?.get(0)?.id,
                response.associationId
            )
        }
    }
}