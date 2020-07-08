package com.versatilemobitech.VeeZee.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.app.SpinnerIOS.SpinnerWindow;
import com.app.SpinnerIOS.SpinnerWindow_interface;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.versatilemobitech.VeeZee.Adapters.CustomAdapterForSpinner;
import com.versatilemobitech.VeeZee.BaseApplication;
import com.versatilemobitech.VeeZee.Model.City;
import com.versatilemobitech.VeeZee.Model.State;
import com.versatilemobitech.VeeZee.Model.UserDetails;
import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.SharedPreference;
import com.versatilemobitech.VeeZee.WebUtils.ServerResponse;
import com.versatilemobitech.VeeZee.interfaces.IParseListener;
import com.versatilemobitech.VeeZee.interfaces.ReturnValue;
import com.versatilemobitech.VeeZee.utils.Constants;
import com.versatilemobitech.VeeZee.utils.PopUtils;
import com.versatilemobitech.VeeZee.utils.Utility;
import com.versatilemobitech.VeeZee.views.BottomSheetListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, IParseListener, SpinnerWindow_interface {
    TextView text_view_title, text_view_proceed,txt_mobile_number;
    EditText edit_text_name, edit_text_gender, edit_text_dob, edit_text_email, edit_text_state, edit_text_city,edt_mobile_number;
    ImageView image_view_male, image_view_female, image_view_back,image_view_other;


    public static final String FROM = "FROM";
    public static final String USER_ID = "USER_ID";

    private DatePicker datePicker;
    private Calendar calendar;

    ArrayList<State> stateArrayList = new ArrayList<>();
    ArrayList<City> cityArrayList = new ArrayList<>();

    private String state_id;
    private String city_id, name = "", gender = "", dob = "", email = "", state = "", city = "", mobile = "",login_type = "",isFrom;
    private String profile_image = "";
    private SpinnerWindow spinnerWindow;
    private String otp;
    String cars_count = "";

    public static final String OTP = "OTP";
    public static final String MOBILE = "MOBILE";
    public static final String USER_DETAILS = "USER_DETAILS";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_profile);

        initView();
        calendar = Calendar.getInstance();

        callServiceForStates();
        getIntentData();
        spinnerWindow = new SpinnerWindow(ProfileActivity.this);
        Log.e("Refererr", "onCreate: "+Utility.getSharedPrefStringData(this,Constants.REFERRER_ID));
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            mobile = intent.getStringExtra(LoginActivity.MOBILE);
            login_type = intent.getStringExtra("LOGIN_TYPE");
            name = intent.getStringExtra("FACEBOOK_NAME");
            email = intent.getStringExtra("FACEBOOK_EMAIL");
            isFrom = intent.getStringExtra("FROM");
            String userImage = intent.getStringExtra("SOCIAL_IMAGE");
            if(isFrom!=null&&!isFrom.isEmpty()&&isFrom.equalsIgnoreCase("FACEBOOK")){
                mobile = "";
                profile_image = userImage;
                edit_text_email.setText(email);
                edit_text_name.setText(name.trim());
                edit_text_email.setFocusable(false);
                edt_mobile_number.setVisibility(View.VISIBLE);
                txt_mobile_number.setVisibility(View.VISIBLE);
                edt_mobile_number.setFocusable(true);
            }else if(Utility.getSharedPrefStringData(this,Constants.IS_VERIFIED).equalsIgnoreCase("0")){
                edit_text_name.setText(Utility.getSharedPrefStringData(this,Constants.NAME).trim());
                String gender = Utility.getSharedPrefStringData(ProfileActivity.this, Constants.GENDER);

                if (gender.equalsIgnoreCase("1")) {
                    gender = "Male";
                    image_view_male.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                    image_view_female.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);
                    image_view_other.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);
                    edit_text_gender.setText(gender);
                } else if(gender.equalsIgnoreCase("2")) {
                    gender = "Female";
                    image_view_female.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                    image_view_male.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);
                    image_view_other.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);
                    edit_text_gender.setText(gender);
                }else if(gender.equalsIgnoreCase("3")) {
                    gender = "Prefer not to say";
                    image_view_female.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);
                    image_view_male.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);
                    image_view_other.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                    edit_text_gender.setText(gender);

                }else {
                    edit_text_gender.setText("");
                }

                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-mm-dd");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
                String finalDate = null;
                try {

                    String s = simpleDateFormat1.format(calendar.getTime());
                    Date date = simpleDateFormat1.parse(s);
                    finalDate = simpleDateFormat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                edit_text_dob.setText(finalDate);
                edt_mobile_number.setText(Utility.getSharedPrefStringData(this,Constants.MOBILE));
                edit_text_email.setText(Utility.getSharedPrefStringData(this,Constants.EMAIL));

                edit_text_state.setText(Utility.getSharedPrefStringData(this,Constants.STATE));
                edit_text_city.setText(Utility.getSharedPrefStringData(this,Constants.CITY));

            } else {
                edit_text_email.setFocusable(true);
                edit_text_email.clearFocus();
                edt_mobile_number.setFocusable(false);
                edt_mobile_number.setText(mobile);
                /*edt_mobile_number.setVisibility(View.GONE);
                txt_mobile_number.setVisibility(View.GONE);*/
            }
        }


    }

    private void initView() {
        image_view_back = findViewById(R.id.image_view_back);

        text_view_title = findViewById(R.id.text_view_description);
        text_view_title.setText("Profile");

        text_view_proceed = findViewById(R.id.text_view_proceed);
        text_view_proceed.setOnClickListener(this);

        edit_text_name = findViewById(R.id.edit_text_name);
        edit_text_gender = findViewById(R.id.edit_text_gender);
        edit_text_dob = findViewById(R.id.edit_text_dob);
        edit_text_email = findViewById(R.id.edit_text_email);
        edit_text_state = findViewById(R.id.edit_text_state);
        edit_text_city = findViewById(R.id.edit_text_city);

        edt_mobile_number = findViewById(R.id.edt_mobile_number);
        txt_mobile_number = findViewById(R.id.txt_mobile_number);

        edit_text_dob.setOnClickListener(this);

        image_view_male = findViewById(R.id.image_view_male);
        image_view_female = findViewById(R.id.image_view_female);
        image_view_other = findViewById(R.id.image_view_other);

        image_view_male.setOnClickListener(this);
        image_view_female.setOnClickListener(this);
        image_view_other.setOnClickListener(this);

        edit_text_state.setOnClickListener(this);
        edit_text_city.setOnClickListener(this);

        image_view_back.setOnClickListener(this);

       // edit_text_name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.image_view_back:
                finish();
                break;

            case R.id.image_view_male:
                edit_text_name.clearFocus();
                edit_text_gender.setText("Male");
                image_view_male.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                image_view_female.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);
                image_view_other.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);
                break;
            case R.id.image_view_female:
                edit_text_name.clearFocus();
                edit_text_gender.setText("Female");
                image_view_female.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                image_view_male.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);
                image_view_other.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);

                break;
            case R.id.image_view_other :
                edit_text_name.clearFocus();
                edit_text_gender.setText("Prefer not to say");
                image_view_other.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                image_view_male.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);
                image_view_female.setColorFilter(ContextCompat.getColor(ProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);
                break;
            case R.id.edit_text_dob:
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileActivity.this,R.style.MyTimePickerDialogStyle, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String myFormat = "MM/dd/yy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM/dd/yy");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
                        String finalDate = null;
                        try {

                            String s = sdf.format(calendar.getTime());
                            Date date = simpleDateFormat1.parse(s);
                            finalDate = simpleDateFormat.format(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        edit_text_dob.setText(finalDate);

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                datePickerDialog.show();
                break;
            case R.id.edit_text_state:
                edit_text_email.clearFocus();
                final String temp_state_id = state_id;
                ArrayList<String> stateNamesArraylist = new ArrayList<>();
                for (State state : stateArrayList) {
                    stateNamesArraylist.add(state.getStateName());
                }

                /*PopUtils.dialogListReturnValue(ProfileActivity.this, stateNamesArraylist, "Select State", edit_text_state, new ReturnValue() {
                    @Override
                    public void returnValues(String value, int positionValue) {
                        edit_text_state.setText(stateArrayList.get(positionValue).getStateName());
                        state_id = stateArrayList.get(positionValue).getStateId();
                        edit_text_city.setText("");

                        if (temp_state_id == state_id) {

                        } else {
                            callServiceForCities(state_id);
                        }

                    }
                });*/
                setBottomSpinner(stateNamesArraylist,new ArrayList<String>(),"States");
               // SpinnerWindow.showSpinner(ProfileActivity.this, "state", stateNamesArraylist, new ArrayList<String>());
                break;
            case R.id.edit_text_city:
                edit_text_email.clearFocus();
                ArrayList<String> cityNamesArraylist = new ArrayList<>();
                if (cityArrayList != null && cityArrayList.size() > 0) {
                    for (City city : cityArrayList) {
                        cityNamesArraylist.add(city.getCityName());
                    }
                    city_id = cityArrayList.get(0).getCityId();

                    /*PopUtils.dialogListReturnValue(ProfileActivity.this, cityNamesArraylist, "Select City", edit_text_city, new ReturnValue() {
                        @Override
                        public void returnValues(String value, int positionValue) {
                            city_id = cityArrayList.get(positionValue).getCityId();
                            edit_text_city.setText(cityArrayList.get(positionValue).getCityName());
                        }
                    });*/
                    setBottomSpinner(cityNamesArraylist,new ArrayList<String>(),"Cities");
                    //SpinnerWindow.showSpinner(ProfileActivity.this, "city", cityNamesArraylist, new ArrayList<String>());
                } else {
                    Utility.showToastMessage(ProfileActivity.this, "Please select state first");
                }
                break;
            case R.id.text_view_proceed:

                name = edit_text_name.getText().toString().trim();
                gender = edit_text_gender.getText().toString();
                if(gender.equalsIgnoreCase("Female")){
                    gender = "2";
                }else if(gender.equalsIgnoreCase("Male")) {
                    gender = "1";
                }else if(gender.equalsIgnoreCase("Prefer not to say")){
                    gender = "3";
                }
                dob = edit_text_dob.getText().toString();
                email = edit_text_email.getText().toString();
                state = edit_text_state.getText().toString();
                city = edit_text_city.getText().toString();
                Log.e("Token",""+Utility.getSharedPrefStringData(this,Constants.DEVICE_TOKEN));

                if (checkForValidations()) {
                    if (Utility.isNetworkAvailable(ProfileActivity.this)) {
                        if (SharedPreference.getStringPreference(this,"SOCIALLOGIN").equalsIgnoreCase("YES"))
                        {
                       //     Toast.makeText(this, "Social login", Toast.LENGTH_SHORT).show();
                            Utility.showLoadingDialog(ProfileActivity.this, "Loading...", false);



                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("mobile", mobile);
                                jsonObject.put("device_type", "1");

                                Log.e("dgdgcvdgvcg","Cdhdvvcd "+Utility.getSharedPrefStringData(this, Constants.KEY_HASH));
                                jsonObject.put("key_hash", Utility.getSharedPrefStringData(this, Constants.KEY_HASH));
                                jsonObject.put("device_id", BaseApplication.getDeviceId(this));
                                jsonObject.put("device_token", Utility.getSharedPrefStringData(this, Constants.DEVICE_TOKEN));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ServerResponse serverResponse = new ServerResponse();
                            serverResponse.serviceRequestJSonObject(ProfileActivity.this, "POST", Constants.OTP_URL, jsonObject, new HashMap<String, String>(), this, Constants.OTP_CODE);



                        }
                        else
                        {
                            Utility.showLoadingDialog(ProfileActivity.this, "Loading...", false);
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("name", name);
                                jsonObject.put("gender", gender);
                                jsonObject.put("dob", dob);
                                jsonObject.put("email", email);
                                jsonObject.put("mobile", mobile);
                                jsonObject.put("state_id", state_id);
                                jsonObject.put("city_id", city_id);
                                jsonObject.put("device_id", BaseApplication.getDeviceId(ProfileActivity.this));
                                jsonObject.put("device_token", Utility.getSharedPrefStringData(this,Constants.DEVICE_TOKEN));
                                jsonObject.put("login_type", login_type);
                                jsonObject.put("profile_image",profile_image);
                                jsonObject.put("device_type","1");
                                jsonObject.put("referred_by",Utility.getSharedPrefStringData(this,Constants.REFERRER_ID));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            ServerResponse serverResponse = new ServerResponse();
                            serverResponse.serviceRequestJSonObject(ProfileActivity.this, "Post", Constants.SIGN_UP_URL, jsonObject, new HashMap<String, String>(), this, Constants.SIGN_UP_CODE);

                        }


                    } else {
                        PopUtils.alertDialog(ProfileActivity.this, Utility.getResourcesString(this, R.string.check_internet), null);
                    }


                } else {
                }


        }

    }

    private boolean checkForValidations() {

        if (name.isEmpty() || name.equalsIgnoreCase("")) {
            Utility.showToastMessage(ProfileActivity.this, "Please enter your name");
            return false;
        } else if (name.length() < 3) {
            Utility.showToastMessage(ProfileActivity.this, "Name should be at least 3 characters in length");
            return false;
        } else if (gender.isEmpty() || gender.equalsIgnoreCase("")) {
            Utility.showToastMessage(ProfileActivity.this, "Please select your gender");
            return false;
        } else if (dob.equalsIgnoreCase("") || dob.isEmpty()) {
            Utility.showToastMessage(ProfileActivity.this, "Please select your Date of birth");
            return false;
        } else if(isFrom!=null&&!isFrom.isEmpty()&&isFrom.equalsIgnoreCase("FACEBOOK")){
            mobile = edt_mobile_number.getText().toString();
            if(mobile.isEmpty()&&mobile.equalsIgnoreCase("")){
                Utility.showToastMessage(ProfileActivity.this, "Please enter your Mobile Number");
                return false;
            }else if(!Utility.isMobile(mobile)){
                Utility.showToastMessage(ProfileActivity.this, "Please enter a valid Mobile Number");
                return false;
            }else if (email.isEmpty() || email.equalsIgnoreCase("")) {
                Utility.showToastMessage(ProfileActivity.this, "Please enter your Email ID");
                return false;
            } else if (!Utility.isEmail(email)) {
                Utility.showToastMessage(ProfileActivity.this, "Please enter a valid Email ID");
                return false;
            } else if (state.isEmpty() || state.equalsIgnoreCase("")) {
                Utility.showToastMessage(ProfileActivity.this, "Please select a State");
                return false;
            } else if (city.isEmpty() || city.equalsIgnoreCase("")) {
                Utility.showToastMessage(ProfileActivity.this, "Please select a City");
                return false;
            }
        }
        else if (email.isEmpty() || email.equalsIgnoreCase("")) {
            Utility.showToastMessage(ProfileActivity.this, "Please enter your Email ID");
            return false;
        } else if (!Utility.isEmail(email)) {
            Utility.showToastMessage(ProfileActivity.this, "Please enter a valid Email ID");
            return false;
        } else if (state.isEmpty() || state.equalsIgnoreCase("")) {
            Utility.showToastMessage(ProfileActivity.this, "Please select a State");
            return false;
        } else if (city.isEmpty() || city.equalsIgnoreCase("")) {
            Utility.showToastMessage(ProfileActivity.this, "Please select a City");
            return false;
        }

        return true;

    }

    private void callServiceForStates() {
        if (Utility.isNetworkAvailable(ProfileActivity.this)) {
            Utility.showLoadingDialog(ProfileActivity.this, "Loading...", false);

            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(ProfileActivity.this, "GET", Constants.STATE_URL, new JSONObject(), new HashMap<String, String>(), this, Constants.STATE_CODE);

        } else {
            PopUtils.alertDialog(ProfileActivity.this, Utility.getResourcesString(this, R.string.check_internet), null);
        }

    }

    private void callServiceForCities(String stateId) {
        if (Utility.isNetworkAvailable(ProfileActivity.this)) {
            Utility.showLoadingDialog(ProfileActivity.this, "Loading...", false);
            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(ProfileActivity.this, "GET", Constants.CITY_URL + "?state_id=" + stateId, new JSONObject(), new HashMap<String, String>(), this, Constants.CITY_CODE);

        } else {
            PopUtils.alertDialog(ProfileActivity.this, Utility.getResourcesString(this, R.string.check_internet), null);
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

            case Constants.OTP_CODE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 200) {
                        otp = jsonObject.optString("otp");
                        cars_count = jsonObject.optString("cars_count");

                        Log.e("gdvdgcdvc","response "+response);
                        Log.e("gdvdgcdvc","LOGIN car count value "+otp);
                        Log.e("gdvdgcdvc","LOGIN car count value "+cars_count);

                        JSONArray detailsJsonArray = jsonObject.optJSONArray("user_details");
                        if (detailsJsonArray != null && detailsJsonArray.length() > 0) {
                            JSONObject detailsJsonObject = detailsJsonArray.optJSONObject(0);

                            String user_id = detailsJsonObject.optString("user_id");
                            String name = detailsJsonObject.optString("name");
                            String mobile = detailsJsonObject.optString("mobile");
                        //    String email = detailsJsonObject.optString("email");
                            String image = detailsJsonObject.optString("image");
                            String dob = detailsJsonObject.optString("dob");
                            String gender = detailsJsonObject.optString("gender");
                            String wallet_amount = detailsJsonObject.optString("wallet_amount");
                            String qr = detailsJsonObject.optString("qr");
                            String state_id = detailsJsonObject.optString("state_id");
                            String city_id = detailsJsonObject.optString("city_id");
                            String is_verified = detailsJsonObject.optString("is_verified");
                            String city = detailsJsonObject.optString("city");
                            String state = detailsJsonObject.optString("state");

                            Utility.setSharedPrefStringData(ProfileActivity.this, Constants.USER_ID, user_id);
                            Utility.setSharedPrefStringData(ProfileActivity.this, Constants.NAME, name);
                            Utility.setSharedPrefStringData(ProfileActivity.this, Constants.MOBILE, mobile);
                            Utility.setSharedPrefStringData(ProfileActivity.this, Constants.EMAIL, email);
                            Utility.setSharedPrefStringData(ProfileActivity.this, Constants.IMAGE, image);
                            Utility.setSharedPrefStringData(ProfileActivity.this, Constants.DOB, dob);
                            Utility.setSharedPrefStringData(ProfileActivity.this, Constants.GENDER, gender);
                            Utility.setSharedPrefStringData(ProfileActivity.this, Constants.WALLET_AMOUNT, wallet_amount);
                            Utility.setSharedPrefStringData(ProfileActivity.this, Constants.QR, qr);
                            Utility.setSharedPrefStringData(ProfileActivity.this, Constants.STATE_ID, state_id);
                            Utility.setSharedPrefStringData(ProfileActivity.this, Constants.CITY_ID, city_id);
                            Utility.setSharedPrefBooleanData(ProfileActivity.this, Constants.IS_LOGGEDIN, true);
                            Utility.setSharedPrefStringData(ProfileActivity.this,Constants.IS_VERIFIED,is_verified);
                            Utility.setSharedPrefStringData(ProfileActivity.this,Constants.CITY,city);
                            Utility.setSharedPrefStringData(ProfileActivity.this,Constants.STATE,state);

                            UserDetails userDetails = new UserDetails(user_id, name, mobile, email, image, dob, gender, wallet_amount,qr,cars_count);

                            Intent intent = new Intent(ProfileActivity.this, VerifyDetailsActivity.class);
                            intent.putExtra(OTP, otp);
                            intent.putExtra(MOBILE, mobile);

                            intent.putExtra("name",name);
                            intent.putExtra("gender",gender);
                            intent.putExtra("dob",dob);
                            intent.putExtra("email",email);
                            intent.putExtra("state_id",state_id);
                            intent.putExtra("city_id",city_id);
                            intent.putExtra("login_type",login_type);
                            intent.putExtra("profile_image",profile_image);

                            intent.putExtra(USER_DETAILS, userDetails);
                            intent.putExtra("PAGE","SocialRegistration");
                            startActivity(intent);
                            Utility.showToastMessage(this, jsonObject.optString("message")/*+" : "+otp*/);

                        } else {

                            Intent intent = new Intent(ProfileActivity.this, VerifyDetailsActivity.class);
                            intent.putExtra(OTP, otp);
                            intent.putExtra(MOBILE, mobile);

                            intent.putExtra("name",name);
                            intent.putExtra("gender",gender);
                            intent.putExtra("dob",dob);
                            intent.putExtra("email",email);
                            intent.putExtra("state_id",state_id);
                            intent.putExtra("city_id",city_id);
                            intent.putExtra("login_type",login_type);
                            intent.putExtra("profile_image",profile_image);
                            intent.putExtra("PAGE","SocialRegistration");
                            startActivity(intent);
                            Utility.showToastMessage(this, jsonObject.optString("message")/*+" : "+otp*/);

                        }

                    }else if(status==401) {
                        PopUtils.alertDialog(ProfileActivity.this, message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(BaseApplication.getInstance()!=null)
                                    BaseApplication.getInstance().callServiceForLogOut();
                            }
                        });

                    } else {
                        Utility.showToastMessage(this, jsonObject.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;


            case Constants.STATE_CODE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 200) {
                        stateArrayList.clear();
                        JSONArray dataJsonArray = jsonObject.optJSONArray("data");
                        for (int i = 0; i < dataJsonArray.length(); i++) {

                            JSONObject dataJsonObject = dataJsonArray.optJSONObject(i);

                            String state_id = dataJsonObject.optString("state_id");
                            String name = dataJsonObject.optString("name");

                            State state = new State(state_id, name);
                            stateArrayList.add(state);

                        }
                    }else if (status == 400) {
                        PopUtils.alertDialog(this, message, null);
                    } else if(status==401) {
                        PopUtils.alertDialog(ProfileActivity.this, message, new View.OnClickListener() {
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
            case Constants.CITY_CODE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 200) {
                        cityArrayList.clear();
                        JSONArray dataJsonArray = jsonObject.optJSONArray("data");
                        for (int i = 0; i < dataJsonArray.length(); i++) {
                            JSONObject dataJsonObject = dataJsonArray.optJSONObject(i);

                            String city_id = dataJsonObject.optString("city_id");
                            String name = dataJsonObject.optString("name");

                            City city = new City(city_id, name);
                            cityArrayList.add(city);
                        }
                    }else if (status == 400) {
                        PopUtils.alertDialog(this, message, null);
                    } else if(status==401) {
                        PopUtils.alertDialog(ProfileActivity.this, message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseApplication.getInstance().callServiceForLogOut();
                            }
                        });

                    }
                    //edit_text_city.setText(cityArrayList.get(0).getCityName());
                    //city_id = cityArrayList.get(0).getCityId();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.SIGN_UP_CODE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    String user_id = jsonObject.optString("user_id");
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

                            Utility.setSharedPrefStringData(ProfileActivity.this, Constants.USER_ID, user_id);
                            Utility.setSharedPrefStringData(ProfileActivity.this, Constants.NAME, name);
                            Utility.setSharedPrefStringData(ProfileActivity.this, Constants.MOBILE, mobile);
                            Utility.setSharedPrefStringData(ProfileActivity.this, Constants.EMAIL, email);
                            Utility.setSharedPrefStringData(ProfileActivity.this, Constants.IMAGE, image);
                            Utility.setSharedPrefStringData(ProfileActivity.this, Constants.DOB, dob);
                            Utility.setSharedPrefStringData(ProfileActivity.this, Constants.GENDER, gender);
                            Utility.setSharedPrefStringData(ProfileActivity.this, Constants.WALLET_AMOUNT, wallet_amount);
                            Utility.setSharedPrefStringData(ProfileActivity.this,Constants.STATE_ID,state_id);
                            Utility.setSharedPrefStringData(ProfileActivity.this,Constants.CITY_ID,city_id);
                            Utility.setSharedPrefStringData(ProfileActivity.this,Constants.CITY,city);
                            Utility.setSharedPrefStringData(ProfileActivity.this,Constants.STATE,state);
                            Utility.setSharedPrefStringData(ProfileActivity.this, Constants.QR, qr);
                            Utility.setSharedPrefBooleanData(ProfileActivity.this, Constants.IS_LOGGEDIN, true);
                            Utility.setSharedPrefStringData(ProfileActivity.this,Constants.IS_VERIFIED,is_verified);

                        }
                        Utility.setSharedPrefStringData(ProfileActivity.this,Constants.IS_EMAIL_VERIFIED_FROM,"SignUp");
                        Utility.showToastMessage(ProfileActivity.this, jsonObject.optString("message"));
                        Intent intent = new Intent(ProfileActivity.this, ClickToProceedActivity.class);
                        intent.putExtra(FROM, "PROFILE");
                        intent.putExtra(USER_ID, user_id);
                        startActivity(intent);
                        finish();
                    } else if (status == 400) {
                        PopUtils.alertDialog(this, message, null);
                    } else if(status==401) {
                        PopUtils.alertDialog(ProfileActivity.this, message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseApplication.getInstance().callServiceForLogOut();
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

        }

    }

    @Override
    public void selectedPosition(String viewFrom, int selected_position) {
        switch (viewFrom){
            case "state" :
                final String temp_state_id = state_id;
                edit_text_state.setText(stateArrayList.get(selected_position).getStateName());
                state_id = stateArrayList.get(selected_position).getStateId();
                edit_text_city.setText("");

                if (temp_state_id == state_id) {

                } else {
                    callServiceForCities(state_id);
                }

                break;
            case "city" :

                city_id = cityArrayList.get(selected_position).getCityId();
                edit_text_city.setText(cityArrayList.get(selected_position).getCityName());

                break;

        }


    }

    private void setBottomSpinner(ArrayList<String> name, ArrayList<String> logos, final String viewFrom) {
        final BottomSheetDialog dialog = new BottomSheetDialog(ProfileActivity.this, R.style.Theme_Design_BottomSheetDialog);
        dialog.setContentView(R.layout.item_spinner_bottom);
        BottomSheetListView rv_list = dialog.findViewById(R.id.bottomList);
        ConstraintLayout frame = dialog.findViewById(R.id.containerBottom);
        TextView txt_title = dialog.findViewById(R.id.txt_title);
        txt_title.setText( viewFrom);

        final ArrayAdapter<String> adapter;
        adapter = new CustomAdapterForSpinner(ProfileActivity.this, R.layout.dialog_dropdown_with_image, name, logos);
        rv_list.setAdapter(adapter);
        rv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(viewFrom.equalsIgnoreCase("States")) {

                    final String temp_state_id = state_id;
                    edit_text_state.setText(stateArrayList.get(position).getStateName());
                    state_id = stateArrayList.get(position).getStateId();
                    edit_text_city.setText("");

                    if (temp_state_id == state_id) {

                    } else {
                        callServiceForCities(state_id);
                    }

                }else if(viewFrom.equalsIgnoreCase("Cities")){

                        city_id = cityArrayList.get(position).getCityId();
                        edit_text_city.setText(cityArrayList.get(position).getCityName());


                }
                dialog.dismiss();
            }

        });
        dialog.show();
    }
}