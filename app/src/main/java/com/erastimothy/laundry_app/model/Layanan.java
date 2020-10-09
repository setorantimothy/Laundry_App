package com.erastimothy.laundry_app.model;

public class Layanan {
    private String name;
    private int id;
    private double harga;

    public Layanan(){}
    public Layanan(String name, int id, double harga) {
        this.name = name;
        this.id = id;
        this.harga = harga;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }
}
