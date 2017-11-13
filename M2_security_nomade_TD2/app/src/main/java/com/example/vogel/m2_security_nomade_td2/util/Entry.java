package com.example.vogel.m2_security_nomade_td2.util;


/**
 * Created by vogel on 23/10/17.
 */
public class Entry{
    private Integer id;
    private String message;
    private String longitude;
    private String latitude;

    public Entry(){}

    public Entry(String message, String longitude, String latitude) {
        this.message = message;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id;}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
