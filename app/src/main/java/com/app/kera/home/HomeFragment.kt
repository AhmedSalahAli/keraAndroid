package com.app.kera.home

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.AbsListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.app.kera.R
import com.app.kera.dailyReport.ui.DailyReportActivity
import com.app.kera.databinding.HomeFragmentBinding
import com.app.kera.education.EducationActivity
import com.app.kera.events.ui.EventsActivity
import com.app.kera.home.adapter.HomeNewsAdapter
import com.app.kera.home.adapter.ImagesListAdapter
import com.app.kera.home.model.HomeNewsUIModel
import com.app.kera.meals.MealsActivity
import com.app.kera.medical.MedicalReportActivity
import com.app.kera.qrCode.QRActivity

import com.app.kera.teacherDailyReport.ui.TeacherDailyReportActivity
import com.app.kera.teacherMedicalReport.TeacherMedicalReportActivity
import com.app.kera.utils.CommonUtils
import com.google.android.material.tabs.TabLayoutMediator
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment(), ImagesListAdapter.CallBack {

    private var isScrolling: Boolean = false
    private var currentItems: Int = 0
    private var totalItems: Int = 0
    private var scrollOutItems: Int = 0
    private var page = 1
    private var totalNumberOfPages: Int = 1
    private lateinit var manager: LinearLayoutManager
    private lateinit var newsList: ArrayList<HomeNewsUIModel.NewsList>
    private lateinit var viewDataBinding: HomeFragmentBinding
    private val viewModel: HomeViewModel by viewModel()
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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.viewModel = viewModel
        viewDataBinding.lifecycleOwner = viewLifecycleOwner

        // Determine user type and fetch profile data
        if (accessType == "teacher") {
            viewDataBinding.imageViewQrCode.visibility = View.VISIBLE
            viewModel.getTeacherProfileData()
        } else {
            viewDataBinding.imageViewQrCode.visibility = View.GONE
            viewModel.getProfileData("user")
        }

        viewDataBinding.veilLayout.loadSkeleton()
        newsList = ArrayList()

        // Fetch nursery data
        viewModel.getNurseryData()

        // Observe profile data and update UI
        userProfileDataObservation()
        teacherProfileDataObservation()

        setupViewPagerWithTabLayout()

        viewDataBinding.newsAdapter = HomeNewsAdapter(ArrayList())
        manager = LinearLayoutManager(requireActivity())
        viewDataBinding.recyclerViewNews.layoutManager = manager

        setupRecyclerViewScrollListener()
        observeDataChanges()
        setupClickListeners()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setupViewPagerWithTabLayout() {
        val viewPager = viewDataBinding.viewPager
        val tabLayout = viewDataBinding.tabLayout

        viewModel.homeNurseryData.observe(viewLifecycleOwner) { data ->
            viewDataBinding.veilLayout.hideSkeleton()
            data.images.removeIf(String::isEmpty)
            val adapter = ImagesListAdapter(data.images, requireContext(), this)
            viewPager.adapter = adapter

            // Attach TabLayout with ViewPager2
            TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

            // Start Auto-Scrolling
            startAutoScroll(viewPager, data.images.size)
        }
    }
    private var autoScrollJob: Job? = null
    private var pageChangeCallback: ViewPager2.OnPageChangeCallback? = null

    private fun startAutoScroll(viewPager: ViewPager2, itemCount: Int) {
        if (itemCount <= 1) return // No need to scroll if only one item exists.

        // Cancel any existing job to prevent duplicates
        autoScrollJob?.cancel()

        // Start auto-scroll coroutine
        autoScrollJob = lifecycleScope.launch {
            while (isActive) {
                delay(5000) // 5 seconds delay
                val nextItem = (viewPager.currentItem + 1) % itemCount
                viewPager.setCurrentItem(nextItem, true)
            }
        }

        // Register a single OnPageChangeCallback if not already registered
        if (pageChangeCallback == null) {
            pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                        autoScrollJob?.cancel() // Stop auto-scroll during user interaction
                    } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                        if (autoScrollJob?.isActive != true) {
                            startAutoScroll(viewPager, itemCount) // Resume auto-scroll
                        }
                    }
                }
            }
            viewPager.registerOnPageChangeCallback(pageChangeCallback!!)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Cleanup to avoid memory leaks
        autoScrollJob?.cancel()
        pageChangeCallback?.let { viewDataBinding.viewPager.unregisterOnPageChangeCallback(it) }
        pageChangeCallback = null
    }

    private fun setupRecyclerViewScrollListener() {
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
    }

    private fun userProfileDataObservation() {
        viewModel.profileUIModel.observe(viewLifecycleOwner) { profile ->
            viewDataBinding.veilLayout.hideSkeleton()
            viewModel.saveProfileResponseToSharedPref(profile)
            if (viewModel.getSelectedChildDataFromSharedPref() == null) {
                viewModel.saveChildDataToSharedPref(profile.students!![0])
            }
            viewModel.getNewsList(profile.associationId!!, page)
        }
    }

    private fun teacherProfileDataObservation() {
        viewModel.teacherProfileUIModel.observe(viewLifecycleOwner) { teacherProfile ->
            viewDataBinding.veilLayout.hideSkeleton()
            viewModel.saveTeacherResponseToSharedPref(teacherProfile)
            viewModel.getNewsList(teacherProfile.associationId!!, page)
        }
    }

    private fun observeDataChanges() {
        viewModel.newsList.observe(viewLifecycleOwner) {
            viewDataBinding.veilLayout.hideSkeleton()
            newsList.clear()
            newsList.addAll(it.newsList)
            totalNumberOfPages = it.pages
            viewDataBinding.newsAdapter!!.news = newsList
            viewDataBinding.newsAdapter!!.notifyDataSetChanged()
        }
    }

    private fun setupClickListeners() {
        viewDataBinding.constraintLayoutDailyReports.setOnClickListener {
            val intent = if (accessType == "teacher") {
                Intent(context, TeacherDailyReportActivity::class.java)
            } else {
                Intent(context, DailyReportActivity::class.java)
            }
            startActivity(intent)
        }

        viewDataBinding.constraintLayoutEvents.setOnClickListener {
            startActivity(Intent(context, EventsActivity::class.java))
        }

        viewDataBinding.constraintLayoutMeals.setOnClickListener {
            startActivity(Intent(context, MealsActivity::class.java))
        }

        viewDataBinding.constraintLayoutMedical.setOnClickListener {
            val intent = if (accessType == "teacher") {
                Intent(context, TeacherMedicalReportActivity::class.java)
            } else {
                Intent(context, MedicalReportActivity::class.java)
            }
            startActivity(intent)
        }

        viewDataBinding.constraintLayoutEducation.setOnClickListener {
            startActivity(Intent(context, EducationActivity::class.java))
        }

        viewDataBinding.imageViewQrCode.setOnClickListener {
            startActivity(Intent(context, QRActivity::class.java))
        }
    }

    override fun onImageClicked(position: Int, imagesList: ArrayList<String>) {
        // Handle image click
    }
}
