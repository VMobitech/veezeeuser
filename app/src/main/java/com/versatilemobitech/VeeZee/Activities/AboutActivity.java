package com.versatilemobitech.VeeZee.Activities;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.versatilemobitech.VeeZee.R;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class AboutActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView toolBarTitle;

    ImageView image_view_coin;
    TextView text_view_wallet_amount, text_view_v,txt_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        getSupportActionBar().setTitle("");
        toolBarTitle.setVisibility(View.VISIBLE);
        toolBarTitle.setText("About");
        toolBarTitle.setGravity(Gravity.LEFT);

        text_view_v = findViewById(R.id.text_view_credits_v);
        image_view_coin = findViewById(R.id.image_view_coin);
        text_view_wallet_amount = findViewById(R.id.text_view_wallet_amount);
        txt_about = findViewById(R.id.txt_about);

        text_view_v.setVisibility(View.GONE);
        text_view_wallet_amount.setVisibility(View.GONE);
        image_view_coin.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            txt_about.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
