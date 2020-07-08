package com.versatilemobitech.VeeZee.Adapters;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.versatilemobitech.VeeZee.Model.Rewards;
import com.versatilemobitech.VeeZee.Model.UserRewards;
import com.versatilemobitech.VeeZee.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<UserRewards> userRewardsArrayList;

    public MyRewardsAdapter(Context mContext, ArrayList<UserRewards> userRewardsArrayList) {
        this.mContext = mContext;
        this.userRewardsArrayList = userRewardsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_rewards, parent, false);
        return new MyRewardsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserRewards userRewards = userRewardsArrayList.get(position);

        Picasso.get().load(userRewards.getLogo()).placeholder(R.drawable.veezee_logo).into(holder.image_view_item);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.text_view_title.setText(Html.fromHtml(userRewards.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.text_view_title.setText(Html.fromHtml(userRewards.getDescription()));
        }*/
        holder.text_view_title.setText(Html.fromHtml(userRewards.getTitle()));
        holder.text_view_product.setText(userRewards.getBrand());
        holder.text_view_voucher_code.setText(userRewards.getVoucher_code());
        holder.bindData(userRewards);


    }

    @Override
    public int getItemCount() {
        return userRewardsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image_view_item,image_view_copy;
        TextView text_view_title, text_view_product, text_view_expirydate, text_view_voucher_code;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_view_item = itemView.findViewById(R.id.img_item);
            image_view_copy = itemView.findViewById(R.id.image_view_copy);
            text_view_title = itemView.findViewById(R.id.text_view_title);
            text_view_product = itemView.findViewById(R.id.txt_product);
            text_view_expirydate = itemView.findViewById(R.id.txt_expire_date);
            text_view_voucher_code = itemView.findViewById(R.id.text_view_voucher_code);
            image_view_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboard.setText(userRewardsArrayList.get(getAdapterPosition()).getVoucher_code());
                    } else {
                        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", userRewardsArrayList.get(getAdapterPosition()).getVoucher_code());
                        clipboard.setPrimaryClip(clip);
                    }
                    Toast.makeText(mContext,"Voucher code copied",Toast.LENGTH_SHORT).show();
                }
            });

        }
        public void bindData(UserRewards rewards){
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date s = simpleDateFormat.parse(rewards.getValid_to());
                Log.d("Date", "" + s);
                String ss = simpleDateFormat.format(date);
                Date date1 = simpleDateFormat.parse(ss);
                Log.d("Date 1", "" + date1);

                if (date1.before(s)) {
                    long diff = s.getTime() - date1.getTime();
                    int diffDays = (int) (diff / (24 * 60 * 60 * 1000));

                    if (diffDays <= 7) {
                        if(diffDays==1){
                            text_view_expirydate.setText("Expires in 0" + diffDays + " Day");
                        }else {
                            text_view_expirydate.setText("Expires in 0" + diffDays + " Days");
                        }
                    } else {
                       text_view_expirydate.setText("Expires " + setExpDate(rewards.getValid_to()));
                    }


                }else if (date1.compareTo(s) == 0) {
                    text_view_expirydate.setText("Expires Today" );
                } else {
                    text_view_expirydate.setText("Expired");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    private String setExpDate(String date) {

        String parsedDate = "";
        Log.e("date", "date" + date);
        //01/04/20

        try {
            Date initDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            parsedDate = formatter.format(initDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parsedDate;
    }
}
