package com.versatilemobitech.VeeZee;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.versatilemobitech.VeeZee.utils.Constants;
import com.versatilemobitech.VeeZee.utils.Utility;

public class InstallReferrerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String referrer = intent.getStringExtra("referrer");
        Utility.setSharedPrefStringData(context, Constants.REFERRER_ID,referrer);
    }
}