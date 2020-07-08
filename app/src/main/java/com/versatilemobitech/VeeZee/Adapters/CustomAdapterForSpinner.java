package com.versatilemobitech.VeeZee.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;
import com.versatilemobitech.VeeZee.R;

import java.util.List;

public class CustomAdapterForSpinner extends ArrayAdapter<String> {
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<String> items;
    private final int mResource;
    private final List<String> logos;

    public CustomAdapterForSpinner(@NonNull Context context, @LayoutRes int resource,
                              @NonNull List objects,@NonNull List logoObjects) {
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

        TextView text_brand =  view.findViewById(R.id.text_view_brand);
        ImageView image_brand = view.findViewById(R.id.image_view_car);

        text_brand.setText(items.get(position));
        if(logos.size()>0) {
            image_brand.setVisibility(View.VISIBLE);
            if(!logos.get(position).equalsIgnoreCase(""))
            Picasso.get().load(logos.get(position)).placeholder(R.drawable.car_placeholder).error(R.drawable.car_placeholder).into(image_brand);
        }else{
            image_brand.setVisibility(View.INVISIBLE);
        }
        return view;
    }
}
