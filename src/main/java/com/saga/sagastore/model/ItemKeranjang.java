package com.saga.sagastore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "item_keranjang")
public class ItemKeranjang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "keranjang_id")
    private Keranjang keranjang;

    @ManyToOne
    @JoinColumn(name = "produk_id")
    private Produk produk;

    @Column(nullable = false)
    private int jumlah;

    public ItemKeranjang(Long id, Keranjang keranjang, Produk produk, int jumlah) {
        this.id = id;
        this.keranjang = keranjang;
        this.produk = produk;
        this.jumlah = jumlah;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Keranjang getKeranjang() {
        return keranjang;
    }

    public void setKeranjang(Keranjang keranjang) {
        this.keranjang = keranjang;
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