package com.versatilemobitech.VeeZee.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.versatilemobitech.VeeZee.Model.CreditsHistory;
import com.versatilemobitech.VeeZee.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder> {
    private Context mContext;
    private List<CreditsHistory> mList;

    public TransactionHistoryAdapter(Context mContext, List<CreditsHistory> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_transaction_history, parent, false);
        return new TransactionHistoryAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text_view_date.setText(setDate(mList.get(position).getCreated_time()));
        holder.txt_add_coins.setText(mList.get(position).getAmount()+" "+mList.get(position).getAction());
        if(mList.get(position).getAction().equals("Spent")){
            holder.txt_add_coins.setTextColor(Color.parseColor("#FD1919"));
        }else {
            holder.txt_add_coins.setTextColor(Color.parseColor("#07A811"));
        }
        if(mList.get(position).getReason().contains("Reward Claimed")||mList.get(position).getReason().equalsIgnoreCase("car request")||
        mList.get(position).getReason().equalsIgnoreCase("Late Penalty")){
            holder.txt_money_type.setText((mList.get(position).getReason()));
        }else {
            holder.txt_money_type.setText("Via "+(mList.get(position).getReason()));
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_view_date,txt_add_coins,txt_money_type;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            text_view_date = itemView.findViewById(R.id.text_view_date);
            txt_add_coins = itemView.findViewById(R.id.txt_add_coins);
            txt_money_type = itemView.findViewById(R.id.txt_money_type);

        }

    }
    private String setDate (String date){
        String month_name = "";
        String day = "";
        try {

            SimpleDateFormat month = new SimpleDateFormat("MMM", Locale.ENGLISH);
            SimpleDateFormat SDday = new SimpleDateFormat("dd", Locale.ENGLISH);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date CreatedDate = sdf.parse(date);

            month_name = month.format(CreatedDate);
            day = SDday.format(CreatedDate);

            System.out.println("Month :" + month_name+"\n"+day);

        }catch (Exception e){
            e.printStackTrace();
        }

        return month_name +"\n"+day;
    }
}
