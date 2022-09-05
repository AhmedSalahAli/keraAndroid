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
import com.app.kera.data.models.teacherDailyReport.*
import com.app.kera.data.models.teacherMedicalReport.MedicalReportResponseModel
import com.app.kera.data.models.teacherMedicalReport.UpdateMedicalRequestModel
import com.app.kera.events.model.ClassUpcomingResponseModel
import com.app.kera.events.model.EventDetailsResponseModel
import com.app.kera.meals.details.MealCommentPostModel
import com.app.kera.medical.model.DisplayMedicalReportResponseModel
import com.app.kera.notification.model.NotificationsResponseModel
import com.app.kera.qrCode.QrCodeModel
import com.app.kera.registration.screen1.CheckAppStep
import com.app.kera.registrationForm.screen1.model.PublishAppStep1Model
import com.app.kera.registrationForm.screen1.model.PublishAppStep2Model
import com.app.kera.registrationForm.screen3.model.PublishAppStep3
import com.app.kera.registrationForm.screen4.model.AccocialtionTermsModel
import com.app.kera.registrationForm.screen4.model.SumbitFinalForm
import com.app.kera.teacherDailyReport.model.CreateReportRequestModel
import com.app.kera.teacherDailyReport.model.PublishReportRequestModel
import com.app.kera.teacherDailyReport.model.UpdateQuestionRequestModel
import com.app.kera.teacherMedicalReport.model.ImageRequest
import retrofit2.http.*

interface ApiService {

    @GET("portal/{page}")
    suspend fun getSchoolsList(
        @Path("page") page: Int,
        @Header("v") version: Int
    ): SchoolsListResponseModel
    @GET("portal/events/upcoming/{page}")
    suspend fun getUpcomingEventList(
        @Path("page") page: Int,
        @Header("v") version: Int
    ): ClassUpcomingResponseModel
    @GET("portal/events/previous/{page}")
    suspend fun getPreviousEventList(
        @Path("page") page: Int,
        @Header("v") version: Int
    ): ClassUpcomingResponseModel

    @GET("portal/single/{id}")
    suspend fun getSchoolDetails(
        @Path("id") id: String,
        @Header("v") version: Int
    ): SchoolDetailsResponseModel

    @GET("class/meals/dates/{classID}")
    suspend fun getClassMealsDates(@Path("classID") id: String): DatesResponseModel

    @GET("portal/meals/{classID}")
    suspend fun getClassMeals(
        @Path("classID") id: String,
       
        @Header("v") version: Int,
        @Query("fromDate", encoded = true) fromDate: String,
        @Query("toDate", encoded = true) toDate: String
    ): ClassMealsResponseModel

    @GET("portal/meals/details/{classID}")
    suspend fun getClassMealDetails(
        @Path("classID") id: String,
       
        @Header("v") version: Int
    ): MealDetailsResponseModel
    @GET("portal/events/{eventID}")
    suspend fun getEventDetails(
        @Path("eventID") id: String,
       
        @Header("v") version: Int
    ): EventDetailsResponseModel

    @GET("class/dates/{classID}")
    suspend fun getClassEducationDates(@Path("classID") id: String): DatesResponseModel

    @GET("general/attendance-dates")
    suspend fun getClassAttendanceDates(
       
        @Header("v") version: Int): DatesResponseModel

    @GET("portal/education/{classID}")
    suspend fun getClassEducationList(
        @Path("classID") id: String,
        @Query("fromDate", encoded = true) fromDate: String,
        @Query("toDate", encoded = true) toDate: String,
       
        @Header("v") version: Int
    ): EducationResponseModel


    @GET("portal/latest/news/{classID}/{page}")
    suspend fun getHomeNews(
        @Path("classID") id: String, @Path("page") page: Int,
        @Header("v") version: Int
    ): NewsResponseModel
    @GET("general/attendance/{page}")
    suspend fun getAttendanceList(
         @Path("page") page: Int,
        
        @Header("v") version: Int,
        @Query("fromDate", encoded = true) fromDate: String
    ): AttendanceResponseModel
    @PUT("portal/meals/comment")
    suspend fun postMealComment(@Body mealCommentModel: MealCommentPostModel): GeneralResponse

    @GET("general/check-application-step")
    suspend fun getAppStep(
       
        @Header("v") version: Int,
        @Header("udid") udid: String,
    ): CheckAppStep
    @GET("{type}/profile")
    suspend fun getProfileData(
       
        @Header("v") version: Int,
        @Path("type") type: String,

    ): ProfileResponseModel
    @GET("teacher/profile")
    suspend fun getTeacherProfileData(
       
        @Header("v") version: Int,
    ): TeacherProfileResponseModel

    @POST("user/login")
    suspend fun login(
       
        @Header("v") version: Int,
        @Body loginRequestModel: LoginRequestModel
    ): LoginResponseModel

    @POST("user/verifiedphone")
    suspend fun verifyPhone(
       
        @Header("v") version: Int,
        @Body verifyPhoneRequestModel: VerifyPhoneRequestModel,
        @Header("Authorization") token: String
    ): LoginResponseModel

    @GET("teacher/classes")
    suspend fun getTeacherReportClasses(
       
        @Header("v") version: Int,
    ): DailyReportClassesResponseModel

    @GET("teacher/class/{classID}/students")
    suspend fun getTeacherStudentsByClassID(
       
        @Header("v") version: Int,
        @Path("classID") classID: String,
    ): TeacherReportStudentsResponseModel

    @GET("teacher/latest/reports/{page}")
    suspend fun getLatestReportsByClassID(
       
        @Header("v") version: Int,
        @Path("page") page: Int,
    ): TeacherReportStudentsResponseModel

    @PUT("portal/favorite")
    suspend fun favouriteSchool(
       
        @Header("v") version: Int,
        @Body requestModel: FavouriteSchoolRequestModel
    ): GeneralResponse

    @GET("teacher/get/report/{reportID}")
    suspend fun getTeacherDailyReportData(
       
        @Header("v") version: Int,
        @Path("reportID") reportID: String,
    ): DailyReportResponseModel
    @GET("teacher/get/mediacl-report/{reportID}")
    suspend fun getTeacherMedicalReportData(
       
        @Header("v") version: Int,
        @Path("reportID") reportID: String,
    ): MedicalReportResponseModel

    @PUT("teacher/update/question/report")
    suspend fun updateDailyReportQuestion(
       
        @Header("v") version: Int,
        @Body requestModel: UpdateQuestionRequestModel
    ): GeneralResponse
    @PUT("teacher/update/mediacl-report")
    suspend fun updateMedicalReportQuestion(
       
        @Header("v") version: Int,
        @Body requestModel: UpdateMedicalRequestModel
    ): GeneralResponse

    @POST("teacher/create/report")
    suspend fun createDailyReport(
       
        @Header("v") version: Int,
        @Body requestModel: CreateReportRequestModel
    ): CreateReportResponseModel
    @POST("teacher/create/mediacl-report")
    suspend fun createMedicalReport(
       
        @Header("v") version: Int,
        @Body requestModel: CreateReportRequestModel
    ): CreateReportResponseModel


    @PUT("teacher/update/mediacl-report/image")
    suspend fun uploadImage(

        @Header("v") version: Int,
        @Body requestModel: ImageRequest,
    ): GeneralResponse


    @GET("teacher/latest/reports/{page}")
    suspend fun getLatestReports(
       
        @Header("v") version: Int,
        @Path("page") page: Int
    ): LatestReportsResponseModel

    @GET("teacher/latest/mediacl-report/{page}")
    suspend fun getLatestMedicalReports(
       
        @Header("v") version: Int,
        @Path("page") page: Int
    ): LatestReportsResponseModel

    @GET("user/notifications/{page}")
    suspend fun getNotifications(
       
        @Header("v") version: Int,
        @Path("page") page: Int
    ): NotificationsResponseModel

    @GET("teacher/notifications/{page}")
    suspend fun getTeacherNotifications(

        @Header("v") version: Int,
        @Path("page") page: Int
    ): NotificationsResponseModel

    @PUT("teacher/publish/report")
    suspend fun publishReport(
       
        @Header("v") version: Int,
        @Body requestModel: PublishReportRequestModel,
    ): GeneralResponse
    @PUT("teacher/publish/medical-report")
    suspend fun publishMedicalReport(
       
        @Header("v") version: Int,
        @Body requestModel: PublishReportRequestModel,
    ): GeneralResponse
    @PUT("user/report-reply")
    suspend fun publishReplay(
       
        @Header("v") version: Int,
        @Body publishReplay: PublishReplay
    ): GeneralResponse
    @POST("general/application/step-1")
    suspend fun publishApp1(
       
        @Header("v") version: Int,
        @Body AppStep1RequestModel: PublishAppStep1Model
    ): GeneralResponse
    @POST("general/qrcode")
    suspend fun publishAttendanceQrCode(
       
        @Header("v") version: Int,
        @Body qrCodeModel: QrCodeModel
    ): GeneralResponse
    @PUT("general/application/step-2")
    suspend fun publishApp2(
       
        @Header("v") version: Int,
        @Body AppStep1RequestModel: PublishAppStep2Model
    ): GeneralResponse
    @PUT("general/application/step-3")
    suspend fun publishApp3(
       
        @Header("v") version: Int,
        @Body AppStep1RequestModel: PublishAppStep3
    ): GeneralResponse
    @PUT("general/application/step-4")
    suspend fun publishApp4(
       
        @Header("v") version: Int,
        @Body AppStep1RequestModel: SumbitFinalForm
    ): GeneralResponse
    @GET("user/dailyreports/{studentID}/{page}}")
    suspend fun getDailyReport(
       
        @Header("v") version: Int,
        @Path("studentID") reportID: String,
        @Path("page") page: Int,
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String,

    ): DisplayDailyReportResponseModel
    @GET("user/medicalreports/{studentID}/{page}}")
    suspend fun getMedicalReport(
       
        @Header("v") version: Int,
        @Path("studentID") reportID: String,
        @Path("page") page: Int,
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String,

        ): DisplayMedicalReportResponseModel
    @GET("general/setting/{associationId}/terms")
    suspend fun getAssociationTerms(
       
        @Header("v") version: Int,
        @Path("associationId") associationId: String,
    ): AccocialtionTermsModel

    @GET("user/portal")
    suspend fun getHomeNurseryData(
       
        @Header("v") version: Int,
    ): HomeImagesResponseModel

    @GET("portal/map")
    suspend fun getMapData(
       
        @Header("v") version: Int,
    ): MapListResponseModel
}