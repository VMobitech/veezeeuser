package com.versatilemobitech.VeeZee.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.app.SpinnerIOS.SpinnerWindow;
import com.app.SpinnerIOS.SpinnerWindow_interface;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.versatilemobitech.VeeZee.Adapters.CustomAdapterForSpinner;
import com.versatilemobitech.VeeZee.Adapters.RewardsAdapter;
import com.versatilemobitech.VeeZee.BaseApplication;
import com.versatilemobitech.VeeZee.Model.City;
import com.versatilemobitech.VeeZee.Model.Rewards;
import com.versatilemobitech.VeeZee.Model.State;
import com.versatilemobitech.VeeZee.Model.UserDetails;
import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.SharedPreference;
import com.versatilemobitech.VeeZee.UtilHelpers.SelectImageDialog;
import com.versatilemobitech.VeeZee.WebUtils.ServerResponse;
import com.versatilemobitech.VeeZee.interfaces.IParseListener;
import com.versatilemobitech.VeeZee.interfaces.ImageDialogInterface;
import com.versatilemobitech.VeeZee.interfaces.ReturnValue;
import com.versatilemobitech.VeeZee.utils.Constants;
import com.versatilemobitech.VeeZee.utils.PopUtils;
import com.versatilemobitech.VeeZee.utils.Utility;
import com.versatilemobitech.VeeZee.views.BottomSheetListView;
import com.versatilemobitech.VeeZee.views.MontserratEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

public class MenuProfileActivity extends AppCompatActivity implements IParseListener, View.OnClickListener, SpinnerWindow_interface, ImageDialogInterface {
    Toolbar toolbar;
    TextView toolBarTitle, text_view_credits_v, text_view_wallet_amount, text_view_update;
    MontserratEditText edit_text_name, edit_text_gender, edit_text_dob, edit_text_mobile, edit_text_email, edit_text_city, edit_text_state;
    ImageView image_view_male, image_view_female, image_view_coin, image_view_calender,image_view_other;
    CircleImageView image_view_user;
    // MontserratEditText edit_text_name1;

    ArrayList<UserDetails> mList = new ArrayList<>();
    ArrayList<State> stateArrayList;
    ArrayList<City> cityArrayList;

    private String state_id;
    private String city_id1, name = "", gender = "", dob = "", email = "", state = "", city = "", mobile = "";

    private Calendar calendar;
    public static final int PICK_IMAGE = 2;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private int year,month,day;

    /*private static final int CAMERA_REQUEST = 1888;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;
*/
    private SpinnerWindow spinnerWindow;

    public String photoFileName = "photo.jpg";
    File photoFile;
    String mCurrentPhotoPath;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private static int RESULT_LOAD_IMAGE = 1;
    String profileImage ="";
    boolean isEmailVerify = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_profile);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        spinnerWindow = new SpinnerWindow(MenuProfileActivity.this);

        init();
    }

    private void init() {
        callServiceForStates();
        setReferences();
        setClickListers();
        getArguments();
    }

    private void getArguments() {
        Intent i = getIntent();
        if (i != null) {
            mobile = i.getStringExtra("MOBILE");
            if(mobile!=null) {
                isEmailVerify = false;
                CallForEditProfile("mobile", mobile);
            }
        }
    }


    private void setClickListers() {

        image_view_female.setOnClickListener(this);
        image_view_male.setOnClickListener(this);
        image_view_other.setOnClickListener(this);
        image_view_user.setOnClickListener(this);

        text_view_update.setOnClickListener(this);

        edit_text_name.setOnClickListener(this);
        edit_text_city.setOnClickListener(this);
        edit_text_state.setOnClickListener(this);
        edit_text_dob.setOnClickListener(this);
        edit_text_state.setOnClickListener(this);
        edit_text_city.setOnClickListener(this);
    }

    private void setReferences() {
        toolBarTitle = findViewById(R.id.toolBarTitle);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        getSupportActionBar().setTitle("");
        toolBarTitle.setVisibility(View.VISIBLE);
        toolBarTitle.setText("Profile");
        toolBarTitle.setGravity(Gravity.LEFT);

        calendar = Calendar.getInstance();

        edit_text_name = findViewById(R.id.edit_text_name);
        edit_text_name.setFocusableInTouchMode(true);

        edit_text_gender = findViewById(R.id.edit_text_gender);
        edit_text_dob = findViewById(R.id.edit_text_dob);
        edit_text_mobile = findViewById(R.id.edit_text_mobile);
        edit_text_email = findViewById(R.id.edit_text_email);

        edit_text_city = findViewById(R.id.edit_text_city);
        edit_text_state = findViewById(R.id.edit_text_state);

        image_view_user = findViewById(R.id.image_view_user);
        image_view_male = findViewById(R.id.image_view_male);
        image_view_female = findViewById(R.id.image_view_female);
        image_view_other = findViewById(R.id.image_view_other);
        image_view_calender = findViewById(R.id.image_view_calender);

        text_view_update = findViewById(R.id.text_view_update);

        text_view_credits_v = findViewById(R.id.text_view_credits_v);
        text_view_credits_v.setVisibility(View.GONE);

        image_view_coin = findViewById(R.id.image_view_coin);
        image_view_coin.setVisibility(View.GONE);

        text_view_wallet_amount = findViewById(R.id.text_view_wallet_amount);
        text_view_wallet_amount.setVisibility(View.GONE);

       // edit_text_name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);


        String image_url = Utility.getSharedPrefStringData(MenuProfileActivity.this, Constants.IMAGE);
        if (image_url != null && !image_url.equalsIgnoreCase("")) {
            Picasso.get().load(image_url).placeholder(R.drawable.ic_user).into(image_view_user);
        }
        String gender = Utility.getSharedPrefStringData(MenuProfileActivity.this, Constants.GENDER);
        if (gender.equalsIgnoreCase("1")) {
            gender = "Male";
            image_view_male.setColorFilter(ContextCompat.getColor(MenuProfileActivity.this, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
            image_view_female.setColorFilter(ContextCompat.getColor(MenuProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);
            image_view_other.setColorFilter(ContextCompat.getColor(MenuProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);
            edit_text_gender.setText(gender);
        } else if(gender.equalsIgnoreCase("2")) {
            gender = "Female";
            image_view_female.setColorFilter(ContextCompat.getColor(MenuProfileActivity.this, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
            image_view_male.setColorFilter(ContextCompat.getColor(MenuProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);
            image_view_other.setColorFilter(ContextCompat.getColor(MenuProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);
            edit_text_gender.setText(gender);
        }else if(gender.equalsIgnoreCase("3")) {
            gender = "Prefer not to say";
            image_view_female.setColorFilter(ContextCompat.getColor(MenuProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);
            image_view_male.setColorFilter(ContextCompat.getColor(MenuProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);
            image_view_other.setColorFilter(ContextCompat.getColor(MenuProfileActivity.this, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
            edit_text_gender.setText(gender);

        }else {
            edit_text_gender.setText("");
        }

        edit_text_name.setText(Utility.getSharedPrefStringData(MenuProfileActivity.this, Constants.NAME));
        //edit_text_gender.setText(gender);
        if(Utility.getSharedPrefStringData(MenuProfileActivity.this,Constants.DOB).equals("1970-01-01")){
            Calendar cal = Calendar.getInstance();

            year = cal.get(Calendar.YEAR);
            month =cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);

            edit_text_dob.setText("");
        }else {
            if(Utility.getSharedPrefStringData(MenuProfileActivity.this, Constants.DOB)!=null&&
                    !Utility.getSharedPrefStringData(MenuProfileActivity.this, Constants.DOB).equalsIgnoreCase("")) {
                edit_text_dob.setText(setExpDate(Utility.getSharedPrefStringData(MenuProfileActivity.this, Constants.DOB)));
            }
        }
        edit_text_mobile.setText(Utility.getSharedPrefStringData(MenuProfileActivity.this, Constants.MOBILE));
        edit_text_email.setText(Utility.getSharedPrefStringData(MenuProfileActivity.this, Constants.EMAIL));
        state = Utility.getSharedPrefStringData(MenuProfileActivity.this, Constants.STATE);
        city = Utility.getSharedPrefStringData(MenuProfileActivity.this, Constants.CITY);

        Log.e("vhbfvgfv","cdcgv "+city);
        Log.e("vhbfvgfv","cdcgv "+state);

        Log.e("vhbfvgfv","cdcgv "+Utility.getSharedPrefStringData(MenuProfileActivity.this, Constants.CITY));

        if (!state.isEmpty() && !city.isEmpty()) {
            edit_text_city.setText(city);
            edit_text_state.setText(state);
        }

        city_id1 = (Utility.getSharedPrefStringData(MenuProfileActivity.this, Constants.CITY_ID));
        Log.e("vhbfvgfv","cdcgv "+city_id1);
        edit_text_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE)) {
                    //Log.i(TAG,"Enter pressed");
                    if (edit_text_name.getText().toString().trim().isEmpty() || edit_text_name.getText().toString().trim().equalsIgnoreCase("")) {
                        Utility.showToastMessage(MenuProfileActivity.this, "Please enter your name");

                    } else if (edit_text_name.getText().toString().length() < 3) {
                        Utility.showToastMessage(MenuProfileActivity.this, "Name should be at least 3 characters in length");

                    } else {
                        isEmailVerify = false;
                        CallForEditProfile("name", edit_text_name.getText().toString().trim());
                    }
                    edit_text_name.clearFocus();
                }
                return false;
            }
        });
        edit_text_name.setKeyImeChangeListener(new MontserratEditText.KeyImeChange() {
            @Override
            public void onKeyIme(int keyCode, KeyEvent event) {
                if (edit_text_name.getText().toString().trim().isEmpty() || edit_text_name.getText().toString().trim().equalsIgnoreCase("")) {
                    Utility.showToastMessage(MenuProfileActivity.this, "Please enter your name");

                } else if (edit_text_name.getText().toString().length() < 3) {
                    Utility.showToastMessage(MenuProfileActivity.this, "Name should be at least 3 characters in length");

                } else {
                    isEmailVerify = false;
                    CallForEditProfile("name", edit_text_name.getText().toString().trim());
                }
                edit_text_name.clearFocus();
                hideKeypad(edit_text_email);

            }
        });
        edit_text_email.setKeyImeChangeListener(new MontserratEditText.KeyImeChange() {
            @Override
            public void onKeyIme(int keyCode, KeyEvent event) {
                String mail = edit_text_email.getText().toString();
                if (mail.isEmpty() || mail.equalsIgnoreCase("")) {
                    Utility.showToastMessage(MenuProfileActivity.this, "Please enter your Email ID");
                } else if (!Utility.isEmail(mail)) {
                    Utility.showToastMessage(MenuProfileActivity.this, "Please enter a valid Email ID");
                } else {
                    isEmailVerify = true;
                    CallForEditProfile("email", edit_text_email.getText().toString());
                }
                edit_text_email.clearFocus();
                hideKeypad(edit_text_email);
            }
        });

        edit_text_email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE)) {
                    //Log.i(TAG,"Enter pressed");
                    String mail = edit_text_email.getText().toString();
                    if (mail.isEmpty() || mail.equalsIgnoreCase("")) {
                        Utility.showToastMessage(MenuProfileActivity.this, "Please enter your Email ID");
                    } else if (!Utility.isEmail(mail)) {
                        Utility.showToastMessage(MenuProfileActivity.this, "Please enter a valid Email ID");
                    } else {
                        isEmailVerify = true;
                        CallForEditProfile("email", edit_text_email.getText().toString());

                      //  Toast.makeText(MenuProfileActivity.this, "Changed", Toast.LENGTH_SHORT).show();

                        SharedPreference.setStringPreference(MenuProfileActivity.this,"EMAILCHNAGED","YES");
                    }
                    edit_text_email.clearFocus();
                }
                return false;
            }
        });

    }

    private boolean checkValidation(){
        if (edit_text_name.getText().toString().trim().isEmpty() || edit_text_name.getText().toString().trim().equalsIgnoreCase("")) {
            Utility.showToastMessage(MenuProfileActivity.this, "Please enter your name");
            return false;
        } else if (edit_text_name.getText().toString().length() < 3) {
            Utility.showToastMessage(MenuProfileActivity.this, "Name should be at least 3 characters in length");
            return false;
        }if (edit_text_email.getText().toString().trim().isEmpty() || edit_text_email.getText().toString().equalsIgnoreCase("")) {
            Utility.showToastMessage(MenuProfileActivity.this, "Please enter your Email ID");
            return false;
        } else if (!Utility.isEmail(edit_text_email.getText().toString().trim())) {
            Utility.showToastMessage(MenuProfileActivity.this, "Please enter a valid Email ID");
            return false;
        }else if (edit_text_state.getText().toString().trim().isEmpty() || edit_text_state.getText().toString().equalsIgnoreCase("")) {
            Utility.showToastMessage(MenuProfileActivity.this, "Please select a State");
            return false;
        } else if (edit_text_city.getText().toString().trim().isEmpty() || edit_text_city.getText().toString().equalsIgnoreCase("")) {
            Utility.showToastMessage(MenuProfileActivity.this, "Please select a City");
            return false;
        } else if (Utility.getSharedPrefStringData(MenuProfileActivity.this, Constants.IS_VERIFIED).equalsIgnoreCase("0")) {
            Utility.showToastMessage(MenuProfileActivity.this, "Please verify your Email Id to use valet and vault services");
            return false;
        }
        return true;
    }

    private void CallForEditProfile(String key, String value) {
        try {
            if (Utility.isNetworkAvailable(this)) {
                Utility.showLoadingDialog(this, "Loading...", false);
                JSONObject params = new JSONObject();
                params.put("user_id", Utility.getSharedPrefStringData(this, Constants.USER_ID));
                params.put(key, value);

                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequestJSonObject(this, "POST", Constants.PROFILE_EDIT, params, new HashMap<String, String>(), this, Constants.PROFILE_EDIT_CODE);

            } else {
                PopUtils.alertDialog(this, Utility.getResourcesString(this, R.string.check_internet), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callServiceForStates() {
        if (Utility.isNetworkAvailable(MenuProfileActivity.this)) {
            //Utility.showLoadingDialog(MenuProfileActivity.this, "Loading...", false);

            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(MenuProfileActivity.this, "GET", Constants.STATE_URL, new JSONObject(), new HashMap<String, String>(), this, Constants.STATE_CODE);

        } else {
            PopUtils.alertDialog(MenuProfileActivity.this, Utility.getResourcesString(this, R.string.check_internet), null);
        }

    }

    private void callServiceForCities(String stateId) {
        if (Utility.isNetworkAvailable(MenuProfileActivity.this)) {
            Utility.showLoadingDialog(MenuProfileActivity.this, "Loading...", false);
            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(MenuProfileActivity.this, "GET", Constants.CITY_URL + "?state_id=" + stateId, new JSONObject(), new HashMap<String, String>(), this, Constants.CITY_CODE);

        } else {
            PopUtils.alertDialog(MenuProfileActivity.this, Utility.getResourcesString(this, R.string.check_internet), null);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public boolean onSupportNavigateUp() {
        if(checkValidation()) {
            onBackPressed();
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if(checkValidation()) {
            super.onBackPressed();
        }
        edit_text_name.clearFocus();
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
            case Constants.STATE_CODE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    stateArrayList = new ArrayList<>();
                    if (status == 200) {

                        stateArrayList.clear();
                        JSONArray dataJsonArray = jsonObject.optJSONArray("data");
                        for (int i = 0; i < dataJsonArray.length(); i++) {

                            JSONObject dataJsonObject = dataJsonArray.optJSONObject(i);

                            String state_id = dataJsonObject.optString("state_id");
                            String name = dataJsonObject.optString("name");

                            State state = new State(state_id, name);
                            stateArrayList.add(state);
                            if (state_id.equals((Utility.getSharedPrefStringData(MenuProfileActivity.this, Constants.STATE_ID)))) {
                                edit_text_state.setText(name);
                                callServiceForCities(state_id);
                            }

                        }
                    }else if(status==401) {
                        PopUtils.alertDialog(MenuProfileActivity.this, message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseApplication.getInstance().callServiceForLogOut();
                            }
                        });

                    }else {
                       // Toast.makeText(MenuProfileActivity.this,message,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.CITY_CODE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String cityName = "";
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");

                     cityArrayList = new ArrayList<>();

                    if (status == 200) {

                        cityArrayList.clear();
                        JSONArray dataJsonArray = jsonObject.optJSONArray("data");
                        for (int i = 0; i < dataJsonArray.length(); i++) {
                            JSONObject dataJsonObject = dataJsonArray.optJSONObject(i);

                            String city_id = dataJsonObject.optString("city_id");
                            cityName = dataJsonObject.optString("name");

                            City city = new City(city_id, cityName);
                            cityArrayList.add(city);

//                            if (city_id.equals(city_id1)) {
//                                edit_text_city.setText(cityName);
//
//                            }
                        }
                    }else if(status==401) {
                        PopUtils.alertDialog(MenuProfileActivity.this, message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseApplication.getInstance().callServiceForLogOut();
                            }
                        });

                    }else {
                        if(message.contains("No data")){
                            cityArrayList.clear();
                        }
                        //Toast.makeText(MenuProfileActivity.this,message,Toast.LENGTH_SHORT).show();
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
                        String otp = jsonObject.optString("otp");
                        Intent update = new Intent(MenuProfileActivity.this, VerifyDetailsActivity.class);
                        update.putExtra("OTP", otp);
                        update.putExtra("MOBILE",edit_text_mobile.getText().toString());
                        update.putExtra("PAGE", "EditProfile");
                        startActivity(update);
                        finish();
                    }else if(status==401) {

                        PopUtils.alertDialog(MenuProfileActivity.this, message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                edit_text_mobile.setText(Utility.getSharedPrefStringData(MenuProfileActivity.this,Constants.MOBILE));
                            }
                        });
                       // Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(MenuProfileActivity.this,message,Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                    break;
            case Constants.PROFILE_EDIT_CODE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    //  String user_id = jsonObject.optString("user_id");
                    if (status==200) {

                        Log.e("chdgcvgdvc","dvgvv "+response.toString());

                        mList.clear();
                        edit_text_name.clearFocus();
                        edit_text_name.setFocusableInTouchMode(true);
                        JSONArray detailsJsonArray = jsonObject.optJSONArray("user_details");
                        if (detailsJsonArray != null && detailsJsonArray.length() > 0) {
                            JSONObject detailsJsonObject = detailsJsonArray.optJSONObject(0);

                            String user_id1 = detailsJsonObject.optString("user_id");
                            name = detailsJsonObject.optString("name");
                            mobile = detailsJsonObject.optString("mobile");
                            email = detailsJsonObject.optString("email");
                            String image = detailsJsonObject.optString("image");
                            dob = detailsJsonObject.optString("dob");
                            gender = detailsJsonObject.optString("gender");
                            state = detailsJsonObject.optString("state");
                            String city1 = detailsJsonObject.optString("city");
                            String wallet_amount = detailsJsonObject.optString("wallet_amount");
                            String state_id = detailsJsonObject.optString("state_id");
                            String city_id = detailsJsonObject.optString("city_id");
                            String qr = detailsJsonObject.optString("qr");
                            String is_verified = detailsJsonObject.optString("is_verified");

                            Utility.setSharedPrefStringData(MenuProfileActivity.this, Constants.USER_ID, user_id1);
                            Utility.setSharedPrefStringData(MenuProfileActivity.this, Constants.NAME, name);
                            Utility.setSharedPrefStringData(MenuProfileActivity.this, Constants.MOBILE, mobile);
                            Utility.setSharedPrefStringData(MenuProfileActivity.this, Constants.EMAIL, email);
                            Utility.setSharedPrefStringData(MenuProfileActivity.this, Constants.IMAGE, image);
                            Utility.setSharedPrefStringData(MenuProfileActivity.this, Constants.DOB, dob);
                            Utility.setSharedPrefStringData(MenuProfileActivity.this, Constants.GENDER, gender);
                            Utility.setSharedPrefStringData(MenuProfileActivity.this, Constants.WALLET_AMOUNT, wallet_amount);
                            Utility.setSharedPrefStringData(MenuProfileActivity.this, Constants.STATE_ID, state_id);

                            Utility.setSharedPrefStringData(MenuProfileActivity.this, Constants.CITY_ID, city_id); // Naga added

                            Utility.setSharedPrefStringData(MenuProfileActivity.this,Constants.IS_VERIFIED,is_verified);

                            if(!city.isEmpty()) {
                                Utility.setSharedPrefStringData(MenuProfileActivity.this, Constants.CITY, city1);
                            }
                            Utility.setSharedPrefStringData(MenuProfileActivity.this, Constants.STATE, state);


                        }
                        edit_text_name.setText(name);
                        if (gender.equals("1")) {
                            edit_text_gender.setText("Male");
                        } else if(gender.equals("2")) {
                            edit_text_gender.setText("Female");
                        }else if(gender.equalsIgnoreCase("3")) {
                            edit_text_gender.setText("Prefer not to say");
                        }else {
                            edit_text_gender.setText("");
                        }
                        if(Utility.getSharedPrefStringData(MenuProfileActivity.this,Constants.DOB).equals("1970-01-01")){
                            Calendar cal = Calendar.getInstance();

                            year = cal.get(Calendar.YEAR);
                            month =cal.get(Calendar.MONTH);
                            day = cal.get(Calendar.DAY_OF_MONTH);

                            edit_text_dob.setText("");
                        }else {
                            if(dob!=null)
                            edit_text_dob.setText(setExpDate(dob));
                        }

                        edit_text_mobile.setText(mobile);
                        edit_text_email.setText(email);
                        Picasso.get().load(Utility.getSharedPrefStringData(this, Constants.IMAGE)).placeholder(R.drawable.ic_user).error(R.drawable.ic_user).into(image_view_user);
                        edit_text_state.setText(state);
//                        edit_text_city.setText(city);*/
                        if(isEmailVerify){

                            SharedPreference.setStringPreference(this,"EDITPROFILE","YES");
                            Utility.setSharedPrefStringData(MenuProfileActivity.this,Constants.IS_EMAIL_VERIFIED_FROM,"EditProfile");
                            Intent editEmailIntent = new Intent(MenuProfileActivity.this,ClickToProceedActivity.class);
                            editEmailIntent.putExtra("TAG","EditProfile");
                            startActivity(editEmailIntent);


                        }else {
                            Utility.showToastMessage(MenuProfileActivity.this, jsonObject.optString("message"));
                        }
                    }else if(status==401) {
                        PopUtils.alertDialog(MenuProfileActivity.this, message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseApplication.getInstance().callServiceForLogOut();
                            }
                        });

                    } else {
                        edit_text_email.setText(Utility.getSharedPrefStringData(MenuProfileActivity.this,Constants.EMAIL));
                        edit_text_name.setText(Utility.getSharedPrefStringData(MenuProfileActivity.this,Constants.NAME));
                        edit_text_dob.setText(Utility.getSharedPrefStringData(MenuProfileActivity.this,Constants.DOB));
                        /*edit_text_state.setText(Utility.getSharedPrefStringData(MenuProfileActivity.this,Constants.STATE));
                        edit_text_city.setText(Utility.getSharedPrefStringData(MenuProfileActivity.this,Constants.CITY));*/
                        edit_text_mobile.setText(Utility.getSharedPrefStringData(MenuProfileActivity.this,Constants.MOBILE));
                        if(Utility.getSharedPrefStringData(MenuProfileActivity.this,Constants.GENDER).equals("2")) {
                            edit_text_gender.setText("Female");
                        }else if(Utility.getSharedPrefStringData(MenuProfileActivity.this,Constants.GENDER).equals("1")) {
                            edit_text_gender.setText("Male");
                        }else if(Utility.getSharedPrefStringData(MenuProfileActivity.this,Constants.GENDER).equals("3")) {
                            edit_text_gender.setText("Prefer not to say");
                        }else {
                            edit_text_gender.setText(" ");
                        }

                        PopUtils.alertDialog(this, message, null);
                        //Utility.showToastMessage(MenuProfileActivity.this, jsonObject.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_male:
                edit_text_gender.setText("Male");
                image_view_male.requestFocus();
                image_view_male.setColorFilter(ContextCompat.getColor(MenuProfileActivity.this, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                image_view_female.setColorFilter(ContextCompat.getColor(MenuProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);
                image_view_other.setColorFilter(ContextCompat.getColor(MenuProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);
                gender = "1";
                isEmailVerify = false;
                CallForEditProfile("gender", gender);
                break;
            case R.id.image_view_female:
                edit_text_gender.setText("Female");
                image_view_female.setColorFilter(ContextCompat.getColor(MenuProfileActivity.this, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                image_view_male.setColorFilter(ContextCompat.getColor(MenuProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);
                image_view_other.setColorFilter(ContextCompat.getColor(MenuProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);
                gender = "2";
                isEmailVerify = false;
                CallForEditProfile("gender", gender);

                break;
            case R.id.image_view_other:
                edit_text_gender.setText("Prefer not to say");
                image_view_female.setColorFilter(ContextCompat.getColor(MenuProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);
                image_view_male.setColorFilter(ContextCompat.getColor(MenuProfileActivity.this, R.color.light_ash), android.graphics.PorterDuff.Mode.SRC_IN);
                image_view_other.setColorFilter(ContextCompat.getColor(MenuProfileActivity.this, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                gender = "3";
                isEmailVerify = false;
                CallForEditProfile("gender", gender);

                break;
            case R.id.text_view_update:
                edit_text_dob.clearFocus();
                mobile = edit_text_mobile.getText().toString();
                if(!mobile.equalsIgnoreCase("")&&!mobile.isEmpty()){
                if (Utility.isMobile(mobile)) {
                     if(!Utility.getSharedPrefStringData(this,Constants.MOBILE).equals(mobile)) {

                         Utility.showLoadingDialog(MenuProfileActivity.this, "Loading...", false);

                         JSONObject jsonObject = new JSONObject();
                         try {
                             jsonObject.put("mobile", mobile);
                             jsonObject.put("device_id", BaseApplication.getDeviceId(this));
                             jsonObject.put("device_type ","1");
                             jsonObject.put("key_hash", Utility.getSharedPrefStringData(this, Constants.KEY_HASH));
                             jsonObject.put("device_token", Utility.getSharedPrefStringData(this,Constants.DEVICE_TOKEN));

                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                         ServerResponse serverResponse = new ServerResponse();
                         serverResponse.serviceRequestJSonObject(MenuProfileActivity.this, "POST", Constants.OTP_URL, jsonObject, new HashMap<String, String>(), this, Constants.OTP_CODE);

                     }else {
                         Toast.makeText(getApplicationContext(),"This number is already registered with this account!",Toast.LENGTH_SHORT).show();
                     }
                }else {
                    Utility.showToastMessage(MenuProfileActivity.this, "Please enter a valid Mobile Number");
                }
            }else {
                    Utility.showToastMessage(MenuProfileActivity.this, "Please enter your Mobile Number");
                }
                break;
            case R.id.edit_text_dob:
                image_view_calender.requestFocus();
                String birth = edit_text_dob.getText().toString();
                DatePickerDialog datePickerDialog = new DatePickerDialog(MenuProfileActivity.this, R.style.MyTimePickerDialogStyle ,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String myFormat = "yyyy-MM-dd"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
                        String finalDate = null;
                        try {

                            String s = sdf.format(calendar.getTime());
                            Date date = simpleDateFormat1.parse(s);
                            dob = s;
                            finalDate = simpleDateFormat.format(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        edit_text_dob.setText(finalDate);
                        isEmailVerify = false;
                        CallForEditProfile("dob", dob);

                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                datePickerDialog.show();
                break;
            case R.id.edit_text_state:
                edit_text_dob.clearFocus();
                edit_text_email.clearFocus();
                final String temp_state_id = state_id;
                ArrayList<String> stateNamesArraylist = new ArrayList<>();
                for (State state : stateArrayList) {
                    stateNamesArraylist.add(state.getStateName());
                }

                if(stateNamesArraylist.size()>0)
                    setBottomSpinner(stateNamesArraylist,new ArrayList<String>(),"States");
               // SpinnerWindow.showSpinner(MenuProfileActivity.this,"state",stateNamesArraylist,new ArrayList<String>());
                break;
            case R.id.edit_text_city:
                edit_text_dob.clearFocus();
                edit_text_email.clearFocus();
                ArrayList<String> cityNamesArraylist = new ArrayList<>();
                //if (cityArrayList != null && cityArrayList.size() > 0) {
                if(!edit_text_state.getText().toString().isEmpty()){
                    for (City city : cityArrayList) {
                        cityNamesArraylist.add(city.getCityName());
                    }
                    if(cityNamesArraylist.size()>0) {

                        setBottomSpinner(cityNamesArraylist,new ArrayList<String>(),"Cities");
                        //SpinnerWindow.showSpinner(MenuProfileActivity.this,"city",cityNamesArraylist,new ArrayList<String>());

                    }else {
                        Toast.makeText(MenuProfileActivity.this,"No data",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Utility.showToastMessage(MenuProfileActivity.this, "Please select state first");
                }
                break;
            case R.id.image_view_user:

               // selectImage();
                openDialog();

                break;

        }

    }

    private void hideKeypad(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private String setExpDate(String date) {
      //  if(date!=null&&!date.equalsIgnoreCase("")) {
            String parsedDate = "";
            Log.e("date", "date" + date);
            //01/04/20

            try {
                Date initDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                parsedDate = formatter.format(initDate);
                Calendar cal = Calendar.getInstance();
                cal.setTime(initDate);
                if (initDate != null) {
                    year = cal.get(Calendar.YEAR);
                    month = cal.get(Calendar.MONTH);
                    day = cal.get(Calendar.DAY_OF_MONTH);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        //}
            return parsedDate;
    }

    private void openDialog() {

        SelectImageDialog selectImageDialog = new SelectImageDialog();
        selectImageDialog.setCallback(MenuProfileActivity.this);
        selectImageDialog.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Dialog_MinWidth);
        selectImageDialog.show(getSupportFragmentManager(), "Select Profile Picture");

    }


    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery" };
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuProfileActivity.this);
        builder.setTitle("Change Profile Picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    if (ContextCompat.checkSelfPermission(MenuProfileActivity.this,Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                MY_CAMERA_REQUEST_CODE);
                    }else {
                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                        photoFile = getPhotoFileUri(photoFileName);

                        Uri fileProvider = FileProvider.getUriForFile(MenuProfileActivity.this, "com.codepath.fileprovider", photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

                        startActivityForResult(intent, 1);
                    }
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public File getPhotoFileUri(String fileName) {

        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "COMPILE");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("COMPILE", "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }
    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(img, selectedImage);
        return img;
    }
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    public Bitmap rotateBitmapOrientation(String photoFilePath) {
        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
        // Read EXIF Data
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        // Return result
        return rotatedBitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            File file = new File(mCurrentPhotoPath);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media
                        .getBitmap(this.getContentResolver(), Uri.fromFile(file));
                if (bitmap != null) {
                    Bitmap bitmap1 = handleSamplingAndRotationBitmap(MenuProfileActivity.this, Uri.fromFile(file));
                  //  new BaseAsyncTask().execute(bitmap1);

                    image_view_user.setImageBitmap(bitmap1);


                    String encodedImage = Utility.encodeImage(bitmap1);
                    isEmailVerify = false;
                    CallForEditProfile("profile_image", encodedImage);

                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {


            Uri path = data.getData();
            try {
                image_view_user.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), path));

                final InputStream imageStream = getContentResolver().openInputStream(path);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                String encodedImage = Utility.encodeImage(decoded);
                isEmailVerify = false;
                CallForEditProfile("profile_image", encodedImage);

            } catch (IOException e) {
                e.printStackTrace();

    }
        }
    }

    class BaseAsyncTask extends AsyncTask<Bitmap, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(MenuProfileActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }

        @Override
        protected String doInBackground(Bitmap... bitmaps) {

            Bitmap bitmap = bitmaps[0];
            if (bitmap != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                return Base64.encodeToString(byteArray, Base64.DEFAULT);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
                profileImage = s;
                isEmailVerify = false;
                CallForEditProfile("profile_image", profileImage);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void selectedPosition(String viewFrom, int selected_position) {
        switch (viewFrom){
            case "state":

               // Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
                edit_text_state.setText(stateArrayList.get(selected_position).getStateName());
                state_id = stateArrayList.get(selected_position).getStateId();

                Log.e("vhbfvgfv","cdcgv "+state);
                Log.e("vhbfvgfv","cdcgv "+stateArrayList.get(selected_position).getStateName());
                Log.e("vhbfvgfv","cdcgv "+city);

              //  Utility.setSharedPrefStringData(MenuProfileActivity.this, Constants.CITY, city);

                Utility.setSharedPrefStringData(MenuProfileActivity.this, Constants.CITY_ID, "");
                callServiceForCities(state_id);
                isEmailVerify = false;
                CallForEditProfile("state_id", state_id);

                Utility.setSharedPrefStringData(MenuProfileActivity.this, Constants.CITY, "");
                edit_text_city.setText("");
                city = "";
                Log.e("fhvbfvd","state city name "+city);


                break;
            case "city" :
                city_id1 = cityArrayList.get(selected_position).getCityId();
                edit_text_city.setText(cityArrayList.get(selected_position).getCityName());
                isEmailVerify = false;
                CallForEditProfile("city_id", city_id1);
                city = cityArrayList.get(selected_position).getCityName();

                Log.e("fhvbfvd","city city name "+city);

                break;
        }

    }
    private void setBottomSpinner(ArrayList<String> name, ArrayList<String> logos, final String viewFrom) {
        final BottomSheetDialog dialog = new BottomSheetDialog(MenuProfileActivity.this, R.style.Theme_Design_BottomSheetDialog);
        dialog.setContentView(R.layout.item_spinner_bottom);
        BottomSheetListView rv_list = dialog.findViewById(R.id.bottomList);
        ConstraintLayout frame = dialog.findViewById(R.id.containerBottom);
        TextView txt_title = dialog.findViewById(R.id.txt_title);
        txt_title.setText( viewFrom);

        final ArrayAdapter<String> adapter;
        adapter = new CustomAdapterForSpinner(MenuProfileActivity.this, R.layout.dialog_dropdown_with_image, name, logos);
        rv_list.setAdapter(adapter);
        rv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if(viewFrom.equalsIgnoreCase("States")){
                    edit_text_state.setText(stateArrayList.get(position).getStateName());
                    state_id = stateArrayList.get(position).getStateId();

                    Log.e("hgbfhvfb","ONE "+city);
                    Log.e("hgbfhvfb","STATE NAME "+(stateArrayList.get(position).getStateName()));

                    Utility.setSharedPrefStringData(MenuProfileActivity.this, Constants.CITY_ID, "");
                    callServiceForCities(state_id);
                    isEmailVerify = false;
                    CallForEditProfile("state_id", state_id);

                    city = "";
                    Utility.setSharedPrefStringData(MenuProfileActivity.this, Constants.CITY, city);
                    edit_text_city.setText("");

                    Log.e("hgbfhvfb","LAST "+city);


                }else if(viewFrom.equalsIgnoreCase("Cities")){
                    city_id1 = cityArrayList.get(position).getCityId();
                    edit_text_city.setText(cityArrayList.get(position).getCityName());
                    isEmailVerify = false;
                    CallForEditProfile("city_id", city_id1);
                    city = cityArrayList.get(position).getCityName();
                }
                dialog.dismiss();
            }

        });
        dialog.show();
    }

    public void requestPermissions() {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            onCameraSelect();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @Override
    public void onCameraSelect() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            requestPermissions();
        } else {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File

                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.versatilemobitech.VeeZee.provider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST);


                }
            }
        }
    }

    @Override
    public void onGallerySelect() {

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            requestPermissions();
        } else {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(i, RESULT_LOAD_IMAGE);


        }

    }
}
