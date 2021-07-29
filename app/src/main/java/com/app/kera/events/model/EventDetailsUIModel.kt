package com.app.kera.events.model

import androidx.lifecycle.MutableLiveData
import com.app.kera.utils.CommonUtils

class EventDetailsUIModel
    (
    var images: ArrayList<String>?,
    var title: MutableLiveData<String>?,
    var description: MutableLiveData<String>?,
    var latitude: MutableLiveData<Double>?,
    var longitude: MutableLiveData<Double>?,

    var address: MutableLiveData<String>?,
    var eventPrice: Float? = null,
    var eventDateE: MutableLiveData<String>?,
    var eventDateD: MutableLiveData<String>?,
    var fromTime: MutableLiveData<String>?,
    var toTime: MutableLiveData<String>?,
    var _id: MutableLiveData<String>?,
    var imagesLink: MutableLiveData<String>?,
    var students: ArrayList<EventDetailsResponseModel.DataBean.StudentData>??,

    ) {
        companion object {
            fun convertResponseModelToUIModel(response: EventDetailsResponseModel.DataBean?): EventDetailsUIModel {
                return EventDetailsUIModel(
                    response?.images?.map {
                        it
                    } as ArrayList,
                    MutableLiveData(response.title),
                    MutableLiveData(response.description),
                    MutableLiveData(response.latitude),
                    MutableLiveData(response.longitude),
                    MutableLiveData(response.address),
                    response.eventPrice ,
                    MutableLiveData(CommonUtils.convertTimeStampToEe(response.eventDate!!)),
                    MutableLiveData(CommonUtils.convertTimeStampToDd(response.eventDate!!)),
                    MutableLiveData(response.fromTime),
                    MutableLiveData(response.toTime),
                    MutableLiveData(response._id),
                    MutableLiveData(response.imagesLink),
                    response?.students?.map {
                        it
                    } as ArrayList,

                    )
            }
        }
    }
