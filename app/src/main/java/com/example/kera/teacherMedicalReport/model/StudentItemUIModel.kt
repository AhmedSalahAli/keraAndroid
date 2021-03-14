package com.example.kera.teacherMedicalReport.model

import androidx.databinding.ObservableBoolean
import com.example.kera.data.models.teacherDailyReport.TeacherReportStudentsResponseModel

class StudentItemUIModel(
    var id: String,
    var name: String,
    var image: String,
    var isSelected :ObservableBoolean
) {
    companion object {
        fun convertResponseModelToUIModel(response: TeacherReportStudentsResponseModel): ArrayList<StudentItemUIModel> {
            return response.students?.map {
                StudentItemUIModel(
                    it.id!!,
                    it.name!!,
                    it.image!!,
                    ObservableBoolean(false)
                )
            } as ArrayList
        }
    }
}