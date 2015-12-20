package com.example.administrator.lifelogger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class map_View extends AppCompatActivity {
    Intent in_get;
    double Lat_get;
    double Lng_get;
    LatLng latlng_get;
    private GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map__view);
        in_get = getIntent();
        Lat_get = in_get.getDoubleExtra("lat", 37.56);
        Lng_get = in_get.getDoubleExtra("lng", 126.97);
        latlng_get = new LatLng(Lat_get, Lng_get);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        Marker seoul = map.addMarker(new MarkerOptions().position(latlng_get)
                .title("위치 표시"));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng_get, 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }
}
