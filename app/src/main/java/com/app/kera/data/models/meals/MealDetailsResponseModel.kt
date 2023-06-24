package com.app.kera.data.models.meals

import com.google.gson.annotations.SerializedName

class MealDetailsResponseModel (

    @SerializedName("message" ) var message : String? = null,
    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("data"    ) var data    : Data?   = Data()

){
    data class Data (

        @SerializedName("title"       ) var title       : String?           = null,
        @SerializedName("mealName"    ) var mealName    : String?           = null,
        @SerializedName("description" ) var description : String?           = null,
        @SerializedName("images"      ) var images      : ArrayList<String> = arrayListOf(),
        @SerializedName("smallImage"  ) var smallImage  : String?           = null,
        @SerializedName("_id"         ) var Id          : String?           = null

    )
}
