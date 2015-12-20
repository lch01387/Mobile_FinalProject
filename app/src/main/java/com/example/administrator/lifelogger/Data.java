package com.example.administrator.lifelogger;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015-12-18.
 * Intent 객체전달을 위한 vo class 정의
 */
public class Data implements Parcelable {

    private Double lat;
    private Double lng;
    private String title;
    private String content;

    public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
        public Data createFromParcel(Parcel in) {
            Data vo = new Data();
            vo.lat = in.readDouble();
            vo.lng = in.readDouble();
            vo.title = in.readString();
            vo.content = in.readString();
            return vo;
        }
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };
    @Override
    public void writeToParcel(Parcel out, int flag) {
        out.writeDouble(lat);
        out.writeDouble(lng);
        out.writeString(title);
        out.writeString(content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Constructor type1
    public Data() {
        //Empty constructor
    }

    /**
     * Constructor type2
     *
     * @param lat
     * @param lng
     * @param title
     * @param content
     */
    public Data(String title, String content, double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
        this.title = title;
        this.content = content;
    }

    // getter.setter.
    public Double getLat() {
        return lat;
    }
    public void setLat(double lat){
        this.lat = lat;
    }
    public Double getLng() {
        return lng;
    }
    public void setLng(double lng){
        this.lng = lng;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content){
        this.content = content;
    }
}