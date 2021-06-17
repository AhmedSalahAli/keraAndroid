package com.example.kera.meals

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kera.R
import com.example.kera.databinding.ActivityMealsBinding
import com.example.kera.education.adapter.DateAdapter
import com.example.kera.meals.adapter.MealsListAdapter
import com.example.kera.meals.details.MealsDetailsActivity
import com.example.kera.profile.StudentsData
import com.example.kera.profile.adapter.ChildrenAdapter
import com.example.kera.utils.CommonUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class MealsActivity : AppCompatActivity(), MealsListAdapter.CallBack,
    DateAdapter.ItemClickNavigator,ChildrenAdapter.CallBack {

    lateinit var viewDataBinding: ActivityMealsBinding
    val viewModel: MealsViewModel by viewModel()
    private var mProgressDialog: ProgressDialog? = null
    lateinit var accessType: String
    var actualeDate:String = ""

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

        viewDataBinding.datesAdapter = DateAdapter(ArrayList(), this,this)
        viewDataBinding.mealsAdapter = MealsListAdapter(ArrayList(), this)
        viewDataBinding.childrenAdapter = ChildrenAdapter(ArrayList(), this,viewModel.getAppRepoInstance())

        accessType = viewModel.getUserType()
        //mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
        viewDataBinding.veilLayout.veil()
        viewDataBinding.recyclerMeals.setAdapter(viewDataBinding.mealsAdapter) // sets your own adapter

        viewDataBinding.recyclerMeals.setLayoutManager(LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)) // sets LayoutManager
        viewDataBinding.recyclerMeals.addVeiledItems(15)
        viewDataBinding.recyclerMeals.veil()

        if (accessType == "teacher") {
            viewModel.getDates("5fc2270ce4441941bbf5bcfd")
            viewDataBinding.imageViewExchange.visibility = View.GONE
            viewDataBinding.imageViewProfile.visibility = View.GONE

        } else {
            viewModel.getDates(viewModel.getSelectedChildDataFromSharedPref()?.classId!!)
            viewModel.getProfileData()
            viewDataBinding.imageViewExchange.visibility = View.VISIBLE
            viewDataBinding.imageViewProfile.visibility = View.VISIBLE

        }
        setupUI(viewDataBinding.mealsLay)
        messageObserver()

        viewDataBinding.imageViewExchange.setOnClickListener {

            if (viewDataBinding.childrenFrame.isVisible){
                stateOfChildrenFrame(false)
            }else{
                stateOfChildrenFrame(true)
            }
        }
        viewDataBinding.imageViewProfile.setOnClickListener {

            if (viewDataBinding.childrenFrame.isVisible){
                stateOfChildrenFrame(false)
            }else{
                stateOfChildrenFrame(true)
            }
        }
        viewModel.profileUIModel.observe(this, {
            //CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.childrenAdapter!!.children = it.students!!
            viewDataBinding.childrenAdapter!!.notifyDataSetChanged()
            if (it.students!!.size ==1){
                viewDataBinding.imageViewExchange.visibility =  View.GONE
            }else{
                viewDataBinding.imageViewExchange.visibility =  View.VISIBLE

            }
        })

        viewModel.datesListLiveData.observe(this, {
            //CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.veilLayout.unVeil()
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
            actualeDate=it[0].actualDate
//            viewModel.getMeals(
//                viewModel.getSelectedChildDataFromSharedPref()!!.classId!!,
//                it[0].actualDate
//            )
        })

        viewModel.mealsList.observe(this, {
          //  CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.recyclerMeals.unVeil()

            viewDataBinding.mealsAdapter!!.mealsList = it

            viewDataBinding.mealsAdapter!!.notifyDataSetChanged()
            if (it.size == 0){
                showNoData()
            }else{
                hideNoData()
            }
        })

        viewDataBinding.imageViewBack.setOnClickListener {
            finish()
        }
    }
    fun setupUI(view: View) {

        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                viewDataBinding.childrenFrame.visibility = View.GONE
                false
            }
        }
    }
    override fun onItemClicked(mealID: String?) {
        val myIntent = Intent(this@MealsActivity, MealsDetailsActivity::class.java)
        myIntent.putExtra("MealID", mealID) //Optional parameters
        startActivity(myIntent)
    }

    private fun messageObserver() {
        viewModel.message.observe(this@MealsActivity, {
            //CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.veilLayout.unVeil()
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
      //  mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
        viewDataBinding.recyclerMeals.veil()
        if (accessType == "teacher") {
            viewModel.getMeals("5fc2270ce4441941bbf5bcfd", date)
        } else {
            viewModel.getMeals(viewModel.getSelectedChildDataFromSharedPref()?.classId!!, date)
        }
        actualeDate = date
    }
    fun stateOfChildrenFrame(state:Boolean){
        if (state){

            viewDataBinding.childrenFrame.setAnimation(
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.fade_in
                )
            )
            viewDataBinding.childrenFrame.visibility = View.VISIBLE

        }else{
            viewDataBinding.childrenFrame.setAnimation(
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.fade_out
                )
            )
            viewDataBinding.childrenFrame.visibility = View.GONE
        }
    }

    override fun onItemClicked(student: StudentsData) {
        stateOfChildrenFrame(false)
        viewModel.saveChildToSharedPref(student)
        viewModel.selectedUser.value = student
        viewModel.selectedUser.value!!.studentCode = "Code:" + student.studentCode
        viewModel.selectedUser.value!!.className = "Class:" + student.className
        //mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
        viewDataBinding.childrenAdapter!!.notifyDataSetChanged()
        if (accessType == "teacher") {
            viewModel.getMeals("5fc2270ce4441941bbf5bcfd", actualeDate)
        } else {
            viewModel.getMeals(viewModel.getSelectedChildDataFromSharedPref()?.classId!!, actualeDate)
        }


    }
    private fun showNoData() {
        viewDataBinding.recyclerMeals.visibility = View.GONE
        viewDataBinding.relNoPlanned.visibility = View.VISIBLE

    }
    private fun hideNoData() {
        viewDataBinding.recyclerMeals.visibility = View.VISIBLE
        viewDataBinding.relNoPlanned.visibility = View.GONE
    }
}