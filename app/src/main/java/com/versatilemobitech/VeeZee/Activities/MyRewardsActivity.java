package com.versatilemobitech.VeeZee.Activities;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.versatilemobitech.VeeZee.Adapters.MyRewardsAdapter;
import com.versatilemobitech.VeeZee.BaseApplication;
import com.versatilemobitech.VeeZee.Model.UserDetails;
import com.versatilemobitech.VeeZee.Model.UserRewards;
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

public class MyRewardsActivity extends AppCompatActivity implements IParseListener {
    Toolbar toolbar;
    TextView toolBarTitle,text_view_wallet_amount,text_view_credits_v,text_view_nodata;
    RecyclerView rv_my_reward;
    MyRewardsAdapter mAdapter;
    ImageView image_view_coin;
    ArrayList<UserRewards> userRewardsArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rewards);
        init();
    }

    private void init() {
        setReferences();
        //setAdapter();
        callServiceForUserRewards();
    }

    private void callServiceForUserRewards() {
        if (Utility.isNetworkAvailable(this)) {
            Utility.showLoadingDialog(this, "Loading...", false);
            String user_id = Utility.getSharedPrefStringData(this, Constants.USER_ID);
            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(this, "GET", Constants.USER_REWARDS + "?user_id="+ user_id /*+ Utility.getSharedPrefStringData(this, Constants.USER_ID)*/, new JSONObject(), new HashMap<String, String>(), this, Constants.USER_REWARDS_CODE);

        } else {
            PopUtils.alertDialog(this, Utility.getResourcesString(this, R.string.check_internet), null);
        }
    }

    private void setAdapter() {
        rv_my_reward.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MyRewardsAdapter(MyRewardsActivity.this, userRewardsArrayList);
        rv_my_reward.setAdapter(mAdapter);
    }

    private void setReferences() {
        rv_my_reward = findViewById(R.id.rv_my_reward);
        text_view_nodata = findViewById(R.id.text_view_nodata);
        toolBarTitle = findViewById(R.id.toolBarTitle);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        getSupportActionBar().setTitle("");
        toolBarTitle.setVisibility(View.VISIBLE);
        toolBarTitle.setText("My Rewards");
        toolBarTitle.setGravity(Gravity.LEFT);

        text_view_credits_v=findViewById(R.id.text_view_credits_v);
        text_view_credits_v.setVisibility(View.GONE);

        image_view_coin=findViewById(R.id.image_view_coin);
        image_view_coin.setVisibility(View.GONE);

        text_view_wallet_amount=findViewById(R.id.text_view_wallet_amount);
        text_view_wallet_amount.setVisibility(View.GONE);

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
        switch (requestCode) {
            case Constants.USER_REWARDS_CODE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 200) {
                        text_view_nodata.setVisibility(View.GONE);
                        userRewardsArrayList.clear();
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                                String reward_id = jsonObject1.optString("reward_id");
                                String title = jsonObject1.optString("title");
                                String voucher_code = jsonObject1.optString("voucher_code");
                                String brand_id = jsonObject1.optString("brand_id");
                                String description = jsonObject1.optString("description");
                                String credits = jsonObject1.optString("credits");
                                String image = jsonObject.optString("image_path") + jsonObject1.optString("image");
                                String valid_from = jsonObject1.optString("valid_from");
                                String valid_to = jsonObject1.optString("valid_to");
                                String whats_inside = jsonObject1.optString("whats_inside");
                                String website = jsonObject1.optString("website");
                                String created_by = jsonObject1.optString("created_by");
                                String created_time = jsonObject1.optString("created_time");
                                String modified_by = jsonObject1.optString("modified_by");
                                String modified_time = jsonObject1.optString("modified_time");
                                String logo = jsonObject.optString("image_path") + jsonObject1.optString("logo");
                                String status1 = jsonObject1.optString("status");
                                String brand = jsonObject1.optString("brand");

                                UserRewards userRewards = new UserRewards(reward_id, title, voucher_code, brand_id, description, credits, image,
                                        valid_from, valid_to, whats_inside, website, created_by, created_time, modified_by, modified_time, status1,logo,brand);

                                userRewardsArrayList.add(userRewards);

                            }
                            setAdapter();

                        }
                    } else if(status==401) {
                        PopUtils.alertDialog(MyRewardsActivity.this, message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseApplication.getInstance().callServiceForLogOut();
                            }
                        });

                    }else if (status == 400) {
                      //  PopUtils.alertDialog(this, jsonObject.optString("message"), null);
                        text_view_nodata.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


        }
    }
}
