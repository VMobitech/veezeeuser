package com.versatilemobitech.VeeZee.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.app.SpinnerIOS.SpinnerWindow;
import com.app.SpinnerIOS.SpinnerWindow_interface;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;
import com.versatilemobitech.VeeZee.Adapters.CustomAdapterForSpinner;
import com.versatilemobitech.VeeZee.BaseApplication;
import com.versatilemobitech.VeeZee.Model.CarBrands;
import com.versatilemobitech.VeeZee.Model.CarColor;
import com.versatilemobitech.VeeZee.Model.CarName;
import com.versatilemobitech.VeeZee.Model.UserCarModel;
import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.WebUtils.ServerResponse;
import com.versatilemobitech.VeeZee.interfaces.IParseListener;
import com.versatilemobitech.VeeZee.interfaces.ReturnValue;
import com.versatilemobitech.VeeZee.utils.Constants;
import com.versatilemobitech.VeeZee.utils.PopUtils;
import com.versatilemobitech.VeeZee.utils.Utility;
import com.versatilemobitech.VeeZee.views.BottomSheetListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CarDetailsActivity extends AppCompatActivity implements View.OnClickListener, IParseListener {
    TextView tool_bar_title, txt_generateQr;
    EditText edit_text_brand, edit_text_name, edit_text_color, edit_text_vehicle_no,
            edit_text_name_other, edit_text_color_other,edit_text_brand_other;
    ImageView image_view_brandLogo, image_view_colorLogo, image_view_nameLogo,ivColorArraow;

    ArrayList<CarBrands> mBrandsList = new ArrayList<>();
    ArrayList<CarName> carNames = new ArrayList<>();
    ArrayList<CarColor> carColors = new ArrayList<>();

    String TAG = "";
    String brand_id = "", carId = "", vehicleNo = "", user_id = "",user_car_id="";
    private boolean isFromCreate;
    ImageView image_view_back;
    LinearLayout linearLayout_back;
    boolean isBrandSelected = false, isCarSelected = false;
    String temp_brand_id = "";
    boolean iseditCar = false;

    private SpinnerWindow spinnerWindow;
    private  UserCarModel model;

    String car_id_adapter_click;
    private String caridChangedNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);
        init();

        //spinnerWindow = new SpinnerWindow(CarDetailsActivity.this);

        getSupportActionBar().hide();
        callServiceForBrands();
    }

    private void init() {
        setReferences();
        setClickListeners();
        getArgument();


        car_id_adapter_click =getIntent().getExtras().getString("car_id");
        Log.e("hfbcdgvcdg","car id new w w w ww "+car_id_adapter_click);

    }

    private void getArgument() {
        Intent i = getIntent();
        if (i != null) {
            TAG = i.getStringExtra("TAG");
            if (TAG.equalsIgnoreCase("CARS")) {
                iseditCar = false;

                user_id = Utility.getSharedPrefStringData(this, Constants.USER_ID);

            }else if(TAG.equalsIgnoreCase("CAREDIT")){
                tool_bar_title.setText("Edit Car Details");
                txt_generateQr.setText("SUBMIT");
                iseditCar = true;
                model = (UserCarModel) i.getSerializableExtra("CAR_DETAILS");
                if(model!=null){
                    edit_text_brand.setText(model.getBrand_name());
                    edit_text_vehicle_no.setText(model.getVehicle_no());
                    user_car_id = model.getUser_car_id();

                    edit_text_vehicle_no.setEnabled(false);

                    edit_text_vehicle_no.setAlpha(0.5f);
                    callServicesForCarColor(model.getCar_name());

                         edit_text_color.setVisibility(View.VISIBLE);
                         edit_text_name.setVisibility(View.VISIBLE);
                         edit_text_brand.setVisibility(View.VISIBLE);

                         edit_text_color_other.setText(model.getColor());
                         edit_text_name_other.setText(model.getCar_name());
                         //edit_text_color_other.setEnabled(false);
                         edit_text_name_other.setEnabled(false);

                       // edit_text_color_other.setAlpha(0.5f);
                        edit_text_name_other.setAlpha(0.5f);
                        edit_text_brand_other.setEnabled(false);
                        edit_text_brand_other.setAlpha(0.5f);

                    // }else {

                         image_view_colorLogo.setVisibility(View.VISIBLE);
                         image_view_brandLogo.setVisibility(View.VISIBLE);


                         Picasso.get().load(model.getImage()).placeholder(R.drawable.car_placeholder).into(image_view_colorLogo);
                         Picasso.get().load(model.getBrand_image()).placeholder(R.drawable.car_placeholder).into(image_view_brandLogo);

                         edit_text_name.setText(model.getCar_name());
                         edit_text_color.setText(model.getColor());

                         isBrandSelected = true;
                         isCarSelected = true;

                         edit_text_name.setEnabled(false);

                       // edit_text_color.setAlpha(0.5f);
                        edit_text_name.setAlpha(0.5f);
                        edit_text_brand.setEnabled(false);
                        edit_text_brand.setAlpha(0.5f);


                //     }

                }


            } else {
                user_id = i.getStringExtra(ProfileActivity.USER_ID);
            }
            if (TAG.equalsIgnoreCase("VALET")) {
                iseditCar = false;
                user_id = Utility.getSharedPrefStringData(CarDetailsActivity.this, Constants.USER_ID);
            }
        } else {

            TAG = "VALET";

        }
    }

    private void setClickListeners() {
        txt_generateQr.setOnClickListener(this);
        edit_text_brand.setOnClickListener(this);
        edit_text_name.setOnClickListener(this);
        edit_text_color.setOnClickListener(this);
        image_view_back.setOnClickListener(this);
        linearLayout_back.setOnClickListener(this);

        edit_text_name_other.setOnClickListener(this);
        edit_text_color_other.setOnClickListener(this);
        edit_text_brand_other.setOnClickListener(this);
    }

    private void setReferences() {

        tool_bar_title = findViewById(R.id.text_view_description);
        txt_generateQr = findViewById(R.id.txt_generateQr);

        edit_text_name = findViewById(R.id.edit_text_name);
        edit_text_color = findViewById(R.id.edit_text_color);
        edit_text_brand = findViewById(R.id.edit_text_brand);
        edit_text_vehicle_no = findViewById(R.id.edit_text_vehicle_no);

        edit_text_name_other = findViewById(R.id.edit_text_name_other);
        edit_text_color_other = findViewById(R.id.edit_text_color_other);
        edit_text_brand_other = findViewById(R.id.edit_text_brand_other);

        image_view_brandLogo = findViewById(R.id.image_view_brandLogo);
        image_view_nameLogo = findViewById(R.id.image_view_nameLogo);
        image_view_colorLogo = findViewById(R.id.image_view_colorLogo);

        ivColorArraow = findViewById(R.id.ivColorArraow);

        image_view_back = findViewById(R.id.image_view_back);
        tool_bar_title.setText("Car Details");

        linearLayout_back = findViewById(R.id.l1_back);

        edit_text_color_other.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                image_view_colorLogo.setVisibility(View.VISIBLE);
                image_view_colorLogo.setImageDrawable(getResources().getDrawable(R.drawable.car_placeholder,null));
                return false;
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_generateQr:
                if (isValid()) {
                    if(iseditCar) {
                        if(!edit_text_brand.getText().toString().equalsIgnoreCase("Other")) {
                            callServiceForEditCar();
                        }else {
                            Intent i = new Intent(CarDetailsActivity.this,MainActivity.class);
                            i.putExtra("TAG", "CARS");
                            startActivity(i);
                        }
                    }else {
                        callServiceForAddCar();
                    }
                }


                break;
            case R.id.edit_text_color_other :
                //Toast.makeText(getApplicationContext(),"COLOR",Toast.LENGTH_SHORT).show();
                image_view_colorLogo.setVisibility(View.VISIBLE);
                break;
            case R.id.edit_text_name_other :
              //  Toast.makeText(getApplicationContext(),"NAME",Toast.LENGTH_SHORT).show();

                break;
            case R.id.edit_text_brand:

                ArrayList<String> brandNames = new ArrayList<>();
                final ArrayList<String> brandLogo = new ArrayList<>();
                for (int j = 0; j < mBrandsList.size(); j++) {
                    brandNames.add(mBrandsList.get(j).getBrandName());
                    brandLogo.add(mBrandsList.get(j).getLogo());
                }

                setBottomSpinner(brandNames,brandLogo,"Brand");
                //SpinnerWindow.showSpinner(CarDetailsActivity.this, "Brand", brandNames, brandLogo);


                break;
            case R.id.edit_text_name:
                ArrayList<String> carNames = new ArrayList<>();
                final ArrayList<String> carImages = new ArrayList<>();
                for (int l = 0; l < this.carNames.size(); l++) {
                    carNames.add(this.carNames.get(l).getCarName());
                    carImages.add(this.carNames.get(l).getCarImage());
                }

                if (edit_text_brand.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please select Car Brand first ", Toast.LENGTH_SHORT).show();

                }else if(edit_text_brand.getText().toString().equalsIgnoreCase("Other")){
                    Utility.showKeyboard(CarDetailsActivity.this);

                } else if (isBrandSelected == false) {
                    Toast.makeText(getApplicationContext(), "Please select Car Brand ", Toast.LENGTH_SHORT).show();
                } else if (carNames.size() > 0) {
                    Utility.hideKeyboard(CarDetailsActivity.this);

                    setBottomSpinner(carNames,new ArrayList<String>(),"Name");
                    //SpinnerWindow.showSpinner(CarDetailsActivity.this, "Name", carNames, new ArrayList<String>());
                } else if (carNames.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No cars available in " + edit_text_brand.getText().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please Select Car Name", Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.edit_text_color:

                ArrayList<String> colorList = new ArrayList<>();
                final ArrayList<String> colorCarsImages = new ArrayList<>();
                for (int j = 0; j < carColors.size(); j++) {
                    colorList.add(carColors.get(j).getColor());
                    colorCarsImages.add(carColors.get(j).getImage());
                }


                if (colorList.size() > 0) {

                    setBottomSpinner(colorList,colorCarsImages,"Color");

                    //SpinnerWindow.showSpinner(CarDetailsActivity.this, "Color", colorList, colorCarsImages);
                } else if (isBrandSelected == false) {
                    Toast.makeText(this, "Please Select Car Brand First", Toast.LENGTH_SHORT).show();
                } else if (isCarSelected == false &&edit_text_color.getVisibility()==View.VISIBLE) {
                    Toast.makeText(this, "Please Select Car Name First", Toast.LENGTH_SHORT).show();
                }else if(edit_text_color_other.getVisibility()==View.VISIBLE && edit_text_color_other.getText().toString().isEmpty()){
                    Toast.makeText(this, "Please Enter Car Name", Toast.LENGTH_SHORT).show();
                }
                /*else if(carColors.size()==0) {
                    Toast.makeText(this, "No Colors Available", Toast.LENGTH_SHORT).show();
                }*/else {
                    Toast.makeText(this,"Please Select Car Color",Toast.LENGTH_SHORT).show();
                }


                break;

            case R.id.image_view_back:
                finish();
                break;

            case R.id.l1_back:
                finish();
                break;
        }
    }

    private void callServiceForAddCar() {
        if (Utility.isNetworkAvailable(CarDetailsActivity.this)) {

            Utility.showLoadingDialog(CarDetailsActivity.this, "Loading...", false);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("user_id", Utility.getSharedPrefStringData(CarDetailsActivity.this, Constants.USER_ID));
                if(edit_text_brand.getVisibility()==View.VISIBLE) {

                    if(edit_text_name_other.getVisibility()==View.VISIBLE){
                        jsonObject.put("brand_id",brand_id);
                        jsonObject.put("other_car", edit_text_name_other.getText().toString());
                        jsonObject.put("other_color", edit_text_color_other.getText().toString());
                    }else if(edit_text_color_other.getVisibility()==View.VISIBLE) {
                        jsonObject.put("brand_id",brand_id);
                        jsonObject.put("car_id", carId);
                        jsonObject.put("other_color", edit_text_color_other.getText().toString());
                    }else {
                        jsonObject.put("car_id", carId);
                    }
                }else {
                    if(edit_text_brand_other.getVisibility()==View.VISIBLE) {
                        jsonObject.put("other_brand", edit_text_brand_other.getText().toString());
                        jsonObject.put("other_car", edit_text_name_other.getText().toString());
                        jsonObject.put("other_color", edit_text_color_other.getText().toString());
                    } else if(edit_text_name_other.getVisibility()==View.VISIBLE){
                        jsonObject.put("other_car", edit_text_name_other.getText().toString());
                        jsonObject.put("other_color", edit_text_color_other.getText().toString());
                        jsonObject.put("brand_id",brand_id);
                    }
                }

                jsonObject.put("vehicle_no", edit_text_vehicle_no.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(CarDetailsActivity.this, "Post", Constants.ADD_CAR_URL, jsonObject, new HashMap<String, String>(), this, Constants.ADD_CAR_CODE);

        } else {
            PopUtils.alertDialog(CarDetailsActivity.this, Utility.getResourcesString(this, R.string.check_internet), null);
        }
    }

    private void callServiceForEditCar() {
        if (Utility.isNetworkAvailable(CarDetailsActivity.this)) {

            Utility.showLoadingDialog(CarDetailsActivity.this, "Loading...", false);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("user_car_id",user_car_id);

                //if(!edit_text_brand.getText().toString().equalsIgnoreCase("Other"))
                if(edit_text_color.getText().toString().equalsIgnoreCase("other"))
                {
                    carId =car_id_adapter_click;
                    Log.e("hfbcdgvcdg","other  "+car_id_adapter_click);
                    edit_text_color.setText(edit_text_color_other.getText().toString().trim());
                    jsonObject.put("other_color",edit_text_color.getText().toString());
                }
//                else
//                {
//                    jsonObject.put("other_color",edit_text_color.getText().toString());
//                }

                Log.e("hfbcdgvcdg","vffhbgv "+carId);

                jsonObject.put("car_id", carId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("vjnfvfvbf","json params "+jsonObject.toString());
            Log.e("vjnfvfvbf","color params "+edit_text_color.getText().toString());
            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(CarDetailsActivity.this, "POST", Constants.EDIT_CAR, jsonObject, new HashMap<String, String>(), this, Constants.EDIT_CAR_CODE);

        } else {
            PopUtils.alertDialog(CarDetailsActivity.this, Utility.getResourcesString(this, R.string.check_internet), null);
        }
    }

    private void callServiceForCars(String brand_id) {
        if (Utility.isNetworkAvailable(CarDetailsActivity.this)) {
            if (!isFromCreate) {
                Utility.showLoadingDialog(CarDetailsActivity.this, "Loading...", false);
            }
            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(CarDetailsActivity.this, "GET", Constants.CARS_URL + "?brand_id=" + brand_id, new JSONObject(), new HashMap<String, String>(), this, Constants.CARS_CODE);

        } else {
            PopUtils.alertDialog(CarDetailsActivity.this, Utility.getResourcesString(this, R.string.check_internet), null);
        }
    }

    private void callServicesForCarColor(String carName) {
        if (Utility.isNetworkAvailable(CarDetailsActivity.this)) {
            if (!isFromCreate) {
                Utility.showLoadingDialog(CarDetailsActivity.this, "Loading...", false);
            }
            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(CarDetailsActivity.this, "GET", Constants.CARS_COLOR_URL + "?car_name=" + carName, new JSONObject(), new HashMap<String, String>(), this, Constants.CARS_COLOR_CODE);

        } else {
            PopUtils.alertDialog(CarDetailsActivity.this, Utility.getResourcesString(this, R.string.check_internet), null);
        }

    }

    private void callServiceForBrands() {
        if (Utility.isNetworkAvailable(CarDetailsActivity.this)) {
            Utility.showLoadingDialog(CarDetailsActivity.this, "Loading...", false);
            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(CarDetailsActivity.this, "GET", Constants.BRANDS_URL, new JSONObject(), new HashMap<String, String>(), this, Constants.BRAND_CODE);

        } else {
            PopUtils.alertDialog(CarDetailsActivity.this, Utility.getResourcesString(this, R.string.check_internet), null);
        }
    }


    private boolean isValid() {
        if (edit_text_brand.getText().toString().isEmpty() /*|| brand_id.equalsIgnoreCase("")*/) {
            Utility.showToastMessage(CarDetailsActivity.this, "Please select Car Brand");
            return  false;
        }else if(edit_text_brand_other.getText().toString().isEmpty() && edit_text_brand.getText().toString().equalsIgnoreCase("other")){
            Utility.showToastMessage(CarDetailsActivity.this, "Please Enter Car Brand");
            return  false;
        }else if(edit_text_name.getVisibility()==View.VISIBLE && edit_text_name.getText().toString().isEmpty()){
            Utility.showToastMessage(CarDetailsActivity.this, "Please select Car Name");
            return false;
        }else if(edit_text_name_other.getVisibility()==View.VISIBLE && edit_text_name_other.getText().toString().isEmpty()){
            Utility.showToastMessage(CarDetailsActivity.this, "Please Enter Car Name");
            return false;
        }

        else if(edit_text_color.getVisibility()==View.VISIBLE && edit_text_color.getText().toString().isEmpty()){
            Utility.showToastMessage(CarDetailsActivity.this, "Please select Car Color");
            return false;
        }else if(edit_text_color_other.getVisibility()==View.VISIBLE && edit_text_color_other.getText().toString().isEmpty()){
            Utility.showToastMessage(CarDetailsActivity.this, "Please Enter Car Color");
            return false;
        }
        else if (edit_text_vehicle_no.getText().toString().isEmpty() || edit_text_vehicle_no.getText().toString().equalsIgnoreCase("")) {
            Utility.showToastMessage(CarDetailsActivity.this, "Please enter Vehicle Number");
            return false;
        } else if (edit_text_vehicle_no.getText().toString().length() < 9) {
            Utility.showToastMessage(CarDetailsActivity.this, "Vehicle Number Should be at least 9 characters in length");
            return false;
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;

    }


    @Override
    public void ErrorResponse(String response, int requestCode) {
        Utility.hideLoadingDialog();
        PopUtils.alertDialog(this, Utility.getResourcesString(this, R.string.something_went_wrong), null);
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {

        switch (requestCode) {
            case Constants.BRAND_CODE:
                try {
                    Utility.hideLoadingDialog();
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");

                    Log.e("vjnfvfvbf","res "+response.toString());
                    Log.e("vjnfvfvbf","vfbfbf "+message.toString());

                    if (status == 200) {
                        mBrandsList.clear();
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                                String brand_id = jsonObject1.optString("brand_id");
                                String name = jsonObject1.optString("name");
                                String logo = jsonObject.optString("brand_image_path") + jsonObject1.optString("logo");
                                CarBrands carBrands = new CarBrands(brand_id, name, logo);
                                mBrandsList.add(carBrands);
                            }
                            mBrandsList.add(new CarBrands("","Other",""));
                            //edit_text_brand.setText(mBrandsList.get(0).getBrandName());
                            if(iseditCar){
                               for(CarBrands brandName : mBrandsList){
                                   if(edit_text_brand.getText().toString().equalsIgnoreCase(brandName.getBrandName())){
                                       brand_id = brandName.getBrandId();
                                   }
                               }
                            }else {
                                brand_id = mBrandsList.get(0).getBrandId();
                            }
                            isFromCreate = true;

                            callServiceForCars(brand_id);
                        }

                    } else if (status == 401) {
                        PopUtils.alertDialog(CarDetailsActivity.this, message, new View.OnClickListener() {
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
            case Constants.CARS_CODE:
                try {
                    if (!isFromCreate) {
                        Utility.hideLoadingDialog();
                    }
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");

                    Log.e("cjdcbhdc","car code "+response.toString());

                    if (status == 200) {
                        carNames.clear();
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int k = 0; k < jsonArray.length(); k++) {
                                JSONObject jsonObject1 = jsonArray.optJSONObject(k);

                                String car_id = jsonObject1.optString("car_id");
                                String name = jsonObject1.optString("name");
                                String color = jsonObject1.optString("color");
                                String image = jsonObject.optString("car_image_path") + jsonObject1.optString("image");

                                CarName carNameAndColor = new CarName(name, car_id, color, image);
                                carNames.add(carNameAndColor);
                            }

                            carNames.add(new CarName("Other","","",""));

                        }
                        String carName = "";

                    } else if (status == 401) {
                        PopUtils.alertDialog(CarDetailsActivity.this, message, new View.OnClickListener() {
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
            case Constants.CARS_COLOR_CODE:
                try {
                    if (!isFromCreate) {
                        Utility.hideLoadingDialog();
                    }
                    JSONObject jsonObjectColor = new JSONObject(response);
                    int status = jsonObjectColor.optInt("status");
                    String message = jsonObjectColor.optString("message");

                    Log.e("dcbdgvgdvd","color dcvcfc "+response);

                    if (status == 200) {
                        carColors.clear();
                        JSONArray jsonArray = jsonObjectColor.optJSONArray("data");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int k = 0; k < jsonArray.length(); k++) {
                                JSONObject jsonObject1 = jsonArray.optJSONObject(k);

                                String car_id = jsonObject1.optString("car_id");
                                String name = jsonObject1.optString("name");
                                String image = jsonObjectColor.optString("car_image_path") + jsonObject1.optString("image");
                                String color = jsonObject1.optString("color");

                                CarColor Colors = new CarColor(car_id, name, image, color);
                                carColors.add(Colors);

                            }
                        }
                        carColors.add(new CarColor("","","","Other"));
                        //edit_text_name.setText(carNames.get(0).getCarName());
                        if(iseditCar)
                        {
                            for(CarColor carColor : carColors){
                                if(edit_text_color.getText().toString().equalsIgnoreCase(carColor.getColor())){
                                    carId = carColor.getCar_id();
                                }
                            }
                        }else {
                            carId = carColors.get(0).getCar_id();
                        }
                        //edit_text_color.setText(carNames.get(0).getCarColor());

                    } else if (status == 401) {
                        PopUtils.alertDialog(CarDetailsActivity.this, message, new View.OnClickListener() {
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

            case Constants.ADD_CAR_CODE:
                Utility.hideLoadingDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    String qr = jsonObject.optString("qr");
                    if (status == 200) {

                        Log.e("dhcbhdcgd","qr "+qr);

                        // Utility.showToastMessage(CarDetailsActivity.this, jsonObject.optString("message"));
                        if (TAG.equals("VALET")) {
                            Intent home = new Intent(getApplicationContext(), AddedCarActivity.class);
                            home.putExtra("TAG", "VALET");
                            home.putExtra("QR", qr);
                            startActivity(home);

                            Log.e("dhcbhdcgd","if ");

                        } else {

                            Log.e("dhcbhdcgd","else ");

                            Intent i = new Intent(getApplicationContext(), AddedCarActivity.class);
                            i.putExtra("name", edit_text_name.getText().toString());

                            i.putExtra("TAG", TAG);
                            i.putExtra("QR", qr);
                            i.putExtra(ProfileActivity.USER_ID, user_id);
                            i.putExtra("vehicle_no", edit_text_vehicle_no.getText().toString());
                            startActivity(i);
                        }
                    } else if (status == 401) {
                        PopUtils.alertDialog(CarDetailsActivity.this, message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseApplication.getInstance().callServiceForLogOut();
                            }
                        });

                    } else {
                        if (message.contains("already")) {
                            PopUtils.alertDialog(CarDetailsActivity.this, message, null);
                        }
                        //Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.EDIT_CAR_CODE :
                Utility.hideLoadingDialog();
                try {

                    JSONObject jsonObjectColor = new JSONObject(response);
                    int status = jsonObjectColor.optInt("status");
                    String message = jsonObjectColor.optString("message");

                    caridChangedNew = jsonObjectColor.optString("car_id");
                    Log.e("vjnfvfvbf","edit res "+response.toString());
                    Log.e("vjnfvfvbf","vfbfbf "+message.toString());
                    Log.e("vjnfvfvbf","vfbfbf "+status);

                    Log.e("vjnfvfvbf","car id  "+caridChangedNew);
                //    car_id_adapter_click = caridChangedNew;
                    Log.e("vjnfvfvbf","car id  "+car_id_adapter_click);


                    if (status == 200) {

                        car_id_adapter_click = caridChangedNew;

                        Toast.makeText(getApplicationContext(),""+message,Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(CarDetailsActivity.this,MainActivity.class);
                        i.putExtra("TAG","CARS");
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }

    }

    private void setBottomSpinner(ArrayList<String> name, ArrayList<String> logos, final String viewFrom) {
        final BottomSheetDialog dialog = new BottomSheetDialog(CarDetailsActivity.this,R.style.Theme_Design_BottomSheetDialog);
        dialog.setContentView(R.layout.item_spinner_bottom);
        BottomSheetListView rv_list = dialog.findViewById(R.id.bottomList);
        ConstraintLayout frame = dialog.findViewById(R.id.containerBottom);
        TextView txt_title = dialog.findViewById(R.id.txt_title);
        txt_title.setText("Car "+viewFrom+"s");

        final ArrayAdapter<String> adapter;
        adapter = new CustomAdapterForSpinner(CarDetailsActivity.this, R.layout.dialog_dropdown_with_image, name, logos);
        rv_list.setAdapter(adapter);
        rv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //returnValue.returnValues(parent.getItemAtPosition(position).toString(), position);


                if (viewFrom.equalsIgnoreCase("Brand")) {

                    ArrayList<String> brandNames = new ArrayList<>();
                    final ArrayList<String> brandLogo = new ArrayList<>();
                    for (int j = 0; j < mBrandsList.size(); j++) {
                        brandLogo.add(mBrandsList.get(j).getLogo());
                        brandNames.add(mBrandsList.get(j).getBrandName());
                    }
                    if(brandLogo.get(position)!= null && !brandLogo.get(position).isEmpty() ) {
                        Picasso.get().load(brandLogo.get(position)).into(image_view_brandLogo);
                    }
                    brand_id = mBrandsList.get(position).getBrandId();


                 //   Toast.makeText(CarDetailsActivity.this, "BRAND", Toast.LENGTH_SHORT).show();

                    edit_text_name.setText("");
                    edit_text_color.setText("");
                    edit_text_color_other.setText("");
                    edit_text_name_other.setText("");

                    if (temp_brand_id != brand_id && ! mBrandsList.get(position).getBrandName().equalsIgnoreCase("Other") ) {
                        temp_brand_id = brand_id;

                        isFromCreate = false;

                        edit_text_brand.setText(mBrandsList.get(position).getBrandName());
                        image_view_brandLogo.setVisibility(View.VISIBLE);
                        isBrandSelected = true;
                        callServiceForCars(brand_id);
                        Picasso.get().load(mBrandsList.get(position).getLogo()).placeholder(R.drawable.car_placeholder)
                                .error(R.drawable.car_placeholder).into(image_view_brandLogo);
                        edit_text_brand.setVisibility(View.VISIBLE);
                        edit_text_brand_other.setVisibility(View.GONE);


                        edit_text_name_other.setVisibility(View.GONE);
                        edit_text_color_other.setVisibility(View.GONE);

                        ivColorArraow.setVisibility(View.VISIBLE);

                        edit_text_name.setVisibility(View.VISIBLE);
                        edit_text_color.setVisibility(View.VISIBLE);

                    }else if(mBrandsList.get(position).getBrandName().equalsIgnoreCase("Other")){
                        edit_text_brand.setText(mBrandsList.get(position).getBrandName());

                        edit_text_name_other.setVisibility(View.VISIBLE);
                        edit_text_color_other.setVisibility(View.VISIBLE);
                        ivColorArraow.setVisibility(View.GONE);
                        edit_text_brand.setVisibility(View.GONE);
                        edit_text_brand_other.setVisibility(View.VISIBLE);
                        image_view_brandLogo.setVisibility(View.VISIBLE);
                        edit_text_brand_other.requestFocus();

                        if(mBrandsList.get(position).getLogo().isEmpty()) {
                            image_view_brandLogo.setImageDrawable(getResources().getDrawable(R.drawable.car_placeholder, null));
                        }else {
                            Picasso.get().load(mBrandsList.get(position).getLogo())
                                    .error(R.drawable.car_placeholder).placeholder(R.drawable.car_placeholder).into(image_view_brandLogo);
                        }
                        edit_text_name.setVisibility(View.GONE);
                        edit_text_color.setVisibility(View.GONE);

                    } else {

                        ivColorArraow.setVisibility(View.VISIBLE);
                        edit_text_name_other.setVisibility(View.GONE);
                        edit_text_color_other.setVisibility(View.GONE);

                        ivColorArraow.setVisibility(View.VISIBLE);

                        edit_text_name.setVisibility(View.VISIBLE);
                        edit_text_color.setVisibility(View.VISIBLE);

                        edit_text_brand.setVisibility(View.VISIBLE);
                        edit_text_brand_other.setVisibility(View.GONE);

                        temp_brand_id = "";
                        callServiceForCars(brand_id);
                    }
                    edit_text_name.setText("");
                    edit_text_color.setText("");

                    edit_text_name_other.setText("");
                    edit_text_color_other.setText("");

                    isCarSelected = false;
                    carNames.clear();
                    carColors.clear();
                    image_view_nameLogo.setVisibility(View.GONE);
                    image_view_colorLogo.setVisibility(View.GONE);
                } else if (viewFrom.equalsIgnoreCase("Name")) {

                    edit_text_color.setText("");
                    edit_text_color_other.setText("");

                    image_view_colorLogo.setVisibility(View.GONE);
                    if (edit_text_brand.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please select Car Brand first ", Toast.LENGTH_SHORT).show();

                    } else if (isBrandSelected == false) {
                        Toast.makeText(getApplicationContext(), "Please select Car Brand ", Toast.LENGTH_SHORT).show();
                    }else if(carNames.get(position).getCarName().equalsIgnoreCase("Other")){
                        edit_text_name_other.setText("");
                        edit_text_name.setVisibility(View.GONE);
                        edit_text_name.setText("Other");
                        edit_text_name_other.setVisibility(View.VISIBLE);
                        edit_text_name_other.requestFocus();
                        edit_text_color.setVisibility(View.GONE);
                        edit_text_color_other.setVisibility(View.VISIBLE);
                        ivColorArraow.setVisibility(View.GONE);
                        edit_text_color.setText("Other");

                    } else if (carNames.size() > 0 && !carNames.get(position).getCarName().equalsIgnoreCase("Other")) {
                        edit_text_color_other.setVisibility(View.GONE);
                        ivColorArraow.setVisibility(View.VISIBLE);
                        edit_text_color.setVisibility(View.VISIBLE);
                        edit_text_name.setText(carNames.get(position).getCarName());
                        isCarSelected = true;
                        carId = carNames.get(position).getCarId();
                        callServicesForCarColor(CarDetailsActivity.this.carNames.get(position).getCarName());
                    } else if (carNames.size() == 0) {
                        Toast.makeText(getApplicationContext(), "No cars available in " + edit_text_brand.getText().toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Select Car Name", Toast.LENGTH_SHORT).show();
                    }
                } else if (viewFrom.equalsIgnoreCase("Color")) {
                    ArrayList<String> colorList = new ArrayList<>();
                    final ArrayList<String> colorCarsImages = new ArrayList<>();
                    for (int j = 0; j < carColors.size(); j++) {
                        colorList.add(carColors.get(j).getColor());
                        colorCarsImages.add(carColors.get(j).getImage());
                    }
                    if(carColors.get(position).getColor().equalsIgnoreCase("Other")){


                        ivColorArraow.setVisibility(View.GONE);

                        edit_text_color_other.setText("");
                        edit_text_color.setVisibility(View.GONE);
                        edit_text_color.setText("Other");
                        edit_text_color_other.setVisibility(View.VISIBLE);
                        image_view_colorLogo.setVisibility(View.VISIBLE);
                        image_view_colorLogo.setImageDrawable(getResources().getDrawable(R.drawable.car_placeholder));
                        edit_text_color_other.requestFocus();

                    }else if (colorList.size() > 0 &&!carColors.get(position).getColor().equalsIgnoreCase("Other")) {

                        edit_text_color.setText(carColors.get(position).getColor());
                        image_view_colorLogo.setVisibility(View.VISIBLE);
                        Picasso.get().load(colorCarsImages.get(position)).error(R.drawable.car_placeholder).into(image_view_colorLogo);
                        brand_id = mBrandsList.get(position).getBrandId();
                        carId = CarDetailsActivity.this.carColors.get(position).getCar_id();

                    } else if (isBrandSelected == false) {
                        Toast.makeText(getApplicationContext(), "Please Select Car Brand First", Toast.LENGTH_SHORT).show();
                    } else if (isCarSelected == false) {
                        Toast.makeText(getApplicationContext(), "Please Select Car Name First", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Select Car Color", Toast.LENGTH_SHORT).show();
                    }
                }

                dialog.dismiss();

            }
        });
        dialog.show();

    }

    /*@Override
    public void selectedPosition(String viewFrom, int selected_position) {
        if (viewFrom.equalsIgnoreCase("Brand")) {
            ArrayList<String> brandNames = new ArrayList<>();
            final ArrayList<String> brandLogo = new ArrayList<>();
            for (int j = 0; j < mBrandsList.size(); j++) {
                brandLogo.add(mBrandsList.get(j).getLogo());
                brandNames.add(mBrandsList.get(j).getBrandName());
            }
            if(brandLogo.get(selected_position)!= null && !brandLogo.get(selected_position).isEmpty() ) {
                Picasso.get().load(brandLogo.get(selected_position)).into(image_view_brandLogo);
            }
            brand_id = mBrandsList.get(selected_position).getBrandId();
            if (temp_brand_id != brand_id && ! mBrandsList.get(selected_position).getBrandName().equalsIgnoreCase("Other") ) {
                temp_brand_id = brand_id;
                isFromCreate = false;

                edit_text_brand.setText(mBrandsList.get(selected_position).getBrandName());
                image_view_brandLogo.setVisibility(View.VISIBLE);
                isBrandSelected = true;
                callServiceForCars(brand_id);
                Picasso.get().load(mBrandsList.get(selected_position).getLogo()).placeholder(R.drawable.car_placeholder)
                        .error(R.drawable.car_placeholder).into(image_view_brandLogo);
                edit_text_brand.setVisibility(View.VISIBLE);
                edit_text_brand_other.setVisibility(View.GONE);

                edit_text_name_other.setVisibility(View.GONE);
                edit_text_color_other.setVisibility(View.GONE);

                edit_text_name.setVisibility(View.VISIBLE);
                edit_text_color.setVisibility(View.VISIBLE);

            }else if(mBrandsList.get(selected_position).getBrandName().equalsIgnoreCase("Other")){
                edit_text_brand.setText(mBrandsList.get(selected_position).getBrandName());

                edit_text_name_other.setVisibility(View.VISIBLE);
                edit_text_color_other.setVisibility(View.VISIBLE);
                edit_text_brand.setVisibility(View.GONE);
                edit_text_brand_other.setVisibility(View.VISIBLE);
                image_view_brandLogo.setVisibility(View.VISIBLE);
                edit_text_brand_other.requestFocus();

                if(mBrandsList.get(selected_position).getLogo().isEmpty()) {
                    image_view_brandLogo.setImageDrawable(getResources().getDrawable(R.drawable.car_placeholder, null));
                }else {
                    Picasso.get().load(mBrandsList.get(selected_position).getLogo())
                            .error(R.drawable.car_placeholder).placeholder(R.drawable.car_placeholder).into(image_view_brandLogo);
                }
                edit_text_name.setVisibility(View.GONE);
                edit_text_color.setVisibility(View.GONE);

            } else {

                edit_text_name_other.setVisibility(View.VISIBLE);
                edit_text_color_other.setVisibility(View.VISIBLE);

                edit_text_name.setVisibility(View.GONE);
                edit_text_color.setVisibility(View.GONE);

                edit_text_brand.setVisibility(View.VISIBLE);
                edit_text_brand_other.setVisibility(View.GONE);

                temp_brand_id = "";
                callServiceForCars(brand_id);
            }
            edit_text_name.setText("");
            edit_text_color.setText("");

            edit_text_name_other.setText("");
            edit_text_color_other.setText("");

            isCarSelected = false;
            carNames.clear();
            carColors.clear();
            image_view_nameLogo.setVisibility(View.GONE);
            image_view_colorLogo.setVisibility(View.GONE);
        } else if (viewFrom.equalsIgnoreCase("Name")) {

            edit_text_color.setText("");
            image_view_colorLogo.setVisibility(View.GONE);
            if (edit_text_brand.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please select Car Brand first ", Toast.LENGTH_SHORT).show();

            } else if (isBrandSelected == false) {
                Toast.makeText(getApplicationContext(), "Please select Car Brand ", Toast.LENGTH_SHORT).show();
            }else if(carNames.get(selected_position).getCarName().equalsIgnoreCase("Other")){
                edit_text_name_other.setText("");
                edit_text_name.setVisibility(View.GONE);
                edit_text_name.setText("Other");
                edit_text_name_other.setVisibility(View.VISIBLE);
                edit_text_name_other.requestFocus();

            } else if (carNames.size() > 0 && !carNames.get(selected_position).getCarName().equalsIgnoreCase("Other")) {

                edit_text_name.setText(carNames.get(selected_position).getCarName());
                isCarSelected = true;
                callServicesForCarColor(CarDetailsActivity.this.carNames.get(selected_position).getCarName());
            } else if (carNames.size() == 0) {
                Toast.makeText(getApplicationContext(), "No cars available in " + edit_text_brand.getText().toString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please Select Car Name", Toast.LENGTH_SHORT).show();
            }
        } else if (viewFrom.equalsIgnoreCase("Color")) {
            ArrayList<String> colorList = new ArrayList<>();
            final ArrayList<String> colorCarsImages = new ArrayList<>();
            for (int j = 0; j < carColors.size(); j++) {
                colorList.add(carColors.get(j).getColor());
                colorCarsImages.add(carColors.get(j).getImage());
            }
            if(carColors.get(selected_position).getColor().equalsIgnoreCase("Other")){
                edit_text_color_other.setText("");
                edit_text_color.setVisibility(View.GONE);
                edit_text_color.setText("Other");
                edit_text_color_other.setVisibility(View.VISIBLE);
                image_view_colorLogo.setVisibility(View.VISIBLE);
                image_view_colorLogo.setImageDrawable(getResources().getDrawable(R.drawable.car_placeholder));
                edit_text_color_other.requestFocus();

            }else if (colorList.size() > 0 &&!carColors.get(selected_position).getColor().equalsIgnoreCase("Other")) {

                edit_text_color.setText(carColors.get(selected_position).getColor());
                image_view_colorLogo.setVisibility(View.VISIBLE);
                Picasso.get().load(colorCarsImages.get(selected_position)).error(R.drawable.car_placeholder).into(image_view_colorLogo);
                brand_id = mBrandsList.get(selected_position).getBrandId();
                carId = CarDetailsActivity.this.carColors.get(selected_position).getCar_id();


            } else if (isBrandSelected == false) {
                Toast.makeText(this, "Please Select Car Brand First", Toast.LENGTH_SHORT).show();
            } else if (isCarSelected == false) {
                Toast.makeText(this, "Please Select Car Name First", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please Select Car Color", Toast.LENGTH_SHORT).show();
            }
        }

    }*/
}
