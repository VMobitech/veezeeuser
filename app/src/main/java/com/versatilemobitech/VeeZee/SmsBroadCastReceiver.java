package com.versatilemobitech.VeeZee;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsBroadCastReceiver extends BroadcastReceiver {

    private Listener listener;
    private Context mContext;

    public SmsBroadCastReceiver() {
    }

    public SmsBroadCastReceiver(Context mCotext,Listener listener) {
        this.listener = listener;
        this.mContext = mCotext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.getAction()) {

            Bundle bundle = intent.getExtras();
            Status status = (Status) bundle.get(SmsRetriever.EXTRA_STATUS);
            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:


                    String message = (String) bundle.get(SmsRetriever.EXTRA_SMS_MESSAGE);

                    String finalMessage = message.substring(0, message.length() - 11);

                    String otp = finalMessage.replaceAll("[^0-9]", "");

                    if (listener != null) {
                        listener.onSMSReceived(otp);
                    }


                    break;

                case CommonStatusCodes.TIMEOUT:
                    Toast.makeText(mContext,"TIME OUT !!",Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    }

    public interface Listener {
        void onSMSReceived(String otp);

        void TimeOut();
    }
}
