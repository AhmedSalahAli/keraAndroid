package com.example.kera.navigation

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.kera.R
import com.example.kera.databinding.NavigationFragmentBinding
import com.example.kera.navigation.adapter.TabLayoutAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener


class NavigationFragment() : Fragment() {
    lateinit var viewDataBinding: NavigationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = DataBindingUtil.inflate(
            inflater, R.layout.navigation_fragment, container, false
        )
        val tabTitles = arrayOf("Map", "List")
        viewDataBinding.viewPager.adapter = TabLayoutAdapter(
            childFragmentManager,
            tabTitles
        )
        viewDataBinding.tabLayout.setupWithViewPager(viewDataBinding.viewPager)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val window: Window =requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        viewDataBinding.viewPager.currentItem = 1
        viewDataBinding.viewPager.addOnPageChangeListener(
            TabLayoutOnPageChangeListener(
                viewDataBinding.tabLayout
            )
        )
        viewDataBinding.tabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewDataBinding.viewPager.currentItem = tab!!.position;
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    interface CallBack {
        fun onBottomBarActionClicked(position: Int)
    }
}