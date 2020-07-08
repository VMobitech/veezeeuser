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
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.versatilemobitech.VeeZee.Adapters.FAQTabAdapter;
import com.versatilemobitech.VeeZee.Adapters.FaqAdapter;
import com.versatilemobitech.VeeZee.Adapters.HistoryTabAdapter;
import com.versatilemobitech.VeeZee.R;

import java.util.ArrayList;

public class FAQsActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    Toolbar toolbar;
    TextView toolBarTitle;
    RecyclerView rv_faq;
    ImageView image_view_back;

    TabLayout tabLayout;
    ViewPager pager;
    FAQTabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);
        init();
    }
    private void init() {
        setReferences();
        setAdapter();
    }

    private void setAdapter() {
        /*rv_faq.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        ArrayList<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");
        mFaqAdapter = new FaqAdapter(this,list);
        rv_faq.setAdapter(mFaqAdapter);*/
        adapter = new FAQTabAdapter(FAQsActivity.this, getSupportFragmentManager(), tabLayout.getTabCount());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    private void setReferences() {
        rv_faq = findViewById(R.id.rv_faq);
        image_view_back = findViewById(R.id.image_view_back);
        tabLayout = findViewById(R.id.tabLayout);
        pager = findViewById(R.id.pager);

        toolBarTitle = findViewById(R.id.toolBarTitle) ;
        toolbar =  findViewById(R.id.toolBar);

        image_view_back.setOnClickListener(this);
        setTabs();
    }

    private void setTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("VALET"));
        tabLayout.addTab(tabLayout.newTab().setText("VAULT"));
        tabLayout.addTab(tabLayout.newTab().setText("REWARDS"));
        tabLayout.addTab(tabLayout.newTab().setText("REFERRAL"));
        tabLayout.addTab(tabLayout.newTab().setText("GENERAL"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        setAdapter();
        tabLayout.addOnTabSelectedListener(FAQsActivity.this);
        tabLayout.setupWithViewPager(pager);
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
