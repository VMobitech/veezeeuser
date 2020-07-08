package com.versatilemobitech.VeeZee.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.versatilemobitech.VeeZee.CarouselLinearLayout;
import com.versatilemobitech.VeeZee.Model.UserCarModel;
import com.versatilemobitech.VeeZee.R;

import java.util.ArrayList;

/**
 * Created by USER on 31-05-2019.
 */

public class ItemFragment extends Fragment {

    private static final String POSITON = "position";
    private static final String SCALE = "scale";
    static ArrayList<UserCarModel> userCarModelArrayList1;

    public static Fragment newInstance(Context context, int pos, float scale, ArrayList<UserCarModel> userCarModelArrayList) {
        Bundle b = new Bundle();
        b.putInt(POSITON, pos);
        b.putFloat(SCALE, scale);
        userCarModelArrayList1 = userCarModelArrayList;
        return Fragment.instantiate(context, ItemFragment.class.getName(), b);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWidthAndHeight();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }

        final int postion = this.getArguments().getInt(POSITON);
        float scale = this.getArguments().getFloat(SCALE);

        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_image, container, false);
        final TextView textView_brand = linearLayout.findViewById(R.id.text_brand);
        final TextView textView_car = linearLayout.findViewById(R.id.text_car);
        CarouselLinearLayout root = linearLayout.findViewById(R.id.root_container);
        final ImageView imageView = linearLayout.findViewById(R.id.pagerImg);

        textView_brand.setTextColor(getResources().getColor(R.color.black));
        textView_car.setTextColor(getResources().getColor(R.color.black));
        root.setScaleBoth(scale);
        if (userCarModelArrayList1 != null) {
            if(!userCarModelArrayList1.get(postion).getImage().isEmpty()) {
                Picasso.get().load(userCarModelArrayList1.get(postion).getImage()).placeholder(R.drawable.car_placeholder).error(R.drawable.car_placeholder).noFade().into(imageView);
                textView_brand.setText(userCarModelArrayList1.get(postion).getBrand_name());
                textView_car.setText(userCarModelArrayList1.get(postion).getCar_name());

            }
            Log.e("Praked","Parked"+userCarModelArrayList1.size());
        }
        return linearLayout;
    }

}
