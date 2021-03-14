package com.example.kera.navigation.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.kera.navigation.ui.NavigationMapFragment
import com.example.kera.schoolsList.SchoolsListFragment


class TabLayoutAdapter(
    fm: FragmentManager,
    val tabTitles: Array<String>,
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getPageTitle(position: Int): CharSequence {
        return tabTitles[position]
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                return NavigationMapFragment()
            }
            1 -> {
                return SchoolsListFragment()
            }
            else -> getItem(position)
        }
    }

    override fun getCount(): Int {
        return tabTitles.size
    }
}