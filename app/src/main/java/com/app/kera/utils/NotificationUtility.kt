package com.app.kera.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.app.kera.R
import com.app.kera.dailyReport.ui.DailyReportActivity
import com.app.kera.dailyReport.ui.ReportDetails
import com.app.kera.data.models.NotificationModel
import com.app.kera.main.ui.MainActivity
import com.app.kera.medical.MedicalReportActivity
import com.app.kera.splash.ui.SplashActivity
import com.app.kera.teacherDailyReport.ui.TeacherDailyReportActivity
import com.app.kera.teacherDailyReport.writeReport.WriteReportActivity
import com.app.kera.teacherMedicalReport.TeacherMedicalReportActivity
import com.app.kera.teacherMedicalReport.writeMedicalReport.WriteMedicalReportActivity

object NotificationUtility {


    // Notification ID.
    private const val NOTIFICATION_ID = 0
    private val channelId = "i.kera.notifications"
    private val description = "kera notification"
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    fun NotificationManager.sendNotification(notificationModel: NotificationModel, applicationContext: Context) {


        // TODO: Step 1.11 create intent
        var contentPendingIntent: PendingIntent? = null
        val requestID = System.currentTimeMillis().toInt()


        if (notificationModel.userType.equals("user")){
            if (notificationModel.type.equals("daily")){

                val intent: Intent = Intent(applicationContext, MainActivity::class.java)
                val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(applicationContext)

                val intentReport: Intent = Intent(
                    applicationContext,
                    DailyReportActivity::class.java
                )
                stackBuilder.addParentStack(MainActivity::class.java)
                stackBuilder.addNextIntent(intent)
                stackBuilder.addNextIntent(intentReport)
                contentPendingIntent = stackBuilder.getPendingIntent(requestID, PendingIntent.FLAG_UPDATE_CURRENT)
            }else if (notificationModel.type.equals("medical")){


                val intent: Intent = Intent(applicationContext, MainActivity::class.java)
                val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(applicationContext)

                val intentReport: Intent = Intent(
                    applicationContext,
                    MedicalReportActivity::class.java
                )
                stackBuilder.addParentStack(MainActivity::class.java)
                stackBuilder.addNextIntent(intent)
                stackBuilder.addNextIntent(intentReport)
                contentPendingIntent = stackBuilder.getPendingIntent(requestID, PendingIntent.FLAG_UPDATE_CURRENT)

            }else{

                val intent: Intent = Intent(applicationContext, MainActivity::class.java)
                val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(applicationContext)

                val intentReport: Intent = Intent(
                    applicationContext,
                    MainActivity::class.java
                )

                stackBuilder.addNextIntent(intentReport)
                contentPendingIntent = stackBuilder.getPendingIntent(requestID, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }else  if (notificationModel.userType.equals("teacher")){
            if (notificationModel.type.equals("daily")){

                val intent: Intent = Intent(applicationContext, MainActivity::class.java)
                val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(applicationContext)


                val intentReports: Intent = Intent(
                    applicationContext,
                    TeacherDailyReportActivity::class.java
                )
                val intentReportDetails: Intent = Intent(
                    applicationContext,
                    WriteReportActivity::class.java
                )
                intentReportDetails.putExtra("reportID", notificationModel.reportId)

                stackBuilder.addParentStack(MainActivity::class.java)
                stackBuilder.addNextIntent(intent)
                stackBuilder.addNextIntent(intentReports)
                stackBuilder.addNextIntent(intentReportDetails)
                contentPendingIntent = stackBuilder.getPendingIntent(requestID, PendingIntent.FLAG_UPDATE_CURRENT)
            }else if (notificationModel.type.equals("medical")){


                val intent: Intent = Intent(applicationContext, MainActivity::class.java)
                val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(applicationContext)


                val intentReports: Intent = Intent(
                    applicationContext,
                    TeacherMedicalReportActivity::class.java
                )
                val intentReportDetails: Intent = Intent(
                    applicationContext,
                    WriteMedicalReportActivity::class.java
                )
                stackBuilder.addParentStack(MainActivity::class.java)
                stackBuilder.addNextIntent(intent)
                stackBuilder.addNextIntent(intentReports)
                stackBuilder.addNextIntent(intentReportDetails)
                contentPendingIntent = stackBuilder.getPendingIntent(requestID, PendingIntent.FLAG_UPDATE_CURRENT)

            }else{

                val intent: Intent = Intent(applicationContext, MainActivity::class.java)
                val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(applicationContext)

                val intentReport: Intent = Intent(
                    applicationContext,
                    MainActivity::class.java
                )

                stackBuilder.addNextIntent(intentReport)
                contentPendingIntent = stackBuilder.getPendingIntent(requestID, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }






        // TODO: You can add style here


        // TODO: Step 1.2 get an instance of NotificationCompat.Builder


         notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
        var builder: NotificationCompat.Builder? = null
        // checking if android version is greater than oreo(API 26) or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.LTGRAY
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)
            builder = NotificationCompat.Builder(
                applicationContext,
                // TODO: Step 1.8 use a notification channel
                channelId

            )
                // TODO: Step 1.3 set title, text and icon to builder
                .setSmallIcon(R.drawable.kera)
                .setContentTitle(notificationModel.title)
                .setContentText(notificationModel.body)
                // TODO: Step 1.13 set content intent
                .setContentIntent(contentPendingIntent)

                // TODO: Step 2.5 set priority
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
        } else {

            builder = NotificationCompat.Builder(
                applicationContext

            )
                // TODO: Step 1.3 set title, text and icon to builder
                .setSmallIcon(R.drawable.kera)
                .setContentTitle(notificationModel.title)
                .setContentText(notificationModel.body)
                // TODO: Step 1.13 set content intent
                .setContentIntent(contentPendingIntent)

                // TODO: Step 2.5 set priority
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
        }

        // Build the notification


        // TODO Step 1.4 call notify
        // Deliver the notification
        if (builder!=null){
            notify(NOTIFICATION_ID, builder.build())

        }
    }

// TODO: Step 1.14 Cancel all notifications
    /**
     * Cancels all notifications.
     *
     */
    fun NotificationManager.cancelNotifications() {
        cancelAll()
    }
}