package com.saga.sagastore.controller;

import com.saga.sagastore.connection.ConnectionManager;
import com.saga.sagastore.model.Pengguna;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Controller
@RequestMapping("/")
public class PenggunaController {

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("pageTitle", "Login");
        model.addAttribute("pengguna", new Pengguna());
        model.addAttribute("active", "login");
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Pengguna pengguna, Model model) throws SQLException {
        boolean authenticated = authenticate(pengguna.getUsername(), pengguna.getPassword());
        if (authenticated) {
            // Autentikasi berhasil
            // Simpan session atau login success logic di sini
            return "redirect:/"; // Arahkan ke halaman utama
        } else {
            // Autentikasi gagal
            model.addAttribute("error", "Username atau password salah.");
            model.addAttribute("active", "login");
            return "login";
        }
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("pageTitle", "Signup");
        model.addAttribute("pengguna", new Pengguna());
        model.addAttribute("active", "signup");
        return "signup";
    }

    private boolean authenticate(String username, String password) throws SQLException {
        String query = "SELECT * FROM pengguna WHERE username=? AND password=?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Jika hasil query mengandung baris, berarti autentikasi berhasil
        }
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute Pengguna pengguna, Model model) {
        boolean registered = register(pengguna);
        if (registered) {
            return "redirect:/login"; // Registrasi berhasil, arahkan ke halaman login
        } else {
            model.addAttribute("error", "Gagal mendaftar pengguna.");
            model.addAttribute("active", "signup");
            return "signup"; // Gagal mendaftar, tetap di halaman signup dengan pesan error
        }
    }

    private boolean register(Pengguna pengguna) {
        String query = "INSERT INTO pengguna (username, password, alamat, nomorTelepon) VALUES (?, ?, ?, ?)";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, pengguna.getUsername());
            statement.setString(2, pengguna.getPassword());
            statement.setString(3, pengguna.getAlamat());
            statement.setString(4, pengguna.getNomorTelepon());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0; // Registrasi berhasil jika ada baris yang dimasukkan
        } catch (SQLException e) {
            e.printStackTrace(); // Tangani pengecualian dengan mencetak stack trace
            return false; // Registrasi gagal
        }
    }
}
