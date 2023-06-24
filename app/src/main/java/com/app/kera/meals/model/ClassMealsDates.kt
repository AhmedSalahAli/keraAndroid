package com.app.kera.meals.model

import com.app.kera.utils.CommonUtils

import com.app.kera.utils.CommonUtils.convertIsoToTimeStamp


class ClassMealsDates(
    var displayDateDay: String,
    var dispayDateDayS:String,
    var displayDateMonth: String,
    var actualDate: String,
    var dateTimestamp: Long
){
    companion object{
        fun convertDate(dates : ArrayList<String>) : ArrayList<ClassMealsDates>{
            return dates.map {
                ClassMealsDates(
                    CommonUtils.convertIsoToDate(it,"dd"),
                    CommonUtils.convertIsoToDate(it,"EEE"),
                    CommonUtils.convertIsoToDate(it,"MMM"),
                    it,
                    convertIsoToTimeStamp(it)
                )
            } as ArrayList
        }
    }
}