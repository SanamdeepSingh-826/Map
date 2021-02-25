package com.dalvikbytes.map;

import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int markerCount=0;
    private Button btnClr,btnDrw,btnDis;
    private List<LatLng> latLngList;
    private Polyline polyline=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        btnClr=findViewById(R.id.btnClr);
        btnDrw=findViewById(R.id.btnDraw);
        btnDis=findViewById(R.id.btnDist);
        latLngList= new ArrayList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnClr.setOnClickListener(view -> {
            if (markerCount==0)
                return;

            clearMap();

        });
        btnDis.setOnClickListener(view -> {
            if (polyline != null) {
                double d = distance(latLngList.get(0).latitude,
                        latLngList.get(1).latitude,
                        latLngList.get(0).longitude,
                        latLngList.get(1).longitude);
                Toast.makeText(MapsActivity.this, "Distance is " + String.format("%.2f", d) + " Km", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(MapsActivity.this, "Firstly draw a Polyline", Toast.LENGTH_SHORT).show();
        });
        btnDrw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (polyline!=null)
                    return;
                if (markerCount<2)
                    return;
                PolylineOptions polylineOptions=new PolylineOptions().addAll(latLngList).width(10f);
                polyline=mMap.addPolyline(polylineOptions);

            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap==null)
            return;

        mMap = googleMap;

        googleMap.setOnMapClickListener(latLng -> {


            if (markerCount>=2){ clearMap(); return; }

            if (markerCount==0)
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker 1"));
            else
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker 2"));

            latLngList.add(latLng);
            markerCount++;

        });

    }

    public void clearMap(){
        mMap.clear();
        markerCount=0;
        polyline=null;
        latLngList.clear();
    }

    public static double distance(double lat1,
                                  double lat2, double lon1,
                                  double lon2)
    {

        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;
        // calculate the result
        return(c * r);
    }

}