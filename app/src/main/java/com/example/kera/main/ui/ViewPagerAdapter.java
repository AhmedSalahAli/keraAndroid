package com.example.kera.main.ui;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.kera.home.HomeFragment;
import com.example.kera.navigation.NavigationFragment;
import com.example.kera.notification.NotificationFragment;
import com.example.kera.profile.ProfileFragment;
import com.example.kera.sideMenu.SideMenuFragment;
import com.example.kera.teacherProfile.TeacherProfileFragment;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NavigationFragment();
            case 1:
                return new NotificationFragment();
            case 2:
                return new HomeFragment();
            case 3:
                return new ProfileFragment();
            case 4:
                return new SideMenuFragment();
            case 5:
                return new TeacherProfileFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
}