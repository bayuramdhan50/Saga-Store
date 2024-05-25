package com.saga.sagastore.model;

public class Pengguna {

    private Long id;
    private String username;
    private String password;
    private String alamat;
    private String nomorTelepon;
    private String role;

    // Konstruktor tanpa argumen
    public Pengguna() {
    }

    // Konstruktor dengan argumen
    public Pengguna(Long id, String username, String password, String alamat, String nomorTelepon, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.alamat = alamat;
        this.nomorTelepon = nomorTelepon;
        this.role = role;
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
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
