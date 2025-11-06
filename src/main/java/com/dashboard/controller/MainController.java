package com.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {


    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("menu", "dashboard");
        return "dashboard/dashboard"; // => /WEB-INF/views/dashboard.jsp
    }

}
