package com.example.kera.meals

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kera.data.network.AppRepo
import com.example.kera.meals.model.ClassMealsDates
import com.example.kera.meals.model.MealsItemUIModel
import com.example.kera.profile.StudentsData
import kotlinx.coroutines.launch

class MealsViewModel(val appRepo: AppRepo) : ViewModel() {


    var datesListLiveData = MutableLiveData<ArrayList<ClassMealsDates>>()
    var mealsList = MutableLiveData<ArrayList<MealsItemUIModel>>()
    var monthAndYearTitleLiveData = MutableLiveData<String>()
    private var calendarDate: String? = null
    var message = MutableLiveData<String>()

    fun getDates(classID: String) {
        viewModelScope.launch {
            try {
                val response = appRepo.getClassMealsDates(classID)
                datesListLiveData.value = ClassMealsDates.convertDate(response.data!!)
            } catch (e: Exception) {
                message.value = e.toString()
            }
        }
    }

    fun getMeals(classID: String,fromDate : String) {
        viewModelScope.launch {
            try {
                val response = appRepo.getClassMeals(classID,fromDate)
                mealsList.value =
                    MealsItemUIModel.convertResponseModelToUIModel(response.data?.Meals)
            } catch (e: Exception) {
                message.value = e.toString()
            }
        }
    }


    fun getSelectedChildDataFromSharedPref(): StudentsData? {
        return appRepo.getSelectedChildData()
    }

    fun getUserType() : String{
        return appRepo.getUserTypeFromSharedPref()
    }
}