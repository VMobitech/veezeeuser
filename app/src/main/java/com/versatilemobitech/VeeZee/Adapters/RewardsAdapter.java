package com.versatilemobitech.VeeZee.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.versatilemobitech.VeeZee.Fragments.RewardItemFragment;
import com.versatilemobitech.VeeZee.Model.Rewards;

import java.util.ArrayList;

public class RewardsAdapter extends FragmentStatePagerAdapter {

    ArrayList<Rewards> rewardsArrayList;


    public RewardsAdapter(@NonNull FragmentManager fm, ArrayList<Rewards> rewardsArrayList) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.rewardsArrayList = rewardsArrayList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return RewardItemFragment.newInstance(rewardsArrayList.get(position));
    }

    @Override
    public int getCount() {
        return rewardsArrayList.size();
    }

}
