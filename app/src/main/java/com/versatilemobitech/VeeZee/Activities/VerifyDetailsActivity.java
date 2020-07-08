package com.versatilemobitech.VeeZee.Activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.broooapps.otpedittext2.OnCompleteListener;
import com.broooapps.otpedittext2.OtpEditText;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.versatilemobitech.VeeZee.BaseApplication;
import com.versatilemobitech.VeeZee.Model.UserDetails;
import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.SmsBroadCastReceiver;
import com.versatilemobitech.VeeZee.UtilHelpers.AppSignatureHelper;
import com.versatilemobitech.VeeZee.WebUtils.ServerResponse;
import com.versatilemobitech.VeeZee.interfaces.IParseListener;
import com.versatilemobitech.VeeZee.utils.Constants;
import com.versatilemobitech.VeeZee.utils.PopUtils;
import com.versatilemobitech.VeeZee.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class VerifyDetailsActivity extends AppCompatActivity implements View.OnClickListener, IParseListener {

    OtpEditText edit_text_otp;
    TextView text_view_resend, text_view_mobile_number, text_view_verify, text_view_otp_expire, text_view_otp_time;
    String otp, enteredOtp, mobile;
    UserDetails userDetails;
    ImageView image_view_back;
    SmsBroadCastReceiver smsBroadCastReceiver;
    String page = "";
    CountDownTimer countDownTimer;
    boolean isResend = false;
    String msg = "";
    String cars_count = "";

    private String city_id, name = "", gender = "", dob = "", email = "", state = "", city = "",login_type = "",isFrom,state_id;
    private String profile_image = "";

    public static final String FROM = "FROM";
    public static final String USER_ID = "USER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_details);

        getSupportActionBar().hide();

        init();
        getIntentData();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            page = intent.getStringExtra("PAGE");
            if (page.equals("LOGIN")) {
                otp = intent.getStringExtra(LoginActivity.OTP);
                userDetails = intent.getParcelableExtra(LoginActivity.USER_DETAILS);
                mobile = intent.getStringExtra(LoginActivity.MOBILE);
                text_view_mobile_number.setText("+91 " + mobile);
            } else if (page.equalsIgnoreCase("SocialRegistration")) {
                mobile = intent.getStringExtra("MOBILE");
                otp = intent.getStringExtra("OTP");
                text_view_mobile_number.setText("+91 " + mobile);


                name = getIntent().getExtras().getString("name");
                gender = getIntent().getExtras().getString("gender");
                dob = getIntent().getExtras().getString("dob");
                email = getIntent().getExtras().getString("email");
                state_id = getIntent().getExtras().getString("state_id");
                city_id = getIntent().getExtras().getString("city_id");
                login_type = getIntent().getExtras().getString("login_type");
                profile_image = getIntent().getExtras().getString("profile_image");

                isResend = true;
                //text_view_resend.performClick();
                Toast.makeText(VerifyDetailsActivity.this, "OTP sent successfully "/*+otp*/, Toast.LENGTH_SHORT).show();
            } else {
                mobile = intent.getStringExtra("MOBILE");
                otp = intent.getStringExtra("OTP");
                text_view_mobile_number.setText("+91 " + mobile);
                isResend = true;
                //text_view_resend.performClick();
                Toast.makeText(VerifyDetailsActivity.this, "OTP sent successfully "/*+otp*/, Toast.LENGTH_SHORT).show();

            }
            timer();
        }
    }

    private void init() {
        edit_text_otp = findViewById(R.id.edit_text_otp);
        text_view_resend = findViewById(R.id.text_view_resend);
        text_view_mobile_number = findViewById(R.id.text_view_mobile_number);
        text_view_verify = findViewById(R.id.text_view_verify);
        text_view_otp_expire = findViewById(R.id.text_view_otp_expire);
        text_view_otp_time = findViewById(R.id.text_view_otp_time);

        image_view_back = findViewById(R.id.image_view_back);

        image_view_back.setOnClickListener(this);
        text_view_verify.setOnClickListener(this);
        text_view_resend.setOnClickListener(this);


        edit_text_otp.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(String value) {
                enteredOtp = value;
            }
        });

        final SmsRetrieverClient smsRetrieverClient = SmsRetriever.getClient(this);
        Task<Void> task = smsRetrieverClient.startSmsRetriever();

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


                smsBroadCastReceiver = new SmsBroadCastReceiver(VerifyDetailsActivity.this, new SmsBroadCastReceiver.Listener() {
                    @Override
                    public void onSMSReceived(String otp) {
                        //Toast.makeText(VerifyDetailsActivity.this, "Your Otp is" + otp, Toast.LENGTH_SHORT).show();
                        edit_text_otp.setText(otp);
                        text_view_verify.performClick();

                    }

                    @Override
                    public void TimeOut() {
                        Toast.makeText(VerifyDetailsActivity.this, "Time Out", Toast.LENGTH_SHORT).show();
                    }
                });


                registerReceiver(smsBroadCastReceiver, new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION));
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(VerifyDetailsActivity.this, "Failure", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void timer() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                text_view_resend.setTextColor(getResources().getColor(R.color.light_ash1));
                text_view_resend.setText("RESEND OTP");

                text_view_otp_expire.setTextColor(getResources().getColor(R.color.black));
                text_view_otp_expire.setText("OTP Expires in");

                text_view_otp_time.setText("" + String.format(Locale.getDefault(), " %02d : %02d ",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60));
                text_view_otp_time.setTextColor(getResources().getColor(R.color.colorAccent));
            }

            public void onFinish() {
                isResend = true;
                otp = "";
                text_view_otp_expire.setText("");
                text_view_otp_time.setText("");
                text_view_resend.setTextColor(getResources().getColor(R.color.colorAccent));
                text_view_resend.setText("RESEND OTP");
            }
        }.start();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.text_view_verify:
                if (enteredOtp != null && !enteredOtp.equalsIgnoreCase("")) {
                    if (enteredOtp.equalsIgnoreCase(otp)) {

                        Log.e("gdvdgcdvc", "OTP car count value " + cars_count);

                        if (page.equals("EditProfile")) {

                            Intent intent = new Intent(VerifyDetailsActivity.this, MenuProfileActivity.class);
                            intent.putExtra("MOBILE", mobile);
                            startActivity(intent);
                            finish();
                            break;
                        }

                        if (userDetails != null) {

                            if (userDetails.getCars_count().isEmpty() || userDetails.getCars_count().equalsIgnoreCase("0")) {
                                Intent intent = new Intent(VerifyDetailsActivity.this, ClickToProceedActivity.class);
                                //intent.putExtra(LoginActivity.MOBILE, mobile);
                                intent.putExtra("CAR_COUNT", userDetails.getCars_count());
                                intent.putExtra("USER_ID", userDetails.getUser_id());
                                intent.putExtra("FROM", "LOGINPAGE");
                                startActivity(intent);

                            } else if (page.equals("LOGIN")) {

                                Utility.showToastMessage(VerifyDetailsActivity.this, "Login Successful");
                                Utility.setSharedPrefStringData(VerifyDetailsActivity.this, Constants.USER_ID, userDetails.getUser_id());
                                Utility.setSharedPrefStringData(VerifyDetailsActivity.this, Constants.NAME, userDetails.getName());
                                Utility.setSharedPrefStringData(VerifyDetailsActivity.this, Constants.MOBILE, userDetails.getMobile());
                                Utility.setSharedPrefStringData(VerifyDetailsActivity.this, Constants.EMAIL, userDetails.getEmail());
                                Utility.setSharedPrefStringData(VerifyDetailsActivity.this, Constants.IMAGE, userDetails.getImage());
                                Utility.setSharedPrefStringData(VerifyDetailsActivity.this, Constants.DOB, userDetails.getDob());
                                Utility.setSharedPrefStringData(VerifyDetailsActivity.this, Constants.GENDER, userDetails.getGender());
                                Utility.setSharedPrefStringData(VerifyDetailsActivity.this, Constants.WALLET_AMOUNT, userDetails.getWallet_amount());
                                Utility.setSharedPrefStringData(VerifyDetailsActivity.this, Constants.QR, userDetails.getQr());

                                Utility.setSharedPrefBooleanData(VerifyDetailsActivity.this, Constants.IS_LOGGEDIN, true);

                                Intent intent = new Intent(VerifyDetailsActivity.this, ClickToProceedActivity.class);
                                intent.putExtra("FROM", "Verify");
                                intent.putExtra("MESSAGE", msg);
                                intent.putExtra("TAG", "LOGINPAGE");
                                intent.putExtra("CAR_COUNT", userDetails.getCars_count());
                                startActivity(intent);
                                finish();
                            }

                        } else if (page.equalsIgnoreCase("SocialRegistration")) {

                            Utility.showLoadingDialog(VerifyDetailsActivity.this, "Loading...", false);
                            JSONObject jsonObject = new JSONObject();
                            try {

                                jsonObject.put("name", name);
                                jsonObject.put("gender", gender);
                                jsonObject.put("dob", dob);
                                jsonObject.put("email", email);
                                jsonObject.put("mobile", mobile);
                                jsonObject.put("state_id", state_id);
                                jsonObject.put("city_id", city_id);
                                jsonObject.put("device_id", BaseApplication.getDeviceId(VerifyDetailsActivity.this));
                                jsonObject.put("device_token", Utility.getSharedPrefStringData(this,Constants.DEVICE_TOKEN));
                                jsonObject.put("login_type", login_type);
                                jsonObject.put("profile_image",profile_image);
                                jsonObject.put("device_type","1");
                                jsonObject.put("referred_by",Utility.getSharedPrefStringData(this,Constants.REFERRER_ID));

                                Log.e("djfvhdvdv","params  "+jsonObject.toString());
                                Log.e("djfvhdvdv","email  "+email);
                                Log.e("djfvhdvdv","mobilenumnber  "+mobile);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            ServerResponse serverResponse = new ServerResponse();
                            serverResponse.serviceRequestJSonObject(VerifyDetailsActivity.this, "Post", Constants.SIGN_UP_URL, jsonObject, new HashMap<String, String>(), this, Constants.SIGN_UP_CODE);

                        } else {
                            Intent intent = new Intent(VerifyDetailsActivity.this, ProfileActivity.class);
                            intent.putExtra(LoginActivity.MOBILE, mobile);
                            intent.putExtra("LOGIN_TYPE", "0");
                            startActivity(intent);
                            finish();
                        }


                    } else {
                        //edit_text_otp.setText("");
                        Utility.showToastMessage(VerifyDetailsActivity.this, "Invalid OTP");
                    }

                } else {
                    Utility.showToastMessage(VerifyDetailsActivity.this, "Invalid OTP");
                }
                break;
            case R.id.image_view_back:
                if (page.equalsIgnoreCase("EditProfile")) {
                    Intent i = new Intent(VerifyDetailsActivity.this, MenuProfileActivity.class);
                    startActivity(i);
                }
                finish();
                break;
            case R.id.text_view_resend:
                if (isResend) {
                    isResend = false;
                    if (countDownTimer != null)
                        countDownTimer.cancel();
                    if (Utility.isNetworkAvailable(this)) {
                        if (Utility.isMobile(mobile)) {
                            timer();
                            edit_text_otp.setText("");
                            Utility.showLoadingDialog(VerifyDetailsActivity.this, "Loading...", false);

                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("mobile", mobile);
                                jsonObject.put("key_hash", Utility.getSharedPrefStringData(this, Constants.KEY_HASH));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ServerResponse serverResponse = new ServerResponse();
                            serverResponse.serviceRequestJSonObject(VerifyDetailsActivity.this, "POST", Constants.OTP_URL, jsonObject, new HashMap<String, String>(), this, Constants.OTP_CODE);

                        } else {
                            Utility.showToastMessage(VerifyDetailsActivity.this, "Please enter a valid mobile number");
                        }
                    } else {
                        Utility.showToastMessage(VerifyDetailsActivity.this, getString(R.string.check_internet));
                    }
                }
                break;


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
        switch (requestCode) {
            case Constants.SIGN_UP_CODE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    String user_id = jsonObject.optString("user_id");

                    Log.e("djfvhdvdv","res "+response.toString());
                    Log.e("djfvhdvdv","message "+message);

                    if (status == 200) {

                        JSONArray detailsJsonArray = jsonObject.optJSONArray("user_details");
                        if (detailsJsonArray != null && detailsJsonArray.length() > 0) {
                            JSONObject detailsJsonObject = detailsJsonArray.optJSONObject(0);

                            user_id = detailsJsonObject.optString("user_id");
                            String name = detailsJsonObject.optString("name");
                            String mobile = detailsJsonObject.optString("mobile");
                            String email = detailsJsonObject.optString("email");
                            String image = detailsJsonObject.optString("image");
                            String dob = detailsJsonObject.optString("dob");
                            String gender = detailsJsonObject.optString("gender");
                            String wallet_amount = detailsJsonObject.optString("wallet_amount");
                            String state_id = detailsJsonObject.optString("state_id");
                            String city_id = detailsJsonObject.optString("city_id");
                            String qr = detailsJsonObject.optString("qr");
                            String is_verified = detailsJsonObject.optString("is_verified");

                            String city = detailsJsonObject.optString("city");
                            String state = detailsJsonObject.optString("state");

                            Utility.setSharedPrefStringData(VerifyDetailsActivity.this, Constants.USER_ID, user_id);
                            Utility.setSharedPrefStringData(VerifyDetailsActivity.this, Constants.NAME, name);
                            Utility.setSharedPrefStringData(VerifyDetailsActivity.this, Constants.MOBILE, mobile);
                            Utility.setSharedPrefStringData(VerifyDetailsActivity.this, Constants.EMAIL, email);
                            Utility.setSharedPrefStringData(VerifyDetailsActivity.this, Constants.IMAGE, image);
                            Utility.setSharedPrefStringData(VerifyDetailsActivity.this, Constants.DOB, dob);
                            Utility.setSharedPrefStringData(VerifyDetailsActivity.this, Constants.GENDER, gender);
                            Utility.setSharedPrefStringData(VerifyDetailsActivity.this, Constants.WALLET_AMOUNT, wallet_amount);
                            Utility.setSharedPrefStringData(VerifyDetailsActivity.this,Constants.STATE_ID,state_id);
                            Utility.setSharedPrefStringData(VerifyDetailsActivity.this,Constants.CITY_ID,city_id);
                            Utility.setSharedPrefStringData(VerifyDetailsActivity.this,Constants.CITY,city);
                            Utility.setSharedPrefStringData(VerifyDetailsActivity.this,Constants.STATE,state);
                            Utility.setSharedPrefStringData(VerifyDetailsActivity.this, Constants.QR, qr);
                            Utility.setSharedPrefBooleanData(VerifyDetailsActivity.this, Constants.IS_LOGGEDIN, true);
                            Utility.setSharedPrefStringData(VerifyDetailsActivity.this,Constants.IS_VERIFIED,is_verified);
                        }
                        Utility.setSharedPrefStringData(VerifyDetailsActivity.this,Constants.IS_EMAIL_VERIFIED_FROM,"SignUp");
                        Utility.showToastMessage(VerifyDetailsActivity.this, jsonObject.optString("message"));
                        Intent intent = new Intent(VerifyDetailsActivity.this, ClickToProceedActivity.class);
                        intent.putExtra(FROM, "PROFILE");
                        intent.putExtra(USER_ID, user_id);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else if (status == 400) {
                        PopUtils.alertDialog(this, message, null);
                    } else if(status==401) {
                        PopUtils.alertDialog(VerifyDetailsActivity.this, message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseApplication.getInstance().callServiceForLogOut();
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case Constants.OTP_CODE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 200) {
                        otp = jsonObject.optString("otp");
                        cars_count = jsonObject.optString("cars_count");
                        Utility.showToastMessage(this, jsonObject.optString("message")/*+" OTP : "+otp*/);
                        JSONArray detailsJsonArray = jsonObject.optJSONArray("user_details");
                        if (detailsJsonArray != null && detailsJsonArray.length() > 0) {
                            JSONObject detailsJsonObject = detailsJsonArray.optJSONObject(0);

                            String user_id = detailsJsonObject.optString("user_id");
                            String name = detailsJsonObject.optString("name");
                            String mobile = detailsJsonObject.optString("mobile");
                            String email = detailsJsonObject.optString("email");
                            String image = detailsJsonObject.optString("image");
                            String dob = detailsJsonObject.optString("dob");
                            String gender = detailsJsonObject.optString("gender");
                            String wallet_amount = detailsJsonObject.optString("wallet_amount");
                            String qr = detailsJsonObject.optString("qr");

                            userDetails = new UserDetails(user_id, name, mobile, email, image, dob, gender, wallet_amount, qr, cars_count);
                            //Utility.setSharedPrefStringData(VerifyDetailsActivity.this,Constants.USER_ID,user_id);

                        }
                        msg = message;
                    } else {
                        Utility.showToastMessage(this, jsonObject.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }

    //hash string   chOTZF8RyKsLy8o3HS/nb5EjRi8=
    class MyListener implements SmsBroadCastReceiver.Listener {

        @Override
        public void onSMSReceived(String otp) {
            edit_text_otp.setText(otp);
//            Toast.makeText(VerifyDetailsActivity.this, "Your Otp is" + otp, Toast.LENGTH_SHORT).show();


        }

        @Override
        public void TimeOut() {
            Toast.makeText(VerifyDetailsActivity.this, "Time Out", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsBroadCastReceiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (page.equalsIgnoreCase("EditProfile")) {
            Intent i = new Intent(VerifyDetailsActivity.this, MenuProfileActivity.class);
            startActivity(i);
        }
        finish();
    }
}
