package com.versatilemobitech.VeeZee.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.UtilHelpers.Utils;
import com.versatilemobitech.VeeZee.utils.Constants;
import com.versatilemobitech.VeeZee.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class ReferAFriendActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView toolBarTitle,txt_refer;

    ImageView image_view_coin;
    TextView text_view_wallet_amount, text_view_v;
    int REFERAL_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_afriend);
        init();
    }

    private void init() {
        setReferences();
    }

    private void setReferences() {
        toolBarTitle = findViewById(R.id.toolBarTitle) ;
        toolbar = findViewById(R.id.toolbar);
        txt_refer = findViewById(R.id.txt_refer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        getSupportActionBar().setTitle("");

        text_view_v = findViewById(R.id.text_view_credits_v);
        image_view_coin = findViewById(R.id.image_view_coin);
        text_view_wallet_amount = findViewById(R.id.text_view_wallet_amount);

        text_view_v.setVisibility(View.GONE);
        text_view_wallet_amount.setVisibility(View.GONE);
        image_view_coin.setVisibility(View.GONE);


        toolBarTitle.setVisibility(View.VISIBLE);
        toolBarTitle.setText("Refer a Friend");
        toolBarTitle.setGravity(Gravity.LEFT);
        txt_refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onShareClick(view);
          }
       });
    }

    public void onShareClick(View v) {
        Constants.ShareQr = "https://veezee.app/" ;
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        share.putExtra(Intent.EXTRA_SUBJECT, "VeeZee");
        String userId = Utility.getSharedPrefStringData(this,Constants.USER_ID);
       // share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.versatilemobitech.ucoduser&hl=en&referrer="+userId );
        share.putExtra(Intent.EXTRA_TEXT, "https://veezee.app/");
        startActivityForResult(Intent.createChooser(share, "Share link!"),REFERAL_CODE);

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REFERAL_CODE) {
                if(requestCode == RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Thank you for your Referral", Toast.LENGTH_SHORT).show();
                }

        }
    }
}
