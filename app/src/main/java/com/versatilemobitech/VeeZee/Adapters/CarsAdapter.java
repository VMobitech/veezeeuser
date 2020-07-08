package com.versatilemobitech.VeeZee.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.versatilemobitech.VeeZee.Activities.CarDetailsActivity;
import com.versatilemobitech.VeeZee.Model.UserCarModel;
import com.versatilemobitech.VeeZee.R;

import java.util.ArrayList;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.ViewHolder> {
    private Context context;
    ArrayList<UserCarModel> userCarModelArrayList;
    boolean refreshed = false;

    public CarsAdapter(Context mContext, ArrayList<UserCarModel> userCarModelArrayList) {
        this.context = mContext;
        this.userCarModelArrayList = userCarModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cars, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final UserCarModel userCarModel = userCarModelArrayList.get(position);

        holder.txt_car_no.setText(userCarModel.getVehicle_no());
        Log.d("My car",userCarModel.getImage());
        Picasso.get().load(userCarModel.getImage()).placeholder(R.drawable.car_placeholder).error(R.drawable.car_placeholder).fit().centerCrop().into(holder.image_view_car);

        holder.check_box_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (userCarModel.isSelected()) {
                    userCarModel.setSelected(false);
                } else {
                    userCarModel.setSelected(true);
                }
            }
        });
        if(refreshed){
            if(userCarModel.isSelected()) {
                userCarModel.setSelected(true);
                holder.check_box_select.setChecked(true);
            }else {
                userCarModel.setSelected(false);
                holder.check_box_select.setChecked(false);
            }
        }

        holder.car_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // if(!userCarModel.getBrand_name().equalsIgnoreCase("Other")) {
                    Intent i = new Intent(context, CarDetailsActivity.class);
                    i.putExtra("CAR_DETAILS", userCarModel);
                    i.putExtra("TAG", "CAREDIT");
                    i.putExtra("car_id",userCarModelArrayList.get(position).getCar_id());
                    context.startActivity(i);
                /*}else {
                    Toast.makeText(context,"Can't edit other brand car",Toast.LENGTH_SHORT).show();
                }*/

            }
        });

    }

    public ArrayList<UserCarModel> getUserCarModelArrayList() {
        return userCarModelArrayList;
    }
    public void getUserCarModelListRefresh( ArrayList<UserCarModel> list,boolean isRefesh) {
        refreshed = isRefesh;
        userCarModelArrayList = list;
    }

    @Override
    public int getItemCount() {
        return userCarModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_car_no;
        CheckBox check_box_select;
        ImageView image_view_car;
        LinearLayout car_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_car_no = itemView.findViewById(R.id.txt_car_no);
            check_box_select = itemView.findViewById(R.id.cb_check);
            image_view_car = itemView.findViewById(R.id.image_view_car);
            car_item = itemView.findViewById(R.id.car_item);
        }
    }
}
