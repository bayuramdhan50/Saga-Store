package com.saga.sagastore.controller;

import com.saga.sagastore.connection.ConnectionManager;
import com.saga.sagastore.model.Produk;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/produk")
public class ProdukController {

    @GetMapping
    public String index(Model model) {
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

        model.addAttribute("pageTitle", "Produk");
        model.addAttribute("produkList", produkList);
        model.addAttribute("activePage", "products");
        return "produk";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM produk WHERE id=?")) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Produk produk = new Produk();
                produk.setId(rs.getLong("id"));
                produk.setNama(rs.getString("nama"));
                produk.setDeskripsi(rs.getString("deskripsi"));
                produk.setHarga(rs.getDouble("harga"));
                produk.setStok(rs.getInt("stok"));
                produk.setNamaGambar(rs.getString("namaGambar"));
                model.addAttribute("produk", produk);
                model.addAttribute("active", "products");
                return "produkDetail";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle error (misalnya, redirect ke halaman error)
        }
        // Produk tidak ditemukan
        return "redirect:/produk";
    }
}