package com.versatilemobitech.VeeZee.Fragments;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.versatilemobitech.VeeZee.Activities.MyRewardsActivity;
import com.versatilemobitech.VeeZee.Activities.ProfileActivity;
import com.versatilemobitech.VeeZee.BaseApplication;
import com.versatilemobitech.VeeZee.Model.Rewards;
import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.WebUtils.ServerResponse;
import com.versatilemobitech.VeeZee.interfaces.IParseListener;
import com.versatilemobitech.VeeZee.utils.Constants;
import com.versatilemobitech.VeeZee.utils.PopUtils;
import com.versatilemobitech.VeeZee.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener, IParseListener {
    LinearLayout ll_voucher_code;
    TextView text_view_Proceed, text_view_money, text_view_youhave, text_view_title, text_view_description,
            text_view_amount, text_view_voucher_code, text_view_whatsinside, text_view_website,
            text_view_voucher_expirydate, text_view_note, text_view_v;
    ImageView img_coin,image_view_copy;
    View view, line;

    Rewards rewards;

    public static BottomSheetFragment newInstance(Rewards rewards) {
        BottomSheetFragment fm = new BottomSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("rewards", rewards);
        fm.setArguments(bundle);
        return fm;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.custom_bottom_dialog, container,
                false);
        init();
        return view;

    }


    private void init() {

        setReferences();
    }

    private void setReferences() {
        ll_voucher_code = view.findViewById(R.id.ll_voucher_code);
        text_view_Proceed = view.findViewById(R.id.text_view_proceed);
        text_view_money = view.findViewById(R.id.text_view_money);
        text_view_youhave = view.findViewById(R.id.text_view_youhave);
        img_coin = view.findViewById(R.id.img_coin);
        image_view_copy = view.findViewById(R.id.image_view_copy);

        text_view_Proceed.setOnClickListener(BottomSheetFragment.this);
        image_view_copy.setOnClickListener(BottomSheetFragment.this);

        text_view_title = view.findViewById(R.id.text_view_title);
        text_view_description = view.findViewById(R.id.text_view_description);
        text_view_amount = view.findViewById(R.id.text_view_amount);
        text_view_voucher_code = view.findViewById(R.id.text_view_voucher_code);
        text_view_whatsinside = view.findViewById(R.id.text_view_whatsinside);
        text_view_website = view.findViewById(R.id.text_view_website);
        text_view_voucher_expirydate = view.findViewById(R.id.text_view_voucher_expirydate);
        text_view_note = view.findViewById(R.id.text_view_note);
        text_view_v = view.findViewById(R.id.text_view_v_);
        line = view.findViewById(R.id.view);



        text_view_title.setText(rewards.getBrand());
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            text_view_description.setText(Html.fromHtml(rewards.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            text_view_description.setText(Html.fromHtml(rewards.getDescription()));
        }*/
        text_view_description.setText(rewards.getTitle());
        text_view_whatsinside.setText(rewards.getWhats_inside());
        text_view_website.setText(rewards.getWebsite());
        text_view_voucher_expirydate.setText(setExpDate(rewards.getValid_to()));
        text_view_money.setText(Utility.getSharedPrefStringData(getActivity(), Constants.WALLET_AMOUNT));
        text_view_amount.setText(rewards.getCredits());
        text_view_voucher_code.setText(rewards.getVoucher_code());
        text_view_note.setText(rewards.getNote());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        rewards = bundle.getParcelable("rewards");


        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_view_proceed:
                if (text_view_Proceed.getText().toString().equals("PROCEED")) {
                    //Toast.makeText(getActivity(), "proceed", Toast.LENGTH_SHORT).show();
                  /*  ll_voucher_code.setVisibility(View.VISIBLE);
                    text_view_Proceed.setText(getResources().getString(R.string.View_rewards));
                    text_view_youhave.setVisibility(View.GONE);
                    text_view_money.setText("Reward Claimed!");
                    text_view_money.setTextSize(21);
                    text_view_money.setTextColor(getResources().getColor(R.color.green));
                    img_coin.setVisibility(View.GONE);*/

                    callServiceForClaimReward();
                } else if (text_view_Proceed.getText().toString().equalsIgnoreCase("view claimed rewards")) {
                    Intent intent = new Intent(getActivity(), MyRewardsActivity.class);
                    startActivity(intent);
                    dismiss();
                }

                break;
            case R.id.image_view_copy :

                if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(text_view_voucher_code.getText().toString());
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text_view_voucher_code.getText().toString());
                    clipboard.setPrimaryClip(clip);
                }
                Toast.makeText(getActivity(),"Voucher code copied",Toast.LENGTH_SHORT).show();

                break;
        }

    }
  /*  private String setExpDate(String date){
        SimpleDateFormat format = new SimpleDateFormat("d");
        SimpleDateFormat format = new SimpleDateFormat("d MM yyyy");
        String date1 = format.format(Date.parse(date));
        return date1;
    }*/
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
    private void callServiceForClaimReward() {
        if (Utility.isNetworkAvailable(getActivity())) {
            Utility.showLoadingDialog(getActivity(), "Loading...", false);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("user_id", Utility.getSharedPrefStringData(getActivity(), Constants.USER_ID));
                jsonObject.put("reward_id", rewards.getReward_id());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestJSonObject(getActivity(), "POST", Constants.CLAIM_REWARD_URL, jsonObject, new HashMap<String, String>(), this, Constants.CLAIM_REWARD_CODE);

        } else {
            PopUtils.alertDialog(getActivity(), Utility.getResourcesString(getActivity(), R.string.check_internet), null);
        }
    }

    @Override
    public void ErrorResponse(String response, int requestCode) {
        Utility.hideLoadingDialog();
        PopUtils.alertDialog(getActivity(), Utility.getResourcesString(getActivity(), R.string.something_went_wrong), null);

    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        Utility.hideLoadingDialog();
        switch (requestCode) {
            case Constants.CLAIM_REWARD_CODE:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 200) {

                        ll_voucher_code.setVisibility(View.VISIBLE);

                        text_view_Proceed.setText(getResources().getString(R.string.View_rewards));
                        text_view_youhave.setVisibility(View.GONE);
                        text_view_money.setText("Reward Claimed!");
                        text_view_money.setTextSize(21);
                        text_view_money.setTextColor(getResources().getColor(R.color.green));
                        img_coin.setVisibility(View.GONE);
                        text_view_v.setVisibility(View.GONE);
                        line.setVisibility(View.GONE);
                        Utility.setSharedPrefStringData(getActivity(), Constants.WALLET_AMOUNT, jsonObject.optString("wallet_balance"));
                        PopUtils.alertDialog(getActivity(),message,null);
                    }else if(status==401) {
                        PopUtils.alertDialog(getActivity(), message, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseApplication.getInstance().callServiceForLogOut();
                            }
                        });

                    } else {
                        PopUtils.alertDialog(getActivity(),message,null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }
}
