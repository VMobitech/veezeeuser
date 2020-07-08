package com.versatilemobitech.VeeZee.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.utils.Constants;
import com.versatilemobitech.VeeZee.utils.Utility;

public class ContactUsActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView toolBarTitle;

    ImageView image_view_coin;
    TextView text_view_wallet_amount, text_view_v, txt_mobile1,txt_mail2;
    LinearLayout ll_phone,ll_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        init();
    }

    private void init() {
        setReferences();
    }

    private void setReferences() {
        toolBarTitle = findViewById(R.id.toolBarTitle);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        getSupportActionBar().setTitle("");
        toolBarTitle.setVisibility(View.VISIBLE);
        toolBarTitle.setText("Contact");
        toolBarTitle.setGravity(Gravity.LEFT);

        text_view_v = findViewById(R.id.text_view_credits_v);
        image_view_coin = findViewById(R.id.image_view_coin);
        text_view_wallet_amount = findViewById(R.id.text_view_wallet_amount);
        txt_mobile1 = findViewById(R.id.txt_mobile1);
        txt_mail2 = findViewById(R.id.txt_mail2);

        text_view_v.setVisibility(View.GONE);
        text_view_wallet_amount.setVisibility(View.GONE);
        image_view_coin.setVisibility(View.GONE);

        ll_phone = findViewById(R.id.ll_phone);
        ll_email = findViewById(R.id.ll_email);

        ll_phone.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);

                intent.setData(Uri.parse("tel:" +txt_mobile1.getText().toString()));

                startActivity(intent);
            }
        });
        ll_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
                selectorIntent.setData(Uri.parse("mailto:"));

                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{txt_mail2.getText().toString()});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
//                emailIntent.putExtra(Intent.EXTRA_TEXT, "Greetings from VeeZee!\n" +
//                        Utility.getSharedPrefStringData(getApplicationContext(), Constants.NAME)+" has shared the valet ticket Please use the following link to download the unique Valet Ticket QR Code.\n" +
//                        "Valet QR Code Link\n"+ Html.fromHtml(Constants.QR));
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                emailIntent.setSelector( selectorIntent );

                startActivity(Intent.createChooser(emailIntent, "Send email..."));


            }
        });

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
