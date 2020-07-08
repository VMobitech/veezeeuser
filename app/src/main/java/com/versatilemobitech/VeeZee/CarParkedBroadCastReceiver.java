package com.versatilemobitech.VeeZee;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.versatilemobitech.VeeZee.Activities.MainActivity;
import com.versatilemobitech.VeeZee.Model.PushForCarHandOverDetails;

import java.util.ArrayList;

public class CarParkedBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent home = new Intent(context, MainActivity.class);
       /* ArrayList<PushForCarHandOverDetails> mModel = new ArrayList<>();
        mModel = (ArrayList<PushForCarHandOverDetails>) home.getExtras().getSerializable("push_details");*/
        PushForCarHandOverDetails pushForCarHandOverDetails = (PushForCarHandOverDetails) intent.getExtras().getSerializable("push_details");
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        home.putExtra("PARKED", "isParked");
        home.putExtra("TAG", "VALETPUSH");
        home.putExtra("CAR", pushForCarHandOverDetails);
        Log.e("BROADCAST","BROADCAST");
        context.startActivity(home);
    }
}
