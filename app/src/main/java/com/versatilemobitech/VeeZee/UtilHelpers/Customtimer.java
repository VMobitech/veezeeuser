package com.versatilemobitech.VeeZee.UtilHelpers;

import android.os.CountDownTimer;


public class Customtimer extends CountDownTimer {

    long current_count = 0;
    boolean already_running = false;

    public Customtimer(long millisInFuture, long countDownInterval) {

        super(millisInFuture, countDownInterval);
        current_count = millisInFuture;

    }


    @Override
    public void onTick(long l) {

        already_running = true;
        current_count = l;
    }


    public long getCurrent_count() {

        return current_count;
    }

    public boolean isAlready_running() {

        return already_running;
    }

    @Override
    public void onFinish() {

        current_count = 0;
    }
}
