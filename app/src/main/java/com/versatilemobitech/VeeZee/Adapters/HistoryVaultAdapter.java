package com.versatilemobitech.VeeZee.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;
import com.versatilemobitech.VeeZee.Fragments.HistoryVaultFragment;
import com.versatilemobitech.VeeZee.Model.HistoryValet;
import com.versatilemobitech.VeeZee.Model.HistoryVault;
import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.UtilHelpers.BlurTransformation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryVaultAdapter extends RecyclerView.Adapter<HistoryVaultAdapter.ViewHolder> {
    private HistoryVaultFragment mContext;
    private ArrayList<HistoryVault>mList;
    ViewGroup mViewGrop;
    Dialog mDialog;
    int indexPosition = 0;
    HistoryVaultAdapter.Listener mListener;


    public HistoryVaultAdapter(HistoryVaultFragment mContext, ArrayList<HistoryVault> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.mListener = (Listener) mContext;
    }

    @NonNull
    @Override
    public HistoryVaultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext.getActivity()).inflate(R.layout.item_history_vault, parent, false);
        mViewGrop=parent;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryVaultAdapter.ViewHolder holder, int position) {
        if(indexPosition==position){

            /*if(holder.rl.getVisibility()==View.VISIBLE){
                holder.rl.setVisibility(View.GONE);
            }else {
                holder.rl.setVisibility(View.VISIBLE);
            }*/
            holder.rl.setVisibility(View.VISIBLE);
        }else {
            holder.rl.setVisibility(View.GONE);
        }
        holder.txt_address.setText(mList.get(position).getAddress());
        holder.txt_title.setText((setExpDate(mList.get(position).getDate())+" | "+mList.get(position).getName()));

        String  pickupedBy = mList.get(position).getPicked_by() ;
        String droppedBy = mList.get(position).getDropped_by();
        pickupedBy =   pickupedBy.substring(0, 1).toUpperCase() + pickupedBy.substring(1);
        droppedBy = droppedBy.substring(0,1).toUpperCase()+droppedBy.substring(1);

        holder.text_view_dropped_by.setText(("Handed over by "+pickupedBy+" at "+mList.get(position).getOut_time()));
        holder.text_view_piked_by.setText("Received by "+droppedBy+" at "+mList.get(position).getIn_time());
        Picasso.get().load(mList.get(position).getImage()).transform(new BlurTransformation(mContext.getActivity())).placeholder(R.drawable.valet_history_placeholder).into(holder.image_view_background);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout ll;
        ImageView img_option_menu,image_view_background;
        RelativeLayout rl;
        TextView txt_title,txt_address,text_view_piked_by,text_view_dropped_by;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll = itemView.findViewById(R.id.ll);
            img_option_menu = itemView.findViewById(R.id.img_option_menu);
            rl = itemView.findViewById(R.id.rl);
            txt_title = itemView.findViewById(R.id.text_view_description);
            text_view_dropped_by = itemView.findViewById(R.id.text_view_dropped_by);
            text_view_piked_by = itemView.findViewById(R.id.text_view_piked_by);
            txt_address = itemView.findViewById(R.id.txt_address);
            image_view_background = itemView.findViewById(R.id.image_view_background);
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    indexPosition = getAdapterPosition();
                    HistoryVault model= mList.get(getAdapterPosition());
                   // model.setExpand(!model.isExpand());
                    notifyDataSetChanged();
                }
            });
            img_option_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDialogForMenu(getAdapterPosition());
                }
            });
        }
        private void setDialogForMenu(final int position){
            final BottomSheetDialog dialog = new BottomSheetDialog(mContext.getActivity());
            dialog.setContentView(R.layout.bottom_dialog_menu);
            TextView txt_report = dialog.findViewById(R.id.txt_report);
            TextView txt_dismiss = dialog.findViewById(R.id.txt_dismiss);
            txt_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    setDialogForReport(position);
                }
            });
            txt_dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        private void setDialogForReport(final int position) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext.getActivity(),R.style.CustomAlertDialog);
            //ViewGroup viewGroup = builder.findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(mContext.getActivity()).inflate(R.layout.dialog_for_report, mViewGrop, false);

            builder.setView(dialogView);
            mDialog = new Dialog(mContext.getActivity());
            mDialog = builder.create();
            //mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            builder.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            Window window = mDialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView txt_title =  dialogView.findViewById(R.id.text_view_description);
            ImageView img_close = dialogView.findViewById(R.id.img_close);
            TextView txt_submit =  dialogView.findViewById(R.id.txt_submit);
            final EditText edt_msg = (EditText) dialogView.findViewById(R.id.edt_msg);
            txt_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String msg = edt_msg.getText().toString();
                    if(!msg.equals("")) {
                        mListener.clickListener(position, msg);
                        mDialog.dismiss();
                    }else {
                        Toast.makeText(mContext.getActivity(),"Report issue can't be empty",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String msg = edt_msg.getText().toString();
                    //mListener.clickListener(position,msg);
                    mDialog.dismiss();
                }
            });
            mDialog.show();

        }
    }
    private String setExpDate(String date) {

        String parsedDate = "";
        Log.e("date", "date" + date);
        //01/04/20

        try {
            Date initDate = new SimpleDateFormat("dd-MM-yyyy").parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat("MMM d");
            parsedDate = formatter.format(initDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parsedDate;
    }
    public interface Listener {
        void clickListener (int position,String msg);
    }
}
