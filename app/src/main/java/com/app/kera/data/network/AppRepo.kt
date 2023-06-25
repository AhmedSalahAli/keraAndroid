package com.app.kera.data.network

import android.content.Context
import android.location.Location
import com.app.kera.attendanceHistory.model.AttendanceResponseModel
import com.app.kera.dailyReport.model.PublishReplay
import com.app.kera.data.models.*
import com.app.kera.data.models.meals.ClassMealsResponseModel
import com.app.kera.data.models.meals.DatesResponseModel
import com.app.kera.data.models.meals.MealDetailsResponseModel
import com.app.kera.data.models.schoolList.FavouriteSchoolRequestModel
import com.app.kera.data.models.schoolList.SchoolDetailsResponseModel
import com.app.kera.data.models.schoolList.SchoolsListResponseModel
import com.app.kera.data.models.teacherMedicalReport.UpdateMedicalRequestModel
import com.app.kera.events.model.ClassUpcomingResponseModel
import com.app.kera.events.model.EventDetailsResponseModel
import com.app.kera.meals.details.MealCommentPostModel
import com.app.kera.preference.AppSharedPreference
import com.app.kera.preference.SharedPrefKeys
import com.app.kera.preference.SharedPrefKeys.Companion.APP_LANG
import com.app.kera.preference.SharedPrefKeys.Companion.CHILD_Data
import com.app.kera.preference.SharedPrefKeys.Companion.ISUSERLOGGEDIN
import com.app.kera.preference.SharedPrefKeys.Companion.LOCATION
import com.app.kera.preference.SharedPrefKeys.Companion.LOGIN_DATA
import com.app.kera.preference.SharedPrefKeys.Companion.NURSERY_LOGO
import com.app.kera.preference.SharedPrefKeys.Companion.STUDENTID
import com.app.kera.preference.SharedPrefKeys.Companion.TEACHER_DATA
import com.app.kera.preference.SharedPrefKeys.Companion.TOKEN
import com.app.kera.preference.SharedPrefKeys.Companion.USERTYPE
import com.app.kera.profile.ProfileUIModel
import com.app.kera.profile.StudentsData
import com.app.kera.qrCode.QrCodeModel
import com.app.kera.registrationForm.screen1.model.PublishAppStep1Model
import com.app.kera.registrationForm.screen1.model.PublishAppStep2Model
import com.app.kera.registrationForm.screen3.model.PublishAppStep3
import com.app.kera.registrationForm.screen4.model.SumbitFinalForm
import com.app.kera.teacherDailyReport.model.CreateReportRequestModel
import com.app.kera.teacherDailyReport.model.PublishReportRequestModel
import com.app.kera.teacherDailyReport.model.UpdateQuestionRequestModel
import com.app.kera.teacherMedicalReport.model.ImageRequest
import com.app.kera.teacherProfile.TeacherProfileUIModel
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.coroutines.coroutineContext


class AppRepo(val sharedPreference: AppSharedPreference,val context:Context) {

    private var service: ApiService = RetrofitClient(sharedPreference.getString(TOKEN,""),
        getLang().toString(), context
    ).getService()

    suspend fun getSchoolsList(page: Int,lat:String?,lon:String?): SchoolsListResponseModel =
        service.getSchoolsList(page,lat,lon,  1)
    suspend fun getUpcomingEventList(page: Int): ClassUpcomingResponseModel =
        service.getUpcomingEventList(page,  1)
    suspend fun getPreviousEventList(page: Int): ClassUpcomingResponseModel =
        service.getPreviousEventList(page,  1)
    suspend fun getSchoolDetails(id: String): SchoolDetailsResponseModel =
        service.getSchoolDetails(id,  1)

    suspend fun getClassMealsDates(classID: String): DatesResponseModel =
        service.getClassMealsDates(classID)

    suspend fun getClassMeals(classID: String, fromDate: String): ClassMealsResponseModel =
        service.getClassMeals(classID,  1, fromDate,fromDate)

    suspend fun getClassMealDetails(classID: String): MealDetailsResponseModel =
        service.getClassMealDetails(classID,  1)
    suspend fun getEventDetails(classID: String): EventDetailsResponseModel =
        service.getEventDetails(classID,  1)

    suspend fun getEducationDates(classID: String): DatesResponseModel =
        service.getClassEducationDates(classID)

    suspend fun getAttendanceDates(): DatesResponseModel =
        service.getClassAttendanceDates(  1)

    suspend fun getEducationList(
        classID: String,
        fromDate: String,
        language: String,
        version: Int
    ): EducationResponseModel =
        service.getClassEducationList(classID, fromDate,fromDate, version)
    suspend fun getAttendanceList(
        page: Int,
        fromDate: String,

    ): AttendanceResponseModel =
        service.getAttendanceList(page,  1, fromDate,fromDate)
    suspend fun getHomeNews(classID: String, pageNumber: Int): NewsResponseModel =
        service.getHomeNews(classID, pageNumber,  1)

    suspend fun postMealComment(commentPostModel: MealCommentPostModel) =
        service.postMealComment(commentPostModel)

    suspend fun getProfileData(language: String, version: Int, type: String) =
        service.getProfileData( version, type)
    suspend fun getAppStep(language: String, version: Int, udid: String) =
        service.getAppStep( version,udid)

    suspend fun getTeacherProfileData(language: String, version: Int) =
        service.getTeacherProfileData( version)

    suspend fun login(language: String, version: Int, loginRequestModel: LoginRequestModel) =
        service.login( version, loginRequestModel)

    suspend fun verifyPhoneNumber(
        language: String,
        version: Int,
        verifyPhoneRequestModel: VerifyPhoneRequestModel,
        token: String
    ) =
        service.verifyPhone( version, verifyPhoneRequestModel, "Bearer $token")

    suspend fun getClasses(language: String, version: Int) =
        service.getTeacherReportClasses( version)

    suspend fun getTeacherStudentsByClassID(language: String, version: Int, classID: String) =
        service.getTeacherStudentsByClassID( version, classID)

    suspend fun getTeacherLatestReportsClassID(language: String, version: Int, page: Int) =
        service.getLatestReportsByClassID( version, page)

    fun saveTokenInSharedPref(token: String) {
        sharedPreference.saveString(TOKEN, token)
    }

    fun saveUserTypeInSharedPref(type: String) {
        sharedPreference.saveString(USERTYPE, type)
    }

    fun saveProfileResponse(profileResponse: ProfileUIModel) {
        sharedPreference.saveString(LOGIN_DATA, Gson().toJson(profileResponse))
    }
    fun saveTeacherResponse(teacherProfileUIModel: TeacherProfileUIModel) {
        sharedPreference.saveString(TEACHER_DATA, Gson().toJson(teacherProfileUIModel))
    }
    fun getTeacherData(): TeacherProfileUIModel {
        return Gson().fromJson(sharedPreference.getString(TEACHER_DATA,""), TeacherProfileUIModel::class.java)
    }
    fun getProfileData(): ProfileUIModel {
        return Gson().fromJson(sharedPreference.getString(LOGIN_DATA,""), ProfileUIModel::class.java)
    }

    fun saveUserLoggedIN() {
        sharedPreference.saveBoolean(ISUSERLOGGEDIN, true)
    }

    fun saveSelectedChildData(childData: StudentsData) {
        sharedPreference.saveString(CHILD_Data, Gson().toJson(childData))
    }

    fun getSelectedChildData(): StudentsData? {
        return Gson().fromJson(sharedPreference.getString(CHILD_Data,""), StudentsData::class.java)
    }

    fun getChildID(): String {
        return sharedPreference.getString(STUDENTID,"")!!
    }

    fun getIsUserLoggedIn(): Boolean {
        return sharedPreference.getBoolean(ISUSERLOGGEDIN)
    }

    fun getUserTypeFromSharedPref(): String {
        return sharedPreference.getString(USERTYPE,"")!!
    }

    suspend fun favouriteSchool(language: String, requestModel: FavouriteSchoolRequestModel) =
        service.favouriteSchool( 1, requestModel)


    fun getLoginResponseModelFromSharedPref(): ProfileResponseModel? {
        return Gson().fromJson(
            sharedPreference.getString(LOGIN_DATA,""),
            object : TypeToken<ProfileResponseModel>() {}.type
        )
    }

    suspend fun getTeacherDailyReportData(reportID: String) =
        service.getTeacherDailyReportData( 1, reportID)
    suspend fun getTeacherMedicalReportData(reportID: String) =
        service.getTeacherMedicalReportData( 1, reportID)
    suspend fun updateDailyReportQuestion(updateQuestionRequestModel: UpdateQuestionRequestModel) =
        service.updateDailyReportQuestion( 1, updateQuestionRequestModel)
    suspend fun updateMedicalReportQuestion(updateQuestionRequestModel: UpdateMedicalRequestModel) =
        service.updateMedicalReportQuestion( 1, updateQuestionRequestModel)

    suspend fun createDailyReport(requestModel: CreateReportRequestModel) =
        service.createDailyReport( 1, requestModel)
    suspend fun createMedicalReport(requestModel: CreateReportRequestModel) =
        service.createMedicalReport( 1, requestModel)


    suspend fun uploadImage(requestModel: ImageRequest) =
        service.uploadImage( 1, requestModel)

    suspend fun getLatestReports(page: Int) = service.getLatestReports( 1, page)

    suspend fun getLatestMedicalReports(page: Int) = service.getLatestMedicalReports( 1, page)
    suspend fun getNotifications(page: Int) = service.getNotifications( 1, page)

    suspend fun getTeacherNotifications(page: Int) = service.getTeacherNotifications( 1, page)


    suspend fun publishReport(requestModel: PublishReportRequestModel) =
        service.publishReport( 1, requestModel)
    suspend fun publishMedicalReport(requestModel: PublishReportRequestModel) =
        service.publishMedicalReport( 1, requestModel)
    suspend fun publishReplay(requestModel: PublishReplay) =
        service.publishReplay( 1, requestModel)
    suspend fun publishApp1(requestModel: PublishAppStep1Model) =
        service.publishApp1( 1, requestModel)
    suspend fun publishApp2(requestModel: PublishAppStep2Model) =
        service.publishApp2( 1, requestModel)
    suspend fun publishApp3(requestModel: PublishAppStep3) =
        service.publishApp3( 1, requestModel)
    suspend fun publishApp4(requestModel: SumbitFinalForm) =
        service.publishApp4( 1, requestModel)
    suspend fun getDailyReportData(studentID: String, fromDate: String, toDate: String,page:Int) =
        service.getDailyReport( 1, studentID,page, fromDate, toDate)
    suspend fun getMedicalReportData(studentID: String, fromDate: String, toDate: String,page:Int) =
        service.getMedicalReport( 1, studentID,page, fromDate, toDate)
    suspend fun getAssociationTerms(associationId:String) =
        service.getAssociationTerms( 1,associationId)
    suspend fun getHomeNurseryData() =
        service.getHomeNurseryData( 1)
    suspend fun publishAttendanceQrCode(requestModel: QrCodeModel) =
        service.publishAttendanceQrCode( 1, requestModel)
    suspend fun getMapData(lat:String?,lon:String?) = service.getMapData( 1,lat,lon)


    fun logOut() {
        val editor = sharedPreference.logout()
        editor.clear()
        editor.apply()
    }

    fun saveFcmData(fcmModelRequest: FcmModelRequest) {
        sharedPreference.saveString(SharedPrefKeys.FCM_DATA, Gson().toJson(fcmModelRequest))
    }
    fun saveLang(lang: String?) {
        lang?.let { sharedPreference.saveString(APP_LANG, it) }
    }
    fun getLang(): String? {
      return  sharedPreference!!.getString(APP_LANG, "en")
    }
    fun saveNurseryLogoToSharedPreference(logo: String?) {
        sharedPreference.saveString(NURSERY_LOGO, logo!!)
    }
    fun saveUserLocation(location :LatLng?) {
        sharedPreference.saveString(LOCATION, Gson().toJson(location))
    }
    fun getUserLocation(): LatLng? {
        if (sharedPreference.getString(LOCATION,"")!=null){
            return Gson().fromJson(sharedPreference.getString(LOCATION,""), LatLng::class.java)

        }else{
            return null
        }
    }
    fun getNurseryLogoFromSharedPreference(): String {
        return sharedPreference.getString(NURSERY_LOGO,"")!!
    }
}