package com.saga.sagastore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.*;

@Controller
@RequestMapping("/")
public class KeranjangController {

    @GetMapping("/keranjang")
    public String keranjang(Model model) {
        model.addAttribute("pageTitle", "Keranjang");
        model.addAttribute("activePage", "keranjang");
        return "keranjang";
    }
}
