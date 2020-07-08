package com.versatilemobitech.VeeZee.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.versatilemobitech.VeeZee.Adapters.HistoryValetAdapter;
import com.versatilemobitech.VeeZee.Adapters.HistoryVaultAdapter;
import com.versatilemobitech.VeeZee.BaseApplication;
import com.versatilemobitech.VeeZee.Model.HistoryValet;
import com.versatilemobitech.VeeZee.Model.HistoryVault;
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
public class HistoryVaultFragment extends Fragment implements IParseListener, HistoryVaultAdapter.Listener {
    RecyclerView rv_vault;
    View view;
    HistoryVaultAdapter mAdapter;
    TextView text_view_nodata;

    ArrayList<HistoryVault> mList = new ArrayList<>();

    public HistoryVaultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_history_vault, container, false);
        init();
        return view;
    }

    private void init() {
        setReferences();
        CallForHistoryValet();
    }

    private void setAdapter() {

        // mList.add("Over The Moon");

        rv_vault.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mAdapter = new HistoryVaultAdapter(HistoryVaultFragment.this,mList);
        rv_vault.setAdapter(mAdapter);
    }
    private void CallForHistoryValet() {
        if (Utility.isNetworkAvailable(getActivity())) {
            Utility.showLoadingDialog(getActivity(), "Loading...", false);
            String userID = Utility.getSharedPrefStringData(getActivity(), Constants.USER_ID);
            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(getActivity(), "GET", Constants.HISTORY_VALUT+"?user_id="+ userID , new JSONObject(), new HashMap<String, String>(), this, Constants.HISTORY_VALUT_CODE);

        } else {
            PopUtils.alertDialog(getActivity(), Utility.getResourcesString(getActivity(), R.string.check_internet), null);
        }
    }

    private void CallReportIssue(int position , String msg){

        if (Utility.isNetworkAvailable(getActivity())) {
            Utility.showLoadingDialog(getActivity(), "Loading...", false);
            String userID = Utility.getSharedPrefStringData(getActivity(),Constants.USER_ID);
            String Vault_history_id = mList.get(position).getVault_history_id();
            JSONObject json = new JSONObject();
            try {
                json.put("user_id",userID);
                json.put("vault_history_id",Vault_history_id);
                json.put("issue",msg);

            }catch (Exception e){
                e.printStackTrace();
            }
            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(getActivity(), "POST", Constants.REPORT_ISSUE_VAULT, json, new HashMap<String, String>(), this, Constants.REPORT_ISSUE_VAULT_CODE);

        } else {
            PopUtils.alertDialog(getActivity(), Utility.getResourcesString(getActivity(), R.string.check_internet), null);
        }
    }

    private void setReferences() {
        rv_vault = view.findViewById(R.id.rv_vault);
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
            case Constants.HISTORY_VALUT_CODE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 200) {
                        mList.clear();
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        if (jsonArray != null && jsonArray.length() > 0) {
//vault_history_id,image,name,address,date,picked_by,in_time,dropped_by,out_time
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                                String vault_history_id = jsonObject1.optString("vault_history_id");
                                String image = jsonObject1.optString("image");
                                String name = jsonObject1.optString("name");
                                String address = jsonObject1.optString("address");
                                String date = jsonObject1.optString("date");
                                String picked_by = jsonObject1.optString("picked_by");
                                String in_time = jsonObject1.optString("in_time");
                                String dropped_by = jsonObject1.optString("dropped_by");
                                String out_time = jsonObject1.optString("out_time");

                                HistoryVault historyVault = new HistoryVault(vault_history_id, image, name, address, date, picked_by, in_time,dropped_by,out_time);
                                mList.add(historyVault);

                            }
                            text_view_nodata.setVisibility(View.GONE);
                            rv_vault.setVisibility(View.VISIBLE);
                            setAdapter();

                        }


                    } else if(status==401) {
                        PopUtils.alertDialog(getActivity(), message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseApplication.getInstance().callServiceForLogOut();
                            }
                        });

                    }else if (status == 400) {
                        text_view_nodata.setVisibility(View.VISIBLE);
                        text_view_nodata.setText("No data found");
                        rv_vault.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.REPORT_ISSUE_VAULT_CODE :

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.optString("status");
                    if (status.equals("true")) {
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
    public void clickListener(int position, String msg) {
        CallReportIssue(position,msg);
    }
}
