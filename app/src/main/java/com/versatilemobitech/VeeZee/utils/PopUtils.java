package com.versatilemobitech.VeeZee.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;


import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.versatilemobitech.VeeZee.Activities.CarDetailsActivity;
import com.versatilemobitech.VeeZee.Adapters.CustomAdapterForSpinner;
import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.interfaces.ReturnValue;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.versatilemobitech.VeeZee.utils.Utility.isMarshmallowOS;


/**
 * Created by USER on 23-08-2018.
 */

public class PopUtils {
    static Dialog dialog = null;
    static String msg;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    public static void dialogListReturnValue(final Context mContext, ArrayList<String> arrayList, String mTitle,
                                             final EditText mEditText, final ReturnValue returnValue) {
        final ArrayAdapter<String> adapter;
        if (isMarshmallowOS()) {
            dialog = new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
        } else {
            dialog = new Dialog(mContext);
        }

        dialog.setContentView(R.layout.dialog_popup);
        dialog.setTitle(mTitle);
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorWhite);


        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
// TODO Auto-generated method stub
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
// TODO Auto-generated method stub
            }
        });

//Prepare ListView in dialog
        // EditText inputSearch=(EditText)dialog.findViewById(R.id.inputSearch);

        ListView dialog_ListView =  dialog.findViewById(R.id.dialogNationality_listview);
        adapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_list_item_1, arrayList);
        dialog_ListView.setAdapter(adapter);
        dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                returnValue.returnValues(parent.getItemAtPosition(position).toString(), position);

                dialog.dismiss();

            }
        });

        dialog.show();
    }

    public static void dialogListReturnValueWithImage(final Context mContext, ArrayList<String> arrayList,ArrayList<String>brandLogo, String mTitle,
                                             final EditText mEditText, final ReturnValue returnValue) {
        final ArrayAdapter<String> adapter;
        Activity activity = (Activity) mContext;

        if (isMarshmallowOS()) {
            dialog = new Dialog(mContext,android.R.style.Theme_Material_Light_Dialog);
        } else {
            dialog = new Dialog(mContext,R.style.CustomAlertDialog2);
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_popup);
       // View v = ((Activity) mContext).getApplication().getWindow().getDecorView();
        //v.setBackgroundResource(android.R.color.transparent);
        //dialog.setContentView(R.layout.dialog_popup);


        if(arrayList.size()>8)
        {
            int width = (int)(mContext.getResources().getDisplayMetrics().widthPixels*0.90);
            int height = (int)(mContext.getResources().getDisplayMetrics().heightPixels*0.60);

            dialog.getWindow().setLayout(width, height);
        }else if(arrayList.size()>6)
        {
            int width = (int)(mContext.getResources().getDisplayMetrics().widthPixels*0.90);
            int height = (int)(mContext.getResources().getDisplayMetrics().heightPixels*0.40);

            dialog.getWindow().setLayout(width, height);
        }else if(arrayList.size()>5)
        {
            int width = (int)(mContext.getResources().getDisplayMetrics().widthPixels*0.90);
            int height = (int)(mContext.getResources().getDisplayMetrics().heightPixels*0.30);

            dialog.getWindow().setLayout(width, height);
        }
        else if(arrayList.size()>=3) {
            int width = (int)(mContext.getResources().getDisplayMetrics().widthPixels*0.90);
            int height = (int)(mContext.getResources().getDisplayMetrics().heightPixels*0.25);

            dialog.getWindow().setLayout(width, height);
        }else if(arrayList.size()>=2) {
            int width = (int)(mContext.getResources().getDisplayMetrics().widthPixels*0.90);
            int height = (int)(mContext.getResources().getDisplayMetrics().heightPixels*0.20);

            dialog.getWindow().setLayout(width, height);
        }else {
            int width = (int)(mContext.getResources().getDisplayMetrics().widthPixels*0.90);
            int height = (int)(mContext.getResources().getDisplayMetrics().heightPixels*0.18);

            dialog.getWindow().setLayout(width, height);
        }

        dialog.setTitle(mTitle);
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorWhite);


        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
// TODO Auto-generated method stub
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
// TODO Auto-generated method stub
            }
        });

//Prepare ListView in dialog
        // EditText inputSearch=(EditText)dialog.findViewById(R.id.inputSearch);

        ListView dialog_ListView =  dialog.findViewById(R.id.dialogNationality_listview);
       /* adapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_list_item_1, arrayList);*/
        adapter = new CustomAdapterForSpinner(mContext,R.layout.dialog_dropdown_with_image,arrayList,brandLogo);
        dialog_ListView.setAdapter(adapter);
        dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                returnValue.returnValues(parent.getItemAtPosition(position).toString(), position);

                dialog.dismiss();

            }
        });

        dialog.show();

    }

    public static void alertDialog(final Context mContext, String message, final View.OnClickListener okClick) {
        TextView mTxtOk, mTxtMessage;
        final Dialog dialog = new Dialog(mContext, R.style.CustomAlertDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = LayoutInflater.from(mContext).inflate(R.layout.alert_dialog, null);
        mTxtOk = v.findViewById(R.id.txtOk);
        mTxtMessage = v.findViewById(R.id.txtMessage);

        dialog.getWindow().getAttributes().windowAnimations = R.style.AlertDialogCustom;
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        mTxtMessage.setText(message);
        mTxtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (okClick != null) {
                    okClick.onClick(v);
                }
            }
        });

        dialog.setContentView(v);
        dialog.setCancelable(false);

        int width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (mContext.getResources().getDisplayMetrics().heightPixels * 0.30);
        dialog.getWindow().setLayout(width, lp.height);

        dialog.show();
    }



    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String timer(){
         CountDownTimer countDownTimer = new CountDownTimer(180000, 1000) {
             String message = "";
            @SuppressLint("DefaultLocale")
            public void onTick(long millisUntilFinished) {
                Constants.isTimer = true;
                message = ""+ String.format(" %02d : %02d ",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
               /* txt_time.setText(""+ String.format(" %02d : %02d ",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));*/
                msg = message;
            }

            public void onFinish() {
                Constants.isTimer = false;
                //txt_time.setText();
                message = "done!";
                msg = message;
            }
        }.start();
         return msg;
    }
}
