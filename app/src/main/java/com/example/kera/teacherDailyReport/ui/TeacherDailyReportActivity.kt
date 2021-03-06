package com.example.kera.teacherDailyReport.ui

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AbsListView
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.kera.R
import com.example.kera.databinding.TeacherDailyReportFragmentBinding
import com.example.kera.teacherDailyReport.adapter.ClassesListAdapter
import com.example.kera.teacherDailyReport.model.CreateReportRequestModel
import com.example.kera.teacherDailyReport.writeReport.WriteReportActivity
import com.example.kera.teacherMedicalReport.adapter.LatestReportsListAdapter
import com.example.kera.teacherMedicalReport.adapter.StudentsListAdapter
import com.example.kera.teacherMedicalReport.model.LatestReportItemUIModel
import com.example.kera.utils.CommonUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList


class TeacherDailyReportActivity : AppCompatActivity(),
    ClassesListAdapter.CallBack,
    LatestReportsListAdapter.CallBack,
    StudentsListAdapter.StudentClicked {

    var isScrolling: Boolean = false
    var currentItems: Int = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0
    var page = 1
    var totalNumberOfPages: Int = 1
    lateinit var manager: LinearLayoutManager
    var latestReports = ArrayList<LatestReportItemUIModel.ReportModel>()

    private var mProgressDialog: ProgressDialog? = null
    var selectedStudents = ArrayList<String>()
    val viewModel: TeacherDailyReportViewModel by viewModel()
    lateinit var viewDataBinding: TeacherDailyReportFragmentBinding

    companion object {
        fun newInstance() = TeacherDailyReportActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding =
            DataBindingUtil.setContentView(this, R.layout.teacher_daily_report_fragment)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;


        viewDataBinding.textViewDate.text = CommonUtils.getCurrentDate_EEE_MM_YYY()
        val content = SpannableString(resources.getString(R.string.mark_as_abscent))
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        viewDataBinding.textViewMarkAsAbsent.text = content

        //mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)


        viewDataBinding.recyclerView9.setAdapter(viewDataBinding.studentsAdapter) // sets your own adapter
        viewDataBinding.recyclerView9.setLayoutManager(StaggeredGridLayoutManager(3,
            StaggeredGridLayoutManager.HORIZONTAL)) // sets LayoutManager
        viewDataBinding.recyclerView9.addVeiledItems(15)
        viewDataBinding.recyclerView9.veil()

        viewDataBinding.recyclerView7.setAdapter(viewDataBinding.classesAdapter) // sets your own adapter
        viewDataBinding.recyclerView7.setLayoutManager(StaggeredGridLayoutManager(1,
            StaggeredGridLayoutManager.HORIZONTAL)) // sets LayoutManager
        viewDataBinding.recyclerView7.addVeiledItems(15)
        viewDataBinding.recyclerView7.veil()

        viewDataBinding.recyclerLatestReports.setAdapter(viewDataBinding.latestReportsAdapter) // sets your own adapter
        viewDataBinding.recyclerLatestReports.setLayoutManager(LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)) // sets LayoutManager
        viewDataBinding.recyclerLatestReports.addVeiledItems(15)
        viewDataBinding.recyclerLatestReports.veil()
        viewDataBinding.recyclerLatestReports.setNestedScrollingEnabled(true);
        viewDataBinding.recyclerLatestReports.getRecyclerView().setPadding(0,0,0,200)
        viewDataBinding.recyclerLatestReports .getRecyclerView().clipToPadding = false
        viewDataBinding.recyclerLatestReports.getVeiledRecyclerView().setPadding(0,0,0,200)
        viewDataBinding.recyclerLatestReports .getVeiledRecyclerView().clipToPadding = false

        viewModel.getClasses()
        viewModel.getLatestReports(page)
        checkNumberOfSelectedStudents()
        searchClickListener()
        messageObserver()
        classesListObservation()
        studentsListObservation()
        viewDataBinding.classesAdapter = ClassesListAdapter(ArrayList(), this, this)
        viewDataBinding.latestReportsAdapter = LatestReportsListAdapter(ArrayList(), this)
        backClickListener()
        createReportClickListener()
        createReportObservation()

        manager = LinearLayoutManager(this)
        viewDataBinding.recyclerLatestReports.setLayoutManager(manager)
        viewDataBinding.recyclerLatestReports.getRecyclerView().addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItems = manager.childCount
                totalItems = manager.itemCount
                scrollOutItems = manager.findFirstVisibleItemPosition()
                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false
                    if (page < totalNumberOfPages) {
                        page += 1
//                        mProgressDialog =
//                            CommonUtils.showLoadingDialog(
//                                this@TeacherDailyReportActivity,
//                                R.layout.progress_dialog
//                            )
                       // viewDataBinding.recyclerLatestReports.veil()
                        Log.e("number of pages", totalNumberOfPages.toString())
                        Log.e("page = ", page.toString())
                        viewModel.getLatestReports(page)
                    }
                }
            }
        })

        getLatestReportsObservation()

        viewDataBinding.checkBoxSelectAll.setOnClickListener {
            selectedStudents.clear()
            if (viewDataBinding.checkBoxSelectAll.isChecked) {
                for (student in viewDataBinding.studentsAdapter!!.studentsList) {
                    student.isSelected.set(true)
                    selectedStudents.add(student.id)
                }
                viewDataBinding.checkBoxSelectAll.setText(getString(R.string.clear_all))
            } else {
                selectedStudents.clear()
                for (student in viewDataBinding.studentsAdapter!!.studentsList) {
                    student.isSelected.set(false)
                }
                viewDataBinding.checkBoxSelectAll.setText(getString(R.string.select_all))

                Log.e("selectedStudents", selectedStudents.size.toString())
            }
            checkNumberOfSelectedStudents()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getClasses()
        viewModel.getLatestReports(page)
        selectedStudents.clear()
        checkNumberOfSelectedStudents()
        viewDataBinding.checkBoxSelectAll.isChecked = false
        viewDataBinding.checkBoxSelectAll.setText(getString(R.string.select_all))

    }

    private fun getLatestReportsObservation() {
        viewModel.latestReportsList.observe(this, {
            //CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.recyclerLatestReports.unVeil()
            totalNumberOfPages = it.pages
            latestReports.addAll(it.reports)
            viewDataBinding.latestReportsAdapter!!.reportsList = latestReports
            viewDataBinding.latestReportsAdapter!!.notifyDataSetChanged()
        })
    }

    private fun createReportObservation() {
        viewModel.createdReportResponseID.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)
            val intent = Intent(
                this@TeacherDailyReportActivity,
                WriteReportActivity()::class.java
            )
            intent.putExtra("reportID", it)
            startActivity(intent)
        })
    }

    private fun createReportClickListener() {
        viewDataBinding.imageViewWriteReport.setOnClickListener {
            if (selectedStudents.size >0){
                mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
                var createReportRequestModel = CreateReportRequestModel()
                createReportRequestModel.students = selectedStudents
                viewModel.createDailyReport(createReportRequestModel)
            }else{
                showMessage(getString(R.string.please_select_at_least_one_student))
            }

        }
    }

    private fun backClickListener() {
        viewDataBinding.imageViewBack.setOnClickListener {
            finish()
        }
    }

    private fun classesListObservation() {
        viewModel.classesList.observe(this, {
            //CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.recyclerView7.unVeil()
            viewDataBinding.textViewNumberOfClasses.text = "${it.size} classes today"
            viewDataBinding.classesAdapter!!.classesList = it
            viewDataBinding.classesAdapter!!.notifyDataSetChanged()
        })
    }

    private fun studentsListObservation() {
        viewModel.studentsList.observe(this, {
            //CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.recyclerView9.unVeil()
            viewDataBinding.studentsAdapter = StudentsListAdapter(it, this, this)
            viewDataBinding.studentsAdapter!!.notifyDataSetChanged()
        })
    }

    private fun searchClickListener() {
        viewDataBinding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                viewDataBinding.studentsAdapter?.filter?.filter(p0.toString())
            }
        })
    }

    private fun messageObserver() {
        viewModel.message.observe(this@TeacherDailyReportActivity, {
            //CommonUtils.hideLoading(mProgressDialog!!)
            showMessage(it)
        })
    }

    private fun showMessage(it: String) {
        Toast.makeText(
            this, it,
            Toast.LENGTH_LONG
        ).show();
    }

    override fun onItemClicked(classID: String?) {
        //mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
        viewDataBinding.recyclerView9.veil()
        viewModel.getStudentsByClass(classID!!)
        viewDataBinding.checkBoxSelectAll.isChecked = false
        viewDataBinding.checkBoxSelectAll.setText(getString(R.string.select_all))

    }

    override fun onStudentClicked(studentID: String?) {
        var wasSelected = false
        for (selectedStudentID in selectedStudents) {
            if (studentID == selectedStudentID) {
                wasSelected = true
            }
        }
        if (wasSelected) {
            selectedStudents.remove(studentID!!)
        } else {
            selectedStudents.add(studentID!!)
        }
        Log.e("selectedStudents", selectedStudents.size.toString())
        checkNumberOfSelectedStudents()
    }

    private fun checkNumberOfSelectedStudents() {
        if (selectedStudents.size > 1) {
            viewDataBinding.textViewStudentsSelected.text =
                "${selectedStudents.size} students are selected"
        } else if (selectedStudents.size == 0) {
            viewDataBinding.textViewStudentsSelected.text = " no student is selected"
        } else {
            viewDataBinding.textViewStudentsSelected.text =
                "${selectedStudents.size} student is selected"
        }
    }

    override fun onReportItemClicked(reportID: String?) {
        val intent = Intent(
            this@TeacherDailyReportActivity,
            WriteReportActivity()::class.java
        )
        intent.putExtra("reportID", reportID)
        startActivity(intent)
    }
}