package com.versatilemobitech.VeeZee.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.versatilemobitech.VeeZee.R;
import com.versatilemobitech.VeeZee.location.GPSTracker;
import com.versatilemobitech.VeeZee.utils.Utility;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap gmap;
    private Toolbar toolbar;
    TextView toolBarTitle;
    GPSTracker gps;
    TextView text_view_credits_v,  text_view_wallet_amount;
    ImageView image_view_coin,google_maps;
    public static Double latitude, longitude;
    private static final String MAP_VIEW_BUNDLE_KEY = "AIzaSyCJgHSG1X0Zx86zba9dkPbZ3Yr2hFNXtxE";
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        toolbar = findViewById(R.id.toolBar);
        toolBarTitle = findViewById(R.id.toolBarTitle);

        google_maps = findViewById(R.id.google_maps);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        getSupportActionBar().setTitle("");
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(mapViewBundle);
        updateLocation();
        mapView.getMapAsync(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        getSupportActionBar().setTitle("");
        toolBarTitle.setVisibility(View.VISIBLE);
        toolBarTitle.setText("Map");
        toolBarTitle.setGravity(Gravity.LEFT);

        text_view_credits_v=findViewById(R.id.text_view_credits_v);
        text_view_credits_v.setVisibility(View.GONE);

        image_view_coin=findViewById(R.id.image_view_coin);
        image_view_coin.setVisibility(View.GONE);

        text_view_wallet_amount=findViewById(R.id.text_view_wallet_amount);
        text_view_wallet_amount.setVisibility(View.GONE);

        getIntentData();

        google_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String label = "I'm Here!";
                String uriBegin = "geo:" + latitude + "," + longitude;
                String query = latitude + "," + longitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(mapIntent);
            }
        });
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            latitude = parseDouble(intent.getStringExtra("lat"));
            longitude = parseDouble(intent.getStringExtra("lng"));
            title = intent.getStringExtra("title");
        }
    }

    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {
       // String addrs = getAddress(latitude,longitude);
        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_tip_location, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.ImageView01);
        TextView textView = (TextView)customMarkerView.findViewById(R.id.text_view) ;
        markerImageView.setImageResource(resId);
        textView.setText(title);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        //gmap.setMinZoomPreference(10);
        if (latitude != null && longitude != null) {
            LatLng ny = new LatLng(latitude, longitude);
            String addrs = getAddress(latitude,longitude);
           // gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
            gmap.getUiSettings().setMapToolbarEnabled(true);
            gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(ny,12));
            gmap.addMarker(new MarkerOptions()
                    .position(ny)
                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_places_on_maps))));
            gmap.setContentDescription(addrs);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private double parseDouble(String s){

        if(s == null || s.isEmpty()||s.matches("^[a-zA-Z]*$"))
            return 0.0;
        else
            return Double.parseDouble(s);
    }

    private void updateLocation() {

        gps = new GPSTracker(this);
        String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 23);
        } else {
            if (gps.canGetLocation()) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                Log.e("latitude", "" + latitude);
                Log.e("longitude", "" + longitude);
            } else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("GPS is settings");
                alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
                alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                        //  finish();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // finish();
                    }
                });
                alertDialog.show();
            }
        }


    }

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
    public static boolean coordinatesValid(double latitude, double longitude) {
        return latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180;
    }
    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
        String add = "";
        try {
            if (!coordinatesValid(latitude, longitude)) {
                //throw new IllegalArgumentException("Not a valid geo location: " + latitude + ", " + longitude);
                Toast.makeText(getApplicationContext(),"Not a valid geo location",Toast.LENGTH_SHORT).show();
            } else {

                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                if(addresses!=null && addresses.size()>0) {
                    Address obj = addresses.get(0);
                    add = obj.getAddressLine(0);

                    Log.v("IGA", "Address" + add);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return add;
    }

}
