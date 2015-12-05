package com.minesweeper.BL.DB;


/**
 * Created by Administrator on 12/1/2015.
 */
public class PlayerRecord {

    private int id;
    private String fullName;
    private String roundTime;
    private String location;
    private String date;


    public PlayerRecord() {
    }

    public PlayerRecord(String fullName, String time, String location) {
        this.fullName = fullName;
        this.roundTime = time;
        this.location = location;
    }

    public int getId() {
        return id;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



}
