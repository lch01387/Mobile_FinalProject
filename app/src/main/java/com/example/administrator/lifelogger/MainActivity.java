package com.example.administrator.lifelogger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;



public class MainActivity extends Activity {
    // Layout 관련 객체들
    TextView tv_today;
    Button bt_map;
    Button bt_create;
    Button bt_renew;
    Intent in_map;
    Intent in_create;
    Intent in_content;
    Context mContext;
    double lat;
    double lng;
    boolean isGPSEnabled;

    // startActivityForResult
    Data givenData;
    int givenIndex;

    // Database 관련 객체들
    SQLiteDatabase db;
    String dbName = "idList.db"; // name of Database;
    String tableName = "idListTable"; // name of Table;
    int dbMode = Context.MODE_PRIVATE;

    //ListView 관련 객체들
    ListView mList;
    ArrayAdapter<String> baseAdapter;
    ArrayList<String> nameList;
    ArrayList<Data> dataList;


    private GoogleMap map;

    // create인텐트에서 정보를 전달받기 위함
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0){ // 0이면 create인텐트(생성)
            if(data != null) { // 보낸 데이터가 있다면
                givenData = data.getParcelableExtra("data");
                insertData(givenData.getTitle(), givenData.getContent(), givenData.getLat(), givenData.getLng());
                //Toast.makeText(mContext, givenData.getTitle(), Toast.LENGTH_LONG).show();
            }
        }else if(resultCode == 1){ // 1이면 content인텐트(삭제)
            if(data != null){
                givenIndex  = data.getIntExtra("delete", -1);
                if(givenIndex != -1){
                    // 받은 index에 해당하는 정보를 db에서 지우기
                    removeData(givenIndex+1);
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        in_map = new Intent(this, map_View.class);
        in_create = new Intent(this, create.class);
        in_content = new Intent(this, content.class);
        tv_today = (TextView) findViewById(R.id.today_date);
        bt_map = (Button) findViewById(R.id.bt_map);
        bt_create = (Button) findViewById(R.id.bt_create);
        bt_renew = (Button)findViewById(R.id.bt_renew);
        mContext = this.getApplication().getApplicationContext();
        mList = (ListView) findViewById(R.id.listView);

        // Create listview
        nameList = new ArrayList<String>();
        baseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, nameList);
        mList.setAdapter(baseAdapter);

        // Database 생성 및 열기
        db = openOrCreateDatabase(dbName,dbMode,null);
        // 테이블 생성
        createTable();

        // Gps사용
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // GPS 프로바이더 사용가능여부
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                lat = location.getLatitude(); // 위도 구하기
                lng = location.getLongitude(); // 경도 구하기
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
                isGPSEnabled = true;
            }

            public void onProviderDisabled(String provider) {
                isGPSEnabled = false;
            }
        };
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        // 오늘의 날자 띄우기
        String today = new java.text.SimpleDateFormat("yyyy-MM-dd k:mm").format(new java.util.Date());
        tv_today.setText(today);

        // 리스트뷰 처음부터 보이게하기
        /*
        nameList.clear();
        selectAll();
        baseAdapter.notifyDataSetChanged();
        */

        // 버튼 '새 로그 만들기' 클릭
        bt_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in_create.putExtra("lat", lat);
                in_create.putExtra("lng", lng);
                startActivityForResult(in_create, 0);
            }
        });
        // 버튼 '나의 현재 위치는?' 클릭
        bt_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGPSEnabled) {  // Gps가 사용 가능할 때
                    //Toast.makeText(mContext, "위도 : " + lat + "경도 : " + lng, Toast.LENGTH_LONG).show();
                    in_map.putExtra("lat", lat); // 위도정보 넘겨주기
                    in_map.putExtra("lng", lng); // 경도정보 넘겨주기
                    Toast.makeText(mContext, "lat : " + lat + "lng" + lng, Toast.LENGTH_LONG).show(); // Gps 사용불가할 때
                    //startActivity(in_map);
                } else {
                    Toast.makeText(mContext, "Gps가 사용 가능하지 않습니다", Toast.LENGTH_LONG).show(); // Gps 사용불가할 때
                }
            }
        });
        // 버튼 '새로고침' 클릭
        bt_renew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameList.clear();
                selectAll();
                baseAdapter.notifyDataSetChanged();
            }
        });
        // 리스트뷰 클릭
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position++;
                String sql = "select * from " + tableName + " where id = " + position + ";";
                Cursor result = db.rawQuery(sql, null);

                // result(Cursor 객체)가 비어 있으면 false 리턴
                if (result.moveToFirst()) {
                    Data data = new Data(result.getString(1), result.getString(2), result.getDouble(3),result.getDouble(4));
                    in_content.putExtra("data", data);
                    //Toast.makeText(mContext, result.getInt(0), Toast.LENGTH_LONG).show();
                    in_content.putExtra("index", result.getInt(0));
                    startActivityForResult(in_content, 1);
                }
                result.close();
            }
        });
    }

    public void deleteElement(ArrayList nameList, ArrayList dataList, int index){ // index에 해당하는 번호를 arrayList에서 지움
        nameList.remove(index);
        dataList.remove(index);
    }
    public void renew(ArrayList NameList){ // list 새로고침
    }
    // Table 생성
    public void createTable() {
        try {
            String sql = "create table " + tableName + "(id integer primary key autoincrement, "
                    + "title text not null, " + "content text not null, " + "lat double, " + "lng double" + ");";
            //lat double" + "lng double
            //sql = "drop table " + tableName;
            db.execSQL(sql);
        } catch (android.database.sqlite.SQLiteException e) {
            Log.d("Lab sqlite", "error: " + e);
        }
    }
    // Data 추가
    public void insertData(String title, String content, Double lat, Double lng) {
        String sql = "insert into " + tableName + " values(NULL,'" + title + "','" + content + "'," + lat + "," + lng + ");";
        db.execSQL(sql);
    }///////////////////////////////////////////////////////////////////////////////////////////////
    // Data 업데이트
    public void updateData(int index, Data data) {
        String sql = "update " + tableName + " set name = '" + data + "' where id = " + index + ";";
        db.execSQL(sql);
    }
    // Data 삭제
    public void removeData(int index) {
        String sql = "delete from " + tableName + " where id = " + index + ";";
        db.execSQL(sql);
    }
    // 모든 Data 읽기
    public void selectAll() {
        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);
        results.moveToFirst();

        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String title = results.getString(1);
            //String content = results.getString(2);
            //Double lat = results.getDouble(3);
            //Double lng = results.getDouble(4);
//            Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_LONG).show();
            //Log.d("lab_sqlite", "index= " + id + " name=" + name);

            nameList.add(title);
            results.moveToNext();
        }
        results.close();
    }
}