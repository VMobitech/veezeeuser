package com.app.SpinnerIOS;

/*
 * Created by Yash on 29/6/17.
 */

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import java.util.ArrayList;

public class SpinnerWindow
{
    private static SpinnerWindow_interface spinnerWindow_interface;


    public SpinnerWindow(SpinnerWindow_interface spinnerWindow_interface)
    {
        SpinnerWindow.spinnerWindow_interface = spinnerWindow_interface;
    }

    public static void showSpinner(final Context context, final String viewFrom, final ArrayList<String> brandNames, final ArrayList<String> brandLogos)
    {
        final ArrayAdapter<String> adapter;
        final Dialog dialog_spinner = new Dialog(context);

        dialog_spinner.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog_spinner.setContentView(R.layout.spinner_dialog);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_spinner.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(lp_number_picker);

        dialog_spinner.getWindow().setGravity(Gravity.BOTTOM);

        dialog_spinner.getWindow().getAttributes().windowAnimations = R.style.custom_alert_dialog_animation_spinner;

        ListView listview_spinner = (ListView) dialog_spinner.findViewById(R.id.listview_spinner);
        ImageView img_close = (ImageView) dialog_spinner.findViewById(R.id.img_close);

        adapter = new CustomAdapterForSpinner_Bottom(context,R.layout.spinner_textview,brandNames,brandLogos);
        //listview_spinner.setAdapter(new ArrayAdapter<String>(context, R.layout.spinner_textview, R.id.number_textview, brandNames,brandLogos));
        listview_spinner.setAdapter(adapter);
        listview_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                spinnerWindow_interface.selectedPosition(viewFrom,position);

                if(dialog_spinner != null)
                {
                    dialog_spinner.dismiss();
                    dialog_spinner.cancel();
                }
            }
        });
    img_close.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialog_spinner.dismiss();
            dialog_spinner.cancel();
        }
    });

        dialog_spinner.show();
    }
}
