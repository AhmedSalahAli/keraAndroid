package com.app.kera.app


import appScreensModule
import com.app.kera.attendanceHistory.di.AttendanceModule
import com.app.kera.contactUs.di.contactUsModule
import com.app.kera.dailyReport.di.dailyReportModule
import com.app.kera.dailyReport.di.reportDetailsModule
import com.app.kera.data.network.apiRepoModule
import com.app.kera.education.di.educationModule
import com.app.kera.events.di.eventsModule
import com.app.kera.home.di.homeModule
import com.app.kera.login.di.loginModule
import com.app.kera.meals.di.mealsModule
import com.app.kera.medical.di.medicalModule
import com.app.kera.navigation.di.navigationModule
import com.app.kera.notification.di.notificationModule
import com.app.kera.pincode.pinCodeModule
import com.app.kera.profile.di.profileModule
import com.app.kera.qrCode.di.QRModule
import com.app.kera.registration.di.registrationModule
import com.app.kera.schoolDetails.di.schoolDetailsModule
import com.app.kera.schoolsList.di.schoolsListModule
import com.app.kera.sideMenu.sideMenuModule
import com.app.kera.splash.di.splashModule
import com.app.kera.teacherDailyReport.di.teacherDailyReportModule
import com.app.kera.teacherMedicalReport.di.teacherMedicalReportModule
import com.app.kera.teacherProfile.di.teacherProfileModule
import com.app.kera.visitor.di.visitorModule

var allFeatures = listOf(
    splashModule,
    navigationModule,
    loginModule,
    schoolDetailsModule,
    schoolsListModule,
    pinCodeModule,
    homeModule,
    registrationModule,
    QRModule,
    visitorModule,
    eventsModule,
    notificationModule,
    educationModule,
    AttendanceModule,
    mealsModule,
    profileModule,
    teacherMedicalReportModule,
    contactUsModule,
    teacherProfileModule,
    appScreensModule,
    medicalModule,
    apiRepoModule,
    teacherDailyReportModule,
    dailyReportModule,
    sideMenuModule,
    reportDetailsModule

)
