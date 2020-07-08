package com.versatilemobitech.VeeZee.Activities;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.versatilemobitech.VeeZee.R;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class Terms_ConditionsActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView toolBarTitle,txt_terms;

    ImageView image_view_coin;
    TextView text_view_wallet_amount, text_view_v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms__conditions);
        init();
    }

    private void init() {
        setReferences();
    }

    private void setReferences() {
        toolBarTitle = findViewById(R.id.toolBarTitle);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        txt_terms = findViewById(R.id.txt_terms);

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            text_view_terms.setText(Html.fromHtml(getResources().getString(R.string.terms_text), Html.FROM_HTML_MODE_COMPACT));
        } else {
            text_view_terms.setText(Html.fromHtml(getResources().getString(R.string.terms_text)));
        }*/


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        getSupportActionBar().setTitle("");
        toolBarTitle.setVisibility(View.VISIBLE);
        toolBarTitle.setText(getResources().getString(R.string.term_condition_wdt_un));
        toolBarTitle.setGravity(Gravity.LEFT);

        text_view_v = findViewById(R.id.text_view_credits_v);
        image_view_coin = findViewById(R.id.image_view_coin);
        text_view_wallet_amount = findViewById(R.id.text_view_wallet_amount);

        text_view_v.setVisibility(View.GONE);
        text_view_wallet_amount.setVisibility(View.GONE);
        image_view_coin.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            txt_terms.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
