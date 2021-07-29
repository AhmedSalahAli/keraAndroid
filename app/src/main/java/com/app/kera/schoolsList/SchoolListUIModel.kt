package com.app.kera.schoolsList

import android.location.Location
import androidx.databinding.ObservableBoolean
import com.app.kera.data.models.schoolList.SchoolsListResponseModel
import com.fonfon.geohash.GeoHash

class SchoolListUIModel(
    var pages: Int,
    var limit: Int,
    var totalPages: Int,
    var schools: List<SchoolData>
) {
    class Tags(
        var id: String? = "",
        var name: String? = ""
    )

    class SchoolData(
        var id: String?,
        var title: String?,
        var location: String?,
        var details: String? = "",
        var image: String?,
        var logo: String?,
        var likes: String?,
        var tags: List<Tags>,
        var longitude: Double,
        var latitude: Double,
        var geoHash: String,
        var isLiked: ObservableBoolean
    )

    companion object {
        fun convertResponseModelToUIModel(response: SchoolsListResponseModel.DataBean): SchoolListUIModel {
            return SchoolListUIModel(
                response.pages,
                response.limit,
                response.total,
                response.docs!!.map { it ->
                    SchoolData(
                        it.id,
                        it.username,
                        it.address,
                        it.shortDescription,
                        it.image,
                        it.logo,
                        it.favorites.toString(),
                        it.tags!!.map {
                            Tags(
                                it.id,
                                it.name
                            )
                        },
                        it.longitude,
                        it.latitude,
                        getGeoHash(it.latitude, it.longitude),
                        ObservableBoolean(it.isFavourite),
                    )
                }
            )
        }

        private fun getGeoHash(latitude: Double, longitude: Double): String {
            val location = Location("geohash")
            location.latitude = latitude
            location.longitude = longitude

            val hash: GeoHash = GeoHash.fromLocation(location, 9)
            hash.toString() //"v12n8trdj"
            return hash.toString()
        }
    }

}