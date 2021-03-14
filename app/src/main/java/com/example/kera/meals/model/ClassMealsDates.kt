package com.example.kera.meals.model

import com.example.kera.utils.CommonUtils
import com.example.kera.utils.CommonUtils.convertTimeStampToDate_mm_dd_yyyy

class ClassMealsDates(
    var displayDate: String,
    var actualDate: String
){
    companion object{
        fun convertDate(dates : ArrayList<String>) : ArrayList<ClassMealsDates>{
            return dates.map {
                ClassMealsDates(
                    CommonUtils.convertTimeStampToDate_dd(it),
                    convertTimeStampToDate_mm_dd_yyyy(it)
                )
            } as ArrayList
        }
    }
}