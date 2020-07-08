package com.versatilemobitech.VeeZee.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;
import com.versatilemobitech.VeeZee.R;

public class AddedCarActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imq_qr, img_ok, img_addcar;
    String name, vehicleNo, brand, color;
    Bitmap bitmap;
    String TAG = "", user_id = "";
    String Qr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_car);
        getSupportActionBar().hide();

        init();
    }

    private void init() {
        setReferences();
        getArguments();
        setClickListeners();

    }

    private void setClickListeners() {
        img_ok.setOnClickListener(this);
        img_addcar.setOnClickListener(this);
    }

    private void getArguments() {
        Intent i = getIntent();
        TAG = i.getStringExtra("TAG");
        name = i.getStringExtra("name");
        vehicleNo = i.getStringExtra("vehicle_no");
        user_id = i.getStringExtra(ProfileActivity.USER_ID);
        Qr = i.getStringExtra("QR");
         /*brand = i.getStringExtra("brand");
         color = i.getStringExtra("color");*/


        Log.e("dhcbhdcgd","cdgvcgdvc "+Qr);

         if(Qr!=null&&!Qr.equals("")&&!Qr.isEmpty()){
             Picasso.get().load(Qr).into(imq_qr);
         }
    }

    private void setReferences() {
        imq_qr = findViewById(R.id.imq_qr);
        img_ok = findViewById(R.id.img_ok);
        img_addcar = findViewById(R.id.img_addcar);
        //generateQrcode();
    }

    private void generateQrcode() {

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            DisplayMetrics metrics = this.getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            width = (width * 70) / 100;
//            int height = metrics.heightPixels;
            BitMatrix bitMatrix = multiFormatWriter.encode(vehicleNo, BarcodeFormat.QR_CODE, width, width);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            /*barcodeEncoder.(ContextCompat.getColor(AddedCarActivity.this, R.color.colorAccent),
                    ContextCompat.getColor(AddedCarActivity.this, R.color.colorPrimaryDark));*/
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imq_qr.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_ok:
                if (TAG != null) {

                    Intent i = new Intent(AddedCarActivity.this, MainActivity.class);
                    //i.putExtra("bitmap",bitmap);
                    if(TAG.equalsIgnoreCase("CARS")){
                        i.putExtra("TAG", "CARS");
                    }else if(TAG.equalsIgnoreCase("FACEBOOK")||TAG.equalsIgnoreCase("LOGINPAGE")){
                        i.putExtra("TAG","VALET");
                    }else {
                        i.putExtra("TAG", TAG);
                    }
                    startActivity(i);
                    finish();
                }

                break;
            case R.id.img_addcar:
                Intent i = new Intent(AddedCarActivity.this, CarDetailsActivity.class);
                //i.putExtra("bitmap",bitmap);
                i.putExtra("TAG", TAG);
                i.putExtra(ProfileActivity.USER_ID, user_id);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AddedCarActivity.this, MainActivity.class);
            i.putExtra("TAG", TAG);
            startActivity(i);
            finish();
    }
}
