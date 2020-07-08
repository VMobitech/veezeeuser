package com.versatilemobitech.VeeZee.Fragments;


import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.versatilemobitech.VeeZee.Activities.MainActivity;
import com.versatilemobitech.VeeZee.Activities.SearchPartner;
import com.versatilemobitech.VeeZee.Adapters.ClubsAdapter;
import com.versatilemobitech.VeeZee.BaseApplication;
import com.versatilemobitech.VeeZee.Model.PartnerModel;
import com.versatilemobitech.VeeZee.Model.searchModel;
import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.WebUtils.ServerResponse;
import com.versatilemobitech.VeeZee.interfaces.IParseListener;
import com.versatilemobitech.VeeZee.utils.Constants;
import com.versatilemobitech.VeeZee.utils.PopUtils;
import com.versatilemobitech.VeeZee.utils.Utility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClubsFragment extends Fragment implements View.OnClickListener, IParseListener {
    private View view;
    RecyclerView rv_clubs;
    ClubsAdapter mClubsAdapter;
    ImageView img_credits;
    TextView toolBarTitle, text_view_nodata;
    //SearchView searchView;
    List<searchModel> mList = new ArrayList<>();
    ArrayList<PartnerModel> partnerModelArrayList = new ArrayList<>();
    int cat_id,pos ;
    SearchView search_club;
    boolean isSearch = false;



    public ClubsFragment newInstance(int value,int position,String TAG) {
        ClubsFragment fragment = new ClubsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("value", value);
        bundle.putInt("position",position);
        cat_id = value;
        fragment.setArguments(bundle);
        MainActivity.ClickToNavigate = TAG;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_clubs, container, false);
        toolBarTitle = ((MainActivity) getActivity()).findViewById(R.id.toolBarTitle);
        img_credits = ((MainActivity) getActivity()).findViewById(R.id.img_credits);
        initComponents();
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle=getArguments();
        if(bundle!=null)
        {
            pos=bundle.getInt("position");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            //if (partnerModelArrayList != null)
            //callServiceForPartners(getArguments().getInt("value"));

        }

    }

    private void callServiceForPartners(int value) {

        if (Utility.isNetworkAvailable(getActivity())) {
            Utility.showLoadingDialog(getActivity(), "Loading...", false);

            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(getActivity(), "GET", Constants.PARTNER_LIST + "?category_id=" + value, new JSONObject(), new HashMap<String, String>(), this, Constants.PARTNER_LIST_CODE);

        } else {
            PopUtils.alertDialog(getActivity(), Utility.getResourcesString(getActivity(), R.string.check_internet), null);
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        callServiceForPartners(getArguments().getInt("value"));

    }

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this))
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initComponents() {
        setReferences();
        setAdapter();
        setClickListeners();
    }

    private void setClickListeners() {
        img_credits.setOnClickListener(ClubsFragment.this);

    }

    private void setAdapter() {
        if(partnerModelArrayList.size()>0) {
            rv_clubs.setVisibility(View.VISIBLE);
            search_club.setVisibility(View.VISIBLE);
            text_view_nodata.setVisibility(View.GONE);
            rv_clubs.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mClubsAdapter = new ClubsAdapter(getActivity(), partnerModelArrayList);
            rv_clubs.setAdapter(mClubsAdapter);
        }else {
            rv_clubs.setVisibility(View.GONE);
            text_view_nodata.setVisibility(View.VISIBLE);
            search_club.setVisibility(View.GONE);
        }
    }

    private void setReferences() {
        rv_clubs = view.findViewById(R.id.rv_clubs);
        text_view_nodata = view.findViewById(R.id.text_view_nodata);
        search_club = view.findViewById(R.id.search_club);
        search_club.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.isEmpty()) {
                    MainActivity.ClickToNavigate = "FRAGMENT";
                    isSearch = mClubsAdapter.filter(newText);
                    if(!isSearch){
                        text_view_nodata.setVisibility(View.VISIBLE);
                    }else {
                        text_view_nodata.setVisibility(View.GONE);
                    }
                }else {
                    if(partnerModelArrayList!=null&&partnerModelArrayList.size()>0) {
                        text_view_nodata.setVisibility(View.GONE);
                        mClubsAdapter.filterList(partnerModelArrayList);
                    }
                }

                return false;
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_credits:
                //searchView.setVisibility(isVisible()?View.VISIBLE:View.GONE);
                break;
            case R.id.search_club :
                InputMethodManager im = ((InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE));
                im.showSoftInput(search_club, 0);
                break;
        }
    }


    @Override
    public void ErrorResponse(String response, int requestCode) {
        Utility.hideLoadingDialog();
        PopUtils.alertDialog(getActivity(), Utility.getResourcesString(getActivity(), R.string.something_went_wrong), null);
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        Utility.hideLoadingDialog();
        switch (requestCode) {
            case Constants.PARTNER_LIST_CODE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 200) {
                        partnerModelArrayList.clear();
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                                String partner_id = jsonObject1.optString("partner_id");
                                String name = jsonObject1.optString("name");
                                String image = jsonObject1.optString("image");
                                String address = jsonObject1.optString("address");
                                String latitude = jsonObject1.optString("latitude");
                                String longitude = jsonObject1.optString("longitude");

                                PartnerModel partnerModel = new PartnerModel(partner_id, name, image, address, latitude, longitude);
                                partnerModelArrayList.add(partnerModel);
                            }
                            setAdapter();
                        }
                    } else if(status==401) {
                        PopUtils.alertDialog(getActivity(), message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseApplication.getInstance().callServiceForLogOut();
                            }
                        });

                    }else {
                        if(rv_clubs!=null) {
                            search_club.setVisibility(View.GONE);
                            rv_clubs.setVisibility(View.GONE);
                            text_view_nodata.setVisibility(View.VISIBLE);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String s){

            if(pos==Integer.parseInt(s)) {
                /*Intent i = new Intent(getActivity(), SearchPartner.class);
                i.putExtra("list",partnerModelArrayList);
                startActivity(i);*/



            }
        }


}
