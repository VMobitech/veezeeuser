package com.versatilemobitech.VeeZee.Activities;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.squareup.picasso.Picasso;
import com.versatilemobitech.VeeZee.Adapters.CarImageSliderAdapter;
import com.versatilemobitech.VeeZee.BaseApplication;
import com.versatilemobitech.VeeZee.Fragments.CarsFragment;
import com.versatilemobitech.VeeZee.Fragments.HomeFragment;
import com.versatilemobitech.VeeZee.Fragments.PartnerFragment;
import com.versatilemobitech.VeeZee.Fragments.RewardsFragment;
import com.versatilemobitech.VeeZee.Fragments.RewardsViewPager2Fragment;
import com.versatilemobitech.VeeZee.Fragments.VaultFragment;
import com.versatilemobitech.VeeZee.Model.PushForCarHandOverDetails;
import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.SharedPreference;
import com.versatilemobitech.VeeZee.utils.Constants;
import com.versatilemobitech.VeeZee.utils.Utility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends BaseApplication implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    BottomNavigationView bottomBar;
    FrameLayout container;
    Toolbar toolbar;
    TextView toolBarTitle, txt_credits, text_view_v, text_view_wallet_amount;
    ImageView img_user_menu, img_credits, image_view_coin,img_veezee;
    /*public static int FIRST_PAGE = 10;*/
    public static int count;
    /*public final static int LOOPS = 1000;*/
    ViewPager mViewPager;
    LinearLayout SliderDots;
    private int dotscount;
    private ImageView[] dots;
    int mSelectedItem;
    public static String ClickToNavigate = "";
    String deviceToken, deviceId;
    PushForCarHandOverDetails pushForCarHandOverDetails;
    String Status = "";

    public static int PAGES = 0;
    public final static int LOOPS = 1000;
    public final static int FIRST_PAGE = PAGES * LOOPS / 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        deviceToken = FirebaseInstanceId.getInstance().getToken();
        deviceId = BaseApplication.getDeviceId(MainActivity.this);

        Log.e("deviceToken", "deviceToken : " + deviceToken);
        Log.e("deviceId", "deviceId : " + deviceId);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        init();
    }

    private void init() {

        SharedPreference.setStringPreference(MainActivity.this,"EMAILCHNAGED","NO");

        setReferences();
        setClickListeners();

        String userImage = Utility.getSharedPrefStringData(this, Constants.IMAGE);
        if (!userImage.equals("") || !userImage.isEmpty()) {
            Picasso.get().load(Utility.getSharedPrefStringData(this, Constants.IMAGE)).placeholder(R.drawable.ic_user).into(img_user_menu);
        }
        getArgument();
        if (ClickToNavigate != null) {
            MenuItem homeItem = bottomBar.getMenu().getItem(0);
            if (ClickToNavigate.equals("VALET") || ClickToNavigate.equals("CLICKTO")) {
                ClickToNavigate = "VALET";
                navigateFragment(new HomeFragment(this, "", null), "VALET", null, this);
                bottomBar.setSelectedItemId(homeItem.getItemId());
            } else if (ClickToNavigate.equals("CARS")) {
                MenuItem carsItem = bottomBar.getMenu().getItem(2);
                navigateFragment(new CarsFragment(), "CARS", null, this);

                bottomBar.setSelectedItemId(carsItem.getItemId());
            } else if (ClickToNavigate.equals("VALETPUSH")) {
                if (pushForCarHandOverDetails != null) {
                    navigateFragment(new HomeFragment(this, "VALETPUSH", pushForCarHandOverDetails), "VALET", null, this);
                    bottomBar.setSelectedItemId(homeItem.getItemId());
                }
            } else {
                MenuItem valutItem = bottomBar.getMenu().getItem(4);
                navigateFragment(new VaultFragment(this), "VAULT", null, MainActivity.this);
                bottomBar.setSelectedItemId(valutItem.getItemId());
            }
        } else {
            MenuItem homeItem = bottomBar.getMenu().getItem(0);
            navigateFragment(new HomeFragment(this, "", null), "VALET", null, MainActivity.this);
            bottomBar.setSelectedItemId(homeItem.getItemId());
        }

    }

    private void getArgument() {
        Intent i = getIntent();
        if(i!=null) {
            ClickToNavigate = i.getStringExtra("TAG");
            String PARKED = i.getStringExtra("PARKED");
            pushForCarHandOverDetails = (PushForCarHandOverDetails) i.getExtras().getSerializable("CAR");

            if (PARKED != null) {
                if (PARKED.equals("isParked")) {
                    // setDialogForPickedCar();
                }
            }
            if(i.getStringExtra("STATUS")!=null&&!
            i.getStringExtra("STATUS").equalsIgnoreCase("")){
                pushForCarHandOverDetails = (PushForCarHandOverDetails) i.getExtras().getSerializable("mPushForCarHandOverDetails");
                Status = i.getStringExtra("STATUS");
                ClickToNavigate = "VALETPUSH";
            }
        }
    }

    private void setClickListeners() {
        bottomBar.setOnNavigationItemSelectedListener(this);
        img_user_menu.setOnClickListener(this);
        img_credits.setOnClickListener(this);
        text_view_wallet_amount.setOnClickListener(this);
    }

    private void setReferences() {
        bottomBar = findViewById(R.id.bottomBar);
        container = findViewById(R.id.container);
        toolBarTitle = findViewById(R.id.toolBarTitle);
        toolbar = findViewById(R.id.toolBar);
        img_user_menu = findViewById(R.id.img_user_menu);
        img_credits = findViewById(R.id.img_credits);
        txt_credits = findViewById(R.id.txt_credits);
        text_view_wallet_amount = findViewById(R.id.text_view_wallet_amount);
        image_view_coin = findViewById(R.id.image_view_coin);

        img_veezee = findViewById(R.id.img_veezee);
        img_veezee.setVisibility(View.INVISIBLE);

        img_credits.setVisibility(View.VISIBLE);
        image_view_coin.setVisibility(View.VISIBLE);
        img_user_menu.setVisibility(View.VISIBLE);
        txt_credits.setVisibility(View.GONE);


        text_view_v = findViewById(R.id.text_view_credits_v);
        text_view_v.setVisibility(View.VISIBLE);


        String user_credits = Utility.getSharedPrefStringData(this, Constants.WALLET_AMOUNT);
        text_view_wallet_amount.setText(user_credits);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolBarTitle.setVisibility(View.VISIBLE);
        toolBarTitle.setText("VeeZee");
        toolBarTitle.setTextColor(getResources().getColor(R.color.colorAccent));
        toolBarTitle.setGravity(Gravity.CENTER);

        /*if(!UserDetails.getInstance(this).getUserImage().isEmpty()){
           // Picasso.with(this).load(UserDetails.getInstance(this).getUserImage()).centerCrop().into(img_user_menu);
        }*/
        // dialogForCars();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        String TAG = "";

        switch (menuItem.getItemId()) {
            case R.id.bt_home:
                TAG = "VALET";
                if(ClickToNavigate.equalsIgnoreCase("VALET")){
                    TAG = "FRAGMENT";
                }else{
                   TAG = "FRAGMENT" ;
                }
                fragment = new HomeFragment(this, "", null);
                break;

            case R.id.bt_partner:
                TAG = "PARTNER";
                fragment = new PartnerFragment();
                break;

            case R.id.bt_cars:
                TAG = "CARS";
                fragment = new CarsFragment();
                break;

            case R.id.bt_rewards:
                TAG = "REWARDS";
                fragment = new RewardsViewPager2Fragment();
                break;
            case R.id.bt_wallet:
                TAG = "VAULT";
                fragment = new VaultFragment(this);
                break;
        }
        ClickToNavigate = TAG;
        mSelectedItem = menuItem.getItemId();
        return navigateFragment(fragment, TAG, null, this);
    }

    private void loadFragment(Fragment f, String tag) {

        navigateFragment(f, tag, null, this);


    }

    public static boolean navigateFragment(Fragment fragment, String tag, Bundle bundle, FragmentActivity fragmentActivity) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, tag);
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        if (tag != null) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commit();
        return true;
    }

    @Override
    public void onBackPressed() {
        MenuItem homeItem = bottomBar.getMenu().getItem(0);

        if (mSelectedItem != homeItem.getItemId()) {

            loadFragment(new HomeFragment(this, "", null), "VALET");
            ClickToNavigate = "FRAGMENT";
            // Select home item
            bottomBar.setSelectedItemId(homeItem.getItemId());
        } else {
            // super.onBackPressed();
            setDialogforExit();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_user_menu:
                startActivity(new Intent(MainActivity.this, MenuActivity.class));
                break;
            case R.id.text_view_wallet_amount:
                startActivity(new Intent(MainActivity.this, VeeZeeCreditsActivity.class));
                break;
            default:
                break;
        }

    }

    private void setImageSlider() {
        CarImageSliderAdapter viewPagerAdapter = new CarImageSliderAdapter(MainActivity.this);

        mViewPager.setAdapter(viewPagerAdapter);
        dotscount = viewPagerAdapter.getCount();

        //autoScroll();

        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            SliderDots.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dot));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void dialogForCars() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.CustomAlertDialog);
        ViewGroup mViewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(mViewGroup.getContext()).inflate(R.layout.dialog_cars_ahead, mViewGroup, false);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        mViewPager = dialogView.findViewById(R.id.pager_images);
        SliderDots = dialogView.findViewById(R.id.SliderDots);
        ImageView close = dialogView.findViewById(R.id.img_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        setImageSlider();
        alertDialog.show();

    }

    private void setDialogForPickedCar() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.CustomAlertDialog);
        ViewGroup mViewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(mViewGroup.getContext()).inflate(R.layout.dialog_valet_experience, mViewGroup, false);
        builder.setView(dialogView);
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        TextView txt_skip = dialogView.findViewById(R.id.txt_skip);
        TextView txt_amount1 = dialogView.findViewById(R.id.txt_amount1);
        TextView txt_amount2 = dialogView.findViewById(R.id.txt_amount2);
        TextView txt_amount3 = dialogView.findViewById(R.id.txt_amount3);


        txt_amount1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });
        txt_amount2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();

            }
        });

        txt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        // setImageSlider();
        alertDialog.show();
    }

    private void setDialogforExit() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.CustomAlertDialog);
        ViewGroup mViewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(mViewGroup.getContext()).inflate(R.layout.custom_dialog_exit, mViewGroup, false);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        TextView txt_yes, txt_cancel;
        txt_yes = dialogView.findViewById(R.id.txt_yes);
        txt_cancel = dialogView.findViewById(R.id.txt_cancel);
        txt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                MainActivity.this.finishAffinity();
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessage(String s) {
        if (s.equals("CarParked")) {
            //callServiceForuserCars();
            ClickToNavigate = "FRAGMENT";
            MenuItem homeItem = bottomBar.getMenu().getItem(0);
            bottomBar.setSelectedItemId(homeItem.getItemId());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(MainActivity.this)) {
            EventBus.getDefault().register(MainActivity.this);
        }

        // clear max_time stored in local preferences for timeout cars
        Log.e("MainActivity.this", "####### ON RESUME ######");
        if (Utility.getMaxTime(MainActivity.this) != null) {

            Map<Integer, String> max_timingsmap = Utility.getMaxTime(MainActivity.this);
            Log.e("MainActivity.this", "MAX_TIMINGS_MAP" + Utility.getMaxTime(MainActivity.this));
            for (int i = 0; i < max_timingsmap.size(); i++) {

                try {

                    if (max_timingsmap.get(i) != null && max_timingsmap.get(i).contains(":")) {

                        String timee = max_timingsmap.get(i);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
                        String datestr1 = simpleDateFormat.format(new Date());
                        Date date1 = simpleDateFormat.parse(datestr1);
                        String expirytime = timee;
                        Date date2 = simpleDateFormat.parse(expirytime);
                        long difference = date2.getTime() - date1.getTime();

                        int Mins = (int) (difference / (1000 * 60)) % 60;
                        long Secs = (int) (difference / 1000) % 60;
                        long minuteandsec = Mins*60*1000 + Secs * 1000;
                        Log.e("MainActivity.this", "LEFT OUT TIME IN PREFERENCES MAP::" + i + "____" + timee);
                        if (minuteandsec <= 0) {
                            //max_timingsmap.remove(i);
                            max_timingsmap.put(i,"Completed");
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Utility.setMaxTime(max_timingsmap, MainActivity.this);
            Log.e("MainActivity.this", "MODIFIED_MAX_TIMINGS_MAP" + Utility.getMaxTime(MainActivity.this));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
