package com.example.kera.home.model

import androidx.lifecycle.MutableLiveData
import com.example.kera.data.models.HomeImagesResponseModel

class ImagesUIModel(
    var name: MutableLiveData<String>,
    var slogan: String? = "",
    var type: String? = "",
    var logo: String? = "",
    var images: ArrayList<String?>
) {
    companion object {
        fun convertResponseModelToUIModel(responseModel: HomeImagesResponseModel.DataBean): ImagesUIModel {
            return ImagesUIModel(
                MutableLiveData(responseModel.portalName),
                responseModel.slogan,
                responseModel.type?.title,
                responseModel.logo,
                responseModel.images?.map {
                    it.image
                } as ArrayList
            )
        }
    }
}