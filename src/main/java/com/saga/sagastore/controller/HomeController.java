package com.saga.sagastore.controller;

import com.saga.sagastore.connection.ConnectionManager;
import com.saga.sagastore.model.Produk;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        List<Produk> produkList = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM produk");
            while (rs.next()) {
                Produk produk = new Produk();
                produk.setId(rs.getLong("id"));
                produk.setNama(rs.getString("nama"));
                produk.setDeskripsi(rs.getString("deskripsi"));
                produk.setHarga(rs.getDouble("harga"));
                produk.setStok(rs.getInt("stok"));
                produk.setNamaGambar(rs.getString("namaGambar"));
                produkList.add(produk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle error (misalnya, redirect ke halaman error)
        }
        model.addAttribute("pageTitle", "Home");
        model.addAttribute("produkList", produkList);
        model.addAttribute("activePage", "home");
        // Other logic for your home page
        return "index"; // or whatever your home page template name is
    }
}
