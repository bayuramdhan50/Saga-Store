package com.saga.sagastore.controller;

import com.saga.sagastore.model.Pengguna;
import com.saga.sagastore.model.Produk;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.saga.sagastore.connection.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/home")
    public String userIndex(Model model) {
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
        model.addAttribute("activePage", "Home");
        return "user/index"; // Ini adalah view name yang harus sesuai dengan file template Anda
    }

    //    ABOUT

    @GetMapping("/about")
    public String userAbout(Model model) {
        model.addAttribute("pageTitle", "About");
        model.addAttribute("activePage", "About");
        return "user/about"; // Ini adalah view name yang harus sesuai dengan file template Anda
    }

    //    KERANJANG

    @GetMapping("/keranjang")
    public String userKeranjang(Model model, HttpServletRequest request) {
        model.addAttribute("pageTitle", "Keranjang");
        model.addAttribute("activePage", "Keranjang");

        List<Produk> produkList = new ArrayList<>();
        double totalHarga = 0;
        try (Connection connection = ConnectionManager.getConnection()) {
            // Ambil ID keranjang dari session
            Long keranjangId = (Long) request.getSession().getAttribute("keranjangId");
            if (keranjangId != null) {
                PreparedStatement stmt = connection.prepareStatement(
                        "SELECT p.id, p.nama, p.deskripsi, p.harga, p.stok, p.namaGambar, ik.jumlah " +
                                "FROM produk p " +
                                "JOIN item_keranjang ik ON p.id = ik.produk_id " +
                                "JOIN keranjang k ON ik.keranjang_id = k.id " +
                                "WHERE k.id = ?");
                stmt.setLong(1, keranjangId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Produk produk = new Produk();
                    produk.setId(rs.getLong("id"));
                    produk.setNama(rs.getString("nama"));
                    produk.setDeskripsi(rs.getString("deskripsi"));
                    produk.setHarga(rs.getDouble("harga"));
                    produk.setStok(rs.getInt("stok"));
                    produk.setNamaGambar(rs.getString("namaGambar"));
                    produk.setJumlah(rs.getInt("jumlah")); //  Mengambil jumlah dari item_keranjang
                    produkList.add(produk);
                    totalHarga += produk.getHarga() * produk.getJumlah();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        model.addAttribute("produkList", produkList);
        model.addAttribute("totalHarga", totalHarga);
        return "user/keranjang";
    }

    @PostMapping("/keranjang/add/{id}")
    public String addToCart(@PathVariable Long id, HttpServletRequest request) {
        try (Connection connection = ConnectionManager.getConnection()) {
            // Ambil ID keranjang dari session (asumsi Anda sudah memiliki mekanisme session)
            Long keranjangId = (Long) request.getSession().getAttribute("keranjangId");
            if (keranjangId == null) {
                // Jika belum ada keranjang, buat keranjang baru dan simpan ID-nya di session
                PreparedStatement stmt = connection.prepareStatement(
                        "INSERT INTO keranjang (pengguna_id) VALUES (?)",
                        Statement.RETURN_GENERATED_KEYS);
                // Masukkan ID pengguna ke sini
                stmt.setLong(1, 1L); // Contoh, masukkan ID pengguna dari session
                stmt.executeUpdate();
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    keranjangId = generatedKeys.getLong(1);
                    request.getSession().setAttribute("keranjangId", keranjangId);
                }
            }

            // Tambahkan item ke keranjang
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO item_keranjang (keranjang_id, produk_id, jumlah) VALUES (?, ?, ?)");
            stmt.setLong(1, keranjangId);
            stmt.setLong(2, id);
            stmt.setInt(3, 1); // Asumsi jumlah default adalah 1
            stmt.executeUpdate();

            return "redirect:/user/produk"; // Arahkan ke halaman produk
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/keranjang/update/{id}")
    @ResponseBody
    public String updateQuantity(@PathVariable Long id, @RequestParam("quantity") int quantity, HttpServletRequest request) {
        try (Connection connection = ConnectionManager.getConnection()) {
            // Ambil ID keranjang dari session
            Long keranjangId = (Long) request.getSession().getAttribute("keranjangId");
            if (keranjangId != null) {
                PreparedStatement stmt = connection.prepareStatement(
                        "UPDATE item_keranjang SET jumlah=? WHERE produk_id=? AND keranjang_id=?");
                stmt.setInt(1, quantity);
                stmt.setLong(2, id);
                stmt.setLong(3, keranjangId);
                stmt.executeUpdate();
                return "OK";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    @GetMapping("/keranjang/remove/{id}")
    @ResponseBody
    public String removeItemFromCart(@PathVariable Long id, HttpServletRequest request) {
        try (Connection connection = ConnectionManager.getConnection()) {
            Long keranjangId = (Long) request.getSession().getAttribute("keranjangId");
            if (keranjangId != null) {
                PreparedStatement stmt = connection.prepareStatement(
                        "DELETE FROM item_keranjang WHERE produk_id=? AND keranjang_id=?");
                stmt.setLong(1, id);
                stmt.setLong(2, keranjangId);
                stmt.executeUpdate();
                return "OK";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    //    TRANSAKSI

    @GetMapping("/transaksi")
    public String userCheckout(Model model) {
        model.addAttribute("pageTitle", "Transaksi");
        model.addAttribute("activePage", "Transaksi");
        return "user/transaksi"; // Ini adalah view name yang harus sesuai dengan file template Anda
    }

    @PostMapping("/transaksi")
    public String checkout(Model model, HttpServletRequest request) {
        // Ambil ID keranjang dari session
        Long keranjangId = (Long) request.getSession().getAttribute("keranjangId");

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO transaksi (pengguna_id, tanggalTransaksi, totalHarga, metodePembayaran) VALUES (?, ?, ?, ?)")) {
            // Masukkan ID pengguna dari session
            stmt.setLong(1, 1L);
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            // Ambil totalHarga dari session atau hitung ulang
            stmt.setDouble(3, (Double) request.getSession().getAttribute("totalHarga"));
            stmt.setString(4, "Cash On Delivery"); // Contoh metode pembayaran

            stmt.executeUpdate();

            // Hapus item dari keranjang (opsional)
//            stmt = connection.prepareStatement("DELETE FROM item_keranjang WHERE keranjang_id=?");
//            stmt.setLong(1, keranjangId);
//            stmt.executeUpdate();

            // Hapus keranjang dari session (opsional)
            request.getSession().removeAttribute("keranjangId");

            // Redirect ke halaman konfirmasi atau halaman lain yang sesuai
            return "redirect:/";
        } catch (SQLException e) {
            e.printStackTrace();
            // Redirect ke halaman error
            return "error";
        }
    }

    //    SERVICES

    @GetMapping("/services")
    public String userServices(Model model) {
        model.addAttribute("pageTitle", "Services");
        model.addAttribute("activePage", "Services");
        return "user/services"; // Ini adalah view name yang harus sesuai dengan file template Anda
    }

    //    PRODUCT

    @GetMapping("/produk")
    public String userProduct(Model model) {
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
        model.addAttribute("pageTitle", "Product");
        model.addAttribute("produkList", produkList);
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

    @GetMapping("/thankyou")
    public String userThankyou(Model model) {
        model.addAttribute("pageTitle", "Thank You");
        model.addAttribute("activePage", "Thank You");
        return "user/thankyou"; // Ini adalah view name yang harus sesuai dengan file template Anda
    }

}
