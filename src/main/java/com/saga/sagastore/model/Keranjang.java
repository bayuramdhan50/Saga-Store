package com.saga.sagastore.model;

import java.util.List;

public class Keranjang {

    private Long id;
    private Pengguna pengguna;
    private List<ItemKeranjang> itemKeranjangs;

    public Keranjang(Long id, Pengguna pengguna, List<ItemKeranjang> itemKeranjangs) {
        this.id = id;
        this.pengguna = pengguna;
        this.itemKeranjangs = itemKeranjangs;
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

    public List<ItemKeranjang> getItemKeranjangs() {
        return itemKeranjangs;
    }

    public void setItemKeranjangs(List<ItemKeranjang> itemKeranjangs) {
        this.itemKeranjangs = itemKeranjangs;
    }
}

