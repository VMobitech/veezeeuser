package com.versatilemobitech.VeeZee;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.versatilemobitech.VeeZee.Activities.LoginActivity;
import com.versatilemobitech.VeeZee.Activities.MenuActivity;
import com.versatilemobitech.VeeZee.WebUtils.ServerResponse;
import com.versatilemobitech.VeeZee.interfaces.IParseListener;
import com.versatilemobitech.VeeZee.utils.Constants;
import com.versatilemobitech.VeeZee.utils.PopUtils;
import com.versatilemobitech.VeeZee.utils.Utility;

import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by Excentd11 on 4/29/2017.
 */

public class BaseApplication extends AppCompatActivity implements IParseListener {
    public static final String TAG = BaseApplication.class.getSimpleName();
    private static Context context;

    public static Context getContext() {
        return context;
    }

    private RequestQueue mRequestQueue;

    private static BaseApplication mInstance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }


    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    public static String getDeviceId(Context context) {

        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public void callServiceForLogOut() {

        if (Utility.isNetworkAvailable(this)) {
            Utility.showLoadingDialog(this, "Loading...", false);
           if(Utility.getSharedPrefStringData(this, Constants.USER_ID)!=null&&!Utility.getSharedPrefStringData(this, Constants.USER_ID).isEmpty()
           &&!Utility.getSharedPrefStringData(this, Constants.USER_ID).equals("")) {
               int userId = Integer.parseInt(Utility.getSharedPrefStringData(this, Constants.USER_ID));

               try {
                   JSONObject params = new JSONObject();
                   params.put("user_id", userId);
                   params.put("device_id", getDeviceId(this));
                   params.put("device_type ", "1");
                   ServerResponse serverResponse = new ServerResponse();
                   serverResponse.serviceRequestJSonObject(this, "POST", Constants.LOG_OUT, params, new HashMap<String, String>(), this, Constants.LOG_OUT_CODE);
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
        } else {
            PopUtils.alertDialog(this, Utility.getResourcesString(this, R.string.check_internet), null);
        }
    }

    @Override
    public void ErrorResponse(String response, int requestCode) {
        Utility.hideLoadingDialog();
        PopUtils.alertDialog(this, Utility.getResourcesString(this, R.string.something_went_wrong), null);
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        Utility.hideLoadingDialog();
        Log.e("LOG_OUT_CODE", "LOG_OUT_CODE" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.optString("status");
            String message = jsonObject.optString("message");
            if (status.equals("true")) {

                Utility.setSharedPrefStringData(BaseApplication.this, Constants.USER_ID, "");
                Utility.setSharedPrefStringData(BaseApplication.this, Constants.NAME, "");
                Utility.setSharedPrefStringData(BaseApplication.this, Constants.MOBILE, "");
                Utility.setSharedPrefStringData(BaseApplication.this, Constants.EMAIL, "");
                Utility.setSharedPrefStringData(BaseApplication.this, Constants.IMAGE, "");
                Utility.setSharedPrefStringData(BaseApplication.this, Constants.DOB, "");
                Utility.setSharedPrefStringData(BaseApplication.this, Constants.GENDER, "");
                Utility.setSharedPrefStringData(BaseApplication.this, Constants.WALLET_AMOUNT, "");
                Utility.setSharedPrefStringData(BaseApplication.this,Constants.STATE_ID,"");
                Utility.setSharedPrefStringData(BaseApplication.this,Constants.CITY_ID,"");
                Utility.setSharedPrefStringData(BaseApplication.this,Constants.CITY,"");
                Utility.setSharedPrefStringData(BaseApplication.this,Constants.STATE,"");

                //facebook logout
                //Toast.makeText(getApplicationContext(),"Logout Successfully",Toast.LENGTH_SHORT).show();
                LoginManager.getInstance().logOut();
                //google+ logout
                GoogleSignInOptions gso = new GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                        build();

                GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(BaseApplication.this,gso);
                googleSignInClient.signOut();
                /*startActivity(new Intent(MenuActivity.this, LoginActivity.class));*/
                Utility.setSharedPrefBooleanData(BaseApplication.this,Constants.IS_LOGGEDIN,false);
                Intent intent = new Intent(BaseApplication.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                this.finishAffinity();
            } else {
                Utility.showToastMessage(this, message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
