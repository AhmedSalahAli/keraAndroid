package com.app.kera.registrationForm.screen1.model

import com.google.gson.annotations.SerializedName

class PublishAppStep2Model {
    @SerializedName("applicationId")
    var applicationId: String? = null
    @SerializedName("fastherName")
    var fastherName: String? = null
    @SerializedName("fatherJob")
    var fatherJob: String? = null
    @SerializedName("fatherPhones")
    var fatherPhones: List<String>? = null
    @SerializedName("fatherProfileImage")
    var fatherProfileImage: String? = null
    @SerializedName("motherName")
    var motherName: String? = null
    @SerializedName("motherJob")
    var motherJob: String? = null
    @SerializedName("motherPhone")
    var motherPhone: List<String>? = null
    @SerializedName("motherProfileImage")
    var motherProfileImage: String? = null

    @SerializedName("relatedName ")
    var relatedName : String? = null
    @SerializedName("relatedrelation ")
    var relatedrelation : String? = null
    @SerializedName("relatedPhones")
    var relatedPhones: List<String>? = null
    @SerializedName("relatedProfileImage ")
    var relatedProfileImage : String? = null

}