package com.example.kera.registration.screen1

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.kera.R
import com.example.kera.databinding.ActivityRegistration1Binding
import com.example.kera.registration.screen2.Registration2Activity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class Registration1Activity : AppCompatActivity() {

    var isMaleGenderSelected: Boolean = false
    var isFemaleGenderSelected: Boolean = false
    val viewModel: Registration1ViewModel by viewModel()
    lateinit var viewDataBinding: ActivityRegistration1Binding
    lateinit var dateListener: DatePickerDialog.OnDateSetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_registration1)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel

        datePicker()
        checkBoxNoClickListener()
        checkBoxYesClickListener()
        nextButtonClickListener()
        backButtonClickListener()
    }

    private fun backButtonClickListener() {
        viewDataBinding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun nextButtonClickListener() {
        viewDataBinding.textView33.setOnClickListener {
            startActivity(Intent(this, Registration2Activity::class.java))
        }
    }

    private fun datePicker() {
        dateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            viewDataBinding.datePicker.text =
                dayOfMonth.toString() + "/" + (month + 1).toString() + "/" + year.toString()
        }

        viewDataBinding.datePicker.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateListener,
                year, month, day
            )
            datePickerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            datePickerDialog.show()
        }
    }

    private fun checkBoxNoClickListener() {
        viewDataBinding.imageViewFemale.setOnClickListener {

            if (isFemaleGenderSelected) {
                isFemaleGenderSelected = false

            } else if (!isFemaleGenderSelected && isMaleGenderSelected) {
                isFemaleGenderSelected = true
                isMaleGenderSelected = false
            } else if (!isFemaleGenderSelected && !isMaleGenderSelected) {
                isFemaleGenderSelected = true
                isMaleGenderSelected = false
            }
            recommendTourFields()
        }
    }

    private fun checkBoxYesClickListener() {
        viewDataBinding.imageViewMale.setOnClickListener {
            if (isMaleGenderSelected) {
                isMaleGenderSelected = false
            } else if (!isMaleGenderSelected && isFemaleGenderSelected) {
                isFemaleGenderSelected = false
                isMaleGenderSelected = true
            } else if (!isMaleGenderSelected && !isFemaleGenderSelected) {
                isMaleGenderSelected = true
            }
            recommendTourFields()
        }
    }

    private fun recommendTourFields() {
        if (isMaleGenderSelected) {
            viewDataBinding.imageViewMale.background =
                resources.getDrawable(R.drawable.rounded_light_blue_stroke)
            viewDataBinding.imageViewFemale.background = null
        } else if (isFemaleGenderSelected) {
            viewDataBinding.imageViewFemale.background =
                resources.getDrawable(R.drawable.rounded_light_pink_stroke)
            viewDataBinding.imageViewMale.background = null
        } else if (!isFemaleGenderSelected && !isMaleGenderSelected) {
            viewDataBinding.imageViewMale.background = null
            viewDataBinding.imageViewFemale.background = null
        }
    }
}