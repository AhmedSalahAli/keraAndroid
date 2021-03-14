package com.example.kera.teacherMedicalReport

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.R
import com.example.kera.databinding.TeacherMedicalReportFragmentBinding
import com.example.kera.teacherMedicalReport.adapter.LatestReportsListAdapter
import com.example.kera.teacherMedicalReport.adapter.MedicalReportClassesListAdapter
import com.example.kera.teacherMedicalReport.adapter.StudentsListAdapter
import com.example.kera.teacherMedicalReport.model.LatestReportItemUIModel
import com.example.kera.teacherMedicalReport.writeMedicalReport.WriteMedicalReportActivity
import com.example.kera.utils.CommonUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class TeacherMedicalReportActivity : AppCompatActivity(),
    MedicalReportClassesListAdapter.CallBack,
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

    val viewModel: TeacherMedicalReportViewModel by viewModel()
    lateinit var viewDataBinding: TeacherMedicalReportFragmentBinding
    private var mProgressDialog: ProgressDialog? = null
    var selectedStudents = ArrayList<String>()

    companion object {
        fun newInstance() = TeacherMedicalReportActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding =
            DataBindingUtil.setContentView(this, R.layout.teacher_medical_report_fragment)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = viewModel

        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        searchClickListener()
        mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
        viewModel.getClasses()
        viewModel.getLatestReports(page)
        viewDataBinding.classesAdapter = MedicalReportClassesListAdapter(ArrayList(), this, this)
        viewDataBinding.latestReportsAdapter = LatestReportsListAdapter(ArrayList(), this)
        classesListObservation()
        studentsListObservation()
        getLatestReportsObservation()


        manager = LinearLayoutManager(this)
        viewDataBinding.recyclerLatestReports.layoutManager = manager
        viewDataBinding.recyclerLatestReports.addOnScrollListener(object :
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
                        mProgressDialog =
                            CommonUtils.showLoadingDialog(
                                this@TeacherMedicalReportActivity,
                                R.layout.progress_dialog
                            )
                        Log.e("number of pages", totalNumberOfPages.toString())
                        Log.e("page = ", page.toString())
                        viewModel.getLatestReports(page)
                    }
                }
            }
        })
        viewDataBinding.imageViewBack.setOnClickListener {
            finish()
        }
    }

    private fun getLatestReportsObservation() {
        viewModel.latestReportsList.observe(this, {
            totalNumberOfPages = it.pages
            latestReports.addAll(it.reports)
            viewDataBinding.latestReportsAdapter!!.reportsList = latestReports
            viewDataBinding.latestReportsAdapter!!.notifyDataSetChanged()
        })
    }

    private fun classesListObservation() {
        viewModel.classesList.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.textView118.text = "${it.size} classes today"
            viewDataBinding.classesAdapter!!.classesList = it
            viewDataBinding.classesAdapter!!.notifyDataSetChanged()
        })
    }

    private fun studentsListObservation() {
        viewModel.studentsList.observe(this, {
            CommonUtils.hideLoading(mProgressDialog!!)
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

    override fun onClassClicked(classID: String?) {
        mProgressDialog = CommonUtils.showLoadingDialog(this, R.layout.progress_dialog)
        viewModel.getStudentsByClass(classID!!)
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
            this@TeacherMedicalReportActivity,
            WriteMedicalReportActivity()::class.java
        )
        intent.putExtra("reportID", reportID)
        startActivity(intent)
    }
}