package com.versatilemobitech.VeeZee.Adapters;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.versatilemobitech.VeeZee.Fragments.ClubsFragment;
import com.versatilemobitech.VeeZee.Fragments.FitnessFragment;
import com.versatilemobitech.VeeZee.Fragments.HotelsFragment;
import com.versatilemobitech.VeeZee.Fragments.PartnerFragment;
import com.versatilemobitech.VeeZee.Fragments.RestaurantFragment;
import com.versatilemobitech.VeeZee.Model.PartnerCategories;

import java.util.ArrayList;
import java.util.List;

public class PartnerTabAdapter extends FragmentPagerAdapter {
    int tabCount;
    PartnerFragment activity;
    ArrayList<PartnerCategories> partnerCategoriesArrayList;
    List<Fragment> fragmentList;

    public PartnerTabAdapter(PartnerFragment activity, FragmentManager fragmentManager, int tabCount, ArrayList<PartnerCategories> partnerCategoriesArrayList, List<Fragment> fragmentList) {
        super(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.tabCount = tabCount;
        this.activity = activity;
        this.partnerCategoriesArrayList = partnerCategoriesArrayList;
        this.fragmentList = fragmentList;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = partnerCategoriesArrayList.get(position).getName();
        return title;
    }
}
