package com.example.kera.schoolDetails.model

import com.example.kera.data.models.schoolList.SchoolDetailsResponseModel

class SchoolDetailsUIModel(
    var id: String?,
    var images: ArrayList<String>,
    var schoolName: String?,
    var details: String?,
    var tags: ArrayList<String>,
    var location: String?,
    var lat : Double,
    var long : Double,
    var phoneNumbers: ArrayList<String>?,
    var numberOfLikes: String?,
    var isFavourite : Boolean,
    var title: String?,
    var slogan: String?,
    var logo: String?,


    ) {
    class Tags(
        var id: String? = "",
        var name: String? = ""
    )

    companion object {
        fun mapResponseModelToUIModel(response: SchoolDetailsResponseModel.DataBean): SchoolDetailsUIModel {
            return SchoolDetailsUIModel(
                response.id,
                response.images!!.map {
                    it.imageLink!!
                } as ArrayList,
                response.username,
                response.description,
                response.tags!!.map {
                    it.name!!
                } as ArrayList,
                response.address,
                response.latitude,
                response.longitude,
                response.phones!!.map {
                    it
                } as ArrayList,
                response.favorites.toString(),
                response.isFavourite,
                response.type?.title,
                response.slogan,
                response.logo,
            )
        }
    }
}