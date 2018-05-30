package com.example.rahul.placessearch;

public class Fav {


    String icon;
    String name;
    String address;
    String heart;
    String placeid;

    Double to_lat, to_lng;

    public Fav() {

        icon="No image found";
        name="No name found";
        address="No address found";
        heart="";
        placeid="";
    }

    public Fav(String icon, String name, String address, String heart, String placeid,Double to_lat, Double to_lng) {
        this.icon = icon;
        this.name = name;
        this.address = address;
        this.heart=heart;
        this.placeid=placeid;
        this.to_lat=to_lat;
        this.to_lng=to_lng;
    }
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHeart() {
        return heart;
    }

    public void setHeart(String address) {
        this.heart = heart;
    }

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    public Double getTo_lat() {
        return to_lat;
    }

    public void setTo_lat(Double to_lat) {
        this.to_lat=to_lat;
    }

    public Double getTo_lng() {
        return to_lng;
    }

    public void setTo_lng(Double to_lng) {
        this.to_lng=to_lng;
    }
}
