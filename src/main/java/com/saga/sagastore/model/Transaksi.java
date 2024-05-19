package com.saga.sagastore.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transaksi")
public class Transaksi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pengguna_id")
    private Pengguna pengguna;

    @Column(nullable = false)
    private LocalDateTime tanggalTransaksi;

    @Column(nullable = false)
    private double totalHarga;

    @Column(nullable = false)
    private String metodePembayaran;

    @OneToMany(mappedBy = "transaksi", cascade = CascadeType.ALL, orphanRemoval = true)
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
