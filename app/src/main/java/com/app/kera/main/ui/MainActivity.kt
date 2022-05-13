package com.app.kera.main.ui

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.app.kera.R
import com.app.kera.app.ForceUpdateChecker
import com.app.kera.databinding.ActivityMainBinding
import com.app.kera.home.HomeFragment
import com.app.kera.navigation.NavigationFragment
import com.app.kera.notification.NotificationFragment
import com.app.kera.profile.ProfileFragment
import com.app.kera.sideMenu.SideMenuFragment
import com.app.kera.teacherProfile.TeacherProfileFragment
import com.app.kera.visitor.NeedToLogin
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.shape.CornerFamily.ROUNDED
import com.google.android.material.shape.MaterialShapeDrawable
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), NavigationFragment.CallBack, ForceUpdateChecker.OnUpdateNeededListener, ForceUpdateChecker.onUpatePreferedListner{
    var positionSelected = 2

    private val mainViewModel: MainViewModel by viewModel()
    lateinit var viewDataBinding: ActivityMainBinding
    lateinit var accessType: String
    var teacher : Int = 0
    private var mViewPagerAdapter: ViewPagerAdapter? = null
    val home: Fragment = HomeFragment()
    val notification: Fragment = NotificationFragment()
    val profile: Fragment = ProfileFragment()
    val sideMenu: Fragment = SideMenuFragment()
    val needtologin: Fragment = NeedToLogin()
    val nearBy: Fragment = NavigationFragment()
    val teacherProfile: Fragment = TeacherProfileFragment()
    val fm: FragmentManager = supportFragmentManager
    var active: Fragment = home
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewModel = mainViewModel
        teacher = R.id.teacherProfileFragment
        accessType = mainViewModel.getUserType()
        val navController = findNavController(R.id.fragment)
        viewDataBinding.bottomNavigationView.uncheckAllItems()
        viewDataBinding.bottomNavigationView.setupWithNavController(navController)

        mainViewModel.getNurseryData()



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

        viewDataBinding.floatingActionButton2.setOnClickListener(View.OnClickListener {
            if (accessType == "visitor") {
                fm.beginTransaction().hide(active).show(needtologin).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out).commit()
                active = needtologin
            } else {
                fm.beginTransaction().hide(active).show(home).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out).commit()
                active = home

            }
            viewDataBinding.bottomNavigationView.uncheckAllItems()

        })


        viewDataBinding.bottomNavigationView.setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )
        fm.beginTransaction().add(R.id.fragment, teacherProfile, "7").hide(teacherProfile).commit();
        fm.beginTransaction().add(R.id.fragment, profile, "6").hide(profile).commit();
        fm.beginTransaction().add(R.id.fragment, sideMenu, "5").hide(sideMenu).commit();

        fm.beginTransaction().add(R.id.fragment, notification, "3").hide(notification).commit();
        fm.beginTransaction().add(R.id.fragment, nearBy, "2").hide(nearBy).commit();
        active = if (accessType == "visitor") {
            fm.beginTransaction().add(R.id.fragment, needtologin, "1").commit();
            fm.beginTransaction().add(R.id.fragment, home, "4").hide(home).commit();
            needtologin
        }else{
            fm.beginTransaction().add(R.id.fragment, home, "1").commit();
            fm.beginTransaction().add(R.id.fragment, needtologin, "4").hide(needtologin).commit();

            home
        }


//        viewDataBinding.bottomNavigationView.setOnNavigationItemSelectedListener {
//
//            when (it.itemId) {
//                R.id.navigationFragment -> {
//                    navController.navigate(R.id.navigationFragment)
//
//
//                }
//                R.id.notificationFragment -> {
//                    navController.navigate(R.id.notificationFragment)
//
//
//                }
//
//                R.id.profileFragment -> {
//                    if (accessType == "user") {
//                        navController.navigate(R.id.profileFragment)
//
//                    } else if (accessType == "visitor") {
//                        navController.navigate(R.id.needToLogin)
//                    } else {
//                        navController.navigate(R.id.teacherProfileFragment)
//
//                    }
//                }
//                R.id.sideMenuFragment -> {
//                    navController.navigate(R.id.sideMenuFragment)
//                }
//            }
//            true
//        }
        Log.i("Access Type for remote", accessType)
        ForceUpdateChecker.with(this).onUpdateNeeded(this).UserType(accessType).check()
        ForceUpdateChecker.with(this).onUpdatePrefered(this).UserType(accessType).check()
    }


private var mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener? =
    BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigationFragment -> {
                fm.beginTransaction().hide(active).show(nearBy).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out).commit()
                active = nearBy
                return@OnNavigationItemSelectedListener true


            }
            R.id.notificationFragment -> {


                fm.beginTransaction().hide(active).show(notification).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out).commit()
                active = notification
                return@OnNavigationItemSelectedListener true
            }

            R.id.profileFragment -> {
                if (accessType == "user") {

                    fm.beginTransaction().hide(active).show(profile).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out).commit()
                    active = profile
                    return@OnNavigationItemSelectedListener true

                } else if (accessType == "visitor") {
                    fm.beginTransaction().hide(active).show(needtologin).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out).commit()
                    active = needtologin
                    return@OnNavigationItemSelectedListener true
                } else {

                    fm.beginTransaction().hide(active).show(teacherProfile).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out).commit()
                    active = teacherProfile
                    return@OnNavigationItemSelectedListener true

                }
            }
            R.id.sideMenuFragment -> {
                fm.beginTransaction().hide(active).show(sideMenu).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out).commit()
                active = sideMenu
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }
    override fun onBottomBarActionClicked(position: Int) {
//        handlingBottomBarClicks(position)

    }
//    fun switchFragment(fragment: Fragment) {
//
//
//        val fm: FragmentManager = supportFragmentManager
//        val transaction: FragmentTransaction = fm.beginTransaction().setCustomAnimations(
//            R.anim.fade_in,
//            R.anim.fade_out
//        )
//
//        transaction.replace(R.id.fragment, fragment)
//        transaction.commit()

//    }
    fun BottomNavigationView.uncheckAllItems() {
        menu.setGroupCheckable(0, true, false)
        for (i in 0 until menu.size()) {
            menu.getItem(i).isChecked = false
        }
        menu.setGroupCheckable(0, true, true)
    }
    private fun redirectStore(updateUrl: String) {

        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(updateUrl)
        startActivity(i)
    }

    override fun onUpdateNeeded(updateUrl: String?) {
        val dialog = AlertDialog.Builder(this, 5)
            .setCancelable(false)
            .setIcon(R.drawable.kera_box)
            .setTitle(resources.getString(R.string.Newversionavailable))
            .setMessage(resources.getString(R.string.Pleaseupdateapptonewversiontocontinuereposting))
            .setPositiveButton(
                resources.getString(R.string.Update)
            ) { dialog, which ->
                redirectStore(updateUrl!!)
                finish()
            }
        dialog.show()
    }

    override fun onUpdatePrefered(updateUrl: String?) {
        val dialog = AlertDialog.Builder(this, 5)
            .setIcon(R.drawable.kera_box)
            .setTitle(resources.getString(R.string.Newversionavailable))
            .setMessage(resources.getString(R.string.Pleaseupdateapptonewversiontocontinuereposting))
            .setPositiveButton(
                resources.getString(R.string.Update)
            ) { dialog, which ->
                redirectStore(updateUrl!!)
                finish()
            }.setNegativeButton(
                resources.getString(R.string.skip)
            ) { dialog, which -> dialog.dismiss() }.create()
        dialog.show()
    }
}
