package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAccessorAdapter extends FragmentPagerAdapter {

    public TabsAccessorAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                MenuFragment menuFragment = new MenuFragment();
                return menuFragment;
            case 1:
                PastOrdersFragment pastOrdersFragment = new PastOrdersFragment();
                return pastOrdersFragment;
            case 2:
                ProfessorCurrentOrdersFragment professorCurrentOrdersFragment = new ProfessorCurrentOrdersFragment();
                return professorCurrentOrdersFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Menu";
            case 1:
                return "Pending Orders";
            case 2:
                return "Professor Orders";
            default:
                return null;
        }
    }
}
