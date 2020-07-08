package com.versatilemobitech.VeeZee.Fragments;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;
import com.versatilemobitech.VeeZee.Activities.MainActivity;
import com.versatilemobitech.VeeZee.Activities.VeeZeeCreditsActivity;
import com.versatilemobitech.VeeZee.Adapters.AdAdapter;
import com.versatilemobitech.VeeZee.Adapters.homeImageSliderAdapter;
import com.versatilemobitech.VeeZee.BaseApplication;
import com.versatilemobitech.VeeZee.Model.AdsMain;
import com.versatilemobitech.VeeZee.Model.AdsSub;
import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.UtilHelpers.AutoScrollViewPager;
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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class VaultFragment extends Fragment implements View.OnClickListener, IParseListener {
    private MainActivity mContext;
    private View view;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    TextView toolBarTitle, txt_share, txt_credits, text_view_v,text_view_wallet_amount;
    ImageView img_user_menu, img_credits, img_share, image_view_qr,img_veezee;
    AutoScrollViewPager pager_images;
    Timer timer;
    final long DELAY_MS = 1500;
    final long PERIOD_MS = 2500;
    int currentPage = 0;
    ViewGroup mViewGrop;
    Dialog dialog, dialog1;
    RecyclerView rv_adds;

    ArrayList<AdsMain> adsMainArrayList = new ArrayList<>();
    ArrayList<AdsSub> adsSubArrayList = new ArrayList<>();


    @SuppressLint("ValidFragment")
    public VaultFragment(MainActivity activity) {
        mContext = activity;
    }

    public VaultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_vault, container, false);
        init();
        mViewGrop = container;
        Toolbar toolbar = ((MainActivity) getActivity()).findViewById(R.id.toolBar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callServiceForAds();
    }

    private void init() {
        setReferences();

        //setDialogForReport();

        setClickListeners();
    }

    private void callServiceForAds() {
        if (Utility.isNetworkAvailable(getActivity())) {
            Utility.showLoadingDialog(getActivity(), "Loading...", false);
            JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.put("type ", "vault");

            }catch (Exception e){
                e.printStackTrace();
            }
            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(getActivity(), "GET", Constants.ADS_URL, jsonObject, new HashMap<String, String>(), this, Constants.ADS_CODE);

        } else {
            PopUtils.alertDialog(getActivity(), Utility.getResourcesString(getActivity(), R.string.check_internet), null);
        }
    }

    private void setClickListeners() {
        img_share.setOnClickListener(this);
        img_credits.setOnClickListener(this);
    }

    private void setReferences() {
        img_user_menu = ((MainActivity) getActivity()).findViewById(R.id.img_user_menu);
        img_credits = ((MainActivity) getActivity()).findViewById(R.id.img_credits);
        txt_share = ((MainActivity) getActivity()).findViewById(R.id.txt_share);
        toolBarTitle = ((MainActivity) getActivity()).findViewById(R.id.toolBarTitle);
        txt_credits = ((MainActivity) getActivity()).findViewById(R.id.txt_credits);
        text_view_v = ((MainActivity) getActivity()).findViewById(R.id.text_view_credits_v);
        text_view_wallet_amount = ((MainActivity)getActivity()).findViewById(R.id.text_view_wallet_amount);

        img_veezee = ((MainActivity)getActivity()).findViewById(R.id.img_veezee);
        img_veezee.setVisibility(View.VISIBLE);

        pager_images = view.findViewById(R.id.pager_images);
        sliderDotspanel = view.findViewById(R.id.SliderDots);
        image_view_qr = view.findViewById(R.id.image_view_qr);
        rv_adds = view.findViewById(R.id.rv_adds);
        img_share = view.findViewById(R.id.img_share);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        img_user_menu.setVisibility(View.VISIBLE);
        toolBarTitle.setVisibility(View.INVISIBLE);
        img_credits.setVisibility(View.GONE);
        txt_credits.setVisibility(View.GONE);
        text_view_v.setVisibility(View.VISIBLE);
        img_credits.setImageResource(R.drawable.ic_credits);
        txt_share.setVisibility(View.GONE);

        (getActivity()).findViewById(R.id.text_view_wallet_amount).setVisibility(View.VISIBLE);
        (getActivity()).findViewById(R.id.image_view_coin).setVisibility(View.VISIBLE);

        toolBarTitle.setTextColor(getResources().getColor(R.color.black));
        toolBarTitle.setGravity(Gravity.CENTER);

        if(Utility.getSharedPrefStringData(getActivity(),Constants.QR)!=null&&!Utility.getSharedPrefStringData(getActivity(),Constants.QR).isEmpty()) {
            Picasso.get().load(Utility.getSharedPrefStringData(getActivity(), Constants.QR)).into(image_view_qr);
            Constants.ShareQr = Utility.getSharedPrefStringData(getActivity(), Constants.QR);

        }
    }

    private void setAdapter() {
        AdAdapter mAdAdapter = new AdAdapter(getActivity(), adsSubArrayList);
        rv_adds.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_adds.setAdapter(mAdAdapter);
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


        pager_images.setAdapter(viewPagerAdapter);
        dotscount = viewPagerAdapter.getCount();
        autoScroll();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));
        pager_images.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.non_active_dot));
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
                if (currentPage ==adsMainArrayList.size() ) {
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

    public void onShareClick(View v) {

        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        share.putExtra(Intent.EXTRA_SUBJECT, "VeeZee");
        //share.putExtra(Intent.EXTRA_TEXT, Utility.getSharedPrefStringData(getActivity(),Constants.QR));
//        share.putExtra(Intent.EXTRA_TEXT,"Greetings from VeeZee!\n" +
//                Utility.getSharedPrefStringData(getActivity(),Constants.NAME)+" has shared the vault ticket for Qr "+Html.fromHtml(Utility.getSharedPrefStringData(getActivity(),Constants.QR))+ ". Please use the following link to download the unique Vault Ticket QR Code.\n" +
//                "Vault QR Code Link\n");
        share.putExtra(Intent.EXTRA_TEXT,""+Html.fromHtml(Utility.getSharedPrefStringData(getActivity(),Constants.QR)));
        startActivity(Intent.createChooser(share, "Share link!"));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_share:
                onShareClick(view);
                break;
            case R.id.img_credits:
                startActivity(new Intent(getActivity(), VeeZeeCreditsActivity.class));
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
                                    if(jsonObject1.optString("type").equalsIgnoreCase("vault")) {

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
                                    if(jsonObject1.optString("type").equalsIgnoreCase("vault")) {

                                        String sort_order = jsonObject1.optString("sort_order");
                                        String banner = banner_path + jsonObject1.optString("banner");

                                        AdsSub adsSub = new AdsSub(sort_order, banner);
                                        adsSubArrayList.add(adsSub);
                                    }
                                }
                            }


                            setImageSlider();
                            setAdapter();
                        }

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
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        String user_credits = Utility.getSharedPrefStringData(getActivity(), Constants.WALLET_AMOUNT);
        text_view_wallet_amount.setText(user_credits);
        String userImage = Utility.getSharedPrefStringData(getActivity(), Constants.IMAGE);
        if (!userImage.equals("") || !userImage.isEmpty()) {
            Picasso.get().load(Utility.getSharedPrefStringData(getActivity(), Constants.IMAGE)).placeholder(R.drawable.ic_user).into(img_user_menu);
        }
        if (!pager_images.isCycle()) {
            pager_images.startAutoScroll();
        }
    }
}
