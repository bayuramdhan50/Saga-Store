package com.saga.sagastore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TransaksiController {
    @GetMapping("/transaksi")
    public String transaksi(Model model) {
//        model.addAttribute("transaksi", new Transaksi());
        return "transaksi";
    }
}
