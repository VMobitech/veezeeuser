package com.versatilemobitech.VeeZee.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;
import com.versatilemobitech.VeeZee.Model.AdsMain;
import com.versatilemobitech.VeeZee.R;

import java.util.ArrayList;

public class homeImageSliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    //private Integer [] images = {R.drawable.fd_ad,R.drawable.fd_ad1,R.drawable.fd_ad,R.drawable.fd_ad1};
    ArrayList<AdsMain> adsMainArrayList;

    public homeImageSliderAdapter(Context context, ArrayList<AdsMain> adsMainArrayList) {
        this.context = context;
        this.adsMainArrayList = adsMainArrayList;
    }

    @Override
    public int getCount() {
        return adsMainArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_image_slider, null);
        ImageView imageView = view.findViewById(R.id.imageView);
        //imageView.setImageResource(images[position]);
        Picasso.get().load(adsMainArrayList.get(position).getBanner()).placeholder(R.drawable.valet_history_placeholder).into(imageView);


        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
