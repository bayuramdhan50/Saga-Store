package com.saga.sagastore.controller;

import com.saga.sagastore.connection.ConnectionManager;
import com.saga.sagastore.model.Pengguna;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.*;

@Controller
@RequestMapping("/")
public class PenggunaController {

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("pengguna", new Pengguna());
        model.addAttribute("active", "login");
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Pengguna pengguna, Model model) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM pengguna WHERE username=? AND password=?")) {
            stmt.setString(1, pengguna.getUsername());
            stmt.setString(2, pengguna.getPassword()); // Asumsi password tidak di-hash di database
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Autentikasi berhasil
                // Simpan session atau login success logic di sini
                return "redirect:/"; // Arahkan ke halaman utama
            } else {
                // Autentikasi gagal
                model.addAttribute("error", "Username atau password salah.");
                model.addAttribute("active", "login");
                return "login";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("pengguna", new Pengguna());
        model.addAttribute("active", "signup");
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute Pengguna pengguna, Model model) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO pengguna (username, password, alamat, nomorTelepon) VALUES (?, ?, ?, ?)")) {
            // Hash password sebelum disimpan ke database
            String hashedPassword = (pengguna.getPassword());
            pengguna.setPassword(hashedPassword);

            stmt.setString(1, pengguna.getUsername());
            stmt.setString(2, pengguna.getPassword());
            stmt.setString(3, pengguna.getAlamat());
            stmt.setString(4, pengguna.getNomorTelepon());
            stmt.executeUpdate();

            return "redirect:/login"; // Arahkan ke halaman login
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
    }
}