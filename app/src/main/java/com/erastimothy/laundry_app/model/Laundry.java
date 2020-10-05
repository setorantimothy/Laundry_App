package com.erastimothy.laundry_app.model;

public class Laundry {
    private String uid,nama,jenis,alamat,status,tanggal,order_id;
    private double kuantitas,harga,biaya_antar,total_pembayaran;

    public Laundry() {}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String  getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public double getKuantitas() {
        return kuantitas;
    }

    public void setKuantitas(double kuantitas) {
        this.kuantitas = kuantitas;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public double getBiaya_antar() {
        return biaya_antar;
    }

    public void setBiaya_antar(double biaya_antar) {
        this.biaya_antar = biaya_antar;
    }

    public double getTotal_pembayaran() {
        return total_pembayaran;
    }

    public void setTotal_pembayaran(double total_pembayaran) {
        this.total_pembayaran = total_pembayaran;
    }

    public Laundry(String uid, String nama, String jenis, String alamat, String status, String tanggal,String order_id, double kuantitas, double harga, double biaya_antar, double total_pembayaran) {
        this.uid = uid;
        this.nama = nama;
        this.jenis = jenis;
        this.alamat = alamat;
        this.status = status;
        this.tanggal = tanggal;
        this.order_id = order_id;
        this.kuantitas = kuantitas;
        this.harga = harga;
        this.biaya_antar = biaya_antar;
        this.total_pembayaran = total_pembayaran;
    }



}
