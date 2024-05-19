package com.saga.sagastore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "item_transaksi")
public class ItemTransaksi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transaksi_id")
    private Transaksi transaksi;

    @ManyToOne
    @JoinColumn(name = "produk_id")
    private Produk produk;

    @Column(nullable = false)
    private int jumlah;

    public ItemTransaksi(Long id, Transaksi transaksi, Produk produk, int jumlah) {
        this.id = id;
        this.transaksi = transaksi;
        this.produk = produk;
        this.jumlah = jumlah;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Transaksi getTransaksi() {
        return transaksi;
    }

    public void setTransaksi(Transaksi transaksi) {
        this.transaksi = transaksi;
    }

    public Produk getProduk() {
        return produk;
    }

    public void setProduk(Produk produk) {
        this.produk = produk;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}