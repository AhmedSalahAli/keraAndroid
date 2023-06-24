package com.app.kera.meals.details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.kera.data.network.AppRepo
import com.app.kera.meals.model.MealsDetailsUIModel
import com.app.kera.profile.ProfileUIModel
import com.app.kera.profile.StudentsData
import kotlinx.coroutines.launch

class MealsDetailsViewModel(val appRepo: AppRepo) : ViewModel() {

    var mealDetails = MutableLiveData<MealsDetailsUIModel>()
    var postCommentObserver = MutableLiveData<Boolean>()
    var message = MutableLiveData<String>()
    var selectedUser = MutableLiveData<StudentsData>()
    var profileUIModel = MutableLiveData<ProfileUIModel>()
    fun getMealDetails(classID: String) {
        viewModelScope.launch {


            try{
                val response = appRepo.getClassMealDetails(classID)

                if (response.status == 200) {
                      mealDetails.postValue(MealsDetailsUIModel.convertResponseModelToUIModel(response.data))
                }
            }catch (e : Exception){


            }
        }
    }

    fun postComment(commentPostModel: MealCommentPostModel) {
        viewModelScope.launch {
            try{
                val response = appRepo.postMealComment(commentPostModel)
                if (response.status == 200) {
                    postCommentObserver.value = true
                }
            }catch (e : Exception){
                postCommentObserver.value = false
              
            }

        }
    }
    fun getUserType() : String{
        return appRepo.getUserTypeFromSharedPref()
    }
    fun getSelectedChildDataFromSharedPref(): StudentsData? {
        var response = appRepo.getSelectedChildData()
        selectedUser.value = response!!
        return appRepo.getSelectedChildData()
    }
    fun getAppRepoInstance() :AppRepo{
        return appRepo
    }
    fun saveChildToSharedPref(studentData: StudentsData) {
        appRepo.saveSelectedChildData(studentData)
    }
    fun getProfileData() {
        viewModelScope.launch {
            try {
                val response = appRepo.getProfileData("en", 1, "user")
                profileUIModel.value = ProfileUIModel.mapResponseModelToUIModel(response.data)
            } catch (e: Exception) {
              
            }
        }
    }
}