package com.example.kera.teacherMedicalReport.model

import androidx.databinding.ObservableField
import com.example.kera.data.models.teacherDailyReport.DailyReportClassesResponseModel

class TeacherReportClassesUIModel(
    var id: String,
    var name: String,
    var isSelected: ObservableField<Boolean>,
) {
    companion object {
        fun convertResponseModelToUIModel(response: DailyReportClassesResponseModel.DataBean): ArrayList<TeacherReportClassesUIModel> {
            return response.classes?.map {
                TeacherReportClassesUIModel(
                    it._id!!,
                    it.name!!,
                    ObservableField(false)
                )
            } as ArrayList
        }
    }
}