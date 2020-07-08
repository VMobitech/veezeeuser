package com.versatilemobitech.VeeZee.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.versatilemobitech.VeeZee.Model.Rewards;
import com.versatilemobitech.VeeZee.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView toolBarTitle, text_view_whatsinside, text_view_website,text_view_voucher_expirydate, text_view_note,text_view_credits_v,text_view_wallet_amount;
    ImageView image_view_coin;

    Rewards rewards;

    String inside,visitWebsite,voucherExpireOn,howToRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getIntentData();
        init();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {

          //  rewards = intent.getParcelableExtra("rewards");

            inside = getIntent().getExtras().getString("inside");
            visitWebsite = getIntent().getExtras().getString("visitWebsite");
            voucherExpireOn = getIntent().getExtras().getString("voucherExpireOn");
            howToRead = getIntent().getExtras().getString("howToRead");


        }
    }

    private void init() {
        setReferences();
    }

    private void setReferences() {
        toolBarTitle = findViewById(R.id.toolBarTitle);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        getSupportActionBar().setTitle("");
        toolBarTitle.setVisibility(View.VISIBLE);
        toolBarTitle.setText("Details");
        toolBarTitle.setGravity(Gravity.LEFT);

        text_view_whatsinside = findViewById(R.id.text_view_whatsinside);
        text_view_website = findViewById(R.id.text_view_website);
        text_view_voucher_expirydate = findViewById(R.id.text_view_voucher_expirydate);
        text_view_note = findViewById(R.id.text_view_note);

//        text_view_whatsinside.setText(rewards.getWhats_inside());
//        text_view_website.setText(rewards.getWebsite());
//        text_view_voucher_expirydate.setText(setExpDate(rewards.getValid_to()));

        text_view_whatsinside.setText(inside);
        text_view_website.setText(visitWebsite);
        text_view_voucher_expirydate.setText(setExpDate(voucherExpireOn));



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
         //   text_view_note.setText(Html.fromHtml(rewards.getHowToRedeem(), Html.FROM_HTML_MODE_COMPACT));
            text_view_note.setText(Html.fromHtml(howToRead, Html.FROM_HTML_MODE_COMPACT));
        } else {
          //  text_view_note.setText(Html.fromHtml(rewards.getHowToRedeem()));
            text_view_note.setText(Html.fromHtml(howToRead));
        }

        text_view_credits_v=findViewById(R.id.text_view_credits_v);
        text_view_credits_v.setVisibility(View.GONE);

        image_view_coin=findViewById(R.id.image_view_coin);
        image_view_coin.setVisibility(View.GONE);

        text_view_wallet_amount=findViewById(R.id.text_view_wallet_amount);
        text_view_wallet_amount.setVisibility(View.GONE);


    }
    private String setExpDate(String date) {

        String parsedDate = "";
        Log.e("date", "date" + date);
        //01/04/20

        try {
            Date initDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            parsedDate = formatter.format(initDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parsedDate;
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
