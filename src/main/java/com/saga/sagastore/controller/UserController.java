package com.saga.sagastore.controller;

import com.saga.sagastore.model.Pengguna;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.saga.sagastore.connection.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/home")
    public String userIndex(Model model) {
        model.addAttribute("pageTitle", "Home");
        model.addAttribute("activePage", "Home");
        return "user/index"; // Ini adalah view name yang harus sesuai dengan file template Anda
    }

    @GetMapping("/about")
    public String userAbout(Model model) {
        model.addAttribute("pageTitle", "About");
        model.addAttribute("activePage", "About");
        return "user/about"; // Ini adalah view name yang harus sesuai dengan file template Anda
    }

    @GetMapping("/keranjang")
    public String userKeranjang(Model model) {
        model.addAttribute("pageTitle", "Keranjang");
        model.addAttribute("activePage", "Keranjang");
        return "user/keranjang"; // Ini adalah view name yang harus sesuai dengan file template Anda
    }

    @GetMapping("/transaksi")
    public String userCheckout(Model model) {
        model.addAttribute("pageTitle", "Transaksi");
        model.addAttribute("activePage", "Transaksi");
        return "user/transaksi"; // Ini adalah view name yang harus sesuai dengan file template Anda
    }

    @GetMapping("/services")
    public String userServices(Model model) {
        model.addAttribute("pageTitle", "Services");
        model.addAttribute("activePage", "Services");
        return "user/services"; // Ini adalah view name yang harus sesuai dengan file template Anda
    }

    @GetMapping("/produk")
    public String userProduct(Model model) {
        model.addAttribute("pageTitle", "Product");
        model.addAttribute("activePage", "Product");
        return "user/produk"; // Ini adalah view name yang harus sesuai dengan file template Anda
    }

//    PROFILE

    @GetMapping("/profile")
    public String userProfile(Model model) {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login"; // Arahkan ke login jika tidak ada sesi
        }
        // Inisialisasi objek model pengguna dan set atribut username
        Pengguna pengguna = new Pengguna();
        pengguna.setUsername(username);
        model.addAttribute("pengguna", pengguna); // Tambahkan objek pengguna ke model
        model.addAttribute("pageTitle", "Profile");
        model.addAttribute("activePage", "Profile");
        return "user/profile"; // Ini adalah view name yang harus sesuai dengan file template Anda
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute Pengguna pengguna, Model model) {
        String username = pengguna.getUsername(); // Ambil username dari pengguna
        if (username == null) {
            // Tangani kasus di mana username null
            model.addAttribute("pageTitle", "Profile");
            model.addAttribute("error", "Failed to update profile: Username is null");
            return "user/profile";
        }

        try (Connection connection = ConnectionManager.getConnection()) {
            String sql = "UPDATE pengguna SET password = ?, nomorTelepon = ? WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, pengguna.getPassword());
                preparedStatement.setString(2, pengguna.getNomorTelepon());
                preparedStatement.setString(3, username); // Menggunakan username yang sudah diperoleh

                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    // Update berhasil
                    model.addAttribute("pageTitle", "Profile");
                    model.addAttribute("success", "Profile updated successfully");
                } else {
                    // Tidak ada baris yang diupdate (mungkin username tidak ditemukan)
                    model.addAttribute("pageTitle", "Profile");
                    model.addAttribute("error", "No rows updated. Username might not be found.");
                }
            }
        } catch (SQLException e) {
            // Tangani kesalahan jika ada
            e.printStackTrace();
            // Redirect kembali ke halaman profil dengan pesan kesalahan jika diperlukan
            model.addAttribute("pageTitle", "Profile");
            model.addAttribute("error", "Failed to update profile. Please try again later.");
            return "user/profile";
        }
        model.addAttribute("pageTitle", "Profile");
        // Redirect kembali ke halaman profil setelah berhasil memperbarui
        model.addAttribute("success", "Profile updated successfully");
        return "user/profile";
    }

}
