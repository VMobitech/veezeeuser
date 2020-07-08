package com.versatilemobitech.VeeZee.Activities;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;
import com.versatilemobitech.VeeZee.Adapters.MenuAdapter;
import com.versatilemobitech.VeeZee.BaseApplication;
import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.WebUtils.ServerResponse;
import com.versatilemobitech.VeeZee.interfaces.IParseListener;
import com.versatilemobitech.VeeZee.utils.Constants;
import com.versatilemobitech.VeeZee.utils.PopUtils;
import com.versatilemobitech.VeeZee.utils.Utility;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener, IParseListener {
    RecyclerView rv_memu, rv_help;
    MenuAdapter mAdapter;
    List<String> mMenuList;
    ImageView imageView_back, image_view_user;
    CircleImageView img_user;
    LinearLayout ll_veezee_credits, ll_history, ll_about, ll_help, ll_refer_friend, ll_delete_acct, ll_logout;
    TextView txt_help;
    boolean isMenuExpand = false;
    TextView text_view_username;
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        init();

    }

    private void init() {
        setReferences();
        setClickListeners();
        setAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String image_url = Utility.getSharedPrefStringData(MenuActivity.this, Constants.IMAGE);
        if (image_url != null && !image_url.equalsIgnoreCase("") ) {
            Picasso.get().load(image_url).placeholder(R.drawable.ic_user).into(img_user);
        }
        text_view_username.setText(Utility.getSharedPrefStringData(MenuActivity.this, Constants.NAME));
    }

    private void setClickListeners() {
        imageView_back.setOnClickListener(this);
        img_user.setOnClickListener(this);
        ll_veezee_credits.setOnClickListener(this);
        ll_history.setOnClickListener(this);
        ll_about.setOnClickListener(this);
        ll_help.setOnClickListener(this);
        ll_refer_friend.setOnClickListener(this);
        ll_delete_acct.setOnClickListener(this);
        ll_logout.setOnClickListener(this);
        txt_help.setOnClickListener(this);
        text_view_username.setOnClickListener(this);
    }

    private void setAdapter() {
        rv_help.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mMenuList = Arrays.asList(getResources().getStringArray(R.array.menu_list));
        mAdapter = new MenuAdapter(this, mMenuList);
        rv_help.setAdapter(mAdapter);
    }

    private void setReferences() {
        rv_help = findViewById(R.id.rv_help);
        imageView_back = findViewById(R.id.image_view_back);
        img_user = findViewById(R.id.image_view_user);
        ll_veezee_credits = findViewById(R.id.ll_veezee_credits);
        ll_history = findViewById(R.id.ll_history);
        ll_about = findViewById(R.id.ll_about);
        ll_help = findViewById(R.id.ll_help);
        ll_refer_friend = findViewById(R.id.ll_refer_friend);
        ll_delete_acct = findViewById(R.id.ll_delete_acct);
        ll_logout = findViewById(R.id.ll_logout);
        txt_help = findViewById(R.id.txt_help);

        //image_view_user = findViewById(R.id.image_view_user);
        text_view_username = findViewById(R.id.text_view_user_name);

        text_view_username.setText(Utility.getSharedPrefStringData(MenuActivity.this, Constants.NAME).trim());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                onBackPressed();
                break;
            case R.id.image_view_user:
                startActivity(new Intent(MenuActivity.this, MenuProfileActivity.class));
                break;
            case R.id.ll_veezee_credits:
                startActivity(new Intent(MenuActivity.this, VeeZeeCreditsActivity.class));
                break;
            case R.id.ll_history:
                startActivity(new Intent(MenuActivity.this, HistoryActivity.class));
                break;
            case R.id.ll_about:
                startActivity(new Intent(MenuActivity.this, AboutActivity.class));
                break;
            case R.id.ll_delete_acct:
                setDialogforDeleteAcct();
                break;
            case R.id.ll_logout:
                setDialogforLogOut();
                break;
            case R.id.ll_refer_friend:
                startActivity(new Intent(MenuActivity.this, ReferAFriendActivity.class));
                break;
            case R.id.txt_help:
                rv_help.setVisibility(rv_help.isShown() ? View.GONE : View.VISIBLE);
                if (rv_help.isShown()) {
                    txt_help.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0);
                } else {
                    txt_help.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down_arrow, 0);
                }
                break;
            case R.id.text_view_user_name:
                startActivity(new Intent(MenuActivity.this, MenuProfileActivity.class));
                break;
        }

    }
    private void callServiceForDeleteAccount() {

        if (Utility.isNetworkAvailable(this)) {
            Utility.showLoadingDialog(this, "Loading...", false);
            try{
               JSONObject params = new JSONObject();
               params.put("user_id",Utility.getSharedPrefStringData(this,Constants.USER_ID));
                params.put("device_id", BaseApplication.getDeviceId(this));
                params.put("device_type ","1");
            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(this, "POST", Constants.DELETE_USER_ACCOUNT ,params, new HashMap<String, String>(), this, Constants.DELETE_USER_ACCOUNT_CODE);
            }catch (Exception e){
                e.printStackTrace();
            }

        } else {
            PopUtils.alertDialog(this, Utility.getResourcesString(this, R.string.check_internet), null);
        }
    }
    private void callServiceForLogOut() {

        if (Utility.isNetworkAvailable(this)) {
            Utility.showLoadingDialog(this, "Loading...", false);
            int userId = Integer.parseInt(Utility.getSharedPrefStringData(this, Constants.USER_ID));
            try{
                JSONObject params = new JSONObject();
                params.put("user_id",userId);
                params.put("device_id", BaseApplication.getDeviceId(this));
                params.put("device_type ","1");
                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequestJSonObject(this, "POST", Constants.LOG_OUT ,params, new HashMap<String, String>(), this, Constants.LOG_OUT_CODE);
            }catch (Exception e){
                e.printStackTrace();
            }

        } else {
            PopUtils.alertDialog(this, Utility.getResourcesString(this, R.string.check_internet), null);
        }
    }

    private void setDialogforDeleteAcct() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this, R.style.CustomAlertDialog);
        ViewGroup mViewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(mViewGroup.getContext()).inflate(R.layout.custom_dialog_delete_account, mViewGroup, false);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        TextView txt_yes, txt_cancel;
        txt_yes = dialogView.findViewById(R.id.txt_yes);
        txt_cancel = dialogView.findViewById(R.id.txt_cancel);
        txt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                callServiceForDeleteAccount();

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

    private void setDialogforLogOut() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this, R.style.CustomAlertDialog);
        ViewGroup mViewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(mViewGroup.getContext()).inflate(R.layout.custom_dialog_logout, mViewGroup, false);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        TextView txt_yes, txt_cancel;
        txt_yes = dialogView.findViewById(R.id.txt_yes);
        txt_cancel = dialogView.findViewById(R.id.txt_cancel);
        txt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                callServiceForLogOut();

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
        super.onBackPressed();
        MainActivity.ClickToNavigate = "VALET";
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
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
            case Constants.DELETE_USER_ACCOUNT_CODE :
            Log.e("DELETE ACCOUNT", "DELETE ACCOUNT" + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");
                if (status.equals("true")) {
                    NotificationManager notifManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notifManager.cancelAll();

                    Utility.setSharedPrefStringData(MenuActivity.this, Constants.USER_ID, "");
                    Utility.setSharedPrefStringData(MenuActivity.this, Constants.NAME, "");
                    Utility.setSharedPrefStringData(MenuActivity.this, Constants.MOBILE, "");
                    Utility.setSharedPrefStringData(MenuActivity.this, Constants.EMAIL, "");
                    Utility.setSharedPrefStringData(MenuActivity.this, Constants.IMAGE, "");
                    Utility.setSharedPrefStringData(MenuActivity.this, Constants.DOB, "");
                    Utility.setSharedPrefStringData(MenuActivity.this, Constants.GENDER, "");
                    Utility.setSharedPrefStringData(MenuActivity.this, Constants.WALLET_AMOUNT, "");
                    Utility.setSharedPrefStringData(MenuActivity.this,Constants.STATE_ID,"");
                    Utility.setSharedPrefStringData(MenuActivity.this,Constants.CITY_ID,"");
                    Utility.setSharedPrefStringData(MenuActivity.this,Constants.CITY,"");
                    Utility.setSharedPrefStringData(MenuActivity.this,Constants.STATE,"");

                    LoginManager.getInstance().logOut();
                    //google+ logout
                    GoogleSignInOptions gso = new GoogleSignInOptions.
                            Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                            build();

                    GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(MenuActivity.this,gso);
                    googleSignInClient.signOut();
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    Utility.setSharedPrefBooleanData(MenuActivity.this, Constants.IS_LOGGEDIN, false);
                    startActivity(new Intent(MenuActivity.this, LoginActivity.class));
                    Intent i = new Intent(MenuActivity.this, LoginActivity.class);
                    startActivity(i);
                    finishAffinity();
                } else {
                    Utility.showToastMessage(this, message);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
            case Constants.LOG_OUT_CODE :
                Log.e("LOG_OUT_CODE", "LOG_OUT_CODE" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    if (status.equals("true")) {

                        NotificationManager notifManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notifManager.cancelAll();

                        Utility.setSharedPrefStringData(MenuActivity.this, Constants.USER_ID, "");
                        Utility.setSharedPrefStringData(MenuActivity.this, Constants.NAME, "");
                        Utility.setSharedPrefStringData(MenuActivity.this, Constants.MOBILE, "");
                        Utility.setSharedPrefStringData(MenuActivity.this, Constants.EMAIL, "");
                        Utility.setSharedPrefStringData(MenuActivity.this, Constants.IMAGE, "");
                        Utility.setSharedPrefStringData(MenuActivity.this, Constants.DOB, "");
                        Utility.setSharedPrefStringData(MenuActivity.this, Constants.GENDER, "");
                        Utility.setSharedPrefStringData(MenuActivity.this, Constants.WALLET_AMOUNT, "");
                        Utility.setSharedPrefStringData(MenuActivity.this,Constants.STATE_ID,"");
                        Utility.setSharedPrefStringData(MenuActivity.this,Constants.CITY_ID,"");
                        Utility.setSharedPrefStringData(MenuActivity.this,Constants.CITY,"");
                        Utility.setSharedPrefStringData(MenuActivity.this,Constants.STATE,"");

                        //facebook logout
                        //Toast.makeText(getApplicationContext(),"Logout Successfully",Toast.LENGTH_SHORT).show();
                        LoginManager.getInstance().logOut();
                        //google+ logout
                        GoogleSignInOptions gso = new GoogleSignInOptions.
                                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                                build();

                        GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(MenuActivity.this,gso);
                        googleSignInClient.signOut();
                        /*startActivity(new Intent(MenuActivity.this, LoginActivity.class));*/
                        Utility.setSharedPrefBooleanData(MenuActivity.this,Constants.IS_LOGGEDIN,false);
                        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Utility.showToastMessage(this, message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
