package com.example.kera.education.model

import com.example.kera.data.models.EducationResponseModel
import com.example.kera.utils.CommonUtils

class EducationListItemModel(
    var title: String? = "",
    var date: String? = "",
    var description: String? = "",
    var image1: String? = "",
    var image2: String? = "",
    var image3: String? = ""
) {
    companion object {
        fun convertResponseModelToUIModel(response: EducationResponseModel.DataBean?): ArrayList<EducationListItemModel> {
            return response?.educations?.map {
                EducationListItemModel(
                    it.title,
                    CommonUtils.convertTimeStampToDate_EEEE_MMM_MM(it.date!!),
                    it.shortDescription,
                    it.images?.get(0),
                    it.images?.get(1),
                    it.images?.get(2),
                )
            } as ArrayList
        }
    }
}