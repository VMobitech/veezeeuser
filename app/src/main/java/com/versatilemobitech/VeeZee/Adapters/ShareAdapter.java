package com.versatilemobitech.VeeZee.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.versatilemobitech.VeeZee.Model.Contacts;
import com.versatilemobitech.VeeZee.Model.PartnerModel;
import com.versatilemobitech.VeeZee.R;

import java.util.ArrayList;
import java.util.List;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder> {
    Context mContext;
    ArrayList<Contacts>mContcts;
    SendClickListener mListener;
    ArrayList<Contacts>ListFiltered;
    ArrayList<Contacts>OrginalList;

    public ShareAdapter(Context mContext, ArrayList<Contacts> mContacts) {
        this.mContext = mContext;
        this.mContcts = mContacts;
        this.OrginalList = mContacts;
        this.mListener = (SendClickListener) mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_share, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txt_userName.setText(mContcts.get(position).getName());
        if(mContcts.get(position).getImage()!=null && !mContcts.get(position).getImage().isEmpty()) {
            Picasso.get().load(mContcts.get(position).getImage()).placeholder(R.drawable.ic_user).into(holder.image_view_user);


            Log.e("fbvfgvfgv","contact i "+mContcts.get(position).getShare_user_id());
            Log.e("fbvfgvfgv","contact name "+mContcts.get(position).getName());

        }
        //holder.image_view_user.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_user,null));
        holder.text_view_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mListener.sendClick(mContcts.get(position).getShare_user_id(),mContcts.get(position).getMobile());

               // mListener.sendClick(position,mContcts.get(position).getMobile());



            }
        });

    }
    public boolean filter(String text) {
        //new array list that will hold the filtered data
        ListFiltered=new ArrayList<>();
        boolean isSearched = false;

        for (int i=0;i<OrginalList.size();i++)
        {
            if(OrginalList.get(i).getName().toLowerCase().contains(text.toLowerCase()))
            {
                ListFiltered.add(OrginalList.get(i));
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

    public void filterList(ArrayList<Contacts> filterdNames) {
        this.mContcts = filterdNames;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mContcts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_userName,text_view_send;
        ImageView image_view_user;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_userName = itemView.findViewById(R.id.txt_userName);
            image_view_user = itemView.findViewById(R.id.image_view_user);
            text_view_send = itemView.findViewById(R.id.text_view_send);
        }
    }
    public interface SendClickListener {
        void sendClick(String shareId ,String mobile);
    }
}
