package com.example.rahul.placessearch;

public class Google_rev {

    private String pp_url, a_name, a_url,text;
    Float rating;
    String time;

    public Google_rev() {
    }

    public Google_rev(String pp_url, String a_name, String a_url,String text,Float rating,String time ) {
        this.pp_url = pp_url;
        this.a_name = a_name;
        this.a_url = a_url;
        this.text = text;
        this.rating = rating;
        this.time = time;
        //time=moment.unix(time).format('YYYY-MM-DD h:mm:ss');
    }

    public String getPp_url_url() {
        return pp_url;
    }

    public void setPp_url(String pp_url) {
        this.pp_url = pp_url;
    }

    public String getA_name() {
        return a_name;
    }

    public void setA_name(String a_name) {
        this.a_name = a_name;
    }

    public String getA_url() {
        return a_url;
    }

    public void setA_url(String a_url) {
        this.a_url = a_url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {


        this.time = time;
    }

}
