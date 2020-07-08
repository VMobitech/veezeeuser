package com.versatilemobitech.VeeZee.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.versatilemobitech.VeeZee.Activities.HistoryActivity;
import com.versatilemobitech.VeeZee.Fragments.ClubsFragment;
import com.versatilemobitech.VeeZee.Fragments.FitnessFragment;
import com.versatilemobitech.VeeZee.Fragments.HistoryValetFragment;
import com.versatilemobitech.VeeZee.Fragments.HistoryVaultFragment;
import com.versatilemobitech.VeeZee.Fragments.HotelsFragment;
import com.versatilemobitech.VeeZee.Fragments.PartnerFragment;
import com.versatilemobitech.VeeZee.Fragments.RestaurantFragment;

public class HistoryTabAdapter extends FragmentPagerAdapter {
    int tabCount;
    HistoryActivity activity;

    public HistoryTabAdapter(HistoryActivity activity, FragmentManager fragmentManager, int tabCount) {
        super(fragmentManager);
        this.tabCount = tabCount;
        this.activity = activity;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                HistoryValetFragment historyValetFragment = new HistoryValetFragment();
                return historyValetFragment;
            case 1:
                HistoryVaultFragment historyVaultFragment = new HistoryVaultFragment();
                return historyVaultFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "VALET";
        } else if (position == 1) {
            title = "VAULT";
        }
        return title;
    }
}
