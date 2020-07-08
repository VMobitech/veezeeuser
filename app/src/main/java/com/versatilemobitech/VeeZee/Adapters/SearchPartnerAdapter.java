package com.versatilemobitech.VeeZee.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.versatilemobitech.VeeZee.Activities.MapActivity;
import com.versatilemobitech.VeeZee.Activities.SearchPartner;
import com.versatilemobitech.VeeZee.Model.PartnerModel;
import com.versatilemobitech.VeeZee.R;

import java.util.ArrayList;
import java.util.List;

public class SearchPartnerAdapter extends RecyclerView.Adapter<SearchPartnerAdapter.ViewHolder>  {
    View view ;
    Context mContext;

    private ArrayList<PartnerModel> movieList;
    private ArrayList<PartnerModel> movieListFiltered;

    public SearchPartnerAdapter(Activity mContext, ArrayList<PartnerModel>list) {
        this.mContext = mContext;
        this.movieList = list;


    }

   public boolean filter(String text) {
       //new array list that will hold the filtered data
      movieListFiltered=new ArrayList<>();
      boolean isSearched = false;

    for (int i=0;i<movieList.size();i++)
    {
        if(movieList.get(i).getName().toLowerCase().contains(text.toLowerCase()))
        {
            movieListFiltered.add(movieList.get(i));
            isSearched = true;
        }else {
            isSearched = false;
        }
    }
    if(movieListFiltered.size()>0){
        isSearched = true;
    }

       //calling a method of the adapter class and passing the filtered list
      filterList(movieListFiltered);
    return isSearched;
   }

    public void filterList(ArrayList<PartnerModel> filterdNames) {
        this.movieList = filterdNames;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public SearchPartnerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_search,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchPartnerAdapter.ViewHolder holder, int position) {

        holder.text_location.setText(movieList.get(position).getAddress());
        holder.text_title.setText(movieList.get(position).getName());
        holder.onBind(movieList.get(position));

    }

    @Override
    public int getItemCount() {
        if(movieList != null){
            return movieList.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_location,text_title;
        ConstraintLayout item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_location = itemView.findViewById(R.id.text_location);
            text_title = itemView.findViewById(R.id.text_title);
            item = itemView.findViewById(R.id.item);
        }

        public void onBind(final PartnerModel partnerModel) {
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!partnerModel.getLatitude().equals("") && !partnerModel.getLongitude().equals("")&&partnerModel.getLatitude().matches("\\d+(\\.\\d+)?")) {
                        Intent i = new Intent(mContext, MapActivity.class);
                        i.putExtra("lat", partnerModel.getLatitude());
                        i.putExtra("lng", partnerModel.getLongitude());
                        i.putExtra("title",partnerModel.getName());
                        Log.e("mapValues", "onClick: "+ partnerModel.getLatitude()+partnerModel.getLongitude()+partnerModel.getName());
                        mContext.startActivity(i);
                        ((SearchPartner) mContext).finish();
                    }else {
                        Toast.makeText(mContext,"In correct map location",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
