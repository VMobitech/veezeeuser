package com.versatilemobitech.VeeZee.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.versatilemobitech.VeeZee.Model.PushForCarHandOverDetails;
import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.utils.Constants;
import com.versatilemobitech.VeeZee.utils.Utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.versatilemobitech.VeeZee.UtilHelpers.AppSignatureHelper.TAG;

public class SplashScreen extends Activity {
    String carStatusPush = "";
    PushForCarHandOverDetails mPushForCarHandOverDetails;
    String latitude,longitude;
    String pushBody = " ";
    String credits = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("HASHKEY", "printHashKey() Hash Key: " + hashKey);
            }
            Log.e("Token",""+Utility.getSharedPrefStringData(this,Constants.DEVICE_TOKEN));
        } catch (NoSuchAlgorithmException e) {
            Log.e("HASHKEY", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("HASHKEY", "printHashKey()", e);
        }

        FirebaseApp.initializeApp(this);
        setSplashScreen();
    }

    private void getIntentArgs() {
        Intent i = getIntent();
        if(i!=null){
            Log.e(TAG, "getArgument: "+i.getStringExtra("status") );
            String title = i.getStringExtra("title");
            String image = i.getStringExtra("image");
            pushBody     = i.getStringExtra("body");
            String driver_name = i.getStringExtra("driver_name");
            String  status = i.getStringExtra("status");
            String token = i.getStringExtra("token");
            String mobile = i.getStringExtra("mobile");
            String serial_no =i.getStringExtra("serial_no");
            String driver_id = i.getStringExtra("driver_id");
            String parking_id = i.getStringExtra("parking_id");
            latitude = i.getStringExtra("latitude");
            longitude = i.getStringExtra("longitude");
            credits = i.getStringExtra("update_credits");
            carStatusPush = status;
            mPushForCarHandOverDetails =
                    new PushForCarHandOverDetails( mobile,status,serial_no,image,title,token,driver_name,driver_id,parking_id);
        }
    }

    private void setSplashScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getIntentArgs();
                //Utility.setSharedPrefBooleanData(SplashScreen.this, Constants.IS_LOGGEDIN, true);

                    if (Utility.getSharedPrefBooleanData(SplashScreen.this, Constants.IS_LOGGEDIN)) {

                        Intent activity = new Intent();
                        if (latitude != null && !latitude.isEmpty()) {

                            activity = new Intent(SplashScreen.this, MapActivity.class);
                            activity.putExtra("lat", latitude);
                            activity.putExtra("lng", longitude);
                            startActivity(activity);
                            finish();
                        } else if (pushBody != null) {
                            if (pushBody.contains("added to")) {
                                activity = new Intent(SplashScreen.this, MainActivity.class);
                                activity.putExtra("TAG", "CARS");
                                startActivity(activity);
                                finish();
                            } else {
                                activity = new Intent(SplashScreen.this, MainActivity.class);
                                activity.putExtra("TAG", "VALET");
                                if (carStatusPush != null && !carStatusPush.equalsIgnoreCase("")) {
                                    activity.putExtra("mPushForCarHandOverDetails", mPushForCarHandOverDetails);
                                    activity.putExtra("STATUS", carStatusPush);
                                }
                                startActivity(activity);
                                finish();
                            }
                        } else {
                            if (Utility.getSharedPrefStringData(SplashScreen.this, Constants.IS_VERIFIED).equalsIgnoreCase("1")) {
                            activity = new Intent(SplashScreen.this, MainActivity.class);
                            activity.putExtra("TAG", "VALET");
                            startActivity(activity);
                            }else {
                                Intent i =  new Intent(SplashScreen.this, ClickToProceedActivity.class);
                                activity.putExtra("TAG", "VALET");
                                startActivity(i);
                            }
                        }
                    } else {
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        intent.putExtra("backup", "no");
                        startActivity(intent);
                        finish();
                    }


            }
        }, 2000);
    }
}
