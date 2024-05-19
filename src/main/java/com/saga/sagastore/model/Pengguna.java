package com.saga.sagastore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pengguna")
public class Pengguna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String alamat;

    @Column(nullable = false)
    private String nomorTelepon;

    // Konstruktor tanpa argumen
    public Pengguna() {
    }

    // Konstruktor dengan argumen
    public Pengguna(Long id, String username, String password, String alamat, String nomorTelepon) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.alamat = alamat;
        this.nomorTelepon = nomorTelepon;
    }

    // Getter dan setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }
}
