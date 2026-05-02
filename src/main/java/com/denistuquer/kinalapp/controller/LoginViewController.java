package com.denistuquer.kinalapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginViewController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}