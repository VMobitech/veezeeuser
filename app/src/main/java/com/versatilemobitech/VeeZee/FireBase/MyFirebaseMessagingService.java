package com.versatilemobitech.VeeZee.FireBase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.EventLog;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.versatilemobitech.VeeZee.Activities.MainActivity;
import com.versatilemobitech.VeeZee.Activities.MapActivity;
import com.versatilemobitech.VeeZee.Activities.SplashScreen;
import com.versatilemobitech.VeeZee.BaseApplication;
import com.versatilemobitech.VeeZee.CarParkedBroadCastReceiver;
import com.versatilemobitech.VeeZee.Model.PushForCarHandOverDetails;
import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.utils.Constants;
import com.versatilemobitech.VeeZee.utils.Utility;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * Created by Excentd11 on 7/29/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    public String title, message, largeIcon;

    private static final String ADMIN_CHANNEL_ID = "ADMIN";
    NotificationManager notificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("pushnotification", "" + remoteMessage.getData().toString());

            if (remoteMessage.getData().isEmpty()) {
                Log.e("notification one", "" + remoteMessage.getData());
                showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
                //showNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("body"));
            } else {
                Log.e("notification two", "" + remoteMessage.getData());
                //showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
                showNotification(remoteMessage.getData());
            }

    }

    private void showNotification(Map<String, String> data) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("TAG","VALET");
        String body = data.get("body");
        String dataStatus = data.get("status");
        title = data.get("title");


        if (dataStatus != null) {
            if (dataStatus.equalsIgnoreCase("arrived")) {

                Intent new_intentt = new Intent("GET ARRIVED STATUS");
                new_intentt.putExtra("CARSTATUS_PUSH", dataStatus);
                new_intentt.putExtra("user_car_id",data.get("user_car_id"));
                new_intentt.putExtra("timer",data.get("timer"));
                this.sendBroadcast(new_intentt);
            }else if(dataStatus.equalsIgnoreCase("direct_scan")){
                Intent intentCarReq = new Intent("DIRECT SCAN");
                intentCarReq.putExtra("is_requested", data.get("is_requested"));
                intentCarReq.putExtra("user_car_id",data.get("user_car_id"));
                intentCarReq.putExtra("status",data.get("status"));
                this.sendBroadcast(intentCarReq);
            }
        }

        if(body.contains("added to")){
            intent.putExtra("TAG","CARS");
        }else if(body.contains("Click to view location")){
            String latitude = data.get("latitude");
            String longitute = data.get("longitude");
            intent = new Intent(this,MapActivity.class);
            intent.putExtra("lat",latitude);
            intent.putExtra("lng", longitute);
            Log.e(TAG, "showNotification Parked: " );
        }else if(body.contains("Late Penalty")){
            String credits = data.get("update_credits");
            Intent new_intentt = new Intent("CREDITS STATUS");
            Utility.setSharedPrefStringData(this, Constants.WALLET_AMOUNT, "" +credits );
            new_intentt.putExtra("update_credits",data.get("update_credits"));
            this.sendBroadcast(new_intentt);
        }



        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setColor(getResources().getColor(R.color.white))
                        .setContentTitle(title)
                        .setSmallIcon(R.drawable.text_empty)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.text_empty))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setLargeIcon(null)
                        .setSound(defaultSoundUri);
        if(Utility.getSharedPrefBooleanData(this,Constants.IS_LOGGEDIN))
        {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT); //FLAG_ONE_SHOT
            notificationBuilder.setContentIntent(pendingIntent);
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.text_empty);
            notificationBuilder.setColor(getResources().getColor(R.color.zxing_transparent));
        } else {
            notificationBuilder.setSmallIcon(R.drawable.text_empty);
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        int notificationId = new Random().nextInt(60000);
        notificationManager.notify(notificationId , notificationBuilder.build());


        String push ="";
        ArrayList<PushForCarHandOverDetails> mList = new ArrayList<>();
        PushForCarHandOverDetails mPushForCarHandOverDetails = null;
        if(dataStatus!=null)
            if(dataStatus.equalsIgnoreCase("scanned")||dataStatus.contains("hand")) {
                // intent.putExtra("PARKED", "PUSH");
                push = "PUSH";
                Constants.isTipPush = true;
                if(dataStatus.equalsIgnoreCase("handedover") ||dataStatus.contains("scanned")) {
                    String title = data.get("title");
                    String image = data.get("image");
                    String driver_name = data.get("driver_name");
                    String  status = data.get("status");
                    String token = data.get("token");
                    String mobile = data.get("mobile");
                    String serial_no = data.get("serial_no");
                    String driver_id = data.get("driver_id");
                    String parking_id = data.get("parking_id");

                    mPushForCarHandOverDetails =
                            new PushForCarHandOverDetails( mobile,status,serial_no,image,title,token,driver_name,driver_id,parking_id);

                    mList.add(mPushForCarHandOverDetails);
                    Constants.pushDetailsList = mPushForCarHandOverDetails;
                }

            }else if(body.contains("parked")){
                Intent intent1 = new Intent(this,MainActivity.class);
                intent1.putExtra("TAG","VALET");
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);


            }else if(body.contains("Baggage deposited")){
                Constants.isTipPush = false;
                Intent intent1 = new Intent(this,MainActivity.class);
                intent1.putExtra("TAG","VAULT");
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            }
        if(push.equals("PUSH")) {

            Intent brodCast = new Intent(this, CarParkedBroadCastReceiver.class);
            brodCast.putExtra("push_details",mPushForCarHandOverDetails);
            brodCast.setAction("com.versatilemobitech.VeeZee");
            sendBroadcast(brodCast);
            //EventBus.getDefault().post(mPushForCarHandOverDetails);
        }

    }

    private void showNotification(String title, String body) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("TAG","VALET");

//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //UserDetails.getInstance(this).setFrom("push");
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT); //FLAG_ONE_SHOT

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                      //  .setSmallIcon(getNotificationIcon())
                        .setSmallIcon(R.drawable.text_empty)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.webpnet_resizeimage))
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        int notificationId = new Random().nextInt(60000);
        notificationManager.notify(notificationId  /*ID of notification*/, notificationBuilder.build());

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("Device Token","VeeZee Device Token "+s);
        Utility.setSharedPrefStringData(getApplicationContext(), Constants.DEVICE_TOKEN,s);
    }
    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_vault_colored : R.mipmap.ic_launcher;
    }
}