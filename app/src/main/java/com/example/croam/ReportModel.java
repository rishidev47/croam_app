package com.example.croam;

public class ReportModel {

    private String name;
    private String media;
    private String latitude;
    private String longitude;
    private String country;
    private String state;
    private String city;
    private String description;
    private String created;

    public ReportModel(String name, String media, String latitude, String longitude,
            String country, String state, String city, String description, String created) {
        this.name = name;
        this.media = media;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.state = state;
        this.city = city;
        this.description = description;
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

}
//{"id":1,"name":"New","media":"https:\/\/storage.googleapis
//                    .com\/report-media\/VID_20200626_120950.mp4","latitude":"28.433556",
//                    "longitude":"77.31817","country":"India","state":"Haryana",
//                    "city":"Faridabad","description":"I am ____","block":null,"likes":null,
//                    "created":"2020-06-26 06:40:11"}
