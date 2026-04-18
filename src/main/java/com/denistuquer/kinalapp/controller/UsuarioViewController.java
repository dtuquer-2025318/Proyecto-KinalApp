package com.denistuquer.kinalapp.controller;

import com.denistuquer.kinalapp.entity.Usuario;
import com.denistuquer.kinalapp.repository.UsuarioRepository;
import com.denistuquer.kinalapp.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/usuario")
public class UsuarioViewController {

    @Autowired
    private IUsuarioService usuarioService; // Cambiamos Repository por Service para que guarde bien

    @GetMapping("/lista")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuario/lista";
    }

    @GetMapping("/nuevo")
    public String registroForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable("id") Integer id, Model model) {
        Optional<Usuario> usuario = usuarioService.buscarPorCodigo(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            return "usuario/formulario";
        }
        return "redirect:/usuario/lista";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute("usuario") Usuario usuario) {
        // ESTA LÍNEA ES LA QUE FALTA PARA QUE SE GUARDE EN LA DB
        usuarioService.guardar(usuario);
        return "redirect:/usuario/lista";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") Integer id) {
        usuarioService.eliminar(id);
        return "redirect:/usuario/lista";
    }
}