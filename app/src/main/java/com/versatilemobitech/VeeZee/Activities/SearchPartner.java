package com.versatilemobitech.VeeZee.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.versatilemobitech.VeeZee.Adapters.SearchPartnerAdapter;
import com.versatilemobitech.VeeZee.Model.PartnerModel;
import com.versatilemobitech.VeeZee.R;

import java.util.ArrayList;

public class SearchPartner extends AppCompatActivity {
    ArrayList<PartnerModel> partnerModelArrayList = new ArrayList<>();
    Bundle b;
    private SearchView searchView;
    private MenuItem searchMenuItem;
    RecyclerView recycler_view;
    //ArrayAdapter<String>  mAdapter;
    EditText edit_text_location;
    SearchPartnerAdapter mAdapter;
    ImageView image_back;
    TextView text_close,text_nodata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_partner);
        recycler_view = findViewById(R.id.recycler_view);
        edit_text_location = findViewById(R.id.edit_text_location);
        image_back = findViewById(R.id.image_back);
        text_close = findViewById(R.id.text_close);
        text_nodata = findViewById(R.id.text_nodata);
        text_close.setVisibility(View.GONE);
        text_nodata.setVisibility(View.GONE);
        recycler_view.setVisibility(View.GONE);
        getArguments();


        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        text_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_text_location.setText("");
            }
        });

        edit_text_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length()>0)
                {
                    recycler_view.setVisibility(View.VISIBLE);
                    text_nodata.setVisibility(View.GONE);
                    boolean b = mAdapter.filter(editable.toString());
                    if(editable.toString().length()>2){
                        text_close.setVisibility(View.VISIBLE);
                        text_nodata.setVisibility(View.GONE);
                    }
                    if(!b){
                        text_nodata.setVisibility(View.VISIBLE);

                    }else {
                        text_nodata.setVisibility(View.GONE);
                    }

                }
                else {
                    //mAdapter = new SearchPartnerAdapter(SearchPartner.this,partnerModelArrayList);
                    recycler_view.setVisibility(View.GONE);
                    if(partnerModelArrayList.size()>0) {
                        mAdapter.filterList(partnerModelArrayList);
                        recycler_view.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        text_nodata.setVisibility(View.GONE);
                    }else {
                        text_nodata.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
    }

    private void getArguments() {

        Intent i = getIntent();
        if(i!= null)
        partnerModelArrayList = (ArrayList<PartnerModel>) i.getSerializableExtra("list");
        if(partnerModelArrayList!= null){
            recycler_view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            mAdapter = new SearchPartnerAdapter(SearchPartner.this,partnerModelArrayList);
            recycler_view.setAdapter(mAdapter);

        }
    }
}
