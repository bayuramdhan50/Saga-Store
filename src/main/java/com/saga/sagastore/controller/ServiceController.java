package com.saga.sagastore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ServiceController {

    @GetMapping("/services")
    public String services(Model model) {
        model.addAttribute("pageTitle", "Services");
        model.addAttribute("activePage", "services");
        return "services";
    }
}
