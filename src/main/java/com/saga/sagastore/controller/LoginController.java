package com.saga.sagastore.controller;

import com.saga.sagastore.connection.ConnectionManager;
import com.saga.sagastore.model.Pengguna;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Controller
@RequestMapping("/")
public class LoginController {

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("pageTitle", "Login");
        model.addAttribute("pengguna", new Pengguna());
        model.addAttribute("active", "login");
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Pengguna pengguna, Model model,HttpServletRequest request) throws SQLException {
        boolean authenticated = authenticate(pengguna.getUsername(), pengguna.getPassword());
        if (authenticated) {
            HttpSession session = request.getSession();
            session.setAttribute("username", pengguna.getUsername());

            String role = getRoleByUsername(pengguna.getUsername());
            session.setAttribute("role", role);

            if ("admin".equals(role)) {
                return "redirect:/admin/dashboard"; // Redirect to admin dashboard
            } else {
                return "redirect:/user/home"; // Redirect to user dashboard
            }
        } else {
            model.addAttribute("error", "Username atau password salah.");
            model.addAttribute("active", "login");
            return "login";
        }
    }

    // Metode autentikasi dan lainnya tetap sama

    private String getRoleByUsername(String username) {
        String role = null;
        String query = "SELECT role FROM pengguna WHERE username=?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                role = resultSet.getString("role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Tangani pengecualian (contoh: logging atau kembalikan nilai default)
        }
        return role;
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

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/login?logout"; // Redirect ke halaman login setelah logout
    }
}

