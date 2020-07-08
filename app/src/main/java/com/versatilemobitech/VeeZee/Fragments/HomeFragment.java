package com.versatilemobitech.VeeZee.Fragments;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.chip.ChipGroup;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.squareup.picasso.Picasso;
import com.versatilemobitech.VeeZee.Activities.CarDetailsActivity;
import com.versatilemobitech.VeeZee.Activities.MainActivity;
import com.versatilemobitech.VeeZee.Activities.VeeZeeCreditsActivity;
import com.versatilemobitech.VeeZee.Adapters.AdAdapter;
import com.versatilemobitech.VeeZee.Adapters.CarouselPagerAdapterMain;
import com.versatilemobitech.VeeZee.Adapters.homeImageSliderAdapter;
import com.versatilemobitech.VeeZee.BaseApplication;
import com.versatilemobitech.VeeZee.Model.AdsMain;
import com.versatilemobitech.VeeZee.Model.AdsSub;
import com.versatilemobitech.VeeZee.Model.CarStatus;
import com.versatilemobitech.VeeZee.Model.PaytmParams;
import com.versatilemobitech.VeeZee.Model.PushForCarHandOverDetails;
import com.versatilemobitech.VeeZee.Model.RequestCar;
import com.versatilemobitech.VeeZee.Model.UserCarModel;
import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.UtilHelpers.AutoScrollViewPager;
import com.versatilemobitech.VeeZee.UtilHelpers.Customtimer;
import com.versatilemobitech.VeeZee.WebUtils.ServerResponse;
import com.versatilemobitech.VeeZee.interfaces.IParseListener;
import com.versatilemobitech.VeeZee.utils.Constants;
import com.versatilemobitech.VeeZee.utils.PopUtils;
import com.versatilemobitech.VeeZee.utils.Utility;
import com.versatilemobitech.VeeZee.views.MontserratEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import me.crosswall.lib.coverflow.core.PagerContainer;

import static com.versatilemobitech.VeeZee.Activities.MainActivity.LOOPS;
import static com.versatilemobitech.VeeZee.Activities.MainActivity.PAGES;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, IParseListener {

    private View view;
    public CarouselPagerAdapterMain adapter;
    public static ViewPager cars_pager;
    /*public static int FIRST_PAGE = 10;*/
    public static int count;
    /*public final static int LOOPS = 1000;*/
    public MainActivity mContext;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    TextView toolBarTitle, txt_share, txt_credits, text_view_v, text_view_wallet_amount, text_view_request_car, text_view_credits_v1;
    ImageView img_user_menu, img_credits, img_share, image_view_qr, image_view_coin1, image_view_check,img_veezee;
    Toolbar toolbar;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 1500;
    final long PERIOD_MS = 2500;
    RecyclerView rv_adds, rv_ratting;
    //JSONArray keyWordsArray;
    HashSet<String> keyWordsSet = new HashSet<>();

    ArrayList<UserCarModel> userCarModelArrayList = new ArrayList<>();
    ArrayList<AdsMain> adsMainArrayList = new ArrayList<>();
    ArrayList<AdsSub> adsSubArrayList = new ArrayList<>();
    String USER_CAR_ID = "";
    boolean isVeezeeClicked = false;

    ScrollView scrollView;
    PagerContainer containaer;
    String navigationTag = "";
    ViewGroup mViewGrop;
    RequestCar mRequestCar;
    PaytmParams params;
    AlertDialog alertDialog;
    PushForCarHandOverDetails mPushForCarHandOverDetails;
    boolean isPush = false;
    float mRating;
    boolean isTipContinue = false;
    AlertDialog sendTipDialog, requestCarDialog ,carStatusDialog;
    CarStatus mCarStatus;
    TextView txt_time;

    int currentcarpostn = 0;

    boolean isAutoScrool = true;
    String mParking_id;
    boolean isRefreshed = false;
    boolean textView1, textView2, textView3, textView4, textView5, textView6 = false;
    ImageView image_view_empty_car;
    AutoScrollViewPager pager_images;
    TextView txt_noOfcars;
    String Qr = "";
    //SendTip
    TextView txt_amount1, txt_amount2, txt_amount3, text_view1, text_view2, text_view3,text_view4, text_view5, text_view6, txt_rating;
    MontserratEditText txt_total_tip;
    RatingBar rating_bar;
    int testTime;
    HashMap<Integer, Long> carStatusMap = new HashMap<>();
    CountDownTimer countDownTimer;
    ChipGroup chip_group;

    int carPosition;
     AlertDialog.Builder builder;

   // AlertDialog carStatusDialog = builder.create();

    private Map<Integer, String> max_times_map = new HashMap<>();
    Map<Integer, Customtimer> customtimerSparseArray = new HashMap<>();
    Customtimer customtimer;
    boolean broadcastreceived = false;

    String requestCarId ;
    ConstraintLayout ll4;
    private String car_status;
    // int requestCarPos ;

    @SuppressLint("ValidFragment")
    public HomeFragment(MainActivity activity, String s, PushForCarHandOverDetails model) {
        mContext = activity;
        if(mContext!=null) {
            navigationTag = mContext.ClickToNavigate;
            mPushForCarHandOverDetails = model;
            if (s != null && !s.isEmpty() && mPushForCarHandOverDetails != null) {
                if (mPushForCarHandOverDetails.getStatus().equalsIgnoreCase("scanned") ||
                        mPushForCarHandOverDetails.getStatus().equalsIgnoreCase("handedover")) {
                    isPush = true;
                    Map<Integer, String> map = Utility.getMaxTime(mContext);
                    if(mPushForCarHandOverDetails.getStatus().equalsIgnoreCase("handedover")){

                    }
                }
            }
        }else {
            return;
        }
    }

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.dummy_home, container, false);
        img_user_menu = ((MainActivity) getActivity()).findViewById(R.id.img_user_menu);
        img_credits = ((MainActivity) getActivity()).findViewById(R.id.img_credits);
        txt_share = ((MainActivity) getActivity()).findViewById(R.id.txt_share);
        toolBarTitle = ((MainActivity) getActivity()).findViewById(R.id.toolBarTitle);
        txt_credits = ((MainActivity) getActivity()).findViewById(R.id.txt_credits);
        text_view_v = ((MainActivity) getActivity()).findViewById(R.id.text_view_credits_v);
        text_view_wallet_amount = ((MainActivity) getActivity()).findViewById(R.id.text_view_wallet_amount);

        img_veezee = ((MainActivity)getActivity()).findViewById(R.id.img_veezee);
        img_veezee.setVisibility(View.VISIBLE);

        Toolbar toolbar = ((MainActivity) getActivity()).findViewById(R.id.toolBar);
        toolbar.setVisibility(View.VISIBLE);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        img_credits.setVisibility(View.GONE);
        img_user_menu.setVisibility(View.VISIBLE);
        toolBarTitle.setVisibility(View.INVISIBLE);
        txt_credits.setVisibility(View.GONE);
        img_credits.setImageResource(R.drawable.ic_credits);
        txt_share.setVisibility(View.GONE);
        //toolBarTitle.setText("VeeZee");
        text_view_v.setVisibility(View.VISIBLE);

        (getActivity()).findViewById(R.id.text_view_wallet_amount).setVisibility(View.VISIBLE);
        (getActivity()).findViewById(R.id.image_view_coin).setVisibility(View.VISIBLE);

        toolBarTitle.setTextColor(getResources().getColor(R.color.colorAccent));
        toolBarTitle.setGravity(Gravity.CENTER);
        initComponents();
        mViewGrop = container;
        return view;
    }

    private void initComponents() {

        setReferences();
        setonClickListener();

    }

    private void setonClickListener() {

        img_share.setOnClickListener(HomeFragment.this);
        img_credits.setOnClickListener(this);
        text_view_request_car.setOnClickListener(this);
        image_view_empty_car.setOnClickListener(this);
    }

    private void setReferences() {

        cars_pager = view.findViewById(R.id.cars_pager);
        pager_images = (AutoScrollViewPager) view.findViewById(R.id.pager_images);
        sliderDotspanel = view.findViewById(R.id.SliderDots);
        img_share = view.findViewById(R.id.img_share);
        rv_adds = view.findViewById(R.id.rv_adds);
        scrollView = view.findViewById(R.id.scrollView);
        image_view_qr = view.findViewById(R.id.image_view_qr);
        text_view_request_car = view.findViewById(R.id.text_view_request_car);
        image_view_coin1 = view.findViewById(R.id.image_view_coin1);
        text_view_credits_v1 = view.findViewById(R.id.text_view_credits_v1);
        image_view_check = view.findViewById(R.id.image_view_check);
        image_view_empty_car = view.findViewById(R.id.image_view_empty_car);

        text_view_request_car.setVisibility(View.GONE);
        text_view_credits_v1.setVisibility(View.GONE);
        image_view_coin1.setVisibility(View.GONE);
        image_view_check.setVisibility(View.GONE);
        // containaer = view.findViewById(R.id.pager_container);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // callServiceForuserCars();

        callServiceForAds();

        if (isPush) {
            if (pager_images != null)
                if (pager_images.isCycle()) {
                    pager_images.stopAutoScroll();
                }
            setDialogForSendTip();
            isPush = false;
        }
        EventBus.getDefault().register(HomeFragment.this);
        getActivity().registerReceiver(receiver, new IntentFilter("GET ARRIVED STATUS"));
        getActivity().registerReceiver(receiver,new IntentFilter("CREDITS STATUS"));
        getActivity().registerReceiver(receiver,new IntentFilter("DIRECT SCAN"));

    }


    private void callServiceForAds() {
        if (Utility.isNetworkAvailable(getActivity())) {
            Utility.showLoadingDialog(getActivity(), "Loading...", false);
            JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.put("type ", "valet");

            }catch (Exception e){
                e.printStackTrace();
            }

            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(getActivity(), "GET", Constants.ADS_URL, jsonObject, new HashMap<String, String>(), this, Constants.ADS_CODE);

        } else {
            PopUtils.alertDialog(getActivity(), Utility.getResourcesString(getActivity(), R.string.check_internet), null);
        }
    }

    private void callServiceForuserCars() {

        if (Utility.isNetworkAvailable(getActivity())) {
            Utility.showLoadingDialog(getActivity(), "Loading...", false);

            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(getActivity(), "GET", Constants.USER_CARS + "?user_id=" + Utility.getSharedPrefStringData(getActivity(), Constants.USER_ID), new JSONObject(), new HashMap<String, String>(), this, Constants.USER_CARS_CODE);

        } else {
            PopUtils.alertDialog(getActivity(), Utility.getResourcesString(getActivity(), R.string.check_internet), null);
        }
    }

    private void setAdapter() {
        if(rv_adds!=null) {
            AdAdapter mAdAdapter = new AdAdapter(getActivity(), adsSubArrayList);
            rv_adds.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            rv_adds.setAdapter(mAdAdapter);
        }

    }

    private void setCarSlider() {

        if (image_view_empty_car != null)
            image_view_empty_car.setVisibility(View.GONE);

        MainActivity.count = userCarModelArrayList.size();

        Log.e("PAGE", "ACTIVITYPAGE : " + navigationTag);

        if (navigationTag.equals("VALET") || navigationTag.equals("VALETPUSH")) {

            Log.e("PAGE", "ACTIVITYPAGE : " + navigationTag);
            adapter = new CarouselPagerAdapterMain(mContext, getFragmentManager(), userCarModelArrayList);
            isAutoScrool = false;

        } else {

            isAutoScrool = false;
            Log.e("PAGE", "FRAGMENTPAGE : " + navigationTag);
            navigationTag = "FRAGMENT";
            adapter = new CarouselPagerAdapterMain(mContext, getChildFragmentManager(), userCarModelArrayList);
        }
        if (cars_pager != null && adapter != null) {
            try {
                cars_pager.setAdapter(adapter);
                cars_pager.setPageTransformer(false, adapter);

                DisplayMetrics metrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int pageMargin = (int) ((metrics.widthPixels / 3.85) * 2.5);
                cars_pager.setPageMargin(-pageMargin);
                // cars_pager.setPadding(5, 5, 5, 5);

                cars_pager.setOffscreenPageLimit(3);

                if (userCarModelArrayList.size() > 2) {
                    cars_pager.setCurrentItem(PAGES * LOOPS / 2);
                } else {
                    cars_pager.setCurrentItem(0);
                }

                cars_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        Log.e("position", "onPageScrolled: " + position + " : " + positionOffset);

                    }

                    @Override
                    public void onPageSelected(int position) {

                        // carPosition = position;
                        position = position % PAGES;

                        Picasso.get().load(userCarModelArrayList.get(position).getQr()).fit().centerCrop().into(image_view_qr);
                        USER_CAR_ID = userCarModelArrayList.get(position).getUser_car_id();
                        Constants.ShareQr = userCarModelArrayList.get(position).getQr();
                        currentcarpostn = position;
                        if (!userCarModelArrayList.get(position).getCar_status().equalsIgnoreCase("")) {
                            if (!userCarModelArrayList.get(position).getCar_status().equalsIgnoreCase("handedover")) {
                                image_view_check.setVisibility(View.VISIBLE);
                            } else {
                                image_view_check.setVisibility(View.GONE);
                            }
                        }else {
                            image_view_check.setVisibility(View.GONE);
                        }
                        if (userCarModelArrayList.get(position).getDirect_scan().equalsIgnoreCase("yes") && !userCarModelArrayList.get(position).getIs_requested().equals("true") && !userCarModelArrayList.get(position).getCar_status().equalsIgnoreCase("arrived")) {
                            text_view_request_car.setVisibility(View.GONE);
                            text_view_credits_v1.setVisibility(View.GONE);
                            image_view_coin1.setVisibility(View.GONE);

                        } else if (userCarModelArrayList.get(position).getIs_requested().equals("false") && userCarModelArrayList.get(position).getCar_status().equals("parked")) {
                            text_view_request_car.setVisibility(View.VISIBLE);
                            text_view_credits_v1.setVisibility(View.VISIBLE);
                            image_view_coin1.setVisibility(View.VISIBLE);
                            //  image_view_check.setVisibility(View.VISIBLE);
                            text_view_request_car.setText("REQUEST CAR");
                        } else if (userCarModelArrayList.get(position).getIs_requested().equals("true") && (userCarModelArrayList.get(position).getCar_status().equals("parked")) || userCarModelArrayList.get(position).getCar_status().equalsIgnoreCase("arrived")) {
                            text_view_request_car.setVisibility(View.VISIBLE);
                            //  image_view_check.setVisibility(View.VISIBLE);
                            text_view_credits_v1.setVisibility(View.GONE);
                            image_view_coin1.setVisibility(View.GONE);
                            text_view_request_car.setText("CAR STATUS");


                        } else {
                            text_view_request_car.setVisibility(View.GONE);
                            text_view_credits_v1.setVisibility(View.GONE);
                            image_view_coin1.setVisibility(View.GONE);
                            // image_view_check.setVisibility(View.GONE);
                        }
                        Qr = userCarModelArrayList.get(position).getQr();
                        //   Toast.makeText(myContext, "selectedCarTypeId "+selectedCarTypeId, Toast.LENGTH_SHORT).show();
                        broadcastreceived = false;
                        currentcarpostn = position;
                        if (timer != null) {

                            timer.cancel();
                            timer = null;
                        }

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                Picasso.get().load(userCarModelArrayList.get(0).getQr()).fit().centerCrop().into(image_view_qr);
                USER_CAR_ID = userCarModelArrayList.get(0).getUser_car_id();
                Qr = userCarModelArrayList.get(0).getQr();
                Constants.ShareQr = userCarModelArrayList.get(0).getQr();

                if (!userCarModelArrayList.get(0).getCar_status().equalsIgnoreCase("")) {
                    if (!userCarModelArrayList.get(0).getCar_status().equalsIgnoreCase("handedover")){
                        image_view_check.setVisibility(View.VISIBLE);
                } else {
                    image_view_check.setVisibility(View.GONE);
                }
            }else{
                    image_view_check.setVisibility(View.GONE);
                }

                if (userCarModelArrayList.get(0).getDirect_scan().equalsIgnoreCase("yes") && !userCarModelArrayList.get(0).getIs_requested().equals("true") &&!userCarModelArrayList.get(0).getCar_status().equalsIgnoreCase("arrived")) {
                text_view_request_car.setVisibility(View.GONE);
                text_view_credits_v1.setVisibility(View.GONE);
                image_view_coin1.setVisibility(View.GONE);
               // image_view_check.setVisibility(View.GONE);
            } else if (userCarModelArrayList.get(0).getIs_requested().equals("false") && userCarModelArrayList.get(0).getCar_status().equals("parked")) {
                text_view_request_car.setVisibility(View.VISIBLE);
                text_view_credits_v1.setVisibility(View.VISIBLE);
               // image_view_check.setVisibility(View.VISIBLE);
                image_view_coin1.setVisibility(View.VISIBLE);
                text_view_request_car.setText("REQUEST CAR");
            } else if ((userCarModelArrayList.get(0).getIs_requested().equals("true") && (userCarModelArrayList.get(0).getCar_status().equals("parked")) || userCarModelArrayList.get(0).getCar_status().equalsIgnoreCase("arrived"))) {
                text_view_request_car.setVisibility(View.VISIBLE);
              //  image_view_check.setVisibility(View.VISIBLE);
                text_view_credits_v1.setVisibility(View.GONE);
                image_view_coin1.setVisibility(View.GONE);
                text_view_request_car.setText("CAR STATUS");
            } else {
                text_view_request_car.setVisibility(View.GONE);
                text_view_credits_v1.setVisibility(View.GONE);
                image_view_coin1.setVisibility(View.GONE);
              //  image_view_check.setVisibility(View.GONE);
            }
            /*}*/
        }catch (Exception e){
                e.printStackTrace();
            }
    }

    }


    private void setImageSlider() {
        homeImageSliderAdapter viewPagerAdapter = new homeImageSliderAdapter(getActivity(), adsMainArrayList);
        if (pager_images != null) {
            pager_images.setAdapter(viewPagerAdapter);
            pager_images.startAutoScroll();
            pager_images.setInterval(3000);
            pager_images.setCycle(true);
            pager_images.setStopScrollWhenTouch(true);
        }
        dotscount = viewPagerAdapter.getCount();

        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {
            try {

                dots[i] = new ImageView(getActivity());
                dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                params.setMargins(8, 0, 8, 0);
                if (sliderDotspanel != null)
                    sliderDotspanel.addView(dots[i], params);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(dots!=null)
        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));
        if (pager_images != null)
            pager_images.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    for (int i = 0; i < dotscount; i++) {
                        try {
                            dots[i].setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.non_active_dot));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    dots[position].setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.active_dot));
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
    }

    private void autoScroll() {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == adsMainArrayList.size()) {
                    currentPage = 0;
                }
                pager_images.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_share:
                if (userCarModelArrayList.size() > 0) {
                    onShareClick(view);
                } else {
                    Toast.makeText(getActivity(), "Please add a car to share!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.img_credits:
                startActivity(new Intent(getActivity(), VeeZeeCreditsActivity.class));
                break;
            case R.id.text_view_request_car:
                if (text_view_request_car.getText().toString().equals("REQUEST CAR")) {
                    callForRequestCar();
                    requestCarId = USER_CAR_ID;

                } else if (text_view_request_car.getText().toString().equals("CAR STATUS")) {
                    //dialogForCars();
                    isRefreshed = false;
                    callForCarStatus(USER_CAR_ID);
                }

                break;
            case R.id.image_view_empty_car:
                Intent i = new Intent(mContext, CarDetailsActivity.class);
                i.putExtra("TAG", "VALET");
                startActivity(i);
                break;
        }
    }

    private void callForRequestCar() {
        if (Utility.isNetworkAvailable(getActivity())) {
            Utility.showLoadingDialog(getActivity(), "Loading...", false);

            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(getActivity(), "GET", Constants.REQUEST_FOR_CAR, new JSONObject(), new HashMap<String, String>(), this, Constants.REQUEST_FOR_CAR_CODE);

        } else {
            PopUtils.alertDialog(getActivity(), Utility.getResourcesString(getActivity(), R.string.check_internet), null);
        }
    }

    private void callForCarStatus(String user_car_id) {
        if (Utility.isNetworkAvailable(getActivity())) {
            Utility.showLoadingDialog(getActivity(), "Loading...", false);
            try {
                JSONObject params = new JSONObject();
                params.put("user_car_id", user_car_id);

                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequestJSonObject(getActivity(), "POST", Constants.REQUEST_CAR_STATUS, params, new HashMap<String, String>(), this, Constants.REQUEST_CAR_STATUS_CODE);

            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            PopUtils.alertDialog(getActivity(), Utility.getResourcesString(getActivity(), R.string.check_internet), null);
        }
    }

    private void callServiceForVipCarRequest(String PAYMENT_TYPE, String ORDERID, String TXNID, String TXNAMOUNT,
                                             String PAYMENTMODE, String CURRENCY, String TXNDATE, String STATUS, String GATEWAYNAME,
                                             String BANKTXNID, String BANKNAME) {
        if (Utility.isNetworkAvailable(getActivity())) {
            Utility.showLoadingDialog(getActivity(), "Loading...", false);

            JSONObject params = new JSONObject();
            try {
                if (PAYMENT_TYPE.equals("1")) {
                    params.put("user_id", Utility.getSharedPrefStringData(getActivity(), Constants.USER_ID));
                    params.put("user_car_id", USER_CAR_ID);
                    params.put("payment_type", PAYMENT_TYPE);
                    params.put("order_id", ORDERID);
                    params.put("txn_id", TXNID);
                    params.put("txn_amount", TXNAMOUNT);
                    params.put("payment_mode", PAYMENTMODE);
                    params.put("currency", CURRENCY);
                    params.put("txn_date", TXNDATE);
                    params.put("status", STATUS);
                    params.put("gateway_name", GATEWAYNAME);
                    params.put("bank_transaction_id", BANKTXNID);
                    params.put("bank_name", BANKNAME);
                } else {
                    params.put("user_id", Utility.getSharedPrefStringData(getActivity(), Constants.USER_ID));
                    params.put("user_car_id", USER_CAR_ID);
                    params.put("payment_type", PAYMENT_TYPE);
                    params.put("txn_amount", TXNAMOUNT);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(getActivity(), "POST", Constants.REQUEST_FOR_VIP_CAR, params, new HashMap<String, String>(), this, Constants.REQUEST_FOR_VIP_CAR_CODE);

        } else {
            PopUtils.alertDialog(getActivity(), Utility.getResourcesString(getActivity(), R.string.check_internet), null);
        }
    }

    private void callServiceForSendTip(String ORDERID, String TXNID, String TXNAMOUNT,
                                       String PAYMENTMODE, String CURRENCY, String TXNDATE, String STATUS, String GATEWAYNAME,
                                       String BANKTXNID, String BANKNAME, String parking_id) {
        if (Utility.isNetworkAvailable(getActivity())) {
            Utility.showLoadingDialog(getActivity(), "Loading...", false);

            JSONObject params = new JSONObject();
            try {

                JSONArray jsonArray = new JSONArray();
                Iterator iterator = keyWordsSet.iterator();
                while (iterator.hasNext()) {
                    jsonArray.put(iterator.next());
                }


                params.put("user_id", Utility.getSharedPrefStringData(getActivity(), Constants.USER_ID));
                params.put("driver_id", mPushForCarHandOverDetails.getDriver_id());
                params.put("order_id", ORDERID);
                params.put("txn_id", TXNID);
                params.put("txn_amount", TXNAMOUNT);
                params.put("payment_mode", PAYMENTMODE);
                params.put("currency", CURRENCY);
                params.put("txn_date", TXNDATE);
                params.put("status", STATUS);
                params.put("gateway_name", GATEWAYNAME);
                params.put("bank_transaction_id", BANKTXNID);
                params.put("bank_name", BANKNAME);
                params.put("rating", mRating);
                params.put("keywords", jsonArray);
                params.put("parking_id", parking_id);


            } catch (Exception e) {
                e.printStackTrace();
            }

            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(getActivity(), "POST", Constants.SEND_TIP, params, new HashMap<String, String>(), this, Constants.SEND_TIP_CODE);

        } else {
            PopUtils.alertDialog(getActivity(), Utility.getResourcesString(getActivity(), R.string.check_internet), null);
        }
    }


    private void dialogForRequestCar() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        View dialogView = LayoutInflater.from(mViewGrop.getContext()).inflate(R.layout.dialog_request_car, mViewGrop, false);
        builder.setView(dialogView);
        requestCarDialog = builder.create();
        ImageView close = dialogView.findViewById(R.id.img_close);
        ImageView img_paytm = dialogView.findViewById(R.id.img_paytm);
        ImageView text_veezee = dialogView.findViewById(R.id.text_veezee);
        TextView txt_paying = dialogView.findViewById(R.id.txt_paying);
        TextView txt_credits = dialogView.findViewById(R.id.txt_credits);
        TextView txt_message = dialogView.findViewById(R.id.txt_message);
        txt_message.setText(mRequestCar.getMessage());
        txt_credits.setText(mRequestCar.getCredits() + " VeeZee Credits");
        txt_paying.setText("Paying Rs: " + mRequestCar.getAmount());
        requestCarDialog.setCancelable(false);
        requestCarDialog.setCanceledOnTouchOutside(false);

        img_paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isVeezeeClicked = false;
                isTipContinue = false;
                CallForGetCheckSum(mRequestCar.getAmount());
                // alertDialog.dismiss();
            }
        });
        text_veezee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isVeezeeClicked = true;
                if (Integer.parseInt(mRequestCar.getAmount()) < Integer.parseInt(Utility.getSharedPrefStringData(getActivity(), Constants.WALLET_AMOUNT))) {
                    callServiceForVipCarRequest("2", "", "", mRequestCar.getCredits(), "", "", "", "", "", "", "");
                    // alertDialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Insufficient wallet amount", Toast.LENGTH_SHORT).show();
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCarDialog.dismiss();
            }
        });
        // setImageSlider();
        requestCarDialog.show();

    }

    private void dialogForCars() {

        builder = new AlertDialog.Builder(mContext, R.style.CustomAlertDialog);
        //ViewGroup mViewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_car_status, null);
        builder.setView(dialogView);
        carStatusDialog = builder.create();
        carStatusDialog.setCanceledOnTouchOutside(false);
        carStatusDialog.setCancelable(false);
        TextView txt_token_no = dialogView.findViewById(R.id.txt_token_no);
        ImageView image_view_car = dialogView.findViewById(R.id.image_view_car);
        TextView txt_car_no = dialogView.findViewById(R.id.txt_car_no);
        txt_noOfcars = dialogView.findViewById(R.id.txt_noOfcars);
        txt_time = dialogView.findViewById(R.id.txt_time);


        ImageView img_call = dialogView.findViewById(R.id.img_call);
        TextView text_view_refresh = dialogView.findViewById(R.id.image_view_refresh);

        TextView dummy_text = dialogView.findViewById(R.id.dummy_text);

        dummy_text.setText(mCarStatus.getDriver_number());
        dummy_text.setVisibility(View.GONE);
        final int carId = Integer.parseInt(mCarStatus.getCar_id());

        int tokenNumber = Integer.parseInt(mCarStatus.getToken_number());
        if (tokenNumber <= 9) {
            txt_token_no.setText("0" + mCarStatus.getToken_number());
        } else {
            txt_token_no.setText(mCarStatus.getToken_number());
        }
        Picasso.get().load(mCarStatus.getCar_image()).placeholder(R.drawable.car_placeholder).into(image_view_car);
        txt_car_no.setText(mCarStatus.getCar_number());
        txt_noOfcars.setText(mCarStatus.getCar_status());
        txt_noOfcars.setVisibility(View.VISIBLE);
        text_view_refresh.setVisibility(View.VISIBLE);

        Log.e("dvbfhbvh","actual car status "+mCarStatus.getCar_status());

        Log.e("dvbfhbvh","  car status response    "+mCarStatus.getStatus());

        if (!mCarStatus.getStatus().equalsIgnoreCase("arrived")) {
            img_call.setVisibility(View.GONE);
            txt_time.setVisibility(View.INVISIBLE);

            Log.e("dvbfhbvh","  car arravid    "+car_status);

        }
        else {
            Map<Integer, Boolean> showTextviewsMap = Utility.getShowTextviewsMap(mContext);
            for (int k = 0; k < showTextviewsMap.size(); k++) {
                showTextviewsMap.put(k, false);
            }
            showTextviewsMap.put(carId, true);
            Utility.setShowTextviewsMap(showTextviewsMap, mContext);

            img_call.setVisibility(View.VISIBLE);
            txt_time.setVisibility(View.VISIBLE);

            Log.e("dvbfhbvh","   car arravid else     "+car_status);

        }


        img_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCarStatus.getDriver_number().length() > 9) {
                    isRefreshed = false;
                    Intent intent = new Intent(Intent.ACTION_DIAL);

                    intent.setData(Uri.parse("tel:" +"+91"+ mCarStatus.getDriver_number()));

                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Driver not assigned", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (Utility.getMaxTime(mContext).get(carId) != null && Utility.getMaxTime(mContext).get(carId).contains("Completed")) {

            // txt_time.setText("Please collect your car immediately");
            if(mCarStatus.getStatus().equalsIgnoreCase("arrived")){
                txt_noOfcars.setVisibility(View.VISIBLE);
                txt_noOfcars.setText("Your car has arrived");
            }else {
                txt_noOfcars.setVisibility(View.INVISIBLE);
            }

        }

        text_view_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isRefreshed = true;
                callForCarStatus(mCarStatus.getCar_id());

                if (Utility.getMaxTime(mContext).get(carId) != null && Utility.getMaxTime(mContext).get(carId).contains("Completed")) {

                   // txt_time.setText("Please collect your car immediately");
                    if(mCarStatus.getStatus().equalsIgnoreCase("arrived")){
                        txt_noOfcars.setVisibility(View.VISIBLE);
                        txt_noOfcars.setText("Your car has arrived");
                    }else {
                        txt_noOfcars.setVisibility(View.INVISIBLE);

                        if (mCarStatus.getStatus().equalsIgnoreCase("parked"))
                        {
                            txt_noOfcars.setVisibility(View.VISIBLE);

                        }

                        Log.e("dvbfhbvh","  car arravid  last refresh   hello "+car_status);

                    }

                }

                Log.e("dvbfhbvh","  car arravid  last refresh   "+car_status);

                Log.e("CLOCK", "onClick: "+txt_time.getText().toString() );
            }
        });


        ImageView close = dialogView.findViewById(R.id.img_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carStatusDialog.dismiss();
                isRefreshed = false;
                //countDownTimer.cancel();
            }
        });


        if (Utility.getMaxTime(mContext).get(carId) != null && Utility.getMaxTime(mContext).get(carId).contains(":")) {

            //text_view_refresh.setVisibility(View.GONE);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
            try {
                String datestr1 = simpleDateFormat.format(new Date());
                Date date1 = simpleDateFormat.parse(datestr1);
                Log.e("## MAP FROM PREFERENCE", Utility.getMaxTime(view.getContext()).size() + "");
                Log.e("## MAP FROM PREFERENCE", Utility.getMaxTime(view.getContext()) + "");

                String expirytime = Utility.getMaxTime(mContext).get(carId);
                Date date2 = simpleDateFormat.parse(expirytime);
                Log.e("## MAXTIME:::", Utility.getMaxTime(mContext).get(carId) + "");
                long difference = date2.getTime() - date1.getTime();

                int Hours = (int) (difference / (1000*60*60));
                int Mins = (int) (difference / (1000 * 60)) % 60;
                long Secs = (int) (difference / 1000) % 60;

                long minuteandsec = Mins*60*1000 + Secs * 1000;

                Log.e("minnnn", "" + minuteandsec);


                Log.e("minnnnaa", "start time " + minuteandsec);

                if (minuteandsec > 0) {

                    Log.e("minnnnaa", "gretet then zero time " + minuteandsec);


                    customtimer = new Customtimer(minuteandsec, 1000);
                    customtimer.start();

                    if (customtimerSparseArray.get(carId) != null) {

                        customtimerSparseArray.remove(carId);

                    }
                    customtimerSparseArray.put(carId, customtimer);
                    Log.e("### BEFORE TIME", "CAR POS::" + carId);
                    Log.e("### BEFORE TIME", customtimerSparseArray + "");
                    runtimer(carId);

                } else {

                    Log.e("minnnnaa", "if zero" + minuteandsec);


                    txt_noOfcars.setText("Your car has arrived");
                    txt_time.setText("Please collect your car immediately");
                    Map<Integer, String> map = Utility.getMaxTime(mContext);
                    text_view_refresh.setVisibility(View.VISIBLE);
                    //map.remove(currentcarpostn);
                    map.put(carId,"Completed");
                    Utility.setMaxTime(map, mContext);
                    Log.e("### MAP AFTER REMOVE", Utility.getMaxTime(mContext) + "");
                }

            } catch (ParseException e) {

                e.printStackTrace();

            }
        }
        else if (Utility.getMaxTime(mContext).get(carId) != null && Utility.getMaxTime(mContext).get(carId).contains("Completed")) {

            txt_time.setText("Please collect your car immediately");
            if(mCarStatus.getStatus().equalsIgnoreCase("arrived")){
                txt_noOfcars.setVisibility(View.VISIBLE);
                txt_noOfcars.setText("Your car has arrived");
            }else {
                txt_noOfcars.setVisibility(View.INVISIBLE);
                txt_time.setVisibility(View.VISIBLE);
                txt_time.setText("Your car is arriving shortly");
            }

        }else if (Utility.getMaxTime(mContext).get(carId) != null && Utility.getMaxTime(mContext).get(carId).contains("requested")&&
               mCarStatus.getCar_status().equalsIgnoreCase("arrived")) {

            //txt_noOfcars.setVisibility(View.GONE);
            text_view_refresh.setVisibility(View.VISIBLE);
            txt_time.setText("Your car is arriving shortly");

            Log.e("dvbfhbvh","car arravide requested"+car_status);

        } else {    // if it is null
            text_view_refresh.setVisibility(View.VISIBLE);
            //txt_time.setText("Your car has arrived");
            //txt_noOfcars.setVisibility(View.GONE);

            Log.e("dvbfhbvh","car arravide request else"+car_status);

        }

       if (mCarStatus.getStatus().equalsIgnoreCase("parked"))
        {
            Log.e("dvbfhbvh","  car arravid  last   "+car_status);
            txt_noOfcars.setText(car_status);
            txt_noOfcars.setVisibility(View.VISIBLE);
            txt_time.setVisibility(View.INVISIBLE);
        }

        carStatusDialog.show();

    }

    private void runtimer(final int CarId) {


        if (timer == null) {

            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {

                    new Handler(Looper.getMainLooper()).post(new Runnable() {

                        @Override
                        public void run() {

                            if (Utility.getShowTextviewsMap(mContext).get(CarId) != null) {

                                boolean showtvatcar = Utility.getShowTextviewsMap(mContext).get(CarId);

                                if (showtvatcar) {

                               //     Log.e("### CUSTOMTIMERS MAP", customtimerSparseArray + "");
                               //     Log.e("### CUSTOMTIMERS MAP customtimerSparseArray ", ""+customtimerSparseArray.get(CarId).getCurrent_count());
                             //       Log.e("CUSTOMTIMERS MAPgetMaxTime", "run: "+Utility.getMaxTime(mContext).get(CarId) );
                                    try {
                                        if (customtimerSparseArray.get(CarId).getCurrent_count() > 0 || Utility.getMaxTime(mContext).get(CarId).contains(":")) {

                                            long time =customtimerSparseArray.get(CarId).getCurrent_count();

                                            Log.e("RUNNING_TIME", "run: "+time );

                                            String RemainingTime = String.format(" %02d : %02d ",
                                                    TimeUnit.MILLISECONDS.toMinutes(time),
                                                    TimeUnit.MILLISECONDS.toSeconds(time) -
                                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
                                            txt_noOfcars.setVisibility(View.VISIBLE);
                                            txt_noOfcars.setText(mCarStatus.getCar_status());
                                            txt_time.setText(RemainingTime);
                                            if(RemainingTime.contains("00 : 00")){

                                                txt_time.setText("Please colllect your car immediately");
                                                Map<Integer, String> map = Utility.getMaxTime(mContext);
                                                //map.remove(currentcarpostn);
                                                map.put(CarId,"Completed");
                                                Utility.setMaxTime(map, mContext);
                                                txt_noOfcars.setVisibility(View.INVISIBLE);
                                                Log.e("### MAP AFTER REMOVE", Utility.getMaxTime(mContext) + "");
                                                if (Utility.getMaxTime(mContext).get(CarId) != null && Utility.getMaxTime(mContext).get(CarId).contains("Completed")) {

                                                    // txt_time.setText("Please collect your car immediately");
                                                    if(mCarStatus.getStatus().equalsIgnoreCase("arrived")){
                                                        txt_noOfcars.setVisibility(View.VISIBLE);
                                                        txt_noOfcars.setText("Your car has arrived");
                                                    }else {
                                                        txt_noOfcars.setVisibility(View.INVISIBLE);
                                                    }

                                                }
                                            }

                                        } else {
                                            Log.e("### CUSTOMTIMERS MAP AFTER REMOVE ", ""+customtimerSparseArray.get(CarId).getCurrent_count());

                                            txt_time.setText("Please collect your car immediately");
                                            Map<Integer, String> map = Utility.getMaxTime(mContext);
                                            //map.remove(currentcarpostn);
                                            map.put(CarId,"Completed");
                                            Utility.setMaxTime(map, mContext);
                                            txt_noOfcars.setVisibility(View.VISIBLE);
                                            txt_noOfcars.setText("Your car has arrived");
                                            Log.e("## MAP AFTER REMOVE", Utility.getMaxTime(mContext) + "");
                                        }
                                    } catch (Exception e) {

                                        Log.e("### TIMERS MAP DATA", e.getMessage());
                                        e.printStackTrace();
                                    }

                                }

                            }


                        }
                    });
                }
            }, 0, 1000);
        }

    }

    private void setDialogforViewCarStatus(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        ViewGroup mViewGroup = getActivity().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(mViewGroup.getContext()).inflate(R.layout.custom_popup_carstatus, mViewGroup, false);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        TextView txtOk,txtMessage;

        txtOk = dialogView.findViewById(R.id.txtOk);
        txtMessage = dialogView.findViewById(R.id.txtMessage);

        txtMessage.setText(message);

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                callForCarStatus(requestCarId);
            }
        });

        alertDialog.show();
    }
    private void setDialogForSendTip() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.CustomAlertDialog);

        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_valet_experience, null, false);
        builder.setView(dialogView);
        sendTipDialog = builder.create();
        TextView txt_skip = dialogView.findViewById(R.id.txt_skip);
        txt_amount1 = dialogView.findViewById(R.id.txt_amount1);
        txt_amount2 = dialogView.findViewById(R.id.txt_amount2);
        txt_amount3 = dialogView.findViewById(R.id.txt_amount3);
        TextView txt_drivername = dialogView.findViewById(R.id.txt_drivername);
        TextView text_view_mobile_number = dialogView.findViewById(R.id.text_view_mobile_number);
        TextView txt_car_no = dialogView.findViewById(R.id.txt_car_no);
        TextView txt_token_no = dialogView.findViewById(R.id.txt_token_no);
        TextView txt_thanks = dialogView.findViewById(R.id.txt_thanks);
        TextView txt_continue = dialogView.findViewById(R.id.txt_continue);
        ImageView image_view_driver = dialogView.findViewById(R.id.image_view_driver);
        //rv_ratting = dialogView.findViewById(R.id.rv_ratting);
        text_view1 = dialogView.findViewById(R.id.text_view1);
        text_view2 = dialogView.findViewById(R.id.text_view2);
        text_view3 = dialogView.findViewById(R.id.text_view3);
        text_view4 = dialogView.findViewById(R.id.text_view4);
        text_view5 = dialogView.findViewById(R.id.text_view5);
        text_view6 = dialogView.findViewById(R.id.text_view6);

        rating_bar = dialogView.findViewById(R.id.rating_bar);
        txt_total_tip = dialogView.findViewById(R.id.txt_total_tip);
        txt_rating = dialogView.findViewById(R.id.txt_rating);
        sendTipDialog.setCancelable(false);
        sendTipDialog.setCanceledOnTouchOutside(false);
        //mPushForCarHandOverDetails = new PushForCarHandOverDetails();
        if (mPushForCarHandOverDetails != null) {
            String mobile = mPushForCarHandOverDetails.getMobile().toString();
            String  name = mPushForCarHandOverDetails.getDriver_name();

            name = name.substring(0, 1).toUpperCase() + name.substring(1);

            txt_drivername.setText(name);
            text_view_mobile_number.setText("XXXXXX" + (mobile.substring(mobile.length() - 4)));
            txt_car_no.setText("# " + mPushForCarHandOverDetails.getSerial_no());

            int tokenNumber = Integer.parseInt(mPushForCarHandOverDetails.getToken());
            if (tokenNumber <= 9) {
                txt_token_no.setText("0" + mPushForCarHandOverDetails.getToken());
            } else {
                txt_token_no.setText(mPushForCarHandOverDetails.getToken());
            }

            txt_thanks.setText("Thank " + name + " by adding tip!");
            Picasso.get().load(mPushForCarHandOverDetails.getImage()).placeholder(R.drawable.ic_user).into(image_view_driver);
            mParking_id = mPushForCarHandOverDetails.getParking_id();
        }
        mRating = 0;

        txt_amount1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_amount1.setTextColor(getResources().getColor(R.color.white));
                txt_amount1.setBackground(getResources().getDrawable(R.drawable.button_background, null));
                txt_total_tip.setText("" + (Integer.parseInt(txt_total_tip.getText().toString().replaceAll("[^a-zA-Z0-9]", "")) + 10));

            }
        });
        txt_amount2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_amount2.setTextColor(getResources().getColor(R.color.white));
                txt_amount2.setBackground(getResources().getDrawable(R.drawable.button_background, null));

                txt_total_tip.setText("" + (Integer.parseInt(txt_total_tip.getText().toString().replaceAll("[^a-zA-Z0-9]", "")) + 20));

            }
        });
        txt_amount3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_amount3.setTextColor(getResources().getColor(R.color.white));
                txt_amount3.setBackground(getResources().getDrawable(R.drawable.button_background, null));
                txt_total_tip.setText("" + (Integer.parseInt(txt_total_tip.getText().toString().replaceAll("[^a-zA-Z0-9]", "")) + 50));

            }
        });
        txt_total_tip.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE)) {
                    //Log.i(TAG,"Enter pressed");

                    txt_amount1.setTextColor(getResources().getColor(R.color.colorAccent));
                    txt_amount1.setBackground(getResources().getDrawable(R.drawable.boarded_background, null));
                    txt_amount2.setTextColor(getResources().getColor(R.color.colorAccent));
                    txt_amount2.setBackground(getResources().getDrawable(R.drawable.boarded_background, null));
                    txt_amount3.setTextColor(getResources().getColor(R.color.colorAccent));
                    txt_amount3.setBackground(getResources().getDrawable(R.drawable.boarded_background, null));
                }
                return false;
            }
        });
        txt_total_tip.setKeyImeChangeListener(new MontserratEditText.KeyImeChange() {
            @Override
            public void onKeyIme(int keyCode, KeyEvent event) {

                txt_amount1.setTextColor(getResources().getColor(R.color.colorAccent));
                txt_amount1.setBackground(getResources().getDrawable(R.drawable.boarded_background, null));
                txt_amount2.setTextColor(getResources().getColor(R.color.colorAccent));
                txt_amount2.setBackground(getResources().getDrawable(R.drawable.boarded_background, null));
                txt_amount3.setTextColor(getResources().getColor(R.color.colorAccent));
                txt_amount3.setBackground(getResources().getDrawable(R.drawable.boarded_background, null));

            }
        });
        mRating = rating_bar.getRating();
        txt_rating.setText("Terrible");
        rating_bar.setRating(1);
        final ArrayList<String>rattingList = new ArrayList<>();
        final ArrayList<String>rattings = new ArrayList<>();
        rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                mRating = rating;
                Log.e("Ratting", "Rating :" + rating);
                if (rating == 1) {
                    txt_rating.setText("Terrible");

                    keyWordsSet.clear();

                    text_view1.setText("Key Misplacement");
                    text_view2.setText("Delay in Pick Up/ Drop Off");
                    text_view3.setText("Vehicle Handling");
                    text_view4.setText("Valet Attitude");
                    text_view5.setText("VeeZee App");
                    text_view6.setText("Other");
                    text_view6.setVisibility(View.VISIBLE);

                } else if (rating == 2) {
                    txt_rating.setText("Bad");
                    keyWordsSet.clear();

                    text_view1.setText("Key Misplacement");
                    text_view2.setText("Delay in Pick Up/ Drop Off");
                    text_view3.setText("Valet Attitude");
                    text_view4.setText("Vehicle Handling");
                    text_view5.setText("VeeZee App");
                    text_view6.setText("Other");
                    text_view6.setVisibility(View.VISIBLE);

                } else if (rating == 3) {
                    txt_rating.setText("Disappointing");
                    keyWordsSet.clear();

                    text_view1.setText("Delay in Pick Up/ Drop Off");
                    text_view2.setText("Valet Attitude");
                    text_view3.setText("Key Misplacement");
                    text_view4.setText("Vehicle Handling");
                    text_view5.setText("VeeZee App");
                    text_view6.setText("Other");
                    text_view6.setVisibility(View.VISIBLE);

                } else if (rating == 4) {
                    txt_rating.setText("Good");
                    keyWordsSet.clear();

                    text_view1.setText("Service Time");
                    text_view2.setText("VeeZee App");
                    text_view3.setText("Vehicle Handling");
                    text_view4.setText("Valet Attitude");
                    text_view5.setText("Other");
                    text_view6.setVisibility(View.GONE);

                } else if (rating == 5) {
                    txt_rating.setText("Exceptional");
                    keyWordsSet.clear();

                    text_view1.setText("VeeZee App");
                    text_view2.setText("Quick Service");
                    text_view3.setText("Vehicle Handling");
                    text_view4.setText("Great Attitude");
                    text_view5.setText("Other");
                    text_view6.setVisibility(View.GONE);

                }else {
                    keyWordsSet.clear();
                    text_view1.setVisibility(View.GONE);
                    text_view2.setVisibility(View.GONE);
                    text_view3.setVisibility(View.GONE);
                    text_view4.setVisibility(View.GONE);
                    text_view5.setVisibility(View.GONE);
                    text_view6.setVisibility(View.GONE);
                }

                text_view1.setBackground(getResources().getDrawable(R.drawable.boarded_with_ash_background, null));
                text_view1.setTextColor(getResources().getColor(R.color.black));

                text_view2.setBackground(getResources().getDrawable(R.drawable.boarded_with_ash_background, null));
                text_view2.setTextColor(getResources().getColor(R.color.black));

                text_view3.setBackground(getResources().getDrawable(R.drawable.boarded_with_ash_background, null));
                text_view3.setTextColor(getResources().getColor(R.color.black));

                text_view4.setBackground(getResources().getDrawable(R.drawable.boarded_with_ash_background, null));
                text_view4.setTextColor(getResources().getColor(R.color.black));

                text_view5.setBackground(getResources().getDrawable(R.drawable.boarded_with_ash_background, null));
                text_view5.setTextColor(getResources().getColor(R.color.black));

                text_view6.setBackground(getResources().getDrawable(R.drawable.boarded_with_ash_background, null));
                text_view6.setTextColor(getResources().getColor(R.color.black));
                //setAdapterForRatting();
            }
        });

        text_view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView1) {
                    textView1 = false;
                    text_view1.setBackground(getResources().getDrawable(R.drawable.boarded_with_ash_background, null));
                    text_view1.setTextColor(getResources().getColor(R.color.black));
                } else {
                    text_view1.setBackground(getResources().getDrawable(R.drawable.boarded_background, null));
                    text_view1.setTextColor(getResources().getColor(R.color.colorAccent));
                    textView1 = true;
                }
                keyWordsSet.add(text_view1.getText().toString());

            }
        });
        text_view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView2) {
                    textView2 = false;
                    text_view2.setBackground(getResources().getDrawable(R.drawable.boarded_with_ash_background, null));
                    text_view2.setTextColor(getResources().getColor(R.color.black));
                } else {
                    textView2 = true;
                    text_view2.setBackground(getResources().getDrawable(R.drawable.boarded_background, null));
                    text_view2.setTextColor(getResources().getColor(R.color.colorAccent));

                }

                keyWordsSet.add(text_view2.getText().toString());

            }
        });
        text_view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView3) {
                    textView3 = false;
                    text_view3.setBackground(getResources().getDrawable(R.drawable.boarded_with_ash_background, null));
                    text_view3.setTextColor(getResources().getColor(R.color.black));
                } else {
                    textView3 = true;
                    text_view3.setBackground(getResources().getDrawable(R.drawable.boarded_background, null));
                    text_view3.setTextColor(getResources().getColor(R.color.colorAccent));

                }
                keyWordsSet.add(text_view3.getText().toString());
            }
        });
        text_view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView4) {
                    textView4 = false;
                    text_view4.setBackground(getResources().getDrawable(R.drawable.boarded_with_ash_background, null));
                    text_view4.setTextColor(getResources().getColor(R.color.black));
                } else {
                    textView4 = true;
                    text_view4.setBackground(getResources().getDrawable(R.drawable.boarded_background, null));
                    text_view4.setTextColor(getResources().getColor(R.color.colorAccent));

                }
                keyWordsSet.add(text_view4.getText().toString());
            }
        });
        text_view5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView5) {
                    textView5 = false;
                    text_view5.setBackground(getResources().getDrawable(R.drawable.boarded_with_ash_background, null));
                    text_view5.setTextColor(getResources().getColor(R.color.black));
                } else {
                    textView5 = true;
                    text_view5.setBackground(getResources().getDrawable(R.drawable.boarded_background, null));
                    text_view5.setTextColor(getResources().getColor(R.color.colorAccent));

                }
                keyWordsSet.add(text_view5.getText().toString());
            }
        });
        text_view6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView6) {
                    textView6 = false;
                    text_view6.setBackground(getResources().getDrawable(R.drawable.boarded_with_ash_background, null));
                    text_view6.setTextColor(getResources().getColor(R.color.black));
                } else {
                    textView6 = true;
                    text_view6.setBackground(getResources().getDrawable(R.drawable.boarded_background, null));
                    text_view6.setTextColor(getResources().getColor(R.color.colorAccent));

                }
                keyWordsSet.add(text_view6.getText().toString());
            }
        });

        txt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTipDialog.dismiss();
            }
        });
        txt_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTipContinue = true;
                if (!txt_total_tip.getText().toString().equals("0") && !txt_total_tip.getText().toString().isEmpty()) {
                    CallForGetCheckSum(txt_total_tip.getText().toString().replaceAll("[^a-zA-Z0-9]", ""));
                    // dialog.dismiss();
                } else {
                    callServiceForSendTip("", "", "", "", "", "", "", "", "", "", mParking_id);
                }
            }
        });
        // setImageSlider();
        sendTipDialog.show();
    }

    private void setAdapterForRatting() {

        if(rv_ratting!=null) {
            AdAdapter mAdAdapter = new AdAdapter(getActivity(), adsSubArrayList);
            rv_ratting.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            rv_ratting.setAdapter(mAdAdapter);
        }
    }

    private void CallForGetCheckSum(String amount) {
        if (Utility.isNetworkAvailable(getActivity())) {
            Utility.showLoadingDialog(getActivity(), "Loading...", false);
            String orderId = Utility.generateString();
            try {

                HashMap<String, String> params = new HashMap<>();
                params.put("MID", "mFgFdF43210827948920");
                params.put("ORDER_ID", orderId);
                params.put("CUST_ID", Utility.getSharedPrefStringData(getActivity(), Constants.USER_ID));
                params.put("INDUSTRY_TYPE_ID", "Retail");
                params.put("CHANNEL_ID", "WAP");
                params.put("TXN_AMOUNT", amount);
                params.put("WEBSITE", "WEBSTAGING");
                params.put("CALLBACK_URL", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId);
                params.put("MOBILE_NO", Utility.getSharedPrefStringData(getActivity(), Constants.MOBILE));
                params.put("EMAIL", Utility.getSharedPrefStringData(getActivity(), Constants.EMAIL));

                ServerResponse serverResponse = new ServerResponse();
                serverResponse.serviceRequest(getActivity(), Constants.REQUEST_FOR_CHECKSUM, params, this, Constants.REQUEST_FOR_CHECKSUM_CODE);


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            PopUtils.alertDialog(getActivity(), Utility.getResourcesString(getActivity(), R.string.check_internet), null);
        }
    }

    private void PayTm(PaytmParams params) {
        PaytmPGService service = null;
        service = PaytmPGService.getStagingService();

        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", params.getMID().trim());
        paramMap.put("ORDER_ID", params.getORDER_ID().trim());
        paramMap.put("CUST_ID", params.getCUST_ID().trim());
        paramMap.put("INDUSTRY_TYPE_ID", params.getINDUSTRY_TYPE_ID().trim());
        paramMap.put("CHANNEL_ID", params.getCHANNEL_ID().trim());
        paramMap.put("TXN_AMOUNT", params.getTXN_AMOUNT().trim());
        paramMap.put("WEBSITE", params.getWEBSITE().trim());
        paramMap.put("CALLBACK_URL", params.getCALLBACK_URL().trim());
        paramMap.put("MOBILE_NO", params.getMOBILE_NO());
        paramMap.put("EMAIL", params.getEMAIL());
        paramMap.put("CHECKSUMHASH", params.getCHECKSUM().trim());

        PaytmOrder order = new PaytmOrder((HashMap<String, String>) paramMap);
        service.initialize(order, null);
        service.startPaymentTransaction(getActivity(), true, true, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionResponse(Bundle inResponse) {
                //Toast.makeText(getActivity(), inResponse.getString("RESPCODE"), Toast.LENGTH_SHORT).show();
                String STATUS = inResponse.getString("STATUS");
                String CHECKSUM = inResponse.getString("CHECKSUMHASH");
                String ORDERID = inResponse.getString("ORDERID");
                String BANKNAME = inResponse.getString("BANKNAME");
                String TXNAMOUNT = inResponse.getString("TXNAMOUNT");
                String TXNDATE = inResponse.getString("TXNDATE");
                String MID = inResponse.getString("MID");
                String TXNID = inResponse.getString("TXNID");
                String RESPCODE = inResponse.getString("RESPCODE");
                String PAYMENTMODE = inResponse.getString("PAYMENTMODE");
                String BANKTXNID = inResponse.getString("BANKTXNID");
                String CURRENCY = inResponse.getString("CURRENCY");
                String GATEWAYNAME = inResponse.getString("GATEWAYNAME");
                String RESPMSG = inResponse.getString("RESPMSG");
                if (!STATUS.equals("TXN_FAILURE")) {
                    if (isTipContinue) {
                        callServiceForSendTip(ORDERID, TXNID, TXNAMOUNT, PAYMENTMODE, CURRENCY, TXNDATE, STATUS, GATEWAYNAME, BANKTXNID, BANKNAME, mParking_id);
                    } else {
                        callServiceForVipCarRequest("1", ORDERID, TXNID, TXNAMOUNT, PAYMENTMODE, CURRENCY, TXNDATE, STATUS, GATEWAYNAME, BANKTXNID, BANKNAME);
                    }
                    if (sendTipDialog != null) {
                        sendTipDialog.dismiss();
                    }
                    if (requestCarDialog != null) {
                        requestCarDialog.dismiss();
                    }
                } else {
                    sendTipDataClear();
                }
            }

            @Override
            public void networkNotAvailable() {
                Toast.makeText(getActivity(), "Network connection error: Check your internet connectivity", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void clientAuthenticationFailed(String inErrorMessage) {
                Toast.makeText(getActivity(), "Authentication failed: Server error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void someUIErrorOccurred(String inErrorMessage) {
                Toast.makeText(getActivity(), "UI Error " + inErrorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                Toast.makeText(getActivity(), "Unable to load webpage", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBackPressedCancelTransaction() {
                Toast.makeText(getActivity(), "Transaction not completed", Toast.LENGTH_SHORT).show();
                sendTipDataClear();

            }

            @Override
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                Toast.makeText(getActivity(), "Transaction Cancelled", Toast.LENGTH_SHORT).show();
                sendTipDataClear();
            }
        });

    }

    private void sendTipDataClear() {
        textView1 = false;
        textView2 = false;
        textView3 = false;
        textView4 = false;
        textView5 = false;
        textView6 = false;
        Toast.makeText(getActivity(), "Transaction not completed", Toast.LENGTH_SHORT).show();
        if (sendTipDialog != null) {
            txt_total_tip.setText("0");
            txt_amount1.setTextColor(getResources().getColor(R.color.colorAccent));
            txt_amount1.setBackground(getResources().getDrawable(R.drawable.boarded_background, null));
            txt_amount2.setTextColor(getResources().getColor(R.color.colorAccent));
            txt_amount2.setBackground(getResources().getDrawable(R.drawable.boarded_background, null));
            txt_amount3.setTextColor(getResources().getColor(R.color.colorAccent));
            txt_amount3.setBackground(getResources().getDrawable(R.drawable.boarded_background, null));
            rating_bar.setRating(1);

            text_view1.setBackground(getResources().getDrawable(R.drawable.boarded_with_ash_background, null));
            text_view1.setTextColor(getResources().getColor(R.color.black));

            text_view2.setBackground(getResources().getDrawable(R.drawable.boarded_with_ash_background, null));
            text_view2.setTextColor(getResources().getColor(R.color.black));

            text_view3.setBackground(getResources().getDrawable(R.drawable.boarded_with_ash_background, null));
            text_view3.setTextColor(getResources().getColor(R.color.black));

            text_view4.setBackground(getResources().getDrawable(R.drawable.boarded_with_ash_background, null));
            text_view4.setTextColor(getResources().getColor(R.color.black));

            text_view5.setBackground(getResources().getDrawable(R.drawable.boarded_with_ash_background, null));
            text_view5.setTextColor(getResources().getColor(R.color.black));

            text_view6.setBackground(getResources().getDrawable(R.drawable.boarded_with_ash_background, null));
            text_view6.setTextColor(getResources().getColor(R.color.black));
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
                    String cars_count = jsonObject.optString("");
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

                                UserCarModel userCarModel = new UserCarModel(user_id, user_car_id, qr, image, name, color, vehicle_no, car_status, is_requested, false,
                                brand_name,car_name,brand_image,direct_scan,last_used,car_id);
                                userCarModelArrayList.add(userCarModel);
                                PAGES = userCarModelArrayList.size();

                            }
                            setCarSlider();

                        }


                    } else if (status == 401) {
                        PopUtils.alertDialog(getActivity(), message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseApplication.getInstance().callServiceForLogOut();
                            }
                        });

                    } else {
                        if (message.contains("No cars added yet")) {
                            if (image_view_empty_car != null) {
                                image_view_empty_car.setVisibility(View.VISIBLE);
                                cars_pager.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.ADS_CODE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 200) {
                        adsMainArrayList.clear();
                        adsSubArrayList.clear();
                        JSONObject dataJsonObject = jsonObject.optJSONObject("data");
                        String banner_path = jsonObject.optString("banner_path");
                        if (dataJsonObject != null) {
                            JSONArray mainJsonArray = dataJsonObject.optJSONArray("main_banners");
                            JSONArray subJsonArray = dataJsonObject.optJSONArray("sub_banners");
                            if (mainJsonArray != null && mainJsonArray.length() > 0) {
                                for (int i = 0; i < mainJsonArray.length(); i++) {
                                    JSONObject jsonObject1 = mainJsonArray.optJSONObject(i);
                                    if(jsonObject1.optString("type").equalsIgnoreCase("valet")) {

                                        String sort_order = jsonObject1.optString("sort_order");
                                        String banner = banner_path + jsonObject1.optString("banner");

                                        AdsMain adsMain = new AdsMain(sort_order, banner);
                                        adsMainArrayList.add(adsMain);
                                    }
                                }
                            }
                            if (subJsonArray != null && subJsonArray.length() > 0) {
                                for (int i = 0; i < subJsonArray.length(); i++) {
                                    JSONObject jsonObject1 = subJsonArray.optJSONObject(i);
                                    if(jsonObject1.optString("type").equalsIgnoreCase("valet")) {

                                        String sort_order = jsonObject1.optString("sort_order");
                                        String banner = banner_path + jsonObject1.optString("banner");

                                        AdsSub adsSub = new AdsSub(sort_order, banner);
                                        adsSubArrayList.add(adsSub);
                                    }
                                }
                            }



                            setAdapter();
                            setImageSlider();
                        }

                    } else if (status == 401) {
                        PopUtils.alertDialog(getActivity(), message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseApplication.getInstance().callServiceForLogOut();
                            }
                        });

                    } else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case Constants.REQUEST_FOR_CAR_CODE:
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 200) {

                        new RequestCar(
                                jsonObject.optString("message"),
                                jsonObject.optString("amount"),
                                jsonObject.optString("credits")
                        );
                        mRequestCar = new RequestCar(jsonObject.optString("message"), jsonObject.optString("credits"), jsonObject.optString("amount"));

                        dialogForRequestCar();

                    } else if (status == 401) {
                        PopUtils.alertDialog(getActivity(), message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseApplication.getInstance().callServiceForLogOut();
                            }
                        });

                    } else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
            case Constants.REQUEST_FOR_CHECKSUM_CODE:
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        int status = jsonObject.optInt("status");
                        String message = jsonObject.optString("message");
                        if (status == 200) {

                            String checkSum = jsonObject.optString("checksum");
                            JSONObject jsonArray = jsonObject.optJSONObject("paytmParams");

                            if (jsonArray != null && jsonArray.length() > 0) {


                                String CUST_ID = jsonArray.optString("CUST_ID");
                                String MOBILE_NO = jsonArray.optString("MOBILE_NO");
                                String CHANNEL_ID = jsonArray.optString("CHANNEL_ID");
                                String ORDER_ID = jsonArray.optString("ORDER_ID");
                                String TXN_AMOUNT = jsonArray.optString("TXN_AMOUNT");
                                String CALLBACK_URL = jsonArray.optString("CALLBACK_URL");
                                String MID = jsonArray.optString("MID");
                                String INDUSTRY_TYPE_ID = jsonArray.optString("INDUSTRY_TYPE_ID");
                                String EMAIL = jsonArray.optString("EMAIL");
                                String WEBSITE = jsonArray.optString("WEBSITE");
                                params = new PaytmParams(CUST_ID, MOBILE_NO, CHANNEL_ID, ORDER_ID, TXN_AMOUNT, CALLBACK_URL,
                                        MID, INDUSTRY_TYPE_ID, EMAIL, WEBSITE, checkSum);


                            }
                            PayTm(params);
                        } else if (status == 400) {
                            Utility.showToastMessage(getActivity(), message);
                        } else if (status == 401) {
                            PopUtils.alertDialog(getActivity(), message, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    BaseApplication.getInstance().callServiceForLogOut();
                                }
                            });

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Constants.REQUEST_FOR_VIP_CAR_CODE:
                Log.e("VIP_CAR_REQUEST", "RESPONCE: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    if (status.equals("true")) {
                        if (isVeezeeClicked) {
                            /*PopUtils.alertDialog(mContext, message, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    callForCarStatus(USER_CAR_ID);
                                }
                            });*/
                            setDialogforViewCarStatus(message);
                            Log.e("VIP1", "VIP1");
                            int walletAmount = jsonObject.optInt("wallet_balance");
                            Log.e("CREDITD", "SuccessResponse: requestCar"+walletAmount );
                            Utility.setSharedPrefStringData(getActivity(), Constants.WALLET_AMOUNT, "" + walletAmount);
                            text_view_wallet_amount.setText(Utility.getSharedPrefStringData(getActivity(), Constants.WALLET_AMOUNT));
                        } else {
                            Log.e("VIP2", "VIP2");
                           // PopUtils.alertDialog(getActivity(), message, null);
                            setDialogforViewCarStatus(message);
                        }
                        if (requestCarDialog != null) {
                            requestCarDialog.dismiss();
                        }
                        callServiceForuserCars();
                        navigationTag = "FRAGMENT";
                    } else {
                        PopUtils.alertDialog(getActivity(), message, null);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case Constants.SEND_TIP_CODE:
                Log.e("SENDTIP", "SENDTIP" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    if (status.equals("true")) {
                        //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        int walletAmount = jsonObject.optInt("wallet_balance");
                        if(!message.equalsIgnoreCase("Rating submitted successfully")) {
                            Utility.setSharedPrefStringData(getActivity(), Constants.WALLET_AMOUNT, "" + walletAmount);
                            text_view_wallet_amount.setText("" + walletAmount);
                            Log.e("CREDITS", "SuccessResponse tip: "+walletAmount );
                        }
                        PopUtils.alertDialog(getActivity(), message, null);
                        sendTipDialog.dismiss();
                    } else {
                        PopUtils.alertDialog(getActivity(), message, null);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case Constants.REQUEST_CAR_STATUS_CODE:
                Log.e("CAR_STATUS", "CAR_STATUS" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    if (status.equals("200")) {
                        JSONObject object = jsonObject.optJSONObject("data");
                        if (object != null && object.length() > 0) {
                          /* for (int i = 0; i < jsonArray.length(); i++) {
                               JSONObject jsonObject1 = jsonArray.optJSONObject(i);*/
                            car_status = object.optString("car_status");
                            String token_number = object.optString("token_number");
                            String driver_number = object.optString("driver_number");
                            String car_image = object.optString("car_image");
                            String car_number = object.optString("car_number");
                            String timer = object.optString("timer");
                            String statusMessage = object.optString("status");
                            String car_id = object.optString("user_car_id");

                            mCarStatus = new CarStatus(car_status, token_number, driver_number, car_image, car_number, timer, statusMessage,car_id);


                        }
                        if (!isRefreshed ) {
                            dialogForCars();
                            isRefreshed = true;
                        } else {
                          //  txt_noOfcars.setText(mCarStatus.getCar_status());
                        }

                       // dialogForCars();
                    } else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;


        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
       /* if (sendTipDialog != null) {
            sendTipDialog.dismiss();
        }*/
        if(carStatusDialog!=null){
            carStatusDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sendTipDialog != null) {
            sendTipDialog.dismiss();
        }
        if (requestCarDialog != null) {
            requestCarDialog.dismiss();
        }
        try {

            if (receiver != null) {
                getActivity().unregisterReceiver(receiver);
            }
        } catch (IllegalArgumentException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    //    Toast.makeText(getActivity(), "Hello", Toast.LENGTH_SHORT).show();

        callServiceForuserCars();


        String user_credits = Utility.getSharedPrefStringData(getActivity(), Constants.WALLET_AMOUNT);
        if(!user_credits.isEmpty() && user_credits!=null &&! user_credits.equals("") ) {

            text_view_wallet_amount.setText(user_credits);
        }
        Log.e("CREDITS", "onResume: "+ user_credits );
        String userImage = Utility.getSharedPrefStringData(getActivity(), Constants.IMAGE);
        if (!userImage.equals("") || !userImage.isEmpty()) {
            Picasso.get().load(Utility.getSharedPrefStringData(getActivity(), Constants.IMAGE)).placeholder(R.drawable.ic_user).into(img_user_menu);
        }
        if (!pager_images.isCycle()) {
            pager_images.startAutoScroll();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(String s) {
        /*if(s.equals("CarParked")){
            callServiceForuserCars();
            navigationTag = "FRAGMENT";
        }*/
    }

    public void onShareClick(View v) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");

        share.putExtra(Intent.EXTRA_SUBJECT, "VeeZee");
       // share.putExtra(Intent.EXTRA_TEXT, Qr);
        share.putExtra(Intent.EXTRA_TEXT,"Greetings from VeeZee!\n" +
                Utility.getSharedPrefStringData(getActivity(),Constants.NAME)+" has shared the valet ticket for "+userCarModelArrayList.get(currentcarpostn).getCar_name() + " with Registration Number "+userCarModelArrayList.get(currentcarpostn).getVehicle_no()+". Please use the following link to download the unique Valet Ticket QR Code.\n" +
                "Valet QR Code Link\n"+ Html.fromHtml(Constants.ShareQr));

        startActivity(Intent.createChooser(share, "Share link!"));

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String credits = intent.getStringExtra("update_credits");
            String  status = intent.getStringExtra("status");
            if (credits != null) {
                String user_credits = Utility.getSharedPrefStringData(getActivity(), Constants.WALLET_AMOUNT);
                text_view_wallet_amount.setText(user_credits);
                Log.e("CREDITS", "onReceive: "+user_credits );
            }else if(status!=null && !status.isEmpty() && status.equalsIgnoreCase("direct_scan")) {
                callServiceForuserCars();

            }else {

                if (!broadcastreceived) {
                    broadcastreceived = true;

                    Intent i = new Intent(getActivity(), MainActivity.class);
                    i.putExtra("TAG","VALET");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    Log.e("### CARSTATUS_PUSH", "RECIEVED BROADACAST");
                    String s = intent.getStringExtra("CARSTATUS_PUSH");
                    int arriviedCar_id = Integer.parseInt(intent.getStringExtra("user_car_id"));
                    String timer = intent.getStringExtra("timer");
                    Log.e("### CARSTATUS_PUSH", s);

                    if (s.equalsIgnoreCase("arrived")) {


                        Map<Integer, String> map = Utility.getMaxTime(mContext);
                        map.put(arriviedCar_id, "requested");
                        Utility.setMaxTime(map, mContext);


                        try {

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
                            String currenttime = simpleDateFormat.format(new Date());
                            Log.e("### current time", currenttime);
                            Date timme = simpleDateFormat.parse(currenttime);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(timme);
                            cal.add(Calendar.MINUTE, Integer.parseInt(timer));
                            String addedTime = simpleDateFormat.format(cal.getTime());

                            Log.e("### new time", addedTime);
                            Log.e("### REQUESTED CAR POSITION", arriviedCar_id + "");
                            Log.e("### INSTANCE PREFERENCE", Utility.getMaxTime(mContext) + "");

                            if (Utility.getMaxTime(mContext).get(arriviedCar_id) == null ||
                                    (Utility.getMaxTime(mContext).get(arriviedCar_id) != null && Utility.getMaxTime(mContext).get(arriviedCar_id).contains("Completed"))
                                    || (Utility.getMaxTime(mContext).get(arriviedCar_id) != null && Utility.getMaxTime(mContext).get(arriviedCar_id).contains("requested"))) {
                                max_times_map = Utility.getMaxTime(mContext);
                                max_times_map.put(arriviedCar_id, addedTime);
                                Utility.setMaxTime(max_times_map, mContext);
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else if (s.equalsIgnoreCase("PARKED")) {

                    }
                }
            }
        }
    };

}
