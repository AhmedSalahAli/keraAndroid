package com.app.kera.registrationForm.screen1.model

import com.google.gson.annotations.SerializedName

class PublishAppStep1Model {
    @SerializedName("name")
    var name: String? = null
    @SerializedName("associationId")
    var associationId: String? = null
    @SerializedName("nationality")
    var nationality: String? = null
    @SerializedName("gender")
    var gender: Int? = null
    @SerializedName("birthDate")
    var birthDate: String? = null
    @SerializedName("udId")
    var udId: String? = null
    @SerializedName("profileImage")
    var profileImage: String? = null

}