package com.versatilemobitech.VeeZee.Fragments;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.versatilemobitech.VeeZee.Activities.CarDetailsActivity;
import com.versatilemobitech.VeeZee.Activities.MainActivity;
import com.versatilemobitech.VeeZee.Adapters.CarsAdapter;
import com.versatilemobitech.VeeZee.BaseApplication;
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

import java.io.File;
import java.io.Serializable;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CarsFragment extends Fragment implements View.OnClickListener, IParseListener {
    private View view;
    RecyclerView rv_cars;
    CarsAdapter mCarsAdapter;
    Toolbar toolbar;
    ViewGroup viewGroup;
    FloatingActionButton fab_add;
    ImageView image_view_delete, image_view_share;
    private int SHARE_CODE = 101;

    ArrayList<UserCarModel> userCarModelArrayList = new ArrayList<>();

    private static final String TAG = "CarsFragment";

    private   JSONArray jsonArray;
    TextView text_view_nodata;


    public CarsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cars, container, false);
        viewGroup = container;
        init();
        androidx.appcompat.widget.Toolbar toolbar = ((MainActivity) getActivity()).findViewById(R.id.toolBar);
        toolbar.setVisibility(View.GONE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    private void init() {
        setReferences();
        setClickListeners();

        callServiceForUserCars();
    }

    private void callServiceForUserCars() {
        if (Utility.isNetworkAvailable(getActivity())) {
            Utility.showLoadingDialog(getActivity(), "Loading...", false);

            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(getActivity(), "GET", Constants.USER_CARS + "?user_id=" + Utility.getSharedPrefStringData(getActivity(), Constants.USER_ID), new JSONObject(), new HashMap<String, String>(), this, Constants.USER_CARS_CODE);

        } else {
            PopUtils.alertDialog(getActivity(), Utility.getResourcesString(getActivity(), R.string.check_internet), null);
        }
    }

    private void setClickListeners() {
        fab_add.setOnClickListener(this);
    }

    private void setAdapter() {
        rv_cars.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        mCarsAdapter = new CarsAdapter(getActivity(), userCarModelArrayList);
        rv_cars.setAdapter(mCarsAdapter);
    }

    private void setReferences() {


        rv_cars = view.findViewById(R.id.rv_cars);
        fab_add = view.findViewById(R.id.fab_add);


        image_view_delete = view.findViewById(R.id.image_view_delete);
        image_view_share = view.findViewById(R.id.image_view_share);

        text_view_nodata = view.findViewById(R.id.text_view_nodata);

        image_view_share.setOnClickListener(this);
        image_view_delete.setOnClickListener(this);
        text_view_nodata.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_share:

                if (mCarsAdapter != null) {
                    ArrayList<UserCarModel> userCarModelArrayList = mCarsAdapter.getUserCarModelArrayList();
                    if (userCarModelArrayList.size() > 0) {
                        jsonArray = new JSONArray();
                        ArrayList<Uri> files = new ArrayList<Uri>();
                        for (int i = 0; i < userCarModelArrayList.size(); i++) {
                            if (userCarModelArrayList.get(i).isSelected()) {
                                jsonArray.put(userCarModelArrayList.get(i).getUser_car_id());

                            }
                        }
                        if (jsonArray.length() > 0) {
                            String link = "",carNo = "",carName ="";
                            for(UserCarModel model : userCarModelArrayList){
                                //if(model.getUser_car_id().equals(jsonArray.opt(0))){
                                if (model.isSelected()) {
                                    link = link + "\n\t" + model.getQr();
                                    if(carNo.equalsIgnoreCase("")){
                                        carNo = carNo+"\t"+model.getVehicle_no();
                                        carName = carName+"\t"+model.getCar_name();
                                    }else {
                                        carNo = carNo+"\t,"+model.getVehicle_no();
                                        carName = carName+"\t,"+model.getCar_name();
                                    }

                                }
                            }
                            for(UserCarModel path : userCarModelArrayList /* List of the files you want to send */) {

                                files.add(Uri.parse(path.getImage()));
                            }
                            onShareClick(view,link,carNo,carName);
                        } else {
                            Utility.showToastMessage(getActivity(), "Please select at least 1 Car");
                        }
                    } else {
                        Utility.showToastMessage(getActivity(), "Please select at least 1 Car");
                    }

                } else {
                    Utility.showToastMessage(getActivity(), "Please select at least 1 Car");
                }


                break;
            case R.id.fab_add:
                Intent carAdd = new Intent(getActivity(), CarDetailsActivity.class);
                carAdd.putExtra("TAG", "CARS");
                startActivity(carAdd);
                break;
            case R.id.img_credits:
                setDialogforDeleteCar();
                break;

            case R.id.image_view_delete:

                if (mCarsAdapter != null) {
                    //ArrayList<UserCarModel>
                            userCarModelArrayList = mCarsAdapter.getUserCarModelArrayList();
                    if (userCarModelArrayList.size() > 0) {
                        jsonArray = new JSONArray();
                        for (int i = 0; i < userCarModelArrayList.size(); i++) {
                            if (userCarModelArrayList.get(i).isSelected()) {
                                jsonArray.put(userCarModelArrayList.get(i).getUser_car_id());

                            }
                        }
                        if (jsonArray.length() > 0) {
                            setDialogforDeleteCar();
                        } else {
                            Utility.showToastMessage(getActivity(), "Please select at least 1 Car");
                        }
                    } else {
                        Utility.showToastMessage(getActivity(), "Please select at least 1 Car");
                    }

                } else {
                    Utility.showToastMessage(getActivity(), "Please select at least 1 Car");
                }


                break;
            default:
                break;
        }

    }
    public void onShareClick(View v,String link,String carNo,String carName) {




        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra("JSON", String.valueOf(jsonArray));
        share.putExtra(Intent.EXTRA_SUBJECT, "VeeZee");
        //share.putExtra(Intent.EXTRA_TEXT,link);
        share.putExtra(Intent.EXTRA_TEXT,"Greetings from VeeZee!\n" +
                Utility.getSharedPrefStringData(getActivity(),Constants.NAME)+" has shared the valet ticket(s) for "+carName + " with Registration Number(s) "+carNo+". Please use the following link(s) to download the unique Valet Ticket QR Code(s).\n" +
                "Valet QR Code Link(s)"+ Html.fromHtml(link));
        share.putExtra(Intent.EXTRA_TITLE,"Please share via VeeZee.");
        startActivityForResult(Intent.createChooser(share, "Please share via VeeZee."),SHARE_CODE);
    }

    private void setDialogforDeleteCar() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        View dialogView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_dialog_delete_car, viewGroup, false);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        TextView txt_yes, txt_cancel;
        txt_yes = dialogView.findViewById(R.id.txt_yes);
        txt_cancel = dialogView.findViewById(R.id.txt_cancel);
        txt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (mCarsAdapter != null) {

                    callServiceForDeleteUsercar(jsonArray);


                }

            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void callServiceForDeleteUsercar(JSONArray jsonArray) {

        if (Utility.isNetworkAvailable(getActivity())) {
            Utility.showLoadingDialog(getActivity(), "Loading...", false);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("user_car_id", jsonArray);
                jsonObject.put("user_id",Utility.getSharedPrefStringData(getActivity(), Constants.USER_ID));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(getActivity(), "POST", Constants.DELETE_USER_CAR, jsonObject, new HashMap<String, String>(), this, Constants.DELETE_USER_CAR_CODE);

        } else {
            PopUtils.alertDialog(getActivity(), Utility.getResourcesString(getActivity(), R.string.check_internet), null);
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
            case Constants.USER_CARS_CODE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 200) {
                        userCarModelArrayList.clear();
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        if (jsonArray != null && jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                                String user_id = jsonObject1.optString("user_id");
                                String user_car_id = jsonObject1.optString("user_car_id");
                                String qr = jsonObject1.optString("qr");
                                String image = jsonObject1.optString("image");
                                String name = jsonObject1.optString("name");
                                String color = jsonObject1.optString("color");
                                String vehicle_no = jsonObject1.optString("vehicle_no");
                                String is_requested = jsonObject1.optString("is_requested");
                                String car_status = jsonObject1.optString("car_status");
                                String brand_name = jsonObject1.optString("brand_name");
                                String car_name = jsonObject1.optString("car_name");
                                String brand_image = jsonObject1.optString("brand_image");
                                String direct_scan = jsonObject1.optString("direct_scan");
                                String last_used = jsonObject1.optString("last_used");

                                String car_id = jsonObject1.optString("car_id");

                                Log.e("hfbcdgvcdg","vffhbgv "+car_id);

                                UserCarModel userCarModel = new UserCarModel(user_id, user_car_id, qr, image, name, color, vehicle_no,car_status,is_requested, false,
                                        brand_name,car_name,brand_image,direct_scan,last_used,car_id);
                                userCarModelArrayList.add(userCarModel);

                            }
                            text_view_nodata.setVisibility(View.GONE);
                            rv_cars.setVisibility(View.VISIBLE);
                            setAdapter();

                        }


                    }
                    else if(status==400) {
                        userCarModelArrayList.clear();
                        text_view_nodata.setVisibility(View.VISIBLE);
                        rv_cars.setVisibility(View.GONE);
                    }else if(status==401) {
                        PopUtils.alertDialog(getActivity(), message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseApplication.getInstance().callServiceForLogOut();
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case Constants.DELETE_USER_CAR_CODE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status");
                    String message = jsonObject.optString("message");
                    if (status == true) {
                        jsonArray =new JSONArray(new ArrayList<>());
                        Utility.showToastMessage(getActivity(),message);
                        callServiceForUserCars();
                    }else {
                        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            if (requestCode == SHARE_CODE) {
                for(int i = 0; i<userCarModelArrayList.size();i++){
                    if(userCarModelArrayList.get(i).isSelected()){
                        userCarModelArrayList.get(i).setSelected(false);
                    }
                }
               setAdapter();
            }
            }
    }
}
