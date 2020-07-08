package com.versatilemobitech.VeeZee.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.versatilemobitech.VeeZee.BaseApplication;
import com.versatilemobitech.VeeZee.Model.UserDetails;
import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.SharedPreference;
import com.versatilemobitech.VeeZee.UtilHelpers.AppSignatureHelper;
import com.versatilemobitech.VeeZee.WebUtils.ServerResponse;
import com.versatilemobitech.VeeZee.interfaces.IParseListener;
import com.versatilemobitech.VeeZee.utils.Constants;
import com.versatilemobitech.VeeZee.utils.PopUtils;
import com.versatilemobitech.VeeZee.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, IParseListener {

    EditText edit_text_mobile;
    TextView text_view_proceed, text_view_terms;
    ImageView image_view_facebook, image_view_googleplus;
    LoginButton login_button;
    SignInButton sign_in_button;
    String socialName, socialEmail, socialGender,socialImage;


    String mobile, otp;

    AlertDialog alertDialog;
    public static final String USER_DETAILS = "USER_DETAILS";
    public static final String OTP = "OTP";
    public static final String MOBILE = "MOBILE";
    public static final String FROM = "FROM";
    public static final String USER_ID = "USER_ID";

    private static final String TAG = "AndroidClarified";
    private GoogleSignInClient googleSignInClient;
    private GoogleApiClient mGoogleApiClient;

    private CallbackManager callbackManager;
    String loginType = "";
    String cars_count = "";
    boolean isFaceBook = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        AppSignatureHelper signatureHelper = new AppSignatureHelper(this);
        signatureHelper.getAppSignatures();


        Log.e("vhfdbvfvg","dfhd "+generateKeyHash());
        init();

    }

    private void init() {

        edit_text_mobile = findViewById(R.id.edit_text_mobile);
        text_view_proceed = findViewById(R.id.text_view_proceed);
        text_view_terms = findViewById(R.id.text_view_terms);
        image_view_facebook = findViewById(R.id.image_view_fb);
        image_view_googleplus = findViewById(R.id.image_view_googleplus);
        login_button = findViewById(R.id.login_button);

        sign_in_button = findViewById(R.id.sign_in_button);
        login_button.setPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        callbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this,"ERROR",Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        text_view_terms.setOnClickListener(this);
        text_view_proceed.setOnClickListener(this);
        image_view_facebook.setOnClickListener(this);
        image_view_googleplus.setOnClickListener(this);

        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                // Retrieving access token using the LoginResult
                AccessToken accessToken = loginResult.getAccessToken();
                useLoginInformation(accessToken);
                isFaceBook = true;
                LoginManager.getInstance().logOut();
                /*AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null) {
                    useLoginInformation(accessToken);
                }*/
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onError(FacebookException error) {

                Log.e("cdgcvgdvh","fvhf "+error.toString());

                Utility.showToastMessage(LoginActivity.this,error.getMessage());
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_view_proceed:
/*                Intent intent = new Intent(this, VerifyDetailsActivity.class);
                startActivity(intent);*/

                SharedPreference.setStringPreference(this,"SOCIALLOGIN","NO");

                isFaceBook = false;
                mobile = edit_text_mobile.getText().toString();
                if(!mobile.isEmpty()&&!mobile.equalsIgnoreCase("")){

                if (Utility.isNetworkAvailable(this)) {
                    if (Utility.isMobile(mobile)) {
                        Utility.showLoadingDialog(LoginActivity.this, "Loading...", false);

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
                        serverResponse.serviceRequestJSonObject(LoginActivity.this, "POST", Constants.OTP_URL, jsonObject, new HashMap<String, String>(), this, Constants.OTP_CODE);

                    } else {
                        Utility.showToastMessage(LoginActivity.this, "Please enter a valid Mobile Number");
                    }
                } else {
                    Utility.showToastMessage(LoginActivity.this, getString(R.string.check_internet));
                }
                }else {
                    Utility.showToastMessage(LoginActivity.this, "Please enter your Mobile Number");
                }



                break;
            case R.id.text_view_terms:
                showTermsDialog();
                break;
            case R.id.image_view_fb:
                loginType = "2";

                SharedPreference.setStringPreference(this,"SOCIALLOGIN","YES");

                login_button.performClick();
                break;
            case R.id.image_view_googleplus:
                isFaceBook = false;
                loginType = "1";

                SharedPreference.setStringPreference(this,"SOCIALLOGIN","YES");

                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
                break;
            case R.id.img_close:
                alertDialog.dismiss();
                break;
        }
    }

    private void loginWithSocialSites(boolean isFromFb){

        if (Utility.isNetworkAvailable(this)) {

                Utility.showLoadingDialog(LoginActivity.this, "Loading...", false);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("mobile", "");
                    jsonObject.put("name", socialName);
                    jsonObject.put("email", socialEmail);
                    jsonObject.put("gender", socialGender);
                    jsonObject.put("dob", "");
                    jsonObject.put("state_id", "");
                    jsonObject.put("city_id", "");
                    jsonObject.put("profile_image",socialImage);
                    jsonObject.put("device_type ","1");
                    jsonObject.put("device_id", BaseApplication.getDeviceId(this));
                    jsonObject.put("device_token", Utility.getSharedPrefStringData(this,Constants.DEVICE_TOKEN));
                    if(isFromFb)
                    jsonObject.put("login_type", 2);
                    else jsonObject.put("login_type", 1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequestJSonObject(LoginActivity.this, "POST", Constants.SOCIAL_LOGINS, jsonObject, new HashMap<String, String>(), this, Constants.SOCIAL_LOGINS_CODE);

        } else {
            Utility.showToastMessage(LoginActivity.this, getString(R.string.check_internet));
        }

    }

    private void showTermsDialog() {
        final Dialog dialog;
        dialog = new Dialog(this);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_terms);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView textView = dialog.findViewById(R.id.txt_term);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textView.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }
        //textView.setMovementMethod(new ScrollingMovementMethod());
        ImageView ivClose = dialog.findViewById(R.id.ivclose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void useLoginInformation(AccessToken accessToken) {

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            //OnCompleted is invoked once the GraphRequest is successful

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    socialEmail = object.getString("email");
                    socialName = object.getString("name");
                    socialImage = object.getJSONObject("picture").getJSONObject("data").getString("url");

                    new MyAsync().execute();
                    Log.e("FACEBOOK","FACEBOOK"+ "Gender :"+ socialGender +"EMAIL :"+ socialEmail +"NAME :"+ socialName);

                    String image = object.getJSONObject("picture").getJSONObject("data").getString("url");

                    Log.e("image","faceBook Image"+image);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("chdchdbcgbd","dbbgv "+e.toString());
                    Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_SHORT).show();
                }
            }
        });
        // We set parameters to the GraphRequest using a Bundle.
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,picture.width(200),birthday");
        request.setParameters(parameters);
        // Initiate the GraphRequest
        request.executeAsync();
    }
    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {

        socialName = (googleSignInAccount.getDisplayName());
        socialEmail = (googleSignInAccount.getEmail());
        socialImage = String.valueOf((googleSignInAccount.getPhotoUrl()));
        Log.e("GMAIL","GMAIL"+"EMAIL :"+ socialEmail +"NAME :"+ socialName + "IMAGE "+socialImage);
        new MyAsync().execute();

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

                        Log.e("gdvdgcdvc","LOGIN car count value "+cars_count);
                        Log.e("gdvdgcdvc","LOGIN car res  "+response.toString());

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
                            String state_id = detailsJsonObject.optString("state_id");
                            String city_id = detailsJsonObject.optString("city_id");
                            String is_verified = detailsJsonObject.optString("is_verified");
                            String city = detailsJsonObject.optString("city");
                            String state = detailsJsonObject.optString("state");

                            Log.e("gdvdgcdvc","LOGIN car count value "+city);


                            Utility.setSharedPrefStringData(LoginActivity.this, Constants.USER_ID, user_id);
                            Utility.setSharedPrefStringData(LoginActivity.this, Constants.NAME, name);
                            Utility.setSharedPrefStringData(LoginActivity.this, Constants.MOBILE, mobile);
                            Utility.setSharedPrefStringData(LoginActivity.this, Constants.EMAIL, email);
                            Utility.setSharedPrefStringData(LoginActivity.this, Constants.IMAGE, image);
                            Utility.setSharedPrefStringData(LoginActivity.this, Constants.DOB, dob);
                            Utility.setSharedPrefStringData(LoginActivity.this, Constants.GENDER, gender);
                            Utility.setSharedPrefStringData(LoginActivity.this, Constants.WALLET_AMOUNT, wallet_amount);
                            Utility.setSharedPrefStringData(LoginActivity.this, Constants.QR, qr);
                            Utility.setSharedPrefStringData(LoginActivity.this, Constants.STATE_ID, state_id);
                            Utility.setSharedPrefStringData(LoginActivity.this, Constants.CITY_ID, city_id);
                            Utility.setSharedPrefBooleanData(LoginActivity.this, Constants.IS_LOGGEDIN, true);
                            Utility.setSharedPrefStringData(LoginActivity.this,Constants.IS_VERIFIED,is_verified);
                            Utility.setSharedPrefStringData(LoginActivity.this,Constants.CITY,city);
                            Utility.setSharedPrefStringData(LoginActivity.this,Constants.STATE,state);


                            UserDetails userDetails = new UserDetails(user_id, name, mobile, email, image, dob, gender, wallet_amount,qr,cars_count);

                            Intent intent = new Intent(LoginActivity.this, VerifyDetailsActivity.class);
                            intent.putExtra(OTP, otp);
                            intent.putExtra(MOBILE, mobile);
                            intent.putExtra(USER_DETAILS, userDetails);
                            intent.putExtra("PAGE","LOGIN");
                            startActivity(intent);
                            Utility.showToastMessage(this, jsonObject.optString("message")/*+" : "+otp*/);

                        } else {

                            Intent intent = new Intent(LoginActivity.this, VerifyDetailsActivity.class);
                            intent.putExtra(OTP, otp);
                            intent.putExtra(MOBILE, mobile);
                            intent.putExtra("PAGE","LOGIN");
                            startActivity(intent);
                            Utility.showToastMessage(this, jsonObject.optString("message")/*+" : "+otp*/);

                        }

                    }else if(status==401) {
                       PopUtils.alertDialog(LoginActivity.this, message, new View.OnClickListener() {
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
                case Constants.SOCIAL_LOGINS_CODE:
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        int status = jsonObject.optInt("status");
                        String message = jsonObject.optString("message");
                       // String user_id1 = jsonObject.optString("user_id");
                        String cars_count = jsonObject.optString("cars_count");
                        String user_id1 = "";
                        if (status == 200) {

                            Log.e("gdvdgcdvc","Scoial LOGIN car res  "+response.toString());

                            JSONArray detailsJsonArray = jsonObject.optJSONArray("user_details");
                            if (detailsJsonArray != null && detailsJsonArray.length() > 0) {
                                JSONObject detailsJsonObject = detailsJsonArray.optJSONObject(0);

                                user_id1 = detailsJsonObject.optString("user_id");
                                String name = detailsJsonObject.optString("name");
                                String mobile = detailsJsonObject.optString("mobile");
                                String email = detailsJsonObject.optString("email");
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


                                Utility.setSharedPrefStringData(LoginActivity.this, Constants.USER_ID, user_id1);
                                Utility.setSharedPrefStringData(LoginActivity.this, Constants.NAME, name);
                                Utility.setSharedPrefStringData(LoginActivity.this, Constants.MOBILE, mobile);
                                Utility.setSharedPrefStringData(LoginActivity.this, Constants.EMAIL, email);
                                Utility.setSharedPrefStringData(LoginActivity.this, Constants.IMAGE, image);
                                Utility.setSharedPrefStringData(LoginActivity.this, Constants.DOB, dob);
                                Utility.setSharedPrefStringData(LoginActivity.this, Constants.GENDER, gender);
                                Utility.setSharedPrefStringData(LoginActivity.this, Constants.WALLET_AMOUNT, wallet_amount);
                                Utility.setSharedPrefStringData(LoginActivity.this, Constants.QR, qr);
                                Utility.setSharedPrefStringData(LoginActivity.this, Constants.STATE_ID, state_id);
                                Utility.setSharedPrefStringData(LoginActivity.this, Constants.CITY_ID, city_id);
                                Utility.setSharedPrefBooleanData(LoginActivity.this, Constants.IS_LOGGEDIN, true);
                                Utility.setSharedPrefStringData(LoginActivity.this,Constants.IS_VERIFIED,is_verified);

                                Utility.setSharedPrefStringData(LoginActivity.this,Constants.CITY,city);
                                Utility.setSharedPrefStringData(LoginActivity.this,Constants.STATE,state);

                                    Utility.showToastMessage(LoginActivity.this, jsonObject.optString("message"));
                                    Intent intent = new Intent(LoginActivity.this, ClickToProceedActivity.class);
                                    intent.putExtra(FROM, "FACEBOOK");
                                    intent.putExtra(USER_ID, user_id1);
                                    intent.putExtra("CAR_COUNT",cars_count);
                                    startActivity(intent);
                                   // finish();
                              //  }
                            }
                            if (message.equalsIgnoreCase("Please Register")) {
                               // Utility.showToastMessage(LoginActivity.this, jsonObject.optString("message"));
                                Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                                intent.putExtra(FROM, "FACEBOOK");
                                intent.putExtra("FACEBOOK_NAME", socialName);
                                intent.putExtra("FACEBOOK_EMAIL", socialEmail);
                                intent.putExtra("LOGIN_TYPE", loginType);
                                intent.putExtra("SOCIAL_IMAGE",socialImage);
                                intent.putExtra(USER_ID, user_id1);
                                startActivity(intent);
                               // finish();

                            }

                        } else if (status == 400) {
                            PopUtils.alertDialog(this, message, null);
                        }else if(status==401) {
                            PopUtils.alertDialog(LoginActivity.this, message,null);

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    break;
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resulrCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resulrCode, data);
        super.onActivityResult(requestCode, resulrCode, data);
        if (resulrCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 101:
                    try {
                        // The Task returned from this call is always completed, no need to attach
                        // a listener.
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        onLoggedIn(account);
                    } catch (ApiException e) {
                        // The ApiException status code indicates the detailed failure reason.
                        Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                    }
                    break;
            }

    }

    public void onStart() {
        super.onStart();
        /*AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            useLoginInformation(accessToken);
        }*/
    }
    public class MyAsync extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {

            try {
                URL url = new URL(socialImage);
                Log.e("URL","URL:"+url+"STRING"+socialImage);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            socialImage = Utility.encodeImage(bitmap);
            //Log.e("SOCIALIMAGE","SOCIALIMAGE: "+bitmap+"ENCODED IMAGE :"+socialImage);
            if(isFaceBook) {
                loginWithSocialSites(true);
            }else {
                loginWithSocialSites(false);
            }
        }
    }


    private String generateKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = (MessageDigest.getInstance("SHA"));
                md.update(signature.toByteArray());
                return new String(Base64.encode(md.digest(), 0));
            }
        }catch (Exception e) {
            Log.e("exception", e.toString());
        }
        return "key hash not found";
    }


}
