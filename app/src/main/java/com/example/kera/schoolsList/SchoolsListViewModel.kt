package com.example.kera.schoolsList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kera.data.models.schoolList.FavouriteSchoolRequestModel
import com.example.kera.data.network.AppRepo
import kotlinx.coroutines.launch


class SchoolsListViewModel(val appRepo: AppRepo) : ViewModel() {

    var schoolsList = MutableLiveData<SchoolListUIModel>()
    var message = MutableLiveData<String>()
    var anApiFailed = MutableLiveData<Boolean>()
    fun getSchoolsList(page: Int) {
        viewModelScope.launch {
            try {
                val response = appRepo.getSchoolsList(page)
                schoolsList.value = SchoolListUIModel.convertResponseModelToUIModel(response.data!!)
            } catch (e: Exception) {
                message.value = e.toString()
                anApiFailed.value = true
            }
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
}