package com.linkdeco.link_deco.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class mainController {
    @GetMapping("/")
    public String index() {
        return "index"; // templates/index.html
    }
}
