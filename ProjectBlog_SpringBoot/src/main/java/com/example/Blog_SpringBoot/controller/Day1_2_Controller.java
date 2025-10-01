package com.example.Blog_SpringBoot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Day1_2_Controller {

    @GetMapping("/day1_2")
    public String day1_2(Model model) {
        model.addAttribute("pageTitle", "Blog Home Page");
        return "day1_2/index";
    }

    @GetMapping("/day1_2/post/{id}")
    public String readDetails(Model model) {
        model.addAttribute("pageTitle", "Details Page");
        return "day1_2/post-detail";
    }
}
