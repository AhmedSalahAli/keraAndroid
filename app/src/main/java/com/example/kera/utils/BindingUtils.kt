package com.example.kera.utils

import android.util.Log
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

object BindingUtils {
    @BindingAdapter("app:error")
    @JvmStatic
    fun bindError(view: EditText, error: String?) {
        if (error != null) {
            Log.e("Error", error)
            view.error = error
            view.requestFocus()
        }
    }

    @BindingAdapter("app:imageDrawableName")
    @JvmStatic
    fun bindimageDrawableName(view: ImageView, image: String?) {
        if (image != null) {
            val identifier =
                view.context.resources.getIdentifier(image, "drawable", view.context.packageName)
            view.setImageResource(identifier)
        }
    }

    @BindingAdapter("app:image")
    @JvmStatic
    fun bindImage(view: ImageView, url: String?) {
        if (url != null) {
            Glide.with(view.context).load(url).into(view)
        }
    }

    @BindingAdapter("app:adapter")
    @JvmStatic
    fun bindAdapter(view: RecyclerView, adapter: Any?) {
        if (adapter != null) {
            view.adapter = adapter as RecyclerView.Adapter<*>?
        }
    }

    @BindingAdapter("app:imageResources")
    @JvmStatic
    fun bindImageResources(view: ImageView, img: Int) {
        view.setImageResource(img)
    }

    @BindingAdapter("app:layout_height")
    @JvmStatic
    fun bindLayoutHeight(view: ImageView, model: Any?) {
        if (model == null) {
            view.layoutParams.height =
                view.layoutParams.height - 250
            view.requestLayout()
        } else {
            view.layoutParams.height =
                view.layoutParams.height + 250
            view.requestLayout()
        }
    }

    @JvmStatic
    @BindingAdapter("android:background")
    fun setBackground(view: TextView, background: Int) {
        view.setBackgroundColor(view.resources.getColor(background))
    }

    @JvmStatic
    @BindingAdapter("android:backgroundDrawable")
    fun setBackgroundDrawable(view: TextView, background: Int) {
        view.background = view.resources.getDrawable(background)
    }

    @JvmStatic
    @BindingAdapter("app:textColor")
    fun setTextColor(view: TextView, color: Int) {
        view.setTextColor(view.resources.getColor(color))
    }

    @BindingAdapter("app:mark")
    @JvmStatic
    fun bindMark(view: CheckBox, isMarked: Boolean?) {
        if (isMarked != null && isMarked == true) {
            view.isSelected = true
        } else {
            view.isSelected = false
        }
    }
}