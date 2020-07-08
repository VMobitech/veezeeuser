package com.versatilemobitech.VeeZee.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.versatilemobitech.VeeZee.Activities.FAQsActivity;
import com.versatilemobitech.VeeZee.Activities.HistoryActivity;
import com.versatilemobitech.VeeZee.Fragments.FaqGeneralFragment;
import com.versatilemobitech.VeeZee.Fragments.FaqReferralFragment;
import com.versatilemobitech.VeeZee.Fragments.FaqRewardsFragment;
import com.versatilemobitech.VeeZee.Fragments.FaqValetFragment;
import com.versatilemobitech.VeeZee.Fragments.FaqVaultFragment;
import com.versatilemobitech.VeeZee.Fragments.HistoryValetFragment;
import com.versatilemobitech.VeeZee.Fragments.HistoryVaultFragment;

public class FAQTabAdapter extends FragmentPagerAdapter {
    int tabCount;
    FAQsActivity activity;

    public FAQTabAdapter(FAQsActivity activity, FragmentManager fragmentManager, int tabCount) {
        super(fragmentManager);
        this.tabCount = tabCount;
        this.activity = activity;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FaqValetFragment historyValetFragment = new FaqValetFragment();
                return historyValetFragment;
            case 1:
                FaqVaultFragment historyVaultFragment = new FaqVaultFragment();
                return historyVaultFragment;
            case 2:
                FaqRewardsFragment faqRewardsFragment = new FaqRewardsFragment();
                return faqRewardsFragment;
            case 3:
                FaqReferralFragment faqReferralFragment = new FaqReferralFragment();
                return faqReferralFragment;
            case 4:
                FaqGeneralFragment faqGeneralFragment = new FaqGeneralFragment();
                return faqGeneralFragment;

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
            title = "Valet";
        } else if (position == 1) {
            title = "Vault";
        }else if (position == 2) {
            title = "Rewards";
        }else if (position == 3) {
            title = "Referral";
        }else if (position == 4) {
            title = "General";
        }
        return title;
    }
}
