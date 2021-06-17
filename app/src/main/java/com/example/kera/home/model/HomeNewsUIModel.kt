package com.example.kera.home.model

import com.example.kera.data.models.NewsResponseModel
import com.example.kera.utils.CommonUtils

class HomeNewsUIModel(
    var total: Int,
    var limit: Int,
    var page: String? = "",
    var pages: Int,
    var newsList: ArrayList<NewsList>
) {

    class NewsList(
        var title: String? = "",
        var date: String? = "",
        var id: String?,
        var image: String?,
    )

    companion object {
        fun convertResponseModelToUIModel(response: NewsResponseModel.DataBean): HomeNewsUIModel {
            return HomeNewsUIModel(
                response.total,
                response.limit,
                response.page,
                response.pages,
                response.docs?.map {
                    NewsList(
                        it.content,
                        CommonUtils.convertTimeStampToDate_EEEE_MMM_MM_yyyy(it.date!!),
                        "# ${it.id}",
                        it.associationId?.image
                    )
                } as ArrayList
            )
        }
    }
}