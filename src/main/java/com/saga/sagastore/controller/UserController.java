package com.saga.sagastore.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/profile")
    public String userProfile(Model model) {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login"; // Arahkan ke login jika tidak ada sesi
        }
        model.addAttribute("username", username);
        model.addAttribute("pageTitle", "Profile");
        model.addAttribute("activePage", "Profile");
        return "user/profile"; // Ini adalah view name yang harus sesuai dengan file template Anda
    }

}
