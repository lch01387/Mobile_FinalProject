package com.example.administrator.lifelogger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

public class create extends AppCompatActivity {
    EditText et_title;
    EditText et_content;
    Button bt_save;
    Button bt_map;
    Button bt_back;
    Intent in_main;
    Intent in_map;

    double lat;
    double lng;
    String title;
    String content;
    LatLng latlng;
    Data data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        et_title = (EditText)findViewById(R.id.et_title);
        et_content = (EditText)findViewById(R.id.et_content);
        bt_save = (Button)findViewById(R.id.bt_save);
        bt_map = (Button)findViewById(R.id.bt_map);
        bt_back = (Button)findViewById(R.id.bt_back);
        in_main = new Intent(); // main으로 돌려보내게 될 intent
        in_map = new Intent(this, map_View.class);


        lat = getIntent().getDoubleExtra("lat", 37.56);
        lng = getIntent().getDoubleExtra("lng", 126.97); // 수정

        // 오늘의 날자 띄우기
        String today = new java.text.SimpleDateFormat("yyyy-MM-dd k:mm").format(new java.util.Date());
        et_content.setText(today);

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get intent data
                /*Intent i = getIntent();
                data = (Data)i.getParcelableExtra("data");*/
                data = new Data(et_title.getText().toString(), et_content.getText().toString(), lat, lng);

                in_main.putExtra("data", data);
                setResult(0, in_main);
                finish();
            } // 제목, 내용, 현재 gps정보를 Data객체에 저장해서 main으로 보낸다.
        });
        bt_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "위도 : " + lat + "경도 : " + lng, Toast.LENGTH_LONG).show();
                in_map.putExtra("lat", lat); // 위도정보 넘겨주기
                in_map.putExtra("lng", lng); // 경도정보 넘겨주기
                startActivity(in_map);
            }
        });
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
