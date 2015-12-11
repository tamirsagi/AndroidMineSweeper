package com.minesweeper.BL.DB;


import com.google.android.maps.GeoPoint;

/**
 * Created by Administrator on 12/1/2015.
 */
public class PlayerRecord {

    private int id;
    private String fullName;
    private String roundTime;
    private double latitude;
    private double longitude;
    private String city;
    private String country;
    private String date;

    private GeoPoint geoPoint;


    public PlayerRecord() {
    }

    public PlayerRecord(String fullName, String time, String city,String country,double latitude,double longitude) {
        this.fullName = fullName;
        this.roundTime = time;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        setGeoPoint();
    }

    public String getId() {
        return "" + id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRoundTime() {
        return roundTime;
    }

    public void setRoundTime(String roundTime) {
        this.roundTime = roundTime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private void setGeoPoint(){
        geoPoint = new GeoPoint((int)(latitude * 1E6),(int)(longitude * 1E6));
    }

    public GeoPoint getGeoPoint(){
        return geoPoint;
    }

}
