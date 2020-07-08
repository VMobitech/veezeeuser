package com.app.SpinnerIOS;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapterForSpinner_Bottom extends ArrayAdapter<String> {
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<String> items;
    private final int mResource;
    private final List<String> logos;

    public CustomAdapterForSpinner_Bottom(@NonNull Context context, @LayoutRes int resource,
                                          @NonNull List objects, @NonNull List logoObjects) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
        logos = logoObjects;

    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);

        TextView text_brand =  view.findViewById(R.id.number_textview);
        ImageView image_brand = view.findViewById(R.id.img_test);
        ConstraintLayout constraintLayout =  view.findViewById(R.id.parent_layout);


        text_brand.setText(items.get(position));
        if(logos.size()>0) {


            image_brand.setVisibility(View.VISIBLE);
            if(logos.get(position)!=null && !logos.get(position).isEmpty()){
            Picasso.get().load(logos.get(position)).placeholder(R.drawable.car_placeholder).error(R.drawable.car_placeholder).into(image_brand);
            }
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.setHorizontalBias(R.id.img_test, (float)0.05);
            //constraintSet.connect(R.id.number_textview,ConstraintSet.LEFT,R.id.img_test,ConstraintSet.RIGHT,0);
            constraintSet.applyTo(constraintLayout);
        }else{

            text_brand.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            image_brand.setVisibility(View.GONE);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.setHorizontalBias(R.id.img_test, (float)0.0);
            //constraintSet.connect(R.id.number_textview,ConstraintSet.LEFT,R.id.parent_layout,ConstraintSet.LEFT,0);
            constraintSet.applyTo(constraintLayout);

        }
        return view;
    }
}
