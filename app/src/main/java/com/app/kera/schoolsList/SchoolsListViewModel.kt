package com.app.kera.schoolsList

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.kera.data.models.schoolList.FavouriteSchoolRequestModel
import com.app.kera.data.network.AppRepo
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch


class SchoolsListViewModel(val appRepo: AppRepo) : ViewModel() {
// test git
    //test git again
    var schoolsList = MutableLiveData<SchoolListUIModel>()
    var message = MutableLiveData<String>()
    var anApiFailed = MutableLiveData<Boolean>()
    fun getSchoolsList(page: Int, latitude: String?, longitude: String?) {
        viewModelScope.launch {
            try {
                val response = appRepo.getSchoolsList(page,latitude,longitude)
                schoolsList.value = SchoolListUIModel.convertResponseModelToUIModel(response.data!!)
            } catch (e: Exception) {
              
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
              
                anApiFailed.value = true
            }
        }
    }
    fun getUserLocation(): LatLng? {
        return appRepo.getUserLocation()
    }
}