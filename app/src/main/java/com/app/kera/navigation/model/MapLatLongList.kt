package com.app.kera.navigation.model

import com.app.kera.data.models.MapListResponseModel

class MapLatLongList(
    var lat: Double,
    var long: Double,
    var image: String?="",
    var _id :String?="",
    var name :String?="",
) {
    companion object {
        fun mapResponseToUI(responseModel: MapListResponseModel): ArrayList<MapLatLongList> {
            return responseModel.data?.map {
                MapLatLongList(
                    it.latitude,
                    it.longitude,
                    it.logo,
                    it.id,
                    it.username
                )
            } as ArrayList
        }
    }
}