package com.example.kera.app


import appScreensModule
import com.example.kera.attendanceHistory.di.AttendanceModule
import com.example.kera.contactUs.di.contactUsModule
import com.example.kera.dailyReport.di.dailyReportModule
import com.example.kera.data.network.apiRepoModule
import com.example.kera.education.di.educationModule
import com.example.kera.events.di.eventsModule
import com.example.kera.home.di.homeModule
import com.example.kera.login.di.loginModule
import com.example.kera.meals.di.mealsModule
import com.example.kera.medical.di.medicalModule
import com.example.kera.navigation.di.navigationModule
import com.example.kera.notification.di.notificationModule
import com.example.kera.pincode.pinCodeModule
import com.example.kera.profile.di.profileModule
import com.example.kera.qrCode.di.QRModule
import com.example.kera.registration.di.registrationModule
import com.example.kera.schoolDetails.di.schoolDetailsModule
import com.example.kera.schoolsList.di.schoolsListModule
import com.example.kera.sideMenu.sideMenuModule
import com.example.kera.splash.di.splashModule
import com.example.kera.teacherDailyReport.di.teacherDailyReportModule
import com.example.kera.teacherMedicalReport.di.teacherMedicalReportModule
import com.example.kera.teacherProfile.di.teacherProfileModule
import com.example.kera.visitor.di.visitorModule

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
    sideMenuModule

)
