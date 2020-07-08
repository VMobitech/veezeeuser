package com.versatilemobitech.VeeZee.Adapters;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.versatilemobitech.VeeZee.Activities.MainActivity;
import com.versatilemobitech.VeeZee.CarouselLinearLayout;
import com.versatilemobitech.VeeZee.Fragments.HomeFragment;
import com.versatilemobitech.VeeZee.Fragments.ItemFragment;
import com.versatilemobitech.VeeZee.Model.UserCarModel;
import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER on 11-06-2019.
 */
public class CarouselPagerAdapterMain extends FragmentPagerAdapter implements ViewPager.PageTransformer/* ViewPager.OnPageChangeListener */ {

    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.5f; // to decrease scaling for left and right
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
    private Map<Integer, Boolean> showstatusarray = new HashMap<>();


    /* public final static float BIG_SCALE = 0.8f;
     public final static float SMALL_SCALE = 0.5f;
     public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;*/

    private MainActivity context;
    private float scale;
    ArrayList<UserCarModel> userCarModelArrayList;

    public CarouselPagerAdapterMain(MainActivity context, FragmentManager fm, ArrayList<UserCarModel> userCarModelArrayList) {
        super(fm);
        this.context = context;
        this.userCarModelArrayList = userCarModelArrayList;
    }


    @Override
    public Fragment getItem(int position) {
        // make the first mViewPager bigger than others
        if (position == MainActivity.FIRST_PAGE)
            scale = BIG_SCALE;
        else
            scale = SMALL_SCALE;

        showstatusarray.put(position,false);
        Utility.setShowTextviewsMap(showstatusarray,context);
        if(userCarModelArrayList.size()>2) {
            position = position % MainActivity.PAGES;
        }else {
            position = position;
        }
        return ItemFragment.newInstance(context, position, scale, userCarModelArrayList);
    }

    @Override
    public int getCount() {
      /*  if (userCarModelArrayList != null) {
            return userCarModelArrayList.size();
        } else {
            return 0;
        }*/
      if(userCarModelArrayList != null) {
          if(userCarModelArrayList.size()>2)
          return context.PAGES * MainActivity.LOOPS;
          else return userCarModelArrayList.size();
      }else {
          return 0;
      }
    }

    @Override
    public void transformPage(View page, float position) {

        CarouselLinearLayout myLinearLayout = page.findViewById(R.id.root_container);
        float scale = BIG_SCALE;
        if (position > 0) {
            scale = scale - position * DIFF_SCALE;
        } else {
            scale = scale + position * DIFF_SCALE;
        }
        if (scale < 0) scale = 0;


        if (position <= -1.0F || position >= 1.0F) {
            page.setAlpha(0.0F);
        } else if (position == 0.0F) {
            page.setAlpha(1.0F);
        } else {
            // position is between -1.0F & 0.0F OR 0.0F & 1.0F
            page.setAlpha(0.9F - Math.abs(position));
        }
        myLinearLayout.setScaleBoth(scale);

    }




   /* @Override
    public Fragment getItem(int position) {
        // make the first pager bigger than others
        try {
            if (position == MainActivity.FIRST_PAGE)
                scale = BIG_SCALE;
            else
                scale = SMALL_SCALE;

            position = position % MainActivity.count;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ItemFragment.newInstance(context, position, scale, userCarModelArrayList);
    }

    @Override
    public int getCount() {
        int count = 0;
        try {
            count = MainActivity.count * MainActivity.LOOPS;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        if (userCarModelArrayList != null) {
            return userCarModelArrayList.size();
        } else {
            return 0;
        }
        //  return count;
    }

    @Override
    public void transformPage(@NonNull View page, float position) {


    }*/

   /* @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        try {
            if (positionOffset >= 0f && positionOffset <= 1f) {
                CarouselLinearLayout cur = getRootView(position);
                cur.setScaleBoth(BIG_SCALE - DIFF_SCALE * positionOffset);
                CarouselLinearLayout next = getRootView(position + 1);
                next.setScaleBoth(SMALL_SCALE + DIFF_SCALE * positionOffset);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageSelected(int position) {
        try {
//            if (positionOffset >= 0f && positionOffset <= 1f) {
            CarouselLinearLayout cur = getRootView(position);
            CarouselLinearLayout prev = null;
            try {
                prev = getRootView(position - 1);
                if (prev != null)
                    prev.setAlpha(.2f);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                CarouselLinearLayout next1 = getRootView(position);
                if (next1 != null)
                    next1.setAlpha(.2f);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                CarouselLinearLayout next2 = getRootView(position + 2);
                if (next2 != null)
                    next2.setAlpha(.2f);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                CarouselLinearLayout next = getRootView(position + 1);
                next.setAlpha(0.2f);
            } catch (Exception e) {
                e.printStackTrace();
            }

            cur.setAlpha(1f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @SuppressWarnings("ConstantConditions")
    private CarouselLinearLayout getRootView(int position) {
        return (CarouselLinearLayout) fragmentManager.findFragmentByTag(this.getFragmentTag(position))
                .getView().findViewById(R.id.root_container);
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + HomeFragment.cars_pager.getId() + ":" + position;
    }*/
}
