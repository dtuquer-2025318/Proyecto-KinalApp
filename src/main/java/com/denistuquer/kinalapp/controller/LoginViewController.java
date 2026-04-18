package com.denistuquer.kinalapp.controller;

import com.denistuquer.kinalapp.entity.Usuario;
import com.denistuquer.kinalapp.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginViewController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/login")
    public String login() {
        return "login"; // templates/login.html
    }

    @PostMapping("/login")
    public String entrar(@RequestParam String username, @RequestParam String password, HttpSession session) {
        // Buscamos usuario en la base de datos
        Usuario usuario = usuarioRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);

        if (usuario != null) {
            session.setAttribute("usuarioLogueado", usuario);
            // REDIRIGE AL MENÚ DESPUÉS DE LOGUEARSE
            return "redirect:/menu";
        } else {
            return "redirect:/login?error=true";
        }
    }
}