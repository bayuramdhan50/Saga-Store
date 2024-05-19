package com.saga.sagastore.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "keranjang")
public class Keranjang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pengguna_id")
    private Pengguna pengguna;

    @OneToMany(mappedBy = "keranjang", cascade = CascadeType.ALL, orphanRemoval = true)
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
