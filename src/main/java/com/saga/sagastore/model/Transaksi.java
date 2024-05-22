package com.saga.sagastore.model;

import java.time.LocalDateTime;
import java.util.List;

public class Transaksi {

    private Long id;
    private Pengguna pengguna;
    private LocalDateTime tanggalTransaksi;
    private double totalHarga;
    private String metodePembayaran;
    private List<ItemTransaksi> itemTransaksis;

    public Transaksi(Long id, Pengguna pengguna, LocalDateTime tanggalTransaksi, double totalHarga, String metodePembayaran, List<ItemTransaksi> itemTransaksis) {
        this.id = id;
        this.pengguna = pengguna;
        this.tanggalTransaksi = tanggalTransaksi;
        this.totalHarga = totalHarga;
        this.metodePembayaran = metodePembayaran;
        this.itemTransaksis = itemTransaksis;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pengguna getPengguna() {
        return pengguna;
    }

    public void setPengguna(Pengguna pengguna) {
        this.pengguna = pengguna;
    }

    public LocalDateTime getTanggalTransaksi() {
        return tanggalTransaksi;
    }

    public void setTanggalTransaksi(LocalDateTime tanggalTransaksi) {
        this.tanggalTransaksi = tanggalTransaksi;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(double totalHarga) {
        this.totalHarga = totalHarga;
    }

    public String getMetodePembayaran() {
        return metodePembayaran;
    }

    public void setMetodePembayaran(String metodePembayaran) {
        this.metodePembayaran = metodePembayaran;
    }

    public List<ItemTransaksi> getItemTransaksis() {
        return itemTransaksis;
    }

    public void setItemTransaksis(List<ItemTransaksi> itemTransaksis) {
        this.itemTransaksis = itemTransaksis;
    }
}
