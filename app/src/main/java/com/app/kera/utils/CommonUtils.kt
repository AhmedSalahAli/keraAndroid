package com.app.kera.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.app.kera.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object CommonUtils {
    val DATE_PICKER_DAY_FORMAT = SimpleDateFormat("EEE d", Locale.ENGLISH)
    val DATE_PICKER_YYYY_MM_DD_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    val DATE_PICKER_EEE_MM_YYY_FORMAT = SimpleDateFormat("EEEE,MMM dd,yyy", Locale.ENGLISH)
    val DATE_PICKER_MMMM_YYYY_FORMAT = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    val DATE_PICKER_24H_FORMAT =
        SimpleDateFormat("HH:mm", Locale.ENGLISH)
    val DATE_PICKER_HH_MM_A_FORMAT = SimpleDateFormat("hh:mm a", Locale.ENGLISH)

    @SuppressLint("all")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
    fun setLocale(activity: Context, languageCode: String?) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = activity.resources
        val config: Configuration = resources.getConfiguration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.getDisplayMetrics())
    }
    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun showLoadingDialog(context: Context, @LayoutRes resId: Int): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.show()

        if (progressDialog.window != null) {
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        progressDialog.apply {
            setContentView(resId)
            isIndeterminate = true
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
        return progressDialog
    }

    fun hideLoading(mProgressDialog: ProgressDialog) {
        if (mProgressDialog.isShowing) {
            mProgressDialog.cancel()
            mProgressDialog.hide()
        }
    }
    fun loadImage(view: ImageView, url: String?) {

        val requestOptions = RequestOptions()
        val circularProgressDrawable = CircularProgressDrawable(view.context)
        circularProgressDrawable.setColorSchemeColors(R.color.purple_500, R.color.purple_200, R.color.purple_700);

        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f

        circularProgressDrawable.start()
        Log.e("image","https://res.cloudinary.com/keraapp/image/upload/"+url?.replace("https://res.cloudinary.com/keraapp/image/upload/",""))
        Glide.with(view.context)
            .load("https://res.cloudinary.com/keraapp/image/upload/"+url?.replace("https://res.cloudinary.com/keraapp/image/upload/",""))
            .apply(requestOptions)
           // .error(R.drawable.image_circle_backgroud)
            .placeholder(circularProgressDrawable)
            .into(view)
    }
    fun loadThump(view: ImageView, url: String?) {

        val requestOptions = RequestOptions()
        val circularProgressDrawable = CircularProgressDrawable(view.context)
        circularProgressDrawable.setColorSchemeColors(R.color.purple_500, R.color.purple_200, R.color.purple_700);

        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f

        circularProgressDrawable.start()
        Log.e("image","https://res.cloudinary.com/keraapp/image/upload/"+url?.replace("https://res.cloudinary.com/keraapp/image/upload/",""))
        Glide.with(view.context)
            .load("https://res.cloudinary.com/keraapp/image/upload/"+url?.replace("https://res.cloudinary.com/keraapp/image/upload/",""))
            .apply(requestOptions)
            .fitCenter()
            // .error(R.drawable.image_circle_backgroud)
            .placeholder(circularProgressDrawable)
            .into(view)
    }
    fun createImageFolder(context: Context): File {
        val storageDir = File(context.filesDir, "Photo")
        if (!storageDir.exists())
            storageDir.mkdirs()
        return storageDir
    }
    fun pxToDp(px: Int, context: Context): Int {
        val displayMetrics: DisplayMetrics = context.getResources().getDisplayMetrics()
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }
    fun shareImage(uri: Uri, context: Context, text: String?) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        // sharingIntent.type = "image/*"
        sharingIntent.type = "*/*"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, text)
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri)
        context.startActivity(Intent.createChooser(sharingIntent, "Share Image Using"))
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            UUID.randomUUID().toString() + ".png",
            "drawing"
        )
        return Uri.parse(path)
    }

    fun loadJSONFromAsset(
        context: Context,
        jsonFileName: String
    ): String {
        val manager = context.assets
        val `is`: InputStream = manager.open(jsonFileName)
        val size: Int = `is`.available()
        val buffer = ByteArray(size)
        `is`.read(buffer)
        `is`.close()
        return String(buffer)
    }

    fun getAge(year: Int, month: Int, day: Int): Int {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob[year, month] = day
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }
        val ageInt = age
        return ageInt
    }

    fun getMonthAndYearFrom_yyyy_mm_dd(date: String): String? {
        return DATE_PICKER_MMMM_YYYY_FORMAT.format(DATE_PICKER_YYYY_MM_DD_FORMAT.parse(date)!!)
    }

    fun getDAY_FORMATFrom_yyyy_mm_dd(date: String): String? {
        return DATE_PICKER_DAY_FORMAT.format(DATE_PICKER_YYYY_MM_DD_FORMAT.parse(date)!!)
    }

    fun getCurrentDate(): String {
        val date = Calendar.getInstance().time
        return DATE_PICKER_YYYY_MM_DD_FORMAT.format(date)
    }

    fun getCurrentDate_EEE_MM_YYY(): String {
        val date = Calendar.getInstance().time
        return DATE_PICKER_EEE_MM_YYY_FORMAT.format(date)
    }

    fun getCurrentTime(): String {
        val date = Calendar.getInstance().time
        return DATE_PICKER_24H_FORMAT.format(date)
    }

    fun parse24HourToDate(time: String): Date {
        return DATE_PICKER_24H_FORMAT.parse(time)!!
    }

    fun getDateBeforeOrAfter(date: String, days: Int): String {
        val myDate = DATE_PICKER_YYYY_MM_DD_FORMAT.parse(date)
        val calendar = Calendar.getInstance()
        calendar.time = myDate!!
        calendar.add(Calendar.DAY_OF_YEAR, days)
        return DATE_PICKER_YYYY_MM_DD_FORMAT.format(calendar.time)
    }

    fun getTime_AM_PM_From_HH_MM(date: String): String {
        return DATE_PICKER_HH_MM_A_FORMAT.format(parse24HourToDate(date))
    }

    fun getTIME_AMPM(date: String): String {
        var spf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS", Locale.ENGLISH)
        val newDate = spf.parse(date)

        spf = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        return spf.format(newDate!!)
    }




    fun convertIsoToTimeStamp(iso: String): Long {
        val calendar = Calendar.getInstance(Locale.getDefault())
        val format =SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        try {
            val date = format.parse(iso)
            calendar.time = date

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return     calendar.timeInMillis

    }

    fun isTodayIso(iso: String): Boolean {
        return     DateUtils.isToday(convertIsoToTimeStamp(iso))
    }
    fun isTodayTimeStamp(time: Long): Boolean {
        return     DateUtils.isToday(time)
    }




    fun convertTimeStampToDate(timestamp: String,dateFormat : String = "hh:mm:ss a EEEE, MMM dd,yyyy"): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp.toLong()
        val date = DateFormat.format(dateFormat, calendar).toString()
        return date
    }
    fun convertIsoToDate(iso: String,dateFormat : String = "hh:mm a EE, MM dd,yyyy"): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        val format =SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        try {
            val date = format.parse(iso)
            calendar.time = date

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return     DateFormat.format(dateFormat, calendar).toString()
    }





    fun convertTimeStampToDate_EEE_MMM_yyyy(timestamp: String): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp.toLong()
        val date = DateFormat.format("EEE, MMM yy", calendar).toString()
        return date
    }
    fun convertTimeStampToDate_EEE_MMM_MM_yyyy(timestamp: String): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp.toLong()
        val date = DateFormat.format("EEEE, MMM dd,yyyy ", calendar).toString()
        return date
    }

    fun getTimeDateFromTimeStamp(timestamp: Long): String? {
        return try {
            val dateFormat = java.text.DateFormat.getDateTimeInstance()
            val netDate = Date(timestamp)
            dateFormat.format(netDate)
        } catch (e: Exception) {
            "date"
        }
    }
    fun getAddressFromMap(context: Context, latitude: Double, longitude: Double) :String{
        val addresses: List<Address>
        val geocoder: Geocoder = Geocoder(context, Locale.getDefault())

        addresses = geocoder.getFromLocation(
            latitude,
            longitude,
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        return if (addresses.isNotEmpty()){
            addresses[0].getAddressLine(0)
        }else{
            ""
        }


    }

}
