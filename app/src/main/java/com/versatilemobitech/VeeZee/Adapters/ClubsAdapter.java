package com.versatilemobitech.VeeZee.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.versatilemobitech.VeeZee.Activities.MapActivity;
import com.versatilemobitech.VeeZee.Model.Contacts;
import com.versatilemobitech.VeeZee.Model.PartnerModel;
import com.versatilemobitech.VeeZee.R;

import java.util.ArrayList;

public class ClubsAdapter extends RecyclerView.Adapter<ClubsAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<PartnerModel> partnerModelArrayList;
    private ArrayList<PartnerModel> ListFiltered;
    private ArrayList<PartnerModel> OrginalArrayList ;

    public ClubsAdapter(Context mContext, ArrayList<PartnerModel> partnerModelArrayList) {
        this.mContext = mContext;
        this.partnerModelArrayList = partnerModelArrayList;
        OrginalArrayList = partnerModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_club, parent, false);
        return new ClubsAdapter.ViewHolder(view);
    }

    public boolean filter(String text) {
        //new array list that will hold the filtered data
        ListFiltered=new ArrayList<>();
        boolean isSearched = false;

        for (int i=0;i<OrginalArrayList.size();i++)
        {
            if(OrginalArrayList.get(i).getName().toLowerCase().contains(text.toLowerCase())||OrginalArrayList.get(i).getAddress().toLowerCase().contains(text.toLowerCase()))
            {
                ListFiltered.add(OrginalArrayList.get(i));
                isSearched = true;
            }else {
                isSearched = false;
            }
        }
        if(ListFiltered.size()>0){
            isSearched = true;
        }

        //calling a method of the adapter class and passing the filtered list
        filterList(ListFiltered);
        return isSearched;
    }

    public void filterList(ArrayList<PartnerModel> filterdNames) {
        this.partnerModelArrayList = filterdNames;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       /* final searchModel mModel = mList.get(position);
        holder.bind(mModel);*/
        final PartnerModel partnerModel = partnerModelArrayList.get(position);

        Picasso.get().load(partnerModel.getImage()).placeholder(R.drawable.partner_placeholder).into(holder.img_club);
        holder.txt_clubname.setText(partnerModel.getName());
        holder.txt_location.setText(partnerModel.getAddress());

        if (position == partnerModelArrayList.size() - 1) {
            holder.view.setVisibility(View.INVISIBLE);
        } else {
            holder.view.setVisibility(View.VISIBLE);
        }

        holder.constrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, MapActivity.class);
                i.putExtra("lat", partnerModel.getLatitude());
                i.putExtra("lng", partnerModel.getLongitude());
                i.putExtra("title",partnerModel.getName());
                Log.e("mapValues", "onClick: "+ partnerModel.getLatitude()+partnerModel.getLongitude()+partnerModel.getName());
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return partnerModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_club;
        private TextView txt_clubname, txt_location;
        View view;
        ConstraintLayout constrain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_club = itemView.findViewById(R.id.img_club);
            txt_clubname = itemView.findViewById(R.id.txt_clubname);
            txt_location = itemView.findViewById(R.id.txt_location);
            constrain = itemView.findViewById(R.id.constrain);

            view = itemView.findViewById(R.id.view);
        }


    }

}
