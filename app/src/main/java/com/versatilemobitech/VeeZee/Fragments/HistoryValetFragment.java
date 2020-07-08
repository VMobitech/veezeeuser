package com.versatilemobitech.VeeZee.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;
import com.versatilemobitech.VeeZee.Activities.HistoryActivity;
import com.versatilemobitech.VeeZee.Adapters.HistoryTabAdapter;
import com.versatilemobitech.VeeZee.Adapters.HistoryValetAdapter;
import com.versatilemobitech.VeeZee.BaseApplication;
import com.versatilemobitech.VeeZee.Model.HistoryValet;
import com.versatilemobitech.VeeZee.Model.UserCarModel;
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
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryValetFragment extends Fragment implements IParseListener, HistoryValetAdapter.Listener {
    private  View view;
    RecyclerView rv_valet;
    HistoryValetAdapter mAdapter;
    HistoryValet mHistoryValetModel;
    TextView text_view_nodata;

    ArrayList<HistoryValet> mList = new ArrayList<>();


    public HistoryValetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_history_valet, container, false);
        init();
        return view;
    }

    private void init() {
        setReferences();
        CallForHistoryValet();
    }

    private void CallForHistoryValet() {

        if (Utility.isNetworkAvailable(getActivity())) {
            Utility.showLoadingDialog(getActivity(), "Loading...", false);
            String userID = Utility.getSharedPrefStringData(getActivity(),Constants.USER_ID);
            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(getActivity(), "GET", Constants.HISTORY_VALET+"?user_id="+ userID , new JSONObject(), new HashMap<String, String>(), this, Constants.HISTORY_VALET_CODE);

        } else {
            PopUtils.alertDialog(getActivity(), Utility.getResourcesString(getActivity(), R.string.check_internet), null);
        }
    }
    private void CallReportIssue(int position , String msg){

        if (Utility.isNetworkAvailable(getActivity())) {
            Utility.showLoadingDialog(getActivity(), "Loading...", false);
            String userID = Utility.getSharedPrefStringData(getActivity(),Constants.USER_ID);
            String parkingId = mList.get(position).getParking_id();
            JSONObject json = new JSONObject();
            try {
               json.put("user_id",userID);
               json.put("parking_id",parkingId);
               json.put("issue",msg);

            }catch (Exception e){
                e.printStackTrace();
            }
            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(getActivity(), "POST", Constants.REPORT_ISSUE_VALET, json, new HashMap<String, String>(), this, Constants.REPORT_ISSUE_VALET_CODE);

        } else {
            PopUtils.alertDialog(getActivity(), Utility.getResourcesString(getActivity(), R.string.check_internet), null);
        }
    }


    private void setAdapter() {

       // mList.add("Over The Moon");
        rv_valet.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mAdapter = new HistoryValetAdapter(HistoryValetFragment.this,mList);
        rv_valet.setAdapter(mAdapter);
    }

    private void setReferences() {
        rv_valet = view.findViewById(R.id.rv_valet);
        text_view_nodata = view.findViewById(R.id.text_view_nodata);
        text_view_nodata.setVisibility(View.GONE);
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
            case Constants.HISTORY_VALET_CODE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 200) {
                        mList.clear();
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        if (jsonArray != null && jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                                String parking_id = jsonObject1.optString("parking_id");
                                String tip = jsonObject1.optString("tip");
                                String name = jsonObject1.optString("name");
                                String address = jsonObject1.optString("address");
                                String date = jsonObject1.optString("date");
                                String picked_by = jsonObject1.optString("picked_by");
                                String in_time = jsonObject1.optString("in_time");
                                String dropped_by = jsonObject1.optString("dropped_by");
                                String out_time = jsonObject1.optString("out_time");
                                String image = jsonObject1.optString("image");
                                String vehicle_no = jsonObject1.optString("vehicle_no");

                                HistoryValet historyValet = new HistoryValet(parking_id, tip, name, address, date, picked_by, in_time,dropped_by,out_time,image,vehicle_no);
                                mList.add(historyValet);

                            }
                            text_view_nodata.setVisibility(View.GONE);
                            rv_valet.setVisibility(View.VISIBLE);
                            setAdapter();

                        }


                    }else if(status==401) {
                        PopUtils.alertDialog(getActivity(), message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseApplication.getInstance().callServiceForLogOut();
                            }
                        });

                    } else if (status == 400) {
                        text_view_nodata.setVisibility(View.VISIBLE);
                        rv_valet.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.REPORT_ISSUE_VALET_CODE :

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.optString("status");
                    if (status.equals("true")) {
                       // Toast.makeText(getActivity(),jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                        PopUtils.alertDialog(getActivity(),jsonObject.optString("message"),null);

                    } else  {
                        Toast.makeText(getActivity(),jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }

    }

    @Override
    public void clickListener(int position ,String message) {
        CallReportIssue(position,message);

    }
}
