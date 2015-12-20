package com.example.administrator.lifelogger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class content extends AppCompatActivity {
    TextView et_title;
    TextView et_content;
    Button bt_map;
    Button bt_delete;
    Button bt_back;
    Intent in_map;
    Intent in_main;

    int index;
    double lat;
    double lng;
    String title;
    String content;
    Data data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        et_title = (TextView)findViewById(R.id.et_title);
        et_content = (TextView)findViewById(R.id.et_content);
        bt_map = (Button)findViewById(R.id.bt_map);
        bt_delete = (Button)findViewById(R.id.bt_delete);
        bt_back = (Button)findViewById(R.id.bt_back);
        in_map = new Intent(this, map_View.class);
        in_main =new Intent(); // main으로 돌려보내게 될 intent

        data = getIntent().getParcelableExtra("data");
        title = data.getTitle();
        content = data.getContent();
        lat = data.getLat();
        lng = data.getLng();

        et_title.setText(title);
        et_content.setText(content);
        // 버튼 "맵 위치" 선택
        bt_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in_map.putExtra("lat", lat); // 위도정보 넘겨주기
                in_map.putExtra("lng", lng); // 경도정보 넘겨주기
                startActivity(in_map);
            }
        });
        // 버튼 "이 로그 삭제" 선택
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in_main.putExtra("delete", getIntent().getIntExtra("index", 0));
                setResult(1, in_main);
                finish();
            }
        });
        // 버튼 "뒤로" 선택
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
