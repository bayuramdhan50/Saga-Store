package com.saga.sagastore.model;

public class ItemKeranjang {

    private Long id;
    private Keranjang keranjang;
    private Produk produk;
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
