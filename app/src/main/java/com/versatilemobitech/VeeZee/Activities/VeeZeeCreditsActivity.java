package com.versatilemobitech.VeeZee.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.versatilemobitech.VeeZee.Adapters.TransactionHistoryAdapter;
import com.versatilemobitech.VeeZee.BaseApplication;
import com.versatilemobitech.VeeZee.Model.CreditsHistory;
import com.versatilemobitech.VeeZee.Model.PartnerCategories;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class VeeZeeCreditsActivity extends AppCompatActivity implements IParseListener {
    RecyclerView rv_transaction;
    TransactionHistoryAdapter mAdapter;
    Toolbar toolbar;
    TextView toolBarTitle;
    List<CreditsHistory> mTrasactionList = new ArrayList<>();

    ImageView image_view_coin;
    TextView text_view_wallet_amount, text_view_v, text_view_creditbalance,txt_no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vee_zee_credits);
        init();
    }

    private void init() {
        setReferences();
        callServiceForCreditsHistory();

    }

    private void setAdapter() {

        rv_transaction.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mAdapter = new TransactionHistoryAdapter(this, mTrasactionList);
        rv_transaction.setAdapter(mAdapter);

    }

    private void setReferences() {
        rv_transaction = findViewById(R.id.rv_transaction);
        toolBarTitle = findViewById(R.id.toolBarTitle);
        toolbar = findViewById(R.id.toolBar);
        txt_no_data = findViewById(R.id.txt_no_data);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        getSupportActionBar().setTitle("");
        toolBarTitle.setVisibility(View.VISIBLE);
        toolBarTitle.setText("VeeZee Credits");
        toolBarTitle.setGravity(Gravity.LEFT);
        toolbar.setPadding(0, 0, 0, 0);

        text_view_wallet_amount = findViewById(R.id.text_view_wallet_amount);
        text_view_v = findViewById(R.id.text_view_credits_v);
        image_view_coin = findViewById(R.id.image_view_coin);
        text_view_creditbalance = findViewById(R.id.txt_credit_balance);

        text_view_creditbalance.setText(Utility.getSharedPrefStringData(this, Constants.WALLET_AMOUNT));

        text_view_v.setVisibility(View.GONE);
        text_view_wallet_amount.setVisibility(View.GONE);
        image_view_coin.setVisibility(View.GONE);
        txt_no_data.setVisibility(View.GONE);
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void callServiceForCreditsHistory() {
        if (Utility.isNetworkAvailable(this)) {
            Utility.showLoadingDialog(this, "Loading...", false);
            String userId = Utility.getSharedPrefStringData(this, Constants.USER_ID);
            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(this, "GET", Constants.CREDITS_HISTORY + "?user_id=" + userId, new JSONObject(), new HashMap<String, String>(), this, Constants.CREDITS_HISTORY_CODE);

        } else {
            PopUtils.alertDialog(VeeZeeCreditsActivity.this, Utility.getResourcesString(this, R.string.check_internet), null);
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
            case Constants.CREDITS_HISTORY_CODE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 200) {
                    mTrasactionList.clear();
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                                String wallet_history_id = jsonObject1.optString("wallet_history_id");
                                String amount = jsonObject1.optString("amount");
                                String action = jsonObject1.optString("action");
                                String reason = jsonObject1.optString("reason");
                                String created_time = jsonObject1.optString("created_time");
                                String transactionStatus = jsonObject1.optString("status");

                                CreditsHistory history = new CreditsHistory(wallet_history_id,amount,action,reason,created_time,transactionStatus);
                                mTrasactionList.add(history);
                            }
                           setAdapter();
                        }
                        text_view_creditbalance.setText(jsonObject.optString("credit_balance"));
                        Utility.setSharedPrefStringData(this, Constants.WALLET_AMOUNT,jsonObject.optString("credit_balance"));
                    }else if(status==401) {
                        PopUtils.alertDialog(VeeZeeCreditsActivity.this,message , new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseApplication.getInstance().callServiceForLogOut();
                            }
                        });

                    }else {
                        txt_no_data.setVisibility(View.VISIBLE);
                        txt_no_data.setText("No data found");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }
}
