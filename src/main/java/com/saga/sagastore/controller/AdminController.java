package com.saga.sagastore.controller;

import com.saga.sagastore.connection.ConnectionManager;
import com.saga.sagastore.model.Pengguna;
import com.saga.sagastore.model.Produk;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private HttpServletRequest request;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login"; // Arahkan ke login jika tidak ada sesi
        }
        // Inisialisasi objek model pengguna dan set atribut username
        Pengguna pengguna = new Pengguna();
        pengguna.setUsername(username);
        model.addAttribute("pengguna", pengguna);
        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("activePage", "dashboard");
        return "admin/dashboard"; // Ini adalah view name yang harus sesuai dengan file template Anda
    }

    @GetMapping("/produk")
    public String adminProduct(Model model) {
        List<Produk> produkList = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection()) {
            String sql = "SELECT * FROM produk";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Produk produk = new Produk();
                        produk.setId(resultSet.getLong("id"));
                        produk.setNama(resultSet.getString("nama"));
                        produk.setDeskripsi(resultSet.getString("deskripsi"));
                        produk.setHarga(resultSet.getDouble("harga"));
                        produk.setStok(resultSet.getInt("stok"));
                        produk.setNamaGambar(resultSet.getString("namaGambar"));
                        produkList.add(produk);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to retrieve products. Please try again later.");
            return "admin/error"; // Tampilkan halaman error jika terjadi kesalahan
        }
        model.addAttribute("produkList", produkList);
        model.addAttribute("pageTitle", "Product");
        model.addAttribute("activePage", "product");
        return "admin/produk";
    }

    @GetMapping("/produk/add")
    public String adminProductAdd(Model model) {
        model.addAttribute("pageTitle", "Product");
        model.addAttribute("activePage", "product");
        model.addAttribute("produk", new Produk());
        return "admin/addproduk"; // Ini adalah view name yang harus sesuai dengan file template Anda
    }

    @PostMapping("/produk/add")
    public String addProduct(@ModelAttribute Produk produk, Model model) {
        try (Connection connection = ConnectionManager.getConnection()) {
            String sql = "INSERT INTO produk (nama, deskripsi, harga, stok, namaGambar) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, produk.getNama());
                preparedStatement.setString(2, produk.getDeskripsi());
                preparedStatement.setDouble(3, produk.getHarga());
                preparedStatement.setInt(4, produk.getStok());
                preparedStatement.setString(5, produk.getNamaGambar());

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    // Produk berhasil ditambahkan
                    model.addAttribute("success", "Product added successfully");
                } else {
                    // Gagal menambahkan produk
                    model.addAttribute("error", "Failed to add product. Please try again later.");
                }
            }
        } catch (SQLException e) {
            // Tangani kesalahan jika terjadi exception SQL
            e.printStackTrace();
            model.addAttribute("error", "Failed to add product. Please try again later.");
        }
        return "redirect:/admin/produk"; // Redirect kembali ke halaman produk setelah berhasil menambahkan produk
    }

    @GetMapping("/produk/editproduk/{id}")
    public String editProductPage(@PathVariable("id") long id, Model model) {
        // Ambil data produk dari database berdasarkan ID
        Produk produk = getProductById(id);
        if (produk != null) {
            model.addAttribute("produk", produk);
            return "admin/editproduk"; // Ganti dengan nama view edit produk Anda
        } else {
            return "redirect:/admin/produk"; // Redirect jika produk tidak ditemukan
        }
    }

    // Metode untuk menangani permintaan edit produk
    @PostMapping("/produk/editproduk/{id}")
    public String editProduct(@ModelAttribute Produk produk, Model model) {
        // Lakukan update data produk di database
        if (updateProduct(produk)) {
            model.addAttribute("success", "Product updated successfully");
        } else {
            model.addAttribute("error", "Failed to update product. Please try again later.");
        }
        return "redirect:/admin/produk"; // Redirect kembali ke halaman produk setelah berhasil mengedit produk
    }

    // Metode untuk menangani permintaan hapus produk
    @GetMapping("/produk/deleteproduk/{id}")
    public String deleteProduct(@PathVariable("id") long id, Model model) {
        // Perform deletion logic here
        if (deleteProductById(id)) {
            model.addAttribute("success", "Product deleted successfully");
        } else {
            model.addAttribute("error", "Failed to delete product. Please try again later.");
        }
        return "redirect:/admin/produk"; // Redirect back to the product page
    }

    // Metode untuk mendapatkan data produk dari database berdasarkan ID
    private Produk getProductById(long id) {
        try (Connection connection = ConnectionManager.getConnection()) {
            String sql = "SELECT * FROM produk WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToProduk(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Metode untuk mengubah data produk di database
    private boolean updateProduct(Produk produk) {
        try (Connection connection = ConnectionManager.getConnection()) {
            String sql = "UPDATE produk SET nama = ?, deskripsi = ?, harga = ?, stok = ?, namaGambar = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, produk.getNama());
                preparedStatement.setString(2, produk.getDeskripsi());
                preparedStatement.setDouble(3, produk.getHarga());
                preparedStatement.setInt(4, produk.getStok());
                preparedStatement.setString(5, produk.getNamaGambar());
                preparedStatement.setLong(6, produk.getId());

                int rowsUpdated = preparedStatement.executeUpdate();
                return rowsUpdated > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Metode untuk menghapus data produk dari database berdasarkan ID
    private boolean deleteProductById(long id) {
        try (Connection connection = ConnectionManager.getConnection()) {
            String sql = "DELETE FROM produk WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, id);

                int rowsDeleted = preparedStatement.executeUpdate();
                return rowsDeleted > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Metode untuk memetakan hasil dari ResultSet menjadi objek Produk
    private Produk mapResultSetToProduk(ResultSet resultSet) throws SQLException {
        Produk produk = new Produk();
        produk.setId(resultSet.getLong("id"));
        produk.setNama(resultSet.getString("nama"));
        produk.setDeskripsi(resultSet.getString("deskripsi"));
        produk.setHarga(resultSet.getDouble("harga"));
        produk.setStok(resultSet.getInt("stok"));
        produk.setNamaGambar(resultSet.getString("namaGambar"));
        return produk;
    }

    @GetMapping("/customer")
    public String adminCustomer(Model model) {
        try (Connection connection = ConnectionManager.getConnection()) {
            String sql = "SELECT id, username, alamat, nomorTelepon, role FROM pengguna";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    List<Pengguna> penggunaList = new ArrayList<>();
                    while (resultSet.next()) {
                        Pengguna pengguna = new Pengguna();
                        pengguna.setId(resultSet.getLong("id"));
                        pengguna.setUsername(resultSet.getString("username"));
                        pengguna.setAlamat(resultSet.getString("alamat"));
                        pengguna.setNomorTelepon(resultSet.getString("nomorTelepon"));
                        pengguna.setRole(resultSet.getString("role"));

                        penggunaList.add(pengguna);
                    }
                    model.addAttribute("penggunaList", penggunaList);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to retrieve customer data. Please try again later.");
        }

        model.addAttribute("pageTitle", "Customer");
        model.addAttribute("activePage", "customer");
        return "admin/customer"; // Sesuaikan dengan view name Anda
    }

}
