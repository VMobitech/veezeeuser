package com.versatilemobitech.VeeZee.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.versatilemobitech.VeeZee.BaseApplication;
import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.SharedPreference;
import com.versatilemobitech.VeeZee.WebUtils.ServerResponse;
import com.versatilemobitech.VeeZee.interfaces.IParseListener;
import com.versatilemobitech.VeeZee.utils.Constants;
import com.versatilemobitech.VeeZee.utils.PopUtils;
import com.versatilemobitech.VeeZee.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ClickToProceedActivity extends AppCompatActivity implements View.OnClickListener, IParseListener {

    Toolbar toolbar;
    TextView toolBarTitle, txt_proceed,text_view_wallet_amount, text_view_v;
    LinearLayout crd_valet, crd_vault;
    String TAG = "";
    String user_id;
    String msg = "";
    String cars_count = "";
    TextView error;
    String is_verified;
    ImageView image_view_coin;
    String clicked = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_to_proceed);
        init();
    }

    private void init() {
        setReferences();
        setClickListeners();
        getArguments();
        callServiceForVerify_Email();
    }

    private void getArguments() {
        Intent i = getIntent();
        TAG = i.getStringExtra("FROM");
        user_id = i.getStringExtra("USER_ID");
        msg = i.getStringExtra("MESSAGE");
        cars_count = i.getStringExtra("CAR_COUNT");


        Log.e("gdvdgcdvc","create car count value "+cars_count);
        //Comes in editProfile
        TAG = i.getStringExtra("TAG");


    }

    private void setClickListeners() {

        crd_vault.setOnClickListener(this);
        crd_valet.setOnClickListener(this);

    }

    private void setReferences() {

        crd_valet = findViewById(R.id.crd_valet);
        crd_vault = findViewById(R.id.crd_vault);
        error = findViewById(R.id.error);
        toolBarTitle = findViewById(R.id.toolBarTitle);
        text_view_v = findViewById(R.id.text_view_credits_v);
        image_view_coin = findViewById(R.id.image_view_coin);
        text_view_wallet_amount = findViewById(R.id.text_view_wallet_amount);

        is_verified = Utility.getSharedPrefStringData(this, Constants.IS_VERIFIED);


    }
    /*public boolean onSupportNavigateUp() {
       // onBackPressed();
        if (Utility.getSharedPrefStringData(ClickToProceedActivity.this,Constants.IS_EMAIL_VERIFIED_FROM).equalsIgnoreCase("EditProfile")) {
            startActivity(new Intent(ClickToProceedActivity.this, MenuProfileActivity.class));

        } else if(Utility.getSharedPrefStringData(ClickToProceedActivity.this,Constants.IS_EMAIL_VERIFIED_FROM).equalsIgnoreCase("SignUp")) {
            startActivity(new Intent(ClickToProceedActivity.this, ProfileActivity.class));
        }
        return true;
    }*/

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.crd_valet:
                clicked = "valet";
                callServiceForVerify_Email();
                /*if (is_verified.equalsIgnoreCase("1")){
                    if (TAG.equals("PROFILE")) {
                        Intent i = new Intent(ClickToProceedActivity.this, CarDetailsActivity.class);
                        i.putExtra("TAG", "CLICKTO");
                        i.putExtra("USER_ID", user_id);
                        startActivity(i);
                    }
               *//* else if(msg.equalsIgnoreCase("Registration successful")){
                    Intent cars = new Intent(ClickToProceedActivity.this, MainActivity.class);
                    cars.putExtra("TAG", "CARS");
                    startActivity(cars);
                }*//*
                    else {
                        if (cars_count.equalsIgnoreCase("0") || cars_count.isEmpty()) {

                            Intent intent = new Intent(ClickToProceedActivity.this, CarDetailsActivity.class);
                            //intent.putExtra(LoginActivity.MOBILE, mobile);
                            intent.putExtra("TAG", TAG);
                            intent.putExtra(ProfileActivity.USER_ID, user_id);
                            startActivity(intent);

                        } else {
                            Intent valet = new Intent(ClickToProceedActivity.this, MainActivity.class);
                            valet.putExtra("TAG", "VALET");
                            startActivity(valet);
                        }
                    }
        }else {
                    error.startAnimation(AnimationUtils.loadAnimation(ClickToProceedActivity.this, android.R.anim.slide_in_left));
                }*/
                break;
            case R.id.crd_vault:
                clicked = "vault";
                callServiceForVerify_Email();
               /* if(is_verified.equalsIgnoreCase("1")) {
                    Intent vault = new Intent(ClickToProceedActivity.this, MainActivity.class);
                    vault.putExtra("TAG", "VAULT");
                    startActivity(vault);
                }else {
                    error.startAnimation(AnimationUtils.loadAnimation(ClickToProceedActivity.this, android.R.anim.slide_in_left));
                }*/
                break;
        }

    }

    private void callServiceForVerify_Email() {

        if (Utility.isNetworkAvailable(this)) {
            Utility.showLoadingDialog(this, "Loading...", false);
            int userId = Integer.parseInt(Utility.getSharedPrefStringData(this, Constants.USER_ID));
            try{
                JSONObject params = new JSONObject();
                params.put("user_id",userId);
                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequestJSonObject(this, "POST", Constants.VERIFY_EMAIL_ID ,params, new HashMap<String, String>(), this, Constants.VERIFY_EMAIL_ID_CODE);
            }catch (Exception e){
                e.printStackTrace();
            }

        } else {
            PopUtils.alertDialog(this, Utility.getResourcesString(this, R.string.check_internet), null);
        }
    }

    private void setDialogforExit() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ClickToProceedActivity.this, R.style.CustomAlertDialog);
        ViewGroup mViewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(mViewGroup.getContext()).inflate(R.layout.custom_dialog_exit, mViewGroup, false);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        TextView txt_yes, txt_cancel;
        txt_yes = dialogView.findViewById(R.id.txt_yes);
        txt_cancel = dialogView.findViewById(R.id.txt_cancel);
        txt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                ClickToProceedActivity.this.finishAffinity();
            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
         setDialogforExit();
        /* super.onBackPressed();

        if(TAG!=null) {
            if (Utility.getSharedPrefStringData(ClickToProceedActivity.this,Constants.IS_EMAIL_VERIFIED_FROM).equalsIgnoreCase("EditProfile")) {
                startActivity(new Intent(ClickToProceedActivity.this, MenuProfileActivity.class));

            } else if(Utility.getSharedPrefStringData(ClickToProceedActivity.this,Constants.IS_EMAIL_VERIFIED_FROM).equalsIgnoreCase("SignUp")) {
                startActivity(new Intent(ClickToProceedActivity.this, ProfileActivity.class));
            }
        }else {

        }*/
    }

    @Override
    public void ErrorResponse(String response, int requestCode) {

    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        Log.e( "SuccessResponse: ","is_verify_Email"+response );
        Utility.hideLoadingDialog();
        if(requestCode== Constants.VERIFY_EMAIL_ID_CODE){
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");

                if(status.equalsIgnoreCase("true")){
                    is_verified = jsonObject.optString("is_verified");
                    Utility.setSharedPrefStringData(this,Constants.IS_VERIFIED,is_verified);
                    is_verified = Utility.getSharedPrefStringData(this, Constants.IS_VERIFIED);

                    if (is_verified.equalsIgnoreCase("1")){
                        error.setVisibility(View.GONE);

                        if(clicked.equalsIgnoreCase("valet")) {


                            Log.e("gdvdgcdvc","car count value "+cars_count);

                            Log.e("fdvbhfdhcbd","OUT SIDE  "+TAG);

                            Log.e("fdvbhfdhcbd","OUT SIDE  "+SharedPreference.getStringPreference(this,"EEDITPROFILE"));

                            Log.e("gdvdgcdvc","value "+SharedPreference.getStringPreference(this,"EMAILCHNAGED"));

                            if (SharedPreference.getStringPreference(this,"EMAILCHNAGED")!=null &&
                                    SharedPreference.getStringPreference(this,"EMAILCHNAGED").equalsIgnoreCase("YES"))
                            {

                                Intent valet = new Intent(ClickToProceedActivity.this, MainActivity.class);
                                valet.putExtra("TAG", "VALET");
                                startActivity(valet);
                                return;
                            }


                            if(TAG!=null){
                                Log.e("fdvbhfdhcbd","TAG "+TAG);

                            if (TAG.equals("PROFILE")) {
                                Intent i = new Intent(ClickToProceedActivity.this, CarDetailsActivity.class);
                                i.putExtra("TAG", "CLICKTO");
                                i.putExtra("USER_ID", user_id);
                                startActivity(i);
                            } else {

                                Log.e("fdvbhfdhcbd","IN SIDE TAG "+TAG);

                                Log.e("gdvdgcdvc","else in car count value "+cars_count);



                                if (cars_count.equalsIgnoreCase("0") || cars_count.isEmpty() &&! TAG.equalsIgnoreCase("EditProfile")) {

                                    Intent intent = new Intent(ClickToProceedActivity.this, CarDetailsActivity.class);
                                    //intent.putExtra(LoginActivity.MOBILE, mobile);
                                    intent.putExtra("TAG", TAG);
                                    intent.putExtra(ProfileActivity.USER_ID, user_id);
                                    startActivity(intent);


                                }
                                else if (TAG.equalsIgnoreCase("EditProfile"))
                                {
                                    Intent valet = new Intent(ClickToProceedActivity.this, MainActivity.class);
                                    valet.putExtra("TAG", "VALET");
                                    valet.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(valet);
                                }
                                else {
                                    Intent valet = new Intent(ClickToProceedActivity.this, MainActivity.class);
                                    valet.putExtra("TAG", "VALET");
                                    startActivity(valet);
                                }
                            }
                        }else {

                                Log.e("gdvdgcdvc","else out car count value "+cars_count);

                                if (cars_count==null || cars_count.equalsIgnoreCase("0") || cars_count.isEmpty() ) {
                                  //  Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ClickToProceedActivity.this, CarDetailsActivity.class);
                                    //intent.putExtra(LoginActivity.MOBILE, mobile);
                                  //  intent.putExtra("TAG", "CAR");
                                    intent.putExtra("TAG", "VALET");
                                    intent.putExtra(ProfileActivity.USER_ID, user_id);
                                    startActivity(intent);

                                }
                                else
                                {
                                    Intent valet = new Intent(ClickToProceedActivity.this, MainActivity.class);
                                    valet.putExtra("TAG", "VALET");
                                    startActivity(valet);
                                }


                            }
                        }
                        else if(clicked.equalsIgnoreCase("vault")){
                            Intent vault = new Intent(ClickToProceedActivity.this, MainActivity.class);
                            vault.putExtra("TAG", "VAULT");
                            startActivity(vault);
                        }

                    }else {
                        showerror();
                        error.startAnimation(AnimationUtils.loadAnimation(ClickToProceedActivity.this, android.R.anim.slide_in_left));


                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
    private void showerror(){
        new CountDownTimer(8000, 10000) {
            public void onTick(long millisUntilFinished) {
                error.setVisibility(View.VISIBLE);
            }

            public void onFinish() {
                error.setVisibility(View.GONE);
            }


        }.start();
    }
}
