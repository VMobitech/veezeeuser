package com.versatilemobitech.VeeZee.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.versatilemobitech.VeeZee.Adapters.ShareAdapter;
import com.versatilemobitech.VeeZee.BaseApplication;
import com.versatilemobitech.VeeZee.Fragments.CarsFragment;
import com.versatilemobitech.VeeZee.Fragments.HomeFragment;
import com.versatilemobitech.VeeZee.Model.Contacts;
import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.WebUtils.ServerResponse;
import com.versatilemobitech.VeeZee.interfaces.IParseListener;
import com.versatilemobitech.VeeZee.utils.Constants;
import com.versatilemobitech.VeeZee.utils.PopUtils;
import com.versatilemobitech.VeeZee.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ShareActivity extends AppCompatActivity implements IParseListener, ShareAdapter.SendClickListener {
    RecyclerView rv_share;
    Toolbar toolbar;
    TextView text_view_title;
    TextView text_view_credits_v, text_view_wallet_amount,text_view_no_data,tvNodataFound;
    ImageView image_view_coin, img_credits;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    ContentResolver cr;
    Cursor cursor;
    Contacts mContacts;
    ArrayList<Contacts> ContactsArrayList;
    ArrayList<Contacts> contactsArrayList; //after remove own contact
    //SearchView search_view;
    ImageView image_back;
    SearchView search_view;
    boolean isSearch = false;
    ShareAdapter mShareAdapter;

    JSONArray objectArray;
    String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        init();
    }

    private void init() {
        setReferences();
        setOnClickListeners();
        getArguments();
        //setAdapter();
    }

    private void getArguments() {
        Intent i = getIntent();
        if(i!=null){
            json =  i.getStringExtra("JSON");
            try {
                if(json!=null)
                objectArray = new JSONArray(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setOnClickListeners() {
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setAdapter(ArrayList<Contacts> contacts) {
         contactsArrayList = new ArrayList<>();

        Iterator<Contacts> iter = contacts.iterator();
        while(iter.hasNext()){
            Contacts str = iter.next();
            if( !str.getMobile().equals(Utility.getSharedPrefStringData(this,Constants.MOBILE))){
                // contacts.remove(str);

                contactsArrayList.add(str);
            }
        }



        rv_share.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mShareAdapter = new ShareAdapter(this, contactsArrayList);
        rv_share.setAdapter(mShareAdapter);

        Log.e("hvbfvbghfdvb","fvhbfghv "+contactsArrayList.size());

        if (contactsArrayList!=null && contactsArrayList.size()>0)
        {
            tvNodataFound.setVisibility(View.GONE);
        }
        else
        {
            tvNodataFound.setVisibility(View.VISIBLE);
        }



    }

    private void setReferences() {
        rv_share = findViewById(R.id.rv_share);
        text_view_title = findViewById(R.id.text_view_title);
        image_back = findViewById(R.id.image_back);
        //toolbar = findViewById(R.id.toolBar);
        img_credits = findViewById(R.id.img_credits);
        search_view = findViewById(R.id.search);
        text_view_no_data = findViewById(R.id.text_view_no_data);


        tvNodataFound = findViewById(R.id.tvNodataFound);

        text_view_title.setVisibility(View.VISIBLE);
        text_view_title.setText("Share");

        search_view.setMaxWidth(Integer.MIN_VALUE);
        search_view.setVisibility(View.VISIBLE);
        text_view_no_data.setVisibility(View.GONE);

        img_credits.setVisibility(View.GONE);
        img_credits.setImageDrawable(getResources().getDrawable(R.drawable.ic_search));

        text_view_credits_v = findViewById(R.id.text_view_credits_v);
        text_view_credits_v.setVisibility(View.GONE);

        image_view_coin = findViewById(R.id.image_view_coin);
        image_view_coin.setVisibility(View.GONE);

        text_view_wallet_amount = findViewById(R.id.text_view_wallet_amount);
        text_view_wallet_amount.setVisibility(View.GONE);

        showContacts();

        search_view.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_view_title.setVisibility(View.GONE);
            }
        });
        search_view.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                text_view_title.setVisibility(View.VISIBLE);
                return false;
            }
        });

        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.isEmpty()) {
                    if (ContactsArrayList != null && ContactsArrayList.size() > 0) {
                        isSearch = mShareAdapter.filter(newText);
                        if (!isSearch) {
                            text_view_no_data.setVisibility(View.VISIBLE);
                            text_view_no_data.setText("No contacts available");
                        } else {

                            text_view_no_data.setVisibility(View.GONE);
                        }
                    } else {
                        if (contactsArrayList != null && contactsArrayList.size() > 0) {
                            text_view_no_data.setVisibility(View.GONE);
                            mShareAdapter.filterList(contactsArrayList);
                        }
                    }
                }else {
                    if(contactsArrayList!=null&&contactsArrayList.size()>0) {
                        text_view_no_data.setVisibility(View.GONE);
                        mShareAdapter.filterList(contactsArrayList);
                    }
                }
                return false;
            }
        });
    }

    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {

            ArrayList<Long> contacts = getContacts();
            callServiceForContacts(contacts);

            Log.e("ContactList", "Contact" + contacts.toString());
            Log.e("ContactList", "Contact" + contacts.size());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(getApplicationContext(), "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected ArrayList<Long> getContacts() {
        ArrayList<Long> mobile = new ArrayList<>();

        Cursor contacts = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        while (contacts.moveToNext()) {
            // Get the current contact name
            String name = contacts.getString(
                    contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY));

            // Get the current contact phone number
            String phoneNumber = contacts.getString(
                    contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


            if(!phoneNumber.isEmpty()&&!phoneNumber.equals("")&&phoneNumber.length()>=10 ) {
                if(phoneNumber.length()<=18){
                    String no =phoneNumber.replace("+91","");
                    if(no.matches("^[7-9][0-9]{9}$")){
                        mobile.add(Long.parseLong(no));
                    }
                }

                //mobile.add(Long.parseLong((phoneNumber.replaceAll("^[0-9-]+$", " "))));
            }
        }
        contacts.close();
        Log.e("MOBILE NUMBERS",""+mobile.toString());
        return mobile;
    }




    private void initCursor() {
        cr = getContentResolver();

        if (cr != null) {
            cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null,
                    null, null);

        }
    }

    private void callServiceForContacts(ArrayList<Long>mobileList) {
        if (Utility.isNetworkAvailable(this)) {
            Utility.showLoadingDialog(this, "Loading...", false);
            try {
                JSONArray jsonArray=new JSONArray(mobileList);
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("contacts",jsonArray);

                Log.e("hbcdvcgv","fvgvb "+jsonArray.toString());

                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequestJSonObject(this, "POST", Constants.REQUEST_CONTACTS ,jsonObject, new HashMap<String, String>(), this, Constants.REQUEST_CONTACTS_CODE);
            }catch (Exception e){
                e.printStackTrace();
            }


        } else {
            PopUtils.alertDialog(this, Utility.getResourcesString(this, R.string.check_internet), null);
        }
    }

    private void callServiceForShare(String shared_car_id) {
        if (Utility.isNetworkAvailable(this)) {
            Utility.showLoadingDialog(this, "Loading...", false);
            try { ;
                JSONObject jsonObject=new JSONObject();
                //user_car_id
                jsonObject.put("user_id",Utility.getSharedPrefStringData(this,Constants.USER_ID));
                jsonObject.put("user_car_id", objectArray);
                jsonObject.put("shared_user_id",shared_car_id);
                jsonObject.put("is_veezee_share","true");


                Log.e("fbvfgvfgv","user_id "+Utility.getSharedPrefStringData(this,Constants.USER_ID));
                Log.e("fbvfgvfgv","user_carid "+objectArray);
                Log.e("fbvfgvfgv","shared_userid "+shared_car_id);

                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequestJSonObject(this, "POST", Constants.SHARE_CAR ,jsonObject, new HashMap<String, String>(), this, Constants.SHARE_CAR_CODE);
            }catch (Exception e){
                e.printStackTrace();
            }


        } else {
            PopUtils.alertDialog(this, Utility.getResourcesString(this, R.string.check_internet), null);
        }
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void ErrorResponse(String response, int requestCode) {
        Utility.hideLoadingDialog();
        PopUtils.alertDialog(this, Utility.getResourcesString(this, R.string.something_went_wrong), null);
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        Utility.hideLoadingDialog();
        switch (requestCode){
            case  Constants.REQUEST_CONTACTS_CODE :
                Log.e("CONTACTS","CONTACTS"+response.toLowerCase());

                Log.e("hbcdvcgv","response "+response.toString());

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if(status == 200) {
                        text_view_no_data.setVisibility(View.GONE);
                        String image_path = jsonObject.optString("image_path");
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        ContactsArrayList = new ArrayList<>();
                        if (jsonArray != null && jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                                String name = jsonObject1.optString("name");
                                String mobile = jsonObject1.optString("mobile");
                                String image = jsonObject1.optString("image");
                                String share_user_id = jsonObject1.getString("user_id");

                                mContacts = new Contacts(name, mobile, image_path + image, share_user_id);
                                ContactsArrayList.add(mContacts);
                            }
                        }
                        if (ContactsArrayList != null && ContactsArrayList.size() > 0) {
                            setAdapter(ContactsArrayList);
                        }
                    }else if(status==401) {
                        PopUtils.alertDialog(ShareActivity.this, message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseApplication.getInstance().callServiceForLogOut();
                            }
                        });

                    }else {
                        text_view_no_data.setVisibility(View.VISIBLE);
                        text_view_no_data.setText("No contacts found");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case Constants.SHARE_CAR_CODE :
                Log.e("SHARE_CAR_CODE","SHARE_CAR_CODE"+response.toLowerCase());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");

                    Log.e("fbvfgvfgv","dvfgvgf"+message);
                    Log.e("fbvfgvfgv","res "+response.toString());

                    if(status==200){

                        PopUtils.alertDialog(this, message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(ShareActivity.this,MainActivity.class);
                                i.putExtra("TAG","CARS");
                                startActivity(i);

                            }
                        });

                    }else if(status==401) {
                        PopUtils.alertDialog(ShareActivity.this, message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseApplication.getInstance().callServiceForLogOut();
                            }
                        });

                    }else {
                        PopUtils.alertDialog(this, message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(ShareActivity.this,MainActivity.class);
                                i.putExtra("TAG","CARS");
                                startActivity(i);

                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }

    }

    @Override
    public void sendClick(String shareId,String number) {
        if(mContacts!=null) {
            if(objectArray!=null) {

                callServiceForShare(shareId);

                Log.e("fbvfgvfgv","share_id "+shareId);
           //     Log.e("fbvfgvfgv","position "+position);

//                for (int i=0;i<ContactsArrayList.size();i++)
//                {
//
//                }
//
             //   Log.e("fbvfgvfgv","position "+position);

             //   Log.e("fbvfgvfgv","contact i "+mContcts.get(position).getShare_user_id());

            }else {

                Uri uri = Uri.parse("smsto:"+number);
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                //it.putExtra("sms_body", Constants.ShareQr);
                it.putExtra("sms_body","Greetings from VeeZee!\n" +
                        Utility.getSharedPrefStringData(ShareActivity.this,Constants.NAME)+" has shared the qr "+ Html.fromHtml(Constants.ShareQr) + ". Please use the following link to download the unique Ticket QR Code");

                startActivity(it);

            }
        }
    }
}
