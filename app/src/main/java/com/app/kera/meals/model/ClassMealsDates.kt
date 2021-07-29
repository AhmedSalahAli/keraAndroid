package com.app.kera.meals.model

import com.app.kera.utils.CommonUtils
import com.app.kera.utils.CommonUtils.convertTimeStampToDate_mm_dd_yyyy

class ClassMealsDates(
    var displayDateDay: String,
    var displayDateMonth: String,
    var actualDate: String
){
    companion object{
        fun convertDate(dates : ArrayList<String>) : ArrayList<ClassMealsDates>{
            return dates.map {
                ClassMealsDates(
                    CommonUtils.convertTimeStampToDate_dd(it),
                    CommonUtils.convertTimeStampToDate_mm(it),
                    convertTimeStampToDate_mm_dd_yyyy(it)
                )
            } as ArrayList
        }
    }
}