package com.versatilemobitech.VeeZee.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.versatilemobitech.VeeZee.Adapters.HistoryTabAdapter;
import com.versatilemobitech.VeeZee.R;

public class HistoryActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    Toolbar toolbar;
    TextView toolBarTitle;
    TabLayout tabLayout;
    ViewPager pager_history;
    HistoryTabAdapter adapter;
    ImageView image_view_back;
    LinearLayout linearBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        init();
    }

    private void init() {
        setReferences();
        setTabs();
    }

    private void setReferences() {
        tabLayout = findViewById(R.id.tabLayout);
        pager_history = findViewById(R.id.pager_history);
        image_view_back = findViewById(R.id.image_view_back);
        linearBack = findViewById(R.id.linear_back);

        image_view_back.setOnClickListener(this);


       /* toolBarTitle = findViewById(R.id.toolBarTitle) ;
        toolbar =  findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        getSupportActionBar().setTitle("");
        toolBarTitle.setVisibility(View.VISIBLE);
        toolBarTitle.setText("History");
        toolBarTitle.setGravity(Gravity.LEFT);*/
    }

    private void setTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("VALET"));
        tabLayout.addTab(tabLayout.newTab().setText("VAULT"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        setAdapter();
        tabLayout.addOnTabSelectedListener(HistoryActivity.this);
        tabLayout.setupWithViewPager(pager_history);
    }

    private void setAdapter() {
        adapter = new HistoryTabAdapter(HistoryActivity.this, getSupportFragmentManager(), tabLayout.getTabCount());
        pager_history.setAdapter(adapter);
        pager_history.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                finish();
                break;
        }
    }
}
