package com.saga.sagastore.model;

import jakarta.persistence.*;
@Entity
@Table(name = "produk")
public class Produk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nama;

    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    @Column(nullable = false)
    private double harga;

    @Column(nullable = false)
    private int stok;

    @Column(nullable = false)
    private String namaGambar;

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
}
