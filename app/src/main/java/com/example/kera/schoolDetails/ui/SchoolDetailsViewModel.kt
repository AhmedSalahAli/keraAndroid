package com.example.kera.schoolDetails.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kera.data.models.schoolList.FavouriteSchoolRequestModel
import com.example.kera.data.network.AppRepo
import com.example.kera.schoolDetails.model.SchoolDetailsUIModel
import kotlinx.coroutines.launch

class SchoolDetailsViewModel(val appRepo: AppRepo) : ViewModel() {

    var schoolDetails = MutableLiveData<SchoolDetailsUIModel>()
    var message = MutableLiveData<String>()
    var anApiFailed = MutableLiveData<Boolean>()
    fun getSchoolsList(schoolID: String) {
        //5fa818094a4e102fe1cb9247
        viewModelScope.launch {
            val response = appRepo.getSchoolDetails(schoolID)
            schoolDetails.value = SchoolDetailsUIModel.mapResponseModelToUIModel(response.data!!)
        }
    }
    fun favouriteSchool(requestModel: FavouriteSchoolRequestModel) {
        viewModelScope.launch {
            try {
                val response = appRepo.favouriteSchool("en", requestModel)
                if (response.status == 200) {
//                    getSchoolsList(3)
                }
            } catch (e: Exception) {
                message.value = e.toString()
                anApiFailed.value = true
            }
        }
    }
    fun getNurseryLogo() : String{
        return appRepo.getNurseryLogoFromSharedPreference()
    }
}