package com.app.kera.events.model

class UpcomingItemUIModel (
    var id: String? = "",
    var title: String? = "",
var shortDescription: String?,
var mainCover: String?,
var latitude: Double? = 0.0,
    var longitude: Double? = 0.0,
    var address: String? = "",
    var eventDate: String? = "",

) {
    companion object {
        fun convertResponseModelToUIModel(response: List<ClassUpcomingResponseModel.DataBean.DocsBean>): ArrayList<UpcomingItemUIModel> {
            return response.map {
                UpcomingItemUIModel(
                    it._id,
                    it.title,
                    it.shortDescription,
                    it.mainCover,
                    it.latitude,
                    it.longitude,
                    it.address,
                    it.eventDate,

                )
            } as ArrayList
        }
    }
}