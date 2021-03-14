package com.example.kera.main.ui

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kera.R
import com.example.kera.databinding.ActivityMainBinding
import com.example.kera.navigation.NavigationFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.shape.CornerFamily.ROUNDED
import com.google.android.material.shape.MaterialShapeDrawable
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), NavigationFragment.CallBack {
    var positionSelected = 2

    private val mainViewModel: MainViewModel by viewModel()
    lateinit var viewDataBinding: ActivityMainBinding
    lateinit var accessType: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = mainViewModel

        accessType = mainViewModel.getUserType()
        val navController = findNavController(R.id.fragment)
        viewDataBinding.bottomNavigationView.uncheckAllItems()
        viewDataBinding.bottomNavigationView.setupWithNavController(navController)
        viewDataBinding.bottomNavigationView.background = null
        viewDataBinding.bottomNavigationView.menu.getItem(2).isEnabled = false


        val bottomBarBackground = viewDataBinding.bottomAppBar2.background as MaterialShapeDrawable
        bottomBarBackground.shapeAppearanceModel = bottomBarBackground.shapeAppearanceModel
            .toBuilder()
            .setTopRightCorner(ROUNDED, 60f)
            .setTopLeftCorner(ROUNDED, 60f)
            .build()
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;



        viewDataBinding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigationFragment -> {
                    navController.navigate(R.id.navigationFragment)
                }
                R.id.notificationFragment -> {
                    navController.navigate(R.id.notificationFragment)
                }
                R.id.profileFragment -> {
                    if (accessType == "user") {
                        navController.navigate(R.id.profileFragment)
                    } else {
                        navController.navigate(R.id.teacherProfileFragment)
                    }
                }
                R.id.sideMenuFragment -> {
                    navController.navigate(R.id.sideMenuFragment)
                }
            }
            true
        }

        viewDataBinding.floatingActionButton2.setOnClickListener {
            viewDataBinding.bottomNavigationView.uncheckAllItems()
            navController.navigate(R.id.homeFragment)
        }


//        viewDataBinding.nearbyConstraint.setOnClickListener {
//            handlingBottomBarClicks(0)
//        }
//        viewDataBinding.notificationConstrains.setOnClickListener {
//            handlingBottomBarClicks(1)
//        }
//        viewDataBinding.homeConstraint.setOnClickListener {
//            handlingBottomBarClicks(2)
//        }
//
//        viewDataBinding.profileConstraint.setOnClickListener {
//            handlingBottomBarClicks(3)
//        }
//        viewDataBinding.settingsConstraint.setOnClickListener {
//            handlingBottomBarClicks(4)
//        }
    }

//    private fun handlingBottomBarClicks(position: Int) {
//        when (position) {
//            0 -> {
//                makePositionSelectedToUnSelected(positionSelected)
//                viewDataBinding.nearbyIcon.setImageResource(R.drawable.ic_nearby)
//                viewDataBinding.nearbyTxt.visibility = View.VISIBLE
//                positionSelected = position
//                findNavController(R.id.container).navigate(R.id.navigationFragment)
//            }
//            1 -> {
//                makePositionSelectedToUnSelected(positionSelected)
//                viewDataBinding.notificationIcon.setImageResource(R.drawable.ic_notification_circle_selected)
//                viewDataBinding.notificationTxt.visibility = View.VISIBLE
//                positionSelected = position
//                findNavController(R.id.container).navigate(R.id.notificationFragment)
//            }
//            2 -> {
//                makePositionSelectedToUnSelected(positionSelected)
//                positionSelected = position
//                findNavController(R.id.container).navigate(R.id.homeFragment)
//            }
//            3 -> {
//                makePositionSelectedToUnSelected(positionSelected)
//                viewDataBinding.profileIcon.setImageResource(R.drawable.ic_person_selected)
//                viewDataBinding.profileTxt.visibility = View.VISIBLE
//                positionSelected = position
//                if (accessType == "user") {
//                    findNavController(R.id.container).navigate(R.id.profileFragment)
//                } else {
//                    findNavController(R.id.container).navigate(R.id.teacherProfileFragment)
//                }
//            }
//            4 -> {
//                makePositionSelectedToUnSelected(positionSelected)
//                viewDataBinding.settingsIcon.setImageResource(R.drawable.ic_menu_selected)
//                viewDataBinding.settingsTxt.visibility = View.VISIBLE
//                positionSelected = position
//                findNavController(R.id.container).navigate(R.id.sideMenuFragment)
//            }
//        }
//    }

//    private fun makePositionSelectedToUnSelected(position: Int) {
//        when (position) {
//            0 -> {
//                viewDataBinding.nearbyIcon.setImageResource(R.drawable.ic_nearby_unselected)
//                viewDataBinding.nearbyTxt.visibility = View.GONE
//            }
//            1 -> {
//                viewDataBinding.notificationIcon.setImageResource(R.drawable.ic_notification)
//                viewDataBinding.notificationTxt.visibility = View.GONE
//
//            }
//            2 -> {
//
//            }
//            3 -> {
//                viewDataBinding.profileIcon.setImageResource(R.drawable.ic_person)
//                viewDataBinding.profileTxt.visibility = View.GONE
//
//            }
//            4 -> {
//                viewDataBinding.settingsIcon.setImageResource(R.drawable.ic_menu)
//                viewDataBinding.settingsTxt.visibility = View.GONE
//
//            }
//        }
//    }

    override fun onBottomBarActionClicked(position: Int) {
//        handlingBottomBarClicks(position)
    }

    fun BottomNavigationView.uncheckAllItems() {
        menu.setGroupCheckable(0, true, false)
        for (i in 0 until menu.size()) {
            menu.getItem(i).isChecked = false
        }
        menu.setGroupCheckable(0, true, true)
    }
}
