package com.versatilemobitech.VeeZee.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.versatilemobitech.VeeZee.Model.AdsSub;
import com.versatilemobitech.VeeZee.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class AdAdapter extends RecyclerView.Adapter<AdAdapter.ViewHolder> {
    Context mContext;
    ArrayList<AdsSub> adsSubArrayList;

    public AdAdapter(Context mContext, ArrayList<AdsSub> adsSubArrayList) {
        this.mContext = mContext;
        this.adsSubArrayList = adsSubArrayList;
    }

    @NonNull
    @Override
    public AdAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_ad, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdAdapter.ViewHolder holder, int position) {

        AdsSub adsSub = adsSubArrayList.get(position);

        Picasso.get().load(adsSub.getBanner()).placeholder(R.drawable.partner_placeholder).into(holder.image_view_ad);

    }

    @Override
    public int getItemCount() {
        return adsSubArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image_view_ad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_view_ad = itemView.findViewById(R.id.img);
        }
    }
}
