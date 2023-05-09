package com.app.kera.navigation.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.app.kera.navigation.ui.MapFragment
import com.app.kera.navigation.ui.NavigationMapFragment
import com.app.kera.navigation.ui.PermissionFragment
import com.app.kera.schoolsList.SchoolsListFragment


class TabLayoutAdapter(
    fm: FragmentManager,
    val tabTitles: Array<String>,
    var isPermissionGranted:Boolean
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getPageTitle(position: Int): CharSequence {
        return tabTitles[position]
    }

    override fun getItem(position: Int): Fragment {
        if (isPermissionGranted){
            return when (position) {

                0 -> {
                    return  MapFragment()
                }
                1 -> {
                    return SchoolsListFragment()
                }

                else -> getItem(position)
            }
        }else{
            return PermissionFragment()

        }

    }

    override fun getCount(): Int {
        return tabTitles.size
    }
}