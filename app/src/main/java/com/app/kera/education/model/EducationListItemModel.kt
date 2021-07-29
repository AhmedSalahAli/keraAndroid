package com.app.kera.education.model

import com.app.kera.data.models.EducationResponseModel
import com.app.kera.utils.CommonUtils

class EducationListItemModel(
    var title: String? = "",
    var date: String? = "",
    var description: String? = "",

    var images:ArrayList<String>
) {
    companion object {
        fun convertResponseModelToUIModel(response: EducationResponseModel.DataBean?): ArrayList<EducationListItemModel> {
            return response?.educations?.map {
                EducationListItemModel(
                    it.title,
                    CommonUtils.convertTimeStampToDate_EEEE_MMM_MM(it.date!!),
                    it.shortDescription,

                    it.images!!.map {
                        it
                    } as ArrayList,
                )
            } as ArrayList
        }
    }
}