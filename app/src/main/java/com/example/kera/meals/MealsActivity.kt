package com.example.kera.meals

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.kera.R
import com.example.kera.databinding.ActivityMealsBinding
import com.example.kera.education.adapter.DateAdapter
import com.example.kera.meals.adapter.MealsListAdapter
import com.example.kera.meals.details.MealsDetailsActivity
import com.example.kera.utils.CommonUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class MealsActivity : AppCompatActivity(), MealsListAdapter.CallBack,
    DateAdapter.ItemClickNavigator {

    lateinit var viewDataBinding: ActivityMealsBinding
    val viewModel: MealsViewModel by viewModel()
    private var mProgressDialog: ProgressDialog? = null
    lateinit var accessType: String

    companion object {
        fun newInstance() = MealsActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_meals)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel

        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        viewDataBinding.datesAdapter = DateAdapter(ArrayList(), this)
        viewDataBinding.mealsAdapter = MealsListAdapter(ArrayList(), this)

        accessType = viewModel.getUserType()
        mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)

        if (accessType == "teacher") {
            viewModel.getDates("5fc2270ce4441941bbf5bcfd")
        } else {
            viewModel.getDates(viewModel.getSelectedChildDataFromSharedPref()?.classId!!)
        }

        messageObserver()

        viewModel.datesListLiveData.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.datesAdapter!!.datesList = it
            viewDataBinding.datesAdapter!!.notifyDataSetChanged()

            if (accessType == "teacher") {
                viewModel.getDates("5fc2270ce4441941bbf5bcfd")
                viewModel.getMeals("5fc2270ce4441941bbf5bcfd", it[0].actualDate)
            } else {
                viewModel.getMeals(
                    viewModel.getSelectedChildDataFromSharedPref()!!.classId!!,
                    it[0].actualDate
                )
            }
//            viewModel.getMeals(
//                viewModel.getSelectedChildDataFromSharedPref()!!.classId!!,
//                it[0].actualDate
//            )
        })

        viewModel.mealsList.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.mealsAdapter!!.mealsList = it
            viewDataBinding.mealsAdapter!!.notifyDataSetChanged()
        })

        viewDataBinding.imageViewBack.setOnClickListener {
            finish()
        }
    }

    override fun onItemClicked(mealID: String?) {
        val myIntent = Intent(this@MealsActivity, MealsDetailsActivity::class.java)
        myIntent.putExtra("MealID", mealID) //Optional parameters
        startActivity(myIntent)
    }

    private fun messageObserver() {
        viewModel.message.observe(this@MealsActivity, {
            CommonUtils.hideLoading(mProgressDialog!!)
            showMessage(it)
        })
    }

    private fun showMessage(it: String) {
        Toast.makeText(
            this, it,
            Toast.LENGTH_LONG
        ).show();
    }

    override fun onDateClick(date: String) {
        mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
        if (accessType == "teacher") {
            viewModel.getMeals("5fc2270ce4441941bbf5bcfd", date)
        } else {
            viewModel.getMeals(viewModel.getSelectedChildDataFromSharedPref()?.classId!!, date)
        }
    }
}