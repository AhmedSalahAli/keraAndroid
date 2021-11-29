package com.app.kera.data.network

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
import com.app.kera.preference.SharedPrefKeys.Companion.CHILD_Data
import com.app.kera.preference.SharedPrefKeys.Companion.ISUSERLOGGEDIN
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
import com.app.kera.teacherProfile.TeacherProfileUIModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class AppRepo(val sharedPreference: AppSharedPreference) {

    private var service: ApiService = RetrofitClient(sharedPreference.getString(TOKEN)).getService()

    suspend fun getSchoolsList(page: Int): SchoolsListResponseModel =
        service.getSchoolsList(page, "en", 1)
    suspend fun getUpcomingEventList(page: Int): ClassUpcomingResponseModel =
        service.getUpcomingEventList(page, "en", 1)
    suspend fun getPreviousEventList(page: Int): ClassUpcomingResponseModel =
        service.getPreviousEventList(page, "en", 1)
    suspend fun getSchoolDetails(id: String): SchoolDetailsResponseModel =
        service.getSchoolDetails(id, "en", 1)

    suspend fun getClassMealsDates(classID: String): DatesResponseModel =
        service.getClassMealsDates(classID)

    suspend fun getClassMeals(classID: String, fromDate: String): ClassMealsResponseModel =
        service.getClassMeals(classID, "en", 1, fromDate,fromDate)

    suspend fun getClassMealDetails(classID: String): MealDetailsResponseModel =
        service.getClassMealDetails(classID, "en", 1)
    suspend fun getEventDetails(classID: String): EventDetailsResponseModel =
        service.getEventDetails(classID, "en", 1)

    suspend fun getEducationDates(classID: String): DatesResponseModel =
        service.getClassEducationDates(classID)

    suspend fun getAttendanceDates(): DatesResponseModel =
        service.getClassAttendanceDates( "en", 1)

    suspend fun getEducationList(
        classID: String,
        fromDate: String,
        language: String,
        version: Int
    ): EducationResponseModel =
        service.getClassEducationList(classID, fromDate,fromDate, language, version)
    suspend fun getAttendanceList(
        page: Int,
        fromDate: String,

    ): AttendanceResponseModel =
        service.getAttendanceList(page, "en", 1, fromDate)
    suspend fun getHomeNews(classID: String, pageNumber: Int): NewsResponseModel =
        service.getHomeNews(classID, pageNumber, "en", 1)

    suspend fun postMealComment(commentPostModel: MealCommentPostModel) =
        service.postMealComment(commentPostModel)

    suspend fun getProfileData(language: String, version: Int, type: String) =
        service.getProfileData(language, version, type)
    suspend fun getAppStep(language: String, version: Int, udid: String) =
        service.getAppStep(language, version,udid)

    suspend fun getTeacherProfileData(language: String, version: Int) =
        service.getTeacherProfileData(language, version)

    suspend fun login(language: String, version: Int, loginRequestModel: LoginRequestModel) =
        service.login(language, version, loginRequestModel)

    suspend fun verifyPhoneNumber(
        language: String,
        version: Int,
        verifyPhoneRequestModel: VerifyPhoneRequestModel,
        token: String
    ) =
        service.verifyPhone(language, version, verifyPhoneRequestModel, "Bearer $token")

    suspend fun getClasses(language: String, version: Int) =
        service.getTeacherReportClasses(language, version)

    suspend fun getTeacherStudentsByClassID(language: String, version: Int, classID: String) =
        service.getTeacherStudentsByClassID(language, version, classID)

    suspend fun getTeacherLatestReportsClassID(language: String, version: Int, page: Int) =
        service.getLatestReportsByClassID(language, version, page)

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
        return Gson().fromJson(sharedPreference.getString(TEACHER_DATA), TeacherProfileUIModel::class.java)
    }
    fun getProfileData(): ProfileUIModel {
        return Gson().fromJson(sharedPreference.getString(LOGIN_DATA), ProfileUIModel::class.java)
    }

    fun saveUserLoggedIN() {
        sharedPreference.saveBoolean(ISUSERLOGGEDIN, true)
    }

    fun saveSelectedChildData(childData: StudentsData) {
        sharedPreference.saveString(CHILD_Data, Gson().toJson(childData))
    }

    fun getSelectedChildData(): StudentsData? {
        return Gson().fromJson(sharedPreference.getString(CHILD_Data), StudentsData::class.java)
    }

    fun getChildID(): String {
        return sharedPreference.getString(STUDENTID)!!
    }

    fun getIsUserLoggedIn(): Boolean {
        return sharedPreference.getBoolean(ISUSERLOGGEDIN)
    }

    fun getUserTypeFromSharedPref(): String {
        return sharedPreference.getString(USERTYPE)!!
    }

    suspend fun favouriteSchool(language: String, requestModel: FavouriteSchoolRequestModel) =
        service.favouriteSchool(language, 1, requestModel)


    fun getLoginResponseModelFromSharedPref(): ProfileResponseModel? {
        return Gson().fromJson(
            sharedPreference.getString(LOGIN_DATA),
            object : TypeToken<ProfileResponseModel>() {}.type
        )
    }

    suspend fun getTeacherDailyReportData(reportID: String) =
        service.getTeacherDailyReportData("en", 1, reportID)
    suspend fun getTeacherMedicalReportData(reportID: String) =
        service.getTeacherMedicalReportData("en", 1, reportID)
    suspend fun updateDailyReportQuestion(updateQuestionRequestModel: UpdateQuestionRequestModel) =
        service.updateDailyReportQuestion("en", 1, updateQuestionRequestModel)
    suspend fun updateMedicalReportQuestion(updateQuestionRequestModel: UpdateMedicalRequestModel) =
        service.updateMedicalReportQuestion("en", 1, updateQuestionRequestModel)

    suspend fun createDailyReport(requestModel: CreateReportRequestModel) =
        service.createDailyReport("en", 1, requestModel)
    suspend fun createMedicalReport(requestModel: CreateReportRequestModel) =
        service.createMedicalReport("en", 1, requestModel)
    suspend fun getLatestReports(page: Int) = service.getLatestReports("en", 1, page)

    suspend fun getLatestMedicalReports(page: Int) = service.getLatestMedicalReports("en", 1, page)
    suspend fun getNotifications(page: Int) = service.getNotifications("en", 1, page)

    suspend fun publishReport(requestModel: PublishReportRequestModel) =
        service.publishReport("en", 1, requestModel)
    suspend fun publishMedicalReport(requestModel: PublishReportRequestModel) =
        service.publishMedicalReport("en", 1, requestModel)
    suspend fun publishReplay(requestModel: PublishReplay) =
        service.publishReplay("en", 1, requestModel)
    suspend fun publishApp1(requestModel: PublishAppStep1Model) =
        service.publishApp1("en", 1, requestModel)
    suspend fun publishApp2(requestModel: PublishAppStep2Model) =
        service.publishApp2("en", 1, requestModel)
    suspend fun publishApp3(requestModel: PublishAppStep3) =
        service.publishApp3("en", 1, requestModel)
    suspend fun publishApp4(requestModel: SumbitFinalForm) =
        service.publishApp4("en", 1, requestModel)
    suspend fun getDailyReportData(studentID: String, fromDate: String, toDate: String,page:Int) =
        service.getDailyReport("en", 1, studentID,page, fromDate, toDate)
    suspend fun getMedicalReportData(studentID: String, fromDate: String, toDate: String,page:Int) =
        service.getMedicalReport("en", 1, studentID,page, fromDate, toDate)
    suspend fun getAssociationTerms(associationId:String) =
        service.getAssociationTerms("en", 1,associationId)
    suspend fun getHomeNurseryData() =
        service.getHomeNurseryData("en", 1)
    suspend fun publishAttendanceQrCode(requestModel: QrCodeModel) =
        service.publishAttendanceQrCode("en", 1, requestModel)
    suspend fun getMapData() = service.getMapData("en", 1)


    fun logOut() {
        val editor = sharedPreference.logout()
        editor.clear()
        editor.apply()
    }

    fun saveFcmData(fcmModelRequest: FcmModelRequest) {
        sharedPreference.saveString(SharedPrefKeys.FCM_DATA, Gson().toJson(fcmModelRequest))
    }

    fun saveNurseryLogoToSharedPreference(logo: String?) {
        sharedPreference.saveString(NURSERY_LOGO, logo!!)
    }

    fun getNurseryLogoFromSharedPreference(): String {
        return sharedPreference.getString(NURSERY_LOGO)!!
    }
}