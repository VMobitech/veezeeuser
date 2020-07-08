package com.versatilemobitech.VeeZee.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.versatilemobitech.VeeZee.Activities.DetailsActivity;
import com.versatilemobitech.VeeZee.Activities.MainActivity;
import com.versatilemobitech.VeeZee.Fragments.BottomSheetFragment;
import com.versatilemobitech.VeeZee.Model.Rewards;
import com.versatilemobitech.VeeZee.R;

import java.util.ArrayList;

public class NewAdapter extends RecyclerView.Adapter<NewAdapter.ViewHolder> {
    Context mContext;
    ArrayList<Rewards> mList;
    Rewards mRewards;
    MainActivity myActivity;

    public NewAdapter(Context mContext, ArrayList<Rewards> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.myActivity = (MainActivity) mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rewards, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        mRewards = mList.get(position);

        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(mContext) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };

        smoothScroller.setTargetPosition(position);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.text_view_title.setText(Html.fromHtml(mRewards.getDescription(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.text_view_title.setText(Html.fromHtml(mRewards.getDescription()));
            }
            Picasso.get().load(mRewards.getImage()).placeholder(R.drawable.valet_history_placeholder).into(holder.image_view_main);
            Picasso.get().load(mRewards.getLogo()).placeholder(R.drawable.rewards_brand_placeholder).into(holder.image_view_sub);

        Log.e("gvdcdvcgd","dbcd "+mRewards.getImage());

        holder.text_view_title.setText(mRewards.getDescription());
        holder.text_view_header_title.setText(mRewards.getTitle());
        holder.text_view_log_title.setText(mRewards.getBrand());

        holder.text_view_redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRewards = mList.get(position);

                BottomSheetFragment addPhotoBottomDialogFragment = BottomSheetFragment.newInstance(mRewards);
                addPhotoBottomDialogFragment.show(myActivity.getSupportFragmentManager(),
                        "add_photo_dialog_fragment");
            }
        });
        holder.text_view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
              //  intent.putExtra("rewards", mRewards);

                intent.putExtra("inside",mList.get(position).getWhats_inside());
                intent.putExtra("visitWebsite",mList.get(position).getWebsite());
                intent.putExtra("voucherExpireOn",mList.get(position).getValid_to());
                intent.putExtra("howToRead",mList.get(position).getHowToRedeem());

                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_view_main,image_view_sub;
        TextView text_view_header_title,text_view_title,text_view_log_title,text_view_redeem,text_view_details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_view_main  = itemView.findViewById(R.id.image_view_main);
            image_view_sub  = itemView.findViewById(R.id.image_view_sub);
            text_view_header_title = itemView.findViewById(R.id.text_view_header_title);
            text_view_title = itemView.findViewById(R.id.text_view_title);
            text_view_log_title = itemView.findViewById(R.id.text_view_log_title);
            text_view_redeem = itemView.findViewById(R.id.text_view_redeem);
            text_view_details = itemView.findViewById(R.id.text_view_details);
        }
    }
}
