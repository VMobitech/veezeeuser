package com.versatilemobitech.VeeZee.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.versatilemobitech.VeeZee.Activities.ContactUsActivity;
import com.versatilemobitech.VeeZee.Activities.DisclaimerActivity;
import com.versatilemobitech.VeeZee.Activities.FAQsActivity;
import com.versatilemobitech.VeeZee.Activities.HistoryActivity;
import com.versatilemobitech.VeeZee.Activities.Terms_ConditionsActivity;
import com.versatilemobitech.VeeZee.Activities.VeeZeeCreditsActivity;
import com.versatilemobitech.VeeZee.R;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHold> {
    private Context context;
    private List<String> mMenuList;
    public MenuAdapter(Context mContext,List<String>menuList) {
        this.context = mContext;
        this.mMenuList = menuList;
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, int position) {
        int icons[] = new int[]{
                R.drawable.ic_message, R.drawable.ic_faq, R.drawable.ic_danger, R.drawable.ic_termsandconditions
        };
        holder.img_item.setImageResource(icons[position]);
        holder.txt_item_name.setText(mMenuList.get(position));
        if (holder.txt_item_name.getText().equals("Contact")) {
            holder.txt_item_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ContactUsActivity.class));
                }
            });
        } else if (holder.txt_item_name.getText().equals("FAQ")) {
            holder.txt_item_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, FAQsActivity.class));
                }
            });
        } else if (holder.txt_item_name.getText().equals("Disclaimer")) {
            holder.txt_item_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, DisclaimerActivity.class));
                }
            });
        } else if (holder.txt_item_name.getText().equals("Terms & Conditions")) {
            holder.txt_item_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, Terms_ConditionsActivity.class));
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return mMenuList.size();
    }


    public class ViewHold extends RecyclerView.ViewHolder {
        ImageView img_item ;
        TextView txt_item_name;
        public ViewHold(@NonNull View itemView) {
            super(itemView);
             img_item = itemView.findViewById(R.id.img_item);
             txt_item_name = itemView.findViewById(R.id.txt_item_name);

        }
    }
}
