package com.example.kera.profile

import androidx.lifecycle.MutableLiveData
import com.example.kera.data.models.ProfileResponseModel

class ProfileUIModel(
    var address: String? = "",
    var email: String? = "",
    var phone: String? = "",
    var associationId: String? = "",
    var name: MutableLiveData<String>,
//    var mobile: String? = "",
    var students: ArrayList<StudentsData>? = null,
    var className: MutableLiveData<String>,
    var selectedStudentCode: String? = "",
    var image: MutableLiveData<String>,

    ) {
    companion object {
        fun mapResponseModelToUIModel(data: ProfileResponseModel.DataBean?): ProfileUIModel {
            return ProfileUIModel(
                data?.location?.address,
                data?.email,
                data?.phoneNumber,
                data?.associationId,
                MutableLiveData(data?.students!![0].username),
                data.students?.map {
                    StudentsData(
                        it.username,
                        it.id,
                        it.profileImage,
                        it.studentCode,
                        it.classId?.id,
                        it.classId?.name
                    )
                } as ArrayList,
                MutableLiveData("Class:" + data.students!![0].classX),
                "Code:" + data.students!![0].studentCode,
                MutableLiveData(data.students!![0].profileImage),
            )
        }
    }
}

class StudentsData(
    var username: String? = "",
    var studentId: String? = "",
    var profileImage: String? = "",
    var studentCode: String? = "",
    var classId: String? = "",
    var className: String? = "",
)