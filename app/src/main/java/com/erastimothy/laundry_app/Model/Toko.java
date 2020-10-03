package com.erastimothy.laundry_app.Model;

public class Toko {
    private String alamat,name,telp;
    private Double longitude,latitude;

    public Toko(String alamat, String name, String telp, Double longitude, Double latitude) {
        this.alamat = alamat;
        this.name = name;
        this.telp = telp;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
