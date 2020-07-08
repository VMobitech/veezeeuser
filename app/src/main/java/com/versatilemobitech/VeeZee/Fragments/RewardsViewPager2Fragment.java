package com.versatilemobitech.VeeZee.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.versatilemobitech.VeeZee.Activities.MainActivity;
import com.versatilemobitech.VeeZee.Activities.MyRewardsActivity;
import com.versatilemobitech.VeeZee.Adapters.NewAdapter;
import com.versatilemobitech.VeeZee.Adapters.RewardsAdapter;
import com.versatilemobitech.VeeZee.BaseApplication;
import com.versatilemobitech.VeeZee.Model.RewardBrand;
import com.versatilemobitech.VeeZee.Model.Rewards;
import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.WebUtils.ServerResponse;
import com.versatilemobitech.VeeZee.interfaces.IParseListener;
import com.versatilemobitech.VeeZee.others.SnappyRecyclerView;
import com.versatilemobitech.VeeZee.others.SpeedyLinearLayoutManager;
import com.versatilemobitech.VeeZee.utils.Constants;
import com.versatilemobitech.VeeZee.utils.PopUtils;
import com.versatilemobitech.VeeZee.utils.Utility;
import com.versatilemobitech.VeeZee.utils.VerticalViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class RewardsViewPager2Fragment extends Fragment implements IParseListener , View.OnClickListener {
    private View view;
    TextView toolBarTitle, txt_credits, txt_share, text_view_v;
    ImageView img_credits, img_user_menu, image_view_food, image_view_shop, image_view_movie,
            image_view_run, image_view_travel, image_view_infinity,img_veezee;
    TextView text_view_nodata;

    ArrayList<Rewards> rewardsArrayList = new ArrayList<>();
    ViewPager2 viewPager;

    RecyclerView rcvRewards;




    ArrayList<RewardBrand> rewardBrandArrayList = new ArrayList<>();

    public RewardsViewPager2Fragment() {
        // Required empty public constructor
    }
    public RewardsViewPager2Fragment newInstance() {
        RewardsViewPager2Fragment fragment = new RewardsViewPager2Fragment();
        Bundle bundle = new Bundle();


        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_rewards_view_pager2, container, false);

        initView();
        return view;

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callServiceForRewardBrands();

    }

    private void callServiceForRewardBrands() {
        if (Utility.isNetworkAvailable(getActivity())) {
            // Utility.showLoadingDialog(getActivity(), "Loading...", false);

            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(getActivity(), "GET", Constants.REWARDS_BRANDS, new JSONObject(), new HashMap<String, String>(), this, Constants.REWARDS_BRAND_CODE);

        } else {
            PopUtils.alertDialog(getActivity(), Utility.getResourcesString(getActivity(), R.string.check_internet), null);
        }

    }

    private void callServiceForRewards(String categoryId) {
        if (Utility.isNetworkAvailable(getActivity())) {
            Utility.showLoadingDialog(getActivity(), "Loading...", false);

            /*ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(getActivity(), "GET", Constants.REWARDS_LIST, new JSONObject(), new HashMap<String, String>(), this, Constants.REWARDS_CODE);*/

            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(getActivity(), "GET", Constants.REWARDS_LIST + "?category_id=" + categoryId, new JSONObject(), new HashMap<String, String>(), this, Constants.REWARDS_CODE);

        } else {
            PopUtils.alertDialog(getActivity(), Utility.getResourcesString(getActivity(), R.string.check_internet), null);
        }
    }

    private void initView() {
        toolBarTitle = ((MainActivity) getActivity()).findViewById(R.id.toolBarTitle);
        txt_credits = ((MainActivity) getActivity()).findViewById(R.id.txt_credits);
        txt_credits.setVisibility(View.GONE);
        txt_share = ((MainActivity) getActivity()).findViewById(R.id.txt_share);

        img_credits = ((MainActivity) getActivity()).findViewById(R.id.img_credits);
        img_credits.setVisibility(View.GONE);

        Toolbar toolbar = ((MainActivity) getActivity()).findViewById(R.id.toolBar);
        toolbar.setVisibility(View.VISIBLE);


        (getActivity()).findViewById(R.id.text_view_wallet_amount).setVisibility(View.GONE);
        (getActivity()).findViewById(R.id.text_view_credits_v).setVisibility(View.GONE);
        (getActivity()).findViewById(R.id.image_view_coin).setVisibility(View.GONE);


        text_view_v = ((MainActivity) getActivity()).findViewById(R.id.text_view_credits_v);
        text_view_v.setVisibility(View.GONE);

        toolBarTitle.setVisibility(View.VISIBLE);
        toolBarTitle.setText("Rewards");
        toolBarTitle.setTextColor(getResources().getColor(R.color.black));
        toolBarTitle.setGravity(Gravity.LEFT);
        toolBarTitle.setPadding(30, 0, 0, 0);


        img_user_menu = ((MainActivity) getActivity()).findViewById(R.id.img_user_menu);
        img_user_menu.setVisibility(View.GONE);

        img_veezee = ((MainActivity)getActivity()).findViewById(R.id.img_veezee);
        img_veezee.setVisibility(View.GONE);

        txt_share.setVisibility(View.VISIBLE);
        txt_share.setText(R.string.my_rewards);
        txt_share.setTextSize(12);


        txt_share.setTextColor(getResources().getColor(R.color.colorAccent));
        txt_share.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.boarded_background));

        viewPager = view.findViewById(R.id.view_pager2);
        //viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        //viewPager.setVerticalScrollBarEnabled(true);

        image_view_food = view.findViewById(R.id.image_view_food);
        image_view_shop = view.findViewById(R.id.image_view_shop);
        image_view_infinity = view.findViewById(R.id.image_view_infinity);
        image_view_movie = view.findViewById(R.id.image_view_movie);
        image_view_run = view.findViewById(R.id.image_view_run);
        image_view_travel = view.findViewById(R.id.image_view_travel);
        text_view_nodata = view.findViewById(R.id.text_view_nodata);

        rcvRewards = view.findViewById(R.id.rcvRewards);


        /*image_view_food.setVisibility(View.GONE);
        image_view_shop.setVisibility(View.GONE);
        image_view_movie.setVisibility(View.GONE);
        image_view_run.setVisibility(View.GONE);
        image_view_travel.setVisibility(View.GONE);
        image_view_infinity.setVisibility(View.GONE);*/

        image_view_food.setOnClickListener(RewardsViewPager2Fragment.this);
        image_view_shop.setOnClickListener(RewardsViewPager2Fragment.this);
        image_view_movie.setOnClickListener(RewardsViewPager2Fragment.this);
        image_view_run.setOnClickListener(RewardsViewPager2Fragment.this);
        image_view_travel.setOnClickListener(RewardsViewPager2Fragment.this);
        image_view_infinity.setOnClickListener(RewardsViewPager2Fragment.this);

        txt_share.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);

        LinearSnapHelper linearSnapHelper = new SnapHelperOneByOne();
        linearSnapHelper.attachToRecyclerView(rcvRewards);

//        SnappyRecyclerView snappyRecyclerView = new SnappyRecyclerView(getActivity());
//        snappyRecyclerView.setScrollBarDefaultDelayBeforeFade(1);

        rcvRewards.setNestedScrollingEnabled(false);
        rcvRewards.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManagernew = new LinearLayoutManager(getActivity())
        {

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getActivity()) {

                    private static final float SPEED =30000f;// Change this value (default=25f)
                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }

                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }

        };



        // ((RecyclerView)viewPager.getChildAt(0)).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        //viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        /*viewPager.setNestedScrollingEnabled(false);
        viewPager.setVerticalScrollBarEnabled(true);*/

       /* viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*/

       /* viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                viewPager.getParent().requestDisallowInterceptTouchEvent(true);
            }
        });*/

    }


    @Override
    public void ErrorResponse(String response, int requestCode) {
        Utility.hideLoadingDialog();
        PopUtils.alertDialog(getActivity(), Utility.getResourcesString(getActivity(), R.string.something_went_wrong), null);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void SuccessResponse(String response, int requestCode) {
        Utility.hideLoadingDialog();
        switch (requestCode) {
            case Constants.REWARDS_CODE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 200) {
                        rewardsArrayList.clear();
                        JSONObject dataJsonObject = jsonObject.optJSONObject("data");
                        String imagePath = jsonObject.optString("image_path");
                        if (dataJsonObject != null) {
                            JSONArray rewardsJsonArray = dataJsonObject.optJSONArray("rewards");
                            if (rewardsJsonArray != null && rewardsJsonArray.length() > 0) {
                                for (int i = 0; i < rewardsJsonArray.length(); i++) {
                                    JSONObject jsonObject1 = rewardsJsonArray.optJSONObject(i);

                                    String reward_id = jsonObject1.optString("reward_id");
                                    String title = jsonObject1.optString("title");
                                    String voucher_code = jsonObject1.optString("voucher_code");
                                    String brand_id = jsonObject1.optString("brand_id");
                                    String description = jsonObject1.optString("description");
                                    String credits = jsonObject1.optString("credits");
                                    String image = imagePath + jsonObject1.optString("image");
                                    String valid_from = jsonObject1.optString("valid_from");
                                    String valid_to = jsonObject1.optString("valid_to");
                                    String whats_inside = jsonObject1.optString("whats_inside");
                                    String website = jsonObject1.optString("website");
                                    String created_by = jsonObject1.optString("created_by");
                                    String created_time = jsonObject1.optString("created_time");
                                    String modified_by = jsonObject1.optString("modified_by");
                                    String modified_time = jsonObject1.optString("modified_time");
                                    String status1 = jsonObject1.optString("status");
                                    String logo = imagePath + jsonObject1.optString("logo");
                                    String  brand = jsonObject1.optString("brand");
                                    String howToRedeem = jsonObject1.optString("how_to_redeem");
                                    String note = jsonObject1.optString("note");

                                    Rewards rewards = new Rewards(reward_id, title, voucher_code, brand_id, description,
                                            credits, image, valid_from, valid_to, whats_inside, website, created_by, created_time,
                                            modified_by, modified_time, status1, logo,brand,howToRedeem,note);

                                    rewardsArrayList.add(rewards);
                                }

                                text_view_nodata.setVisibility(View.GONE);
                               // viewPager.setVisibility(View.VISIBLE);
                                rcvRewards.setVisibility(View.VISIBLE);

                                NewAdapter rewardsAdapter = new NewAdapter(getActivity(), rewardsArrayList);
                               // viewPager.setAdapter(rewardsAdapter);
                                rcvRewards.setAdapter(rewardsAdapter);



                               /* viewPager.setOnTouchListener(new View.OnTouchListener() {
                                    public boolean onTouch(View v, MotionEvent e) {
                                        // How far the user has to scroll before it locks the parent vertical scrolling.
                                        final int margin = 5;
                                        final int fragmentOffset = v.getScrollX() % v.getWidth();

                                        if (fragmentOffset > margin && fragmentOffset < v.getWidth() - margin) {
                                            viewPager.getParent().requestDisallowInterceptTouchEvent(true);
                                        }
                                        return false;
                                    }
                                });*/
                            }
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
                        text_view_nodata.setText(message);
                        viewPager.setVisibility(View.GONE);
                        rcvRewards.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.REWARDS_BRAND_CODE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 200) {
                        rewardBrandArrayList.clear();
                        JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                        JSONArray jsonArray = jsonObject1.optJSONArray("category");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject2 = jsonArray.optJSONObject(i);

                                String category_id = jsonObject2.optString("category_id");
                                String name = jsonObject2.optString("name");
                                String logo = jsonObject.optString("image_path") + jsonObject2.optString("logo");
                                String logo_selected = jsonObject.optString("image_path") + jsonObject2.optString("logo_selected");
                                RewardBrand rewardBrand = new RewardBrand(category_id, name, logo, logo_selected);
                                rewardBrandArrayList.add(rewardBrand);

                                if (i == 0) {
                                    image_view_food.setVisibility(View.VISIBLE);
                                    Picasso.get().load(logo_selected).placeholder(R.drawable.ic_restaurant_cutlery).into(image_view_food);
                                } else if (i == 1) {
                                    image_view_shop.setVisibility(View.VISIBLE);
                                    Picasso.get().load(logo).placeholder(R.drawable.ic_shopping_bag).into(image_view_shop);
                                } else if (i == 2) {
                                    image_view_movie.setVisibility(View.VISIBLE);
                                    Picasso.get().load(logo).placeholder(R.drawable.ic_movie_clapper).into(image_view_movie);
                                } else if (i == 3) {
                                    image_view_run.setVisibility(View.VISIBLE);
                                    Picasso.get().load(logo).placeholder(R.drawable.ic_running).into(image_view_run);
                                } else if (i == 4) {
                                    image_view_travel.setVisibility(View.VISIBLE);
                                    Picasso.get().load(logo).placeholder(R.drawable.ic_traveler_with_a_suitcase).into(image_view_travel);
                                } else if (i == 5) {
                                    image_view_infinity.setVisibility(View.VISIBLE);
                                    Picasso.get().load(logo).placeholder(R.drawable.ic_infinity).into(image_view_infinity);
                                }


                            }
                            // Picasso.get().load(rewardBrandArrayList.get(0).getSelectedLogo()).placeholder(R.drawable.veezee_logo).into(image_view_food);
                            callServiceForRewards(rewardBrandArrayList.get(0).getCategory_id());
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

        }

    }


    @Override
    public void onClick(View view) {
        if(rewardBrandArrayList!=null&&rewardBrandArrayList.size()>0) {
            switch (view.getId()) {

                case R.id.txt_share:
                    Intent intent = new Intent(getActivity(), MyRewardsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.image_view_food:
                    RewardBrand brand0 = rewardBrandArrayList.get(0);
                    Picasso.get().load(brand0.getSelectedLogo()).placeholder(R.drawable.ic_restaurant_cutlery).into(image_view_food);
                    Picasso.get().load(rewardBrandArrayList.get(1).getLogo()).placeholder(R.drawable.ic_shopping_bag).into(image_view_shop);
                    Picasso.get().load(rewardBrandArrayList.get(2).getLogo()).placeholder(R.drawable.ic_movie_clapper).into(image_view_movie);
                    Picasso.get().load(rewardBrandArrayList.get(3).getLogo()).placeholder(R.drawable.ic_running).into(image_view_run);
                    Picasso.get().load(rewardBrandArrayList.get(4).getLogo()).placeholder(R.drawable.ic_traveler_with_a_suitcase).into(image_view_travel);
                    Picasso.get().load(rewardBrandArrayList.get(5).getLogo()).placeholder(R.drawable.ic_infinity).into(image_view_infinity);
                    callServiceForRewards(rewardBrandArrayList.get(0).getCategory_id());
                    break;
                case R.id.image_view_shop:

                    RewardBrand brand1 = rewardBrandArrayList.get(1);
                    Picasso.get().load(brand1.getSelectedLogo()).placeholder(R.drawable.ic_shopping_bag).into(image_view_shop);
                    Picasso.get().load(rewardBrandArrayList.get(0).getLogo()).placeholder(R.drawable.ic_restaurant_cutlery).into(image_view_food);
                    Picasso.get().load(rewardBrandArrayList.get(2).getLogo()).placeholder(R.drawable.ic_movie_clapper).into(image_view_movie);
                    Picasso.get().load(rewardBrandArrayList.get(3).getLogo()).placeholder(R.drawable.ic_running).into(image_view_run);
                    Picasso.get().load(rewardBrandArrayList.get(4).getLogo()).placeholder(R.drawable.ic_traveler_with_a_suitcase).into(image_view_travel);
                    Picasso.get().load(rewardBrandArrayList.get(5).getLogo()).placeholder(R.drawable.ic_infinity).into(image_view_infinity);
                    callServiceForRewards(rewardBrandArrayList.get(1).getCategory_id());
                    break;
                case R.id.image_view_movie:
                    RewardBrand brand2 = rewardBrandArrayList.get(2);
                    Picasso.get().load(rewardBrandArrayList.get(1).getLogo()).placeholder(R.drawable.ic_shopping_bag).into(image_view_shop);
                    Picasso.get().load(rewardBrandArrayList.get(0).getLogo()).placeholder(R.drawable.ic_restaurant_cutlery).into(image_view_food);
                    Picasso.get().load(rewardBrandArrayList.get(3).getLogo()).placeholder(R.drawable.ic_running).into(image_view_run);
                    Picasso.get().load(rewardBrandArrayList.get(4).getLogo()).placeholder(R.drawable.ic_traveler_with_a_suitcase).into(image_view_travel);
                    Picasso.get().load(rewardBrandArrayList.get(5).getLogo()).placeholder(R.drawable.ic_infinity).into(image_view_infinity);
                    Picasso.get().load(brand2.getSelectedLogo()).placeholder(R.drawable.ic_movie_clapper).into(image_view_movie);
                    callServiceForRewards(rewardBrandArrayList.get(2).getCategory_id());
                    break;
                case R.id.image_view_run:
                    RewardBrand brand3 = rewardBrandArrayList.get(3);
                    Picasso.get().load(rewardBrandArrayList.get(1).getLogo()).placeholder(R.drawable.ic_shopping_bag).into(image_view_shop);
                    Picasso.get().load(rewardBrandArrayList.get(2).getLogo()).placeholder(R.drawable.ic_movie_clapper).into(image_view_movie);
                    Picasso.get().load(rewardBrandArrayList.get(0).getLogo()).placeholder(R.drawable.ic_restaurant_cutlery).into(image_view_food);
                    Picasso.get().load(rewardBrandArrayList.get(4).getLogo()).placeholder(R.drawable.ic_traveler_with_a_suitcase).into(image_view_travel);
                    Picasso.get().load(rewardBrandArrayList.get(5).getLogo()).placeholder(R.drawable.ic_infinity).into(image_view_infinity);
                    Picasso.get().load(brand3.getSelectedLogo()).placeholder(R.drawable.ic_running).into(image_view_run);
                    callServiceForRewards(rewardBrandArrayList.get(3).getCategory_id());
                    break;
                case R.id.image_view_travel:
                    RewardBrand brand4 = rewardBrandArrayList.get(4);
                    Picasso.get().load(rewardBrandArrayList.get(1).getLogo()).placeholder(R.drawable.ic_shopping_bag).into(image_view_shop);
                    Picasso.get().load(rewardBrandArrayList.get(2).getLogo()).placeholder(R.drawable.ic_movie_clapper).into(image_view_movie);
                    Picasso.get().load(rewardBrandArrayList.get(3).getLogo()).placeholder(R.drawable.ic_running).into(image_view_run);
                    Picasso.get().load(rewardBrandArrayList.get(0).getLogo()).placeholder(R.drawable.ic_restaurant_cutlery).into(image_view_food);
                    Picasso.get().load(rewardBrandArrayList.get(5).getLogo()).placeholder(R.drawable.ic_infinity).into(image_view_infinity);
                    Picasso.get().load(brand4.getSelectedLogo()).placeholder(R.drawable.ic_traveler_with_a_suitcase).into(image_view_travel);
                    callServiceForRewards(rewardBrandArrayList.get(4).getCategory_id());
                    break;
                case R.id.image_view_infinity:
                    RewardBrand brand5 = rewardBrandArrayList.get(5);
                    Picasso.get().load(rewardBrandArrayList.get(1).getLogo()).placeholder(R.drawable.ic_shopping_bag).into(image_view_shop);
                    Picasso.get().load(rewardBrandArrayList.get(2).getLogo()).placeholder(R.drawable.ic_movie_clapper).into(image_view_movie);
                    Picasso.get().load(rewardBrandArrayList.get(3).getLogo()).placeholder(R.drawable.ic_running).into(image_view_run);
                    Picasso.get().load(rewardBrandArrayList.get(4).getLogo()).placeholder(R.drawable.ic_traveler_with_a_suitcase).into(image_view_travel);
                    Picasso.get().load(rewardBrandArrayList.get(0).getLogo()).placeholder(R.drawable.ic_restaurant_cutlery).into(image_view_food);
                    Picasso.get().load(brand5.getSelectedLogo()).placeholder(R.drawable.ic_infinity).into(image_view_infinity);
                    callServiceForRewards(rewardBrandArrayList.get(5).getCategory_id());
                    break;
            }
        }
    }
}
