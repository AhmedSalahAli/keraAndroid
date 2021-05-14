package com.example.kera.home

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kera.R
import com.example.kera.dailyReport.ui.DailyReportActivity
import com.example.kera.databinding.HomeFragmentBinding
import com.example.kera.education.EducationActivity
import com.example.kera.events.ui.EventsActivity
import com.example.kera.home.adapter.HomeNewsAdapter
import com.example.kera.home.adapter.ImagesListAdapter
import com.example.kera.home.model.HomeNewsUIModel
import com.example.kera.meals.MealsActivity
import com.example.kera.medical.MedicalReportActivity
import com.example.kera.teacherDailyReport.ui.TeacherDailyReportActivity
import com.example.kera.teacherMedicalReport.TeacherMedicalReportActivity
import com.example.kera.utils.CommonUtils
import com.smarteist.autoimageslider.SliderAnimations
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    var isScrolling: Boolean = false
    var currentItems: Int = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0
    var page = 1
    var totalNumberOfPages: Int = 1
    lateinit var manager: LinearLayoutManager
    lateinit var newsList: ArrayList<HomeNewsUIModel.NewsList>
    lateinit var viewDataBinding: HomeFragmentBinding
    val viewModel: HomeViewModel by viewModel()
    private lateinit var accessType: String
    private var mProgressDialog: ProgressDialog? = null

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = DataBindingUtil.inflate(
            inflater, R.layout.home_fragment, container, false
        )
        accessType = viewModel.getUserTypeFromSharedPref()
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.viewModel = viewModel
        viewDataBinding.lifecycleOwner = viewLifecycleOwner

        if (accessType == "teacher") {
            viewDataBinding.imageViewQrCode.visibility = View.VISIBLE
            viewModel.getTeacherProfileData()
        } else {
            viewDataBinding.imageViewQrCode.visibility = View.GONE
            viewModel.getProfileData("user")
        }

        //mProgressDialog = CommonUtils.showLoadingDialog(requireActivity(), R.layout.progress_dialog)
        viewDataBinding.veilLayout.veil()

        newsList = ArrayList()


        viewModel.getNurseryData()
        userProfileDataObservation()
        teacherProfileDataObservation()
        val window: Window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        viewDataBinding.newsAdapter = HomeNewsAdapter(ArrayList())
        manager = LinearLayoutManager(requireActivity())
        viewDataBinding.recyclerViewNews.layoutManager = manager

        viewDataBinding.recyclerViewNews.addOnScrollListener(object :
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
                        mProgressDialog = CommonUtils.showLoadingDialog(
                            requireActivity(),
                            R.layout.progress_dialog
                        )
                        viewModel.getNewsList(
                            viewModel.getSelectedChildDataFromSharedPref()?.classId!!,
                            page
                        )
                    }
                }
            }
        })

        viewModel.homeNurseryData.observe(viewLifecycleOwner, {
          //  CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.veilLayout.unVeil()

            val adapter = ImagesListAdapter(it.images)
            viewDataBinding.recyclerView3.setSliderAdapter(adapter)
            viewDataBinding.recyclerView3.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            viewDataBinding.recyclerView3.scrollTimeInSec = 4 //set scroll delay in seconds :
            viewDataBinding.recyclerView3.startAutoCycle()
        })

        messageObserver()
        viewModel.newsList.observe(viewLifecycleOwner, {
            //CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.veilLayout.unVeil()

            newsList.clear()
            newsList.addAll(it.newsList)
            totalNumberOfPages = it.pages
            viewDataBinding.newsAdapter!!.news = newsList
            viewDataBinding.newsAdapter!!.notifyDataSetChanged()
        })

        viewDataBinding.constraintLayoutDailyReports.setOnClickListener {
            if (accessType == "teacher") {
                startActivity(Intent(context, TeacherDailyReportActivity::class.java))
            } else {
                startActivity(Intent(context, DailyReportActivity::class.java))
            }
        }
        viewDataBinding.constraintLayoutEvents.setOnClickListener {
            startActivity(Intent(context, EventsActivity::class.java))
        }

        viewDataBinding.constraintLayoutMeals.setOnClickListener {
            startActivity(Intent(context, MealsActivity::class.java))
        }

        viewDataBinding.constraintLayoutMedical.setOnClickListener {
            if (accessType == "teacher") {
                startActivity(Intent(context, TeacherMedicalReportActivity::class.java))
            } else {
                startActivity(Intent(context, MedicalReportActivity::class.java))
            }
        }

        viewDataBinding.constraintLayoutEducation.setOnClickListener {
            startActivity(Intent(context, EducationActivity::class.java))
        }
    }

    private fun messageObserver() {
        viewModel.message.observe(viewLifecycleOwner, {
            showMessage(it)
        })
    }

    private fun showMessage(it: String) {
       // CommonUtils.hideLoading(mProgressDialog!!)
        viewDataBinding.veilLayout.unVeil()

        Toast.makeText(
            requireContext(), it,
            Toast.LENGTH_LONG
        ).show();
    }

    fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.cancel()
        }
    }

    private fun userProfileDataObservation() {
        viewModel.profileUIModel.observe(viewLifecycleOwner, {
            //CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.veilLayout.unVeil()

            viewModel.saveProfileResponseToSharedPref(it)
            if (viewModel.getSelectedChildDataFromSharedPref() == null) {
                viewModel.saveChildDataToSharedPref(it.students!![0])
            }
            viewModel.getNewsList(viewModel.getSelectedChildDataFromSharedPref()?.classId!!, page)
        })
    }


    private fun teacherProfileDataObservation() {
        viewModel.teacherProfileUIModel.observe(viewLifecycleOwner, {
           // CommonUtils.hideLoading(mProgressDialog!!)
            viewDataBinding.veilLayout.unVeil()

//            viewModel.saveProfileResponseToSharedPref(it)
            viewModel.getNewsList("5fc2270ce4441941bbf5bcfd", page)
        })
    }
}