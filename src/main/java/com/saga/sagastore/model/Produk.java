package com.saga.sagastore.model;

public class Produk {

    private Long id;
    private String nama;
    private String deskripsi;
    private double harga;
    private int stok;
    private String namaGambar;
    private int jumlah;

    public Produk() {
    }

    public Produk(Long id, String nama, String deskripsi, double harga, int stok, String namaGambar) {
        this.id = id;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.stok = stok;
        this.namaGambar = namaGambar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public String getNamaGambar() {
        return namaGambar;
    }

    public void setNamaGambar(String namaGambar) {
        this.namaGambar = namaGambar;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}

