package com.example.kera.navigation.model

import com.example.kera.data.models.MapListResponseModel

class MapLatLongList(
    var lat: Double,
    var long: Double,
    var image: String?=""
) {
    companion object {
        fun mapResponseToUI(responseModel: MapListResponseModel): ArrayList<MapLatLongList> {
            return responseModel.data?.map {
                MapLatLongList(
                    it.latitude,
                    it.longitude,
                    it.logo
                )
            } as ArrayList
        }
    }
}