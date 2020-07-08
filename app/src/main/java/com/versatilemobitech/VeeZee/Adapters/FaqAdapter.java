package com.versatilemobitech.VeeZee.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.versatilemobitech.VeeZee.R;

import java.util.ArrayList;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {
    Context mContext;
    int indexPosition = 0;
    ArrayList<String> mList;
    boolean isVisible = false ;

    public FaqAdapter(Context mContext,ArrayList<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public FaqAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(mContext).inflate(R.layout.item_faq,parent,false);
      return new FaqAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FaqAdapter.ViewHolder holder, int position) {

        if(indexPosition==position){

            /*if(holder.txt_description.getVisibility()==View.VISIBLE){
                holder.txt_description.setVisibility(View.GONE);
                holder.img_down.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_down_arrow_colored));
                //holder.txt_item.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(mContext,R.drawable.ic_down_arrow_colored),null);
            }else {
                holder.txt_description.setVisibility(View.VISIBLE);
                holder.img_down.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_arrow_up));

                //holder.txt_item.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(mContext,R.drawable.ic_arrow_up),null);
            }*/
            holder.txt_description.setVisibility(View.VISIBLE);
            holder.img_down.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_arrow_up));
        }else {
            holder.txt_description.setVisibility(View.GONE);
            holder.img_down.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_down_arrow_colored));

            // holder.txt_item.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(mContext,R.drawable.ic_down_arrow_colored),null);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_description,txt_item;
        ImageView img_down;
        public ViewHolder(View view) {
            super(view);
            txt_description = view.findViewById(R.id.txt_description);
            txt_item = view.findViewById(R.id.txt_item);
            img_down = view.findViewById(R.id.img_down);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                txt_description.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            }

            txt_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    indexPosition = getAdapterPosition();
                    if(txt_description.getVisibility()==View.VISIBLE){
                        txt_description.setVisibility(View.GONE);
                       img_down.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_down_arrow_colored));
                        //holder.txt_item.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(mContext,R.drawable.ic_down_arrow_colored),null);
                    }else {
                        txt_description.setVisibility(View.VISIBLE);
                        img_down.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_arrow_up));

                        notifyDataSetChanged();   //holder.txt_item.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(mContext,R.drawable.ic_arrow_up),null);
                    }

                }
            });



        }
    }
}
